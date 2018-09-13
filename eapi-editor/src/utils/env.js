/**
 * 配置编译环境和线上环境之间的切换
 */

let baseUrl = 'http://localhost:7050'; // 接口域名
let context = '/v2/api-docs';
let env = process.env.SERVER_ENV;
if (env === 'dev') { // 开发环境，接口地址为测试环境地址
  baseUrl = 'http://localhost:7050';
} else if (env === 'test') { // 测试环境
  // baseUrl = 'http://10.133.255.201:7050';
} else if (env === 'pre') { // 预生产环境
  // baseUrl = 'https://api-pre.meimeitech.com';
} else if (env === 'prod') { // 生产环境
  // baseUrl = 'https://api.meimeitech.com';
}
export {baseUrl, context};
