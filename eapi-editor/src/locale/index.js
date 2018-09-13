import Vue from 'vue';
import VueI18n from 'vue-i18n';

import Locales from './locale';
import zhLocale from 'iview/src/locale/lang/zh-CN';
import enLocale from 'iview/src/locale/lang/en-US';
import zhTLocale from 'iview/src/locale/lang/zh-TW';

Vue.use(VueI18n);

// 多语言配置
const locales = Locales;
const mergeZH = Object.assign(zhLocale, locales['zh-CN']);
const mergeEN = Object.assign(enLocale, locales['en-US']);
const mergeTW = Object.assign(zhTLocale, locales['zh-TW']);

const i18n = new VueI18n({});
i18n.setLocaleMessage('zh-CN', mergeZH);
i18n.setLocaleMessage('en-US', mergeEN);
i18n.setLocaleMessage('zh-TW', mergeTW);

export default i18n;
