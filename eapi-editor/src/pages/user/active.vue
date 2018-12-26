<template>
  <div class="user_active_success">
    <div class="cont">
      <div class="cont1 width960">
        <div class="cont1_o">
          <div class="cont1_div">
            {{ title }}
          </div>
          <div class="panel_box">
            <div class="singleLine" v-if="activeSuccess">
              {{$t("activeSuccess.content")}}<router-link to="/user/login">{{$t("activeSuccess.loginNow")}}</router-link>
            </div>
            <div  class="singleLine" v-else>
              {{$t("activeSuccess.contentFail")}}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {emailActivate} from '../../utils/interface';

  export default {
      name: 'activeSuccess',
      data() {
        return {
          title: this.$t('activeSuccess.title'),
					activeSuccess: true
        };
      },
      methods: {
        emailActivate: async function() { // login
          //
          await emailActivate({
            activateCode: this.$route.query.activateCode
          }, (response) => {
            if (response.header.code === '0') {
              this.title = this.$t('activeSuccess.title');
            } else {
              this.title = this.$t('activeSuccess.failTitle');
              this.activeSuccess = false;
            }
          });
        }
      },
      mounted() {
        this.emailActivate();
      }
    };
</script>

<style lang="less">
  @import '../../assets/css/enter.less';
</style>
