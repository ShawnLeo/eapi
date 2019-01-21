<template>
  <div class="header">
    <Header>
      <div style="min-width: 1070px;">
        <router-link class="fl logo" style="margin-right: 25px;" :to="{path: '/project/index'}">
          <img width="150" height="50" src="@/assets/img/logo1.png">
        </router-link>
        <Menu ref="mainMenus" mode="horizontal" theme="dark" :active-name="$route.meta.menuActive" class="fl"
              @on-select="selectFn">
          <Menu-Item name="/project/index">
            {{ $t("menus.index") }}
          </Menu-Item>
          <Menu-Item name="/project/list">
            {{ $t("menus.projects") }}
          </Menu-Item>

          <Menu-Item name="/code/generator">
            {{ $t("menus.codegen") }}
          </Menu-Item>

          <Menu-Item name="/code/docs">
              文档
          </Menu-Item>
        </Menu>

        <div class="select_lang fr">
          <dl>
            <dt>{{ $t("currentLang") }} <Icon type="arrow-down-b"></Icon></dt>
            <dd>
              <a @click="switchLang('en-US')">English</a>
              <a @click="switchLang('zh-CN')">简体中文</a>
              <a @click="switchLang('zh-TW')">繁體中文</a>
            </dd>
          </dl>
        </div>

        <div class="head-user fr">
          <li class="head-user" v-if="state.user.authId">
            <span class="title" growing-ignore="true">{{state.user.authId}} <Icon type="arrow-down-b"></Icon></span>
            <ul class="head-user-list">
              <li><a href="javascript:void(0);" @click="logout" class="btn_logout"><Icon type="log-out"></Icon>退出登录</a></li>
            </ul>
          </li>
          <li class="head-user" v-else>
            <span class="title" growing-ignore="true" @click="goLogin">登录</span>
          </li>
        </div>
      </div>
    </Header>
  </div>
</template>

<script>
  import {ACCESS_TOKEN} from '../../utils/const';
  import {removeStore} from '../../utils/storage';
  export default {
    name: 't-header',
    data() {
      return {
        msg: '',
        value2: 0
      };
    },
    methods: {
      selectFn(a) {
        this.$router.push({path: a});
      },
      goLogin() {
        this.$router.push('/user/login');
      },
      switchLang(lang) {
        window.localStorage.lang = lang;
        this.$i18n.locale = lang;
        this.$store.dispatch('switchLang', lang);
      },
      logout: async function () {
//        await logout((response) => {
//          if (response.header.code === '0') {
            this.$store.dispatch('userInit', '');
            removeStore(ACCESS_TOKEN);
            this.$router.push('/');
//          }
//        });
      }
    },
    computed: {
      state() {
        return this.$store.state.app;
      }
    },
    updated() {
      this.$nextTick(() => {
        if (this.$refs.mainMenus) {
          this.$refs.mainMenus.updateActiveName();
        }
      });
    }
  };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .header{
    .ivu-layout-header {
      background: #292E4E;
      .ivu-menu-dark{
        background: #292E4E;
      }
    }
    .select_lang {
      font-size: 14px;
      position: relative;
      z-index: 999;
      cursor: pointer;
      color: #C7CCE6;
      dt {
        height: 20px;
        /*border: 1px solid;*/
        /*border-radius: 20px;*/
        line-height: 20px;
        padding: 0 12px;
        display: inline-block;
      }
      dd {
        display: none;
        position: absolute;
        top: 56px;
        width: 220px;
        right: 0;
        background: #292E4E;
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.4);
        border-radius: 3px;
        a {
          float: left;
          color: #C7CCE6;
          width: 50%;
          height: 40px;
          line-height: 40px;
          text-align: center;
          font-family: "PingFang SC", "Helvetica", "Noto Sans KR", serif;
        }
        a:hover {
          background: #1e2235;
          color: #7A98F7;
        }
      }
      &:hover {
        color: #7A98F7;
        dt:after {
          color: #7A98F7;
        }
        dd {
          display: block;
        }
        i.ivu-icon-arrow-down-b {
          transform: scale(1) rotate(180deg)
        }
      }
    }

    .head-user {
      position: relative;
      cursor: pointer;
      color: #C7CCE6;
      font-size: 14px;
      span {
        padding: 0 8px
      }
      .head-user-list {
        display: none;
        position: absolute;
        padding: 10px 0;
        z-index: 999;
        top: 56px;
        background: #262a42;
        box-shadow: 0 8px 16px rgba(0, 0, 0, .4);
        border-radius: 3px;
        width: 100%;
        min-width: 150px;
        li {
          height: 40px;
          font-size: 14px;
          padding-left: 20px;
          &:last-of-type a {
            border-bottom: none
          }
          &:hover {
            background: #1e2235
          }
          a {
            padding: 0;
            color: #C7CCE6;
            line-height: 40px;
            display: block;
            border-bottom: 1px solid #1f2943
          }
          i {
            padding-right: 14px
          }
        }
      }

      &:hover {
        span.title {
          color: #7a98f7;
          i.ivu-icon-arrow-down-b {
            transform: scale(1) rotate(180deg)
          }
        }
        .head-user-list {
          display: block
        }
      }
    }
  }
</style>
