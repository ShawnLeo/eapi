<template>
  <div class="resetPassEmail">
    <div class="cont">
      <div class="cont1 width960">
        <div class="cont1_o">
          <div class="cont1_div">
            {{ $t("resetPassEmail.title") }}
          </div>
          <div class="panel_box">
            <div class="cont-pad_Box">
              <div class="mainCont">{{ $t("resetPassEmail.contentBefore") }}{{email}}{{ $t("resetPassEmail.contentAfter") }}<a @click="emailActivateSend">{{ $t("resetPassEmail.resendEmail") }}</a></div>
              <dl>
                <dt>{{ $t("resetPassEmail.tipsTit") }}</dt>
                <dd>{{ $t("resetPassEmail.tips01") }}</dd>
                <dd>{{ $t("resetPassEmail.tips02") }}</dd>
                <dd>{{ $t("resetPassEmail.tips03") }}</dd>
                <dd>{{ $t("resetPassEmail.tips04") }}</dd>
              </dl>
            </div>
            <div class="btnBox">
              <Button type="primary"
                      @click="checkEmail()">
                <span>{{ $t("resetPassEmail.btnName") }}</span>
              </Button>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {emailResetSend} from '../../utils/interface';
  export default {
    name: 'registerValidEmail',
    data() {
      return {
        validToken: this.$route.query.validToken,
        email: this.$route.query.username
      };
    },
    methods: {
      emailActivateSend: async function() {
        this.$Spin.show();
        // 重行发送邮件
        await emailResetSend({
          email: this.email,
          validToken: this.validToken
        }, (response) => {
          if (response.header.code === '0') {
            this.$Message.success(this.$t('resetPassEmail.resendEmailSuccess'));
//          } else {
//            this.$Message.error(response.header.message);
          }
        });
        this.$Spin.hide();
      },
			checkEmail () {
				// 重行发送邮件
				// console.log(this.email.split('@')[1]);
				let url = this.email.split('@')[1];
				let toUrl = '';
				switch (url) {
					case 'sina.com': toUrl = `http://mail.${url}.cn`; break;
					case 'gmail.com': toUrl = 'https://mail.google.com/'; break;
					case 'hotmail.com': toUrl = `http://www.${url}`; break;
					case 'yahoo.com': toUrl = `https://cn.overview.mail.${url}/`; break;
					case 'yahoo.com.cn': toUrl = `https://cn.overview.mail.${url}/`; break;
					case 'citiz.com': toUrl = 'http://citiz.online.sh.cn/'; break;
					case 'eyou.com': toUrl = `http://www.${url}/`; break;
					case '56.com': toUrl = `http://www.${url}/`; break;
					case 'aim.com': toUrl = `http://www.mail.${url}/`; break;
					case 'inbox.com': toUrl = `http://email.${url}`; break;
					case '163.com': toUrl = 'https://email.163.com'; break;
					case '126.com': toUrl = 'https://email.163.com'; break;
					case 'yeah.net': toUrl = 'https://email.163.com'; break;
					default: toUrl = `http://mail.${url}/`;
				}
				window.location.href = toUrl;
			}
    }
  };
</script>

<style lang="less">
  @import '../../assets/css/enter.less';
</style>
