import Vue from 'vue';
import App from './App.vue';

import iView from 'iview';
import store from './store/store';
import i18n from './locale';

import 'iview/dist/styles/iview.css';
import './assets/css/cover.less';
import './assets/css/common.less';
import './assets/css/layout.less';

import router from './router';

Vue.config.productionTip = false;

Vue.use(iView);

new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app');
