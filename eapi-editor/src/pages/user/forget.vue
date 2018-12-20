<template>
  <div class="enter-email">
    <div class="cont">
      <div class="cont1">
        <div class="cont1_o">
          <div class="cont1_div">
            {{ $t("enterEmail.title") }}
          </div>
          <Form class="password-form" ref="formValidate" :model="formValidate" :rules="ruleValidate">
            <div class="user">
              <Form-item prop="email" :label="$t('enterEmail.email')">
                <Input type="text" v-model="formValidate.email" :placeholder="$t('enterEmail.enterEmail')"/>
              </Form-item>
            </div>
            <div id="your-dom-id" class="nc-container user"></div>
            <Button type="primary" id="loginbutton" :loading="loading"
                    @click="handleSubmit('formValidate')">
              <span v-if="!loading">{{$t("enterEmail.btnName")}}</span>
              <span v-else>{{$t("enterEmail.btnLoading")}}</span>
            </Button>
          </Form>
        </div>
      </div>
    </div>
    <!--<validModal :openValid="openValid" @sureValid="sureValid" :type="3" :email="formValidate.email"-->
                <!--@closeModal="closeModal" :token="formValidate.kaptchaValidToken"></validModal>-->
  </div>
</template>

<script>
  /* eslint-disable no-undef,new-cap */
  import {emailResetSend, validToken} from '../../utils/const';
//  import ValidModal from '../../components/otc/validModal';
  import { appkey } from '../../utils/env';
  export default {
    name: 'enterEmail',
//    components: {
//      ValidModal
//    },
    data() {
//      const validateEmail = (rule, value, callback) => {
//        console.log('验证邮箱');
//      };
      const formValidate = {
        email: this.$route.query.username,
        kaptchaCodeId: '',
        kaptchaValidToken: ''
      };
      return {
        openValid: false,
        formValidate: formValidate,
        ruleValidate: {
          email: [
            {required: true, message: this.$t('enterEmail.enterEmail'), trigger: 'blur'}
          ]
        },
        nc_token: [appkey, (new Date()).getTime(), Math.random()].join(':'),
        NC_Opt: {
          renderTo: '#your-dom-id',
          appkey: appkey,
          scene: 'nc_activity',
          token: this.nc_token,
          customWidth: 300,
          trans: {'key1': 'code0'},
          elementID: ['usernameID'],
          is_Opt: 0,
          language: 'cn',
          isEnabled: true,
          timeout: 3000,
          times: 5,
          apimap: {
            // 'analyze': '//a.com/nocaptcha/analyze.jsonp',
            // 'get_captcha': '//b.com/get_captcha/ver3',
            // 'get_captcha': '//pin3.aliyun.com/get_captcha/ver3'
            // 'get_img': '//c.com/get_img',
            // 'checkcode': '//d.com/captcha/checkcode.jsonp',
            // 'umid_Url': '//e.com/security/umscript/3.2.1/um.js',
            // 'uab_Url': '//aeu.alicdn.com/js/uac/909.js',
            // 'umid_serUrl': 'https://g.com/service/um.json'
          },
          callback: function (data) {
//            window.console && console.log('nc_token=', data.token);
//            window.console && console.log('csessionid=', data.csessionid);
//            window.console && console.log('sig=', data.sig);
//            window.console && console.log('nc_scene=', this.opt.scene);
            formValidate.kaptchaCodeId = data.csessionid + ',' + data.sig + ',' + data.token + ',pc_' + this.opt.scene;
          }
        },
        loading: false
      };
    },
    methods: {
      handleSubmit(name) { // login
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            await validToken({
              email: this.formValidate.email,
              type: 2,
              kaptchaCodeId: this.formValidate.kaptchaCodeId
            }, response => {
              if (response.header.code === '0') {
                this.formValidate.kaptchaValidToken = response.body.kaptchaValidToken; // 下发具有有效期的图形验证码token
                this.openValid = true;
              }
            });
            this.captcha();
            this.formValidate.kaptchaCodeId = ''; // 清空上一次生成的验证码
          }
        });
      },
      sureValid: function(result) {
        this.emailResetSend(result.smsCode, result.gvCodeId);
      },
      emailResetSend: async function(smsCode, gvCodeId) {
        await emailResetSend({
          email: this.formValidate.email,
          smsCode: smsCode,
          gvCodeId: gvCodeId,
          kaptchaValidToken: this.formValidate.kaptchaValidToken
        }, response => {
          if (response.header.code === '0') {
            this.$router.push({path: '/user/resetPassEmail',
              query: {
                username: this.formValidate.email,
                validToken: response.body.validToken // 谷歌短信验证成功token(用于重新发送邮件接口)
              }
            });
          }
        });
      },
      closeModal() {
        this.openValid = false;
      },
      captcha: function () {
        let nc = new noCaptcha(this.NC_Opt);
        nc.upLang('cn', {
          _startTEXT: '请按住滑块，拖动到最右边',
          _yesTEXT: '验证通过',
          _error300: '哎呀，出错了，点击<a href=\'javascript:__nc.reset()\'>刷新</a>再来一次',
          _errorNetwork: '网络不给力，请<a href=\'javascript:__nc.reset()\'>点击刷新</a>'
        });
      }
    },
    mounted() {
      this.captcha();
    }
  };
</script>

<style lang="less">
  @import '../../assets/css/enter.less';
</style>
