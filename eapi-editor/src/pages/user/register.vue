<template>
  <div class="register">
    <div class="cont">
      <div class="cont1">
        <div class="cont1_o">
          <div class="cont1_div">
            {{$t("register.title")}}
          </div>
          <!--<Tabs :value="tabType" @on-click="toTab">-->
            <!--<TabPane label="手机注册" name="phone"></TabPane>-->
            <!--<TabPane label="邮箱注册" name="email"></TabPane>-->
          <!--</Tabs>-->
          <Form class="register-form" ref="formValidate" :model="formValidate" :rules="ruleValidate">
            <!--<div class="user" v-if="tabType === 'phone'">-->
              <!--<Form-item prop="mobile" :label='$t("register.phone")' class="phone-reg">-->
                <!--<i-input v-model="phoneAppend">-->
                  <!--<Select v-model="phonePrepend" slot="prepend">-->
                    <!--<Option v-for="(item, index) in phonePrependList" :value="item.globalText" :key="index"></Option>-->
                  <!--</Select>-->
                <!--</i-input>-->
                <!--<input v-model="formValidate.mobile" type="hidden"/>-->
              <!--</Form-item>-->
            <!--</div>-->
            <div class="user" v-if="tabType === 'email'">
              <Form-item prop="email" :label='$t("register.email")' class="email-reg">
                <Input type="text" v-model="formValidate.email" :placeholder='$t("register.enterEmail")'
                       @on-enter="handleSubmit('formValidate')"/>
              </Form-item>
            </div>
            <div class="user">
              <Form-item prop="password" :label='$t("register.password")'>
                <Input type="password" v-model="formValidate.password" :placeholder='$t("register.enterPassword")'
                       @on-enter="handleSubmit('formValidate')"/>
              </Form-item>
            </div>

            <div class="user">
              <Form-item prop="rePassword" :label='$t("register.rePassword")'>
                <Input type="password" v-model="formValidate.rePassword" :placeholder='$t("register.enterRePassword")'
                       @on-enter="handleSubmit('formValidate')"/>
              </Form-item>
            </div>

            <div id="your-dom-id" class="nc-container user"></div>

            <Form-item label="" prop="interest">
              <Checkbox-group v-model="formValidate.interest">
                <Checkbox :label="true">
                  {{$t("register.read")}}
                  <router-link to="/user/protocol" target="_blank" class="left">{{$t("register.protocolName")}}</router-link>
                  和
                  <router-link to="/user/privacy" target="_blank" class="left">{{$t("register.privacyName")}}</router-link>
                </Checkbox>
              </Checkbox-group>
            </Form-item>
            <Button type="primary" id="loginbutton" :loading="login_loading"
                    @click="handleSubmit('formValidate')">
              <span v-if="!login_loading">{{$t("register.btnName")}}</span>
              <span v-else>{{$t("register.btnLoading")}}</span>
            </Button>
          </Form>
        </div>

      </div>
    </div>
    <Modal v-model="smsModal"
           :title="$t('register.smsTxt')"
           width="720px">
      <div class="modal-form">
        <Form class="valid-form" ref="smsValidate" :rules="smsRule" :model="smsValidate" label-position="top">
          <div class="user" prop="smsCode">
            <Form-item prop="smsCode" :label="$t('validModal.smsCode')">
              <Row>
                <i-col span="15">
                  <Input type="text" v-model="smsValidate.smsCode" :placeholder="$t('validModal.enterSmsCode')"/>
                </i-col>
                <i-col span="8" offset="1">
                  <Button type="primary" @click="getNoteCode" class="note-code" :disabled="getPhoneCodeDisabled">{{getPhoneCode}}</Button>
                </i-col>
              </Row>
            </Form-item>
          </div>
        </Form>
      </div>
      <div slot="footer" class="onlySure">
        <Button type="primary" long @click="submitCode('smsValidate')" :loading="login_loading">{{ $t('validModal.sure')}}</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  /* eslint-disable no-undef,new-cap */
  import {register, validToken, smsSend} from '../../utils/const';
  import { appkey } from '../../utils/env';
  import { countdown } from '@/utils/utils';
  export default {
    name: 'register',
    data() {
      const validatePassword = (rule, value, callback) => {
        let $pattern = /^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,32}$/; // 大小写都得有
//        let $pattern = /^(?=.*[0-9].*)((?=.*[A-Z].*)|(?=.*[a-z].*)).{8,32}$/; // 大小写任写一个
//        let $pattern = /^\d+$/; // 纯数字
        if (!$pattern.test(value)) {
          callback(new Error(this.$t('register.passErrorL4')));
        } else {
          callback();
        }
      };
      const validatePassCheck = (rule, value, callback) => {
        if (value !== this.formValidate.password) {
          callback(new Error(this.$t('register.passErrorL2')));
        } else {
          callback();
        }
      };
      const checkPhone = (rule, val, callback) => {
        if (val) {
          if (val && !Number.isInteger(+val)) {
            callback(new Error(this.$t('safeValid.numberEL')));
          }
          if (val.length !== 14) {
            callback(new Error(this.$t('safeValid.phoneLenEL')));
          }
          if (!/^[1][3,4,5,7,8][0-9]{9}$/.test(val.substr(3, 14))) {
            callback(new Error(this.$t('safeValid.phoneLenEL2')));
          } else {
            callback();
          }
        } else {
          callback();
        }
      };
      const checkNum = (rule, val, callback) => {
        if (val && !Number.isInteger(+val)) {
          callback(new Error(this.$t('safeValid.numberEL')));
        } else {
          callback();
        }
      };
      const formValidate = {
        email: '',
        password: '',
        rePassword: '',
        kaptchaCodeId: '',
        kaptchaValidToken: '',
        interest: [true],
        mobile: '',
        smsCode: ''
      };
      return {
//        formValidate: {
//          email: '',
//          password: '',
//          rePassword: '',
//          interest: []
//        },
        formValidate: formValidate,
        ruleValidate: {
          email: [
            {required: true, message: this.$t('register.enterEmail'), trigger: 'blur'},
            {type: 'email', message: this.$t('register.emailError'), trigger: 'blur'}
          ],
          password: [
            {required: true, message: this.$t('register.enterPassword'), trigger: 'blur'},
            {min: 8, max: 32, message: this.$t('register.passErrorL3'), trigger: 'blur'},
            {validator: validatePassword, trigger: 'blur'}
          ],
          rePassword: [
            {required: true, message: this.$t('register.enterRePassword'), trigger: 'blur'},
            {min: 8, max: 32, message: this.$t('register.passErrorL3'), trigger: 'blur'},
            {validator: validatePassCheck, trigger: 'blur'}
          ],
          interest: [
            { required: true, type: 'array', min: 1, message: this.$t('register.readError'), trigger: 'change' }
          ],
          mobile: [
            {type: 'string', required: true, message: this.$t('safeValid.phoneL'), trigger: 'blur'},
            {validator: checkPhone, trigger: 'blur'}
          ]
        },
        nc_token: [appkey, (new Date()).getTime(), Math.random()].join(':'),
        NC_Opt: {
          renderTo: '#your-dom-id',
          appkey: appkey,
          scene: 'nc_register',
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
        login_loading: false,
        tabType: 'email',
        phoneAppend: '',
        phonePrepend: '+86',
        phonePrependList: [
          {globalText: '+86'}
        ],
        smsModal: false,
        getPhoneCodeDisabled: false,
        getPhoneCode: this.$t('safeValid.noteActionLabel'),
        smsValidate: {
          smsCode: ''
        },
        smsRule: {
          smsCode: [
            {type: 'string', required: true, message: this.$t('safeValid.smsCodeL1'), trigger: 'blur'},
            {validator: checkNum, trigger: 'blur'},
            {min: 6, max: 6, message: this.$t('safeValid.codeLenEL'), trigger: 'blur'}
          ]
        }
      };
    },
    methods: {
      handleSubmit(name) { // register
        this.$refs[name].validate(async (valid) => {
          // 注册
          if (valid) {
            if (!this.formValidate.kaptchaCodeId) {
              this.$Message.error(this.$t('login.kaptEr4'));
              return;
            }
            this.login_loading = true;
            if (this.tabType === 'email') {
              await register({
                email: this.formValidate.email,
                password: this.formValidate.password,
                kaptchaCodeId: this.formValidate.kaptchaCodeId
              }, (response) => {
                if (response.header.code === '0') {
                  this.$Message.success(this.$t('register.regSuccess'));
                  this.$router.push({path: '/user/registerValidEmail', query: { email: this.formValidate.email }});
                } else {
                  this.$Message.error(response.header.message);
                }
              });
            } else if (this.tabType === 'phone') {
              await validToken({
                email: this.formValidate.mobile,
                kaptchaCodeId: this.formValidate.kaptchaCodeId,
                type: 1
              }, (response) => {
                if (response.header.code === '0') {
                  this.formValidate.kaptchaValidToken = response.body.kaptchaValidToken;
                  this.smsModal = true;
                } else {
                  this.captcha();
                  this.formValidate.kaptchaCodeId = '';
                  this.$Message.error(response.header.message);
                }
              });
            }
          }
          this.captcha();
          this.login_loading = false;
        });
      },
      captcha: async function () {
        let nc = new noCaptcha(this.NC_Opt);
        nc.upLang('cn', {
          _startTEXT: '请按住滑块，拖动到最右边',
          _yesTEXT: '验证通过',
          _error300: '哎呀，出错了，点击<a href=\'javascript:__nc.reset()\'>刷新</a>再来一次',
          _errorNetwork: '网络不给力，请<a href=\'javascript:__nc.reset()\'>点击刷新</a>'
        });
      },
      toTab (val) {
        this.tabType = val;
      },
      getNoteCode: async function (e) {
        let _this = this;
        let params = {
          mobile: this.formValidate.mobile,
          token: this.formValidate.kaptchaValidToken,
          type: 4
        };
        await smsSend(params, (response) => {
          if (response.header.code === '0') {
            this.getPhoneCodeDisabled = true;
            countdown((count) => {
              _this.getPhoneCode = `${this.$t('validModal.sendL') + count + this.$t('validModal.sendL2')}`;
            }, () => {
              _this.getPhoneCode = this.$t('validModal.sendL3');
              _this.getPhoneCodeDisabled = false;
            });
          } else {
            this.$Message.error(response.header.message);
            _this.getPhoneCodeDisabled = false;
          }
        });
      },
      submitCode (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            let params = {
              invitedCode: '',
              kaptchaValidToken: this.formValidate.kaptchaValidToken,
              mobile: this.formValidate.mobile,
              smsCode: this.smsValidate.smsCode,
              password: this.formValidate.password,
              type: 2
            };
            await register(params, (response) => {
              if (response.header.code === '0') {
                this.$Message.success(this.$t('register.regSuccess'));
                this.$router.push('/user/login');
              } else {
                this.$Message.error(response.header.message);
              }
            });
          }
        });
      }
    },
    mounted() {
      this.captcha();
    },
    watch: {
      'phoneAppend': function (val) {
        this.formValidate.mobile = this.phonePrepend + val;
      },
      'phonePrepend': function (val) {
        this.formValidate.mobile = val + this.phoneAppend;
      }
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less">
  @import '../../assets/css/enter.less';
  .nc-container .nc_wrapper {width: auto !important;}
  .register{
    .ivu-tabs{
      background: rgba(32, 37, 72, 0.8);
      padding: 0 20px;
      box-sizing: border-box;
      .ivu-tabs-bar{
        border-bottom: 0;
        .ivu-tabs-tab{
          color: #D9EAFF;
          font-size: 18px;
        }
        .ivu-tabs-tab-active{
          color: #1188FC;
        }
      }
    }
    .phone-reg{
      .ivu-input-group-prepend{
        background: none;
        border: 1px solid rgba(216, 234, 255, 0.5);
        border-right: 0;
        width: 90px;
        .ivu-select-selected-value{
          color: #D8EAFF;
        }
      }
    }
    .email-reg{
      .ivu-input-group-prepend{
        background: none;
        border: 0;
        padding: 0;
      }
      .ivu-input{
        border-radius: 6px !important;
      }
    }
  }
</style>
