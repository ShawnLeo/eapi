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
            <div class="user">
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
  </div>
</template>

<script>
  import {register} from '../../utils/interface';

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
      return {
        formValidate: {
					email: '',
					password: '',
					rePassword: '',
					interest: [true]
				},
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
          ]
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
        getPhoneCode: this.$t('safeValid.noteActionLabel')
      };
    },
    methods: {
      handleSubmit(name) { // register
        this.$refs[name].validate(async (valid) => {
          // 注册
          if (valid) {
//            if (!this.formValidate.kaptchaCodeId) {
//              this.$Message.error(this.$t('login.kaptEr4'));
//              return;
//            }
            this.login_loading = true;
            await register({
              email: this.formValidate.email,
              password: this.formValidate.password
            }, (response) => {
                this.$Message.success(this.$t('register.regSuccess'));
                this.$router.push({path: '/user/active/send', query: { email: this.formValidate.email }});
            });

          }
//          this.captcha();
          this.login_loading = false;
        });
      },
      submitCode (name) {
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            let params = {
              invitedCode: '',
              mobile: this.formValidate.mobile,
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
//      this.captcha();
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
