/**
 * 配置编译环境和线上环境之间的切换
 */

const baseUrl = process.env.VUE_APP_SERVER_URL; // 接口域名
const context = process.env.VUE_APP_CONTEXT;

export {baseUrl, context};
