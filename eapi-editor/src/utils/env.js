/**
 * 配置编译环境和线上环境之间的切换
 */

let baseUrl = 'http://localhost:7050'; // 接口域名
let context = '/v2/api-docs';
let env = process.env.NODE_ENV;

if (env === 'development') { // 开发环境
  baseUrl = 'http://localhost:7050';
} else { // 发布环境
  baseUrl = 'http://10.133.255.201:7050';
}

export {baseUrl, context};
