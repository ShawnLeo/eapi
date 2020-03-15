import Vue from 'vue';
import App from './App.vue';

import iView from 'iview';
import VueClipboard from 'vue-clipboard2';
import store from './store/store';
import i18n from './locale';


import animated from 'animate.css';
import 'iview/dist/styles/iview.css';
import './assets/css/cover.less';
import './assets/css/common.less';
import './assets/css/layout.less';

import router from './router';
import VueClipboard from 'vue-clipboard2';
Vue.config.productionTip = false;

Vue.use(VueClipboard);

Vue.use(iView);
Vue.use(animated);
Vue.use(VueClipboard);

new Vue({
  i18n,
  router,
  store,
  render: h => h(App)
}).$mount('#app');
