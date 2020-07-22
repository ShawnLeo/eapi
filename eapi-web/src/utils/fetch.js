import axios from 'axios';
import router from '../router';
import {realBaseUrl} from './env';
import {Message} from 'iview';
import {removeStore, getStore} from './storage';
import {ACCESS_TOKEN, USER_INFO} from './const';

axios.interceptors.request.use(
    config => {
      // 设置token
      const token = getStore(ACCESS_TOKEN);
      token && (config.headers.Authorization = 'Bearer ' + token);
      // config.headers['client-id'] = appSecret;
      config.metadata = {startTime: new Date()};
      // console.log(config.metadata);
      return config;
    },
    error => {
      return Promise.error(error);
    });

export const responseHandler = function (response, options, metadata) { // 公共响应码集中处理

  let code = response.header.code;
  code = Number.parseInt(code, 10);
  if (code === 0 || code === 200) {
    options.callback(response, metadata);
    return;
  }
  switch (code) {
    case -101: // 请登录
      removeStore(USER_INFO);
      removeStore(ACCESS_TOKEN);
      router.push({path: '/user/login'});
      break;
    default:
      if (options && options.doNotToast) { // 不要弹Toast，错误代码自己处理
        options.callback(response, metadata);
      } else { // 默认toast处理
        Message.error(response.header.message);
      }
      break;
  }
};

export const fetch = (url = '', options = {}, type = 'GET',
    responseType = 'json') => {

  axios.request({
    url: url,
    baseURL: realBaseUrl,
    method: type.toLowerCase(),
    params: options.reqParams || {}, // 业务params 请求参数
    data: options.reqBody || {},
    responseType: responseType
  }).then((response) => {

    let metadata = {
      duration: new Date() - response.config.metadata.startTime,
      httpStatus: response.status
    };
    console.groupCollapsed('[' + url + ']返回信息');
    console.info('状态：' + metadata.httpStatus);
    console.info('耗时：' + metadata.duration + ' ms');
    console.info(response.data);
    console.groupEnd();

    if (responseType === 'json') { // 统一处理数据
      responseHandler(response.data, options, metadata);
    } else {
      options.callback(response.data);
    }
  }).catch(function (error) {
    errorHandler(url, options, error);
  });
};

const errorHandler = (url, options, error) => {

  let metadata = {};

  let e = {code: '', message: ''};
  if (error.response) {
    metadata.duration = new Date() - error.response.config.metadata.startTime;
    metadata.httpStatus = error.response.status;

    console.groupCollapsed('[' + url + '] 请求出错');
    console.info('状态：' + metadata.httpStatus);
    console.info('耗时：' + metadata.duration + ' ms');
    console.error('错误信息：' + error);
    console.groupEnd();

    e.code = error.response.status;
    e.message = error.response.statusText;
    switch (e.code) { // 异常情况
      case 400:
        e.message = '请求信息有误';
        break;
      case 401:
        e.message = '权限不足';
        break;
      case 404:
        e.message = '数据不存在';
        break;
      case 405:
        e.message = '错误的请求类型';
        break;
      case 500:
        e.message = '服务器开小差了，请稍后再试';
        break;
      case 501:
        console.log('接口[' + url + ']还未实现');
        e = error.response.data;
        break;
      case 503:
        e.message = '系统维护，请稍后再试';
        break;
    }
  } else {
    e.code = 600;
    e.message = (error.message === 'Network Error') ? '网络异常, 请检查网络稍后再试'
        : '数据处理错误';
    console.groupCollapsed('[' + url + '] 请求出错');
    console.error('错误信息：' + error.message);
    console.groupEnd();
  }
  responseHandler(e, options, metadata);
};
