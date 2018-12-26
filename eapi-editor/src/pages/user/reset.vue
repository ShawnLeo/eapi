<template>
  <div class="user_resetPass">
    <div class="cont">
      <div class="cont1">
        <div class="cont1_o">
          <div class="cont1_div">
            {{ $t("resetPass.title") }}
          </div>
          <Form class="password-form" ref="formValidate" :model="formValidate" :rules="ruleValidate">
            <div class="user">
              <Form-item prop="password" :label="$t('resetPass.newPass')">
                <Input type="password" v-model="formValidate.password" :placeholder="$t('resetPass.enterNewPass')"
                       @on-enter="handleSubmit('formValidate')"/>
              </Form-item>
            </div>

            <div class="user">
              <Form-item prop="newPassword" :label="$t('resetPass.rePassword')">
                <Input type="password" v-model="formValidate.newPassword" :placeholder="$t('resetPass.enterPassword')"
                       @on-enter="handleSubmit('formValidate')"/>
              </Form-item>
            </div>

            <!--<div class="tips">{{ $t("resetPass.tips") }}</div>-->
            <Button type="primary" id="loginbutton" :loading="login_loading"
                    @click="handleSubmit('formValidate')">
              <span v-if="!login_loading">{{ $t("resetPass.btnName") }}</span>
              <span v-else>{{ $t("resetPass.btnLoading") }}</span>
            </Button>
          </Form>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import {emailReset} from '../../utils/interface';

  export default {
    name: 'resetPassword',
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
        if (value === '') {
          callback(new Error(this.$t('resetPass.enterPassword')));
        } else if (value !== this.formValidate.password) {
          callback(new Error(this.$t('resetPass.psdEL')));
        } else {
          callback();
        }
      };
      return {
        formValidate: {
					resetCode: this.$route.query.activateCode,
          password: '',
          newPassword: ''
        },
        ruleValidate: {
          password: [
            {required: true, message: this.$t('resetPass.enterOriginPass'), trigger: 'blur'},
            {min: 8, max: 32, message: this.$t('register.passErrorL3'), trigger: 'blur'},
            {validator: validatePassword, trigger: 'blur'}
          ],
          newPassword: [
            {required: true, message: this.$t('resetPass.enterPassword'), trigger: 'blur'},
            {validator: validatePassCheck, trigger: 'blur'}
          ]
        },
        login_loading: false
      };
    },
    methods: {
      handleSubmit(name) { // login
        this.$refs[name].validate(async (valid) => {
          this.login_loading = true;
          // 登陆
          if (valid) {
            await emailReset(this.formValidate, (response) => {
                this.$Message.success(this.$t('resetPass.resetSuccess'));
                this.$router.push('/');
            });
          }
          this.login_loading = false;
        });
      }
    },
    mounted() {
      console.log(this.$route.query.username);
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less">
  @import '../../assets/css/enter.less';
</style>
