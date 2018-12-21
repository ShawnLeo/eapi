import axios from 'axios';
import i18n from '../locale';
import router from '../router';
import { baseUrl } from './env';
import {Message} from 'iview';
import {removeStore, getStore} from './storage';
import {ACCESS_TOKEN, USER_INFO} from './const';

export const responseHandler = function (response, options) { // 公共响应码集中处理
  let code = response.header.code;
  code = Number.parseInt(code, 10);
  if (code === 0) {
    options.callback(response);
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
        options.callback(response);
      } else { // 默认toast处理
        Message.error(response.header.message);
      }
      break;
  }
};

export const fetch = async (url = '', options = {}, type = 'GET', responseType = 'json') => {
  await axios.request({
    url: url,
    baseURL: baseUrl,
    method: type.toLowerCase(),
    headers: {'Authorization': 'Bearer ' + getStore(ACCESS_TOKEN)},
    params: options.reqParams || {}, // 业务params 请求参数
    data: options.reqBody || {},
		responseType: responseType
  }).then((response) => {
    console.log('====== 返回信息 ======');
    console.log('url：' + url);
    console.log('返回数据：');
    console.log(response.data);
    if (responseType == 'json') {
			responseHandler(response.data, options);
    } else {
			options.callback(response.data);
    }

  }).catch(function (error) {
		errorHandler(url , options,  error);
  });
};

const errorHandler =  (url , options,  error) => {
	console.log('====== 请求出错 ======');
	console.log('url：' + url);
	console.log('错误信息：' + error);
	let e = {header: {code: '', message: ''}};
	if (error.response) {
		e.header.code = error.response.status;
		e.header.message = error.response.statusText;
		switch (e.header.code) { // 异常情况
			case 400:
				e.header.message = i18n.t('responseError.code400');
				break;
			case 401:
				e.header.message = i18n.t('responseError.code401');
				break;
			case 404:
				e.header.message = i18n.t('responseError.code404');
				break;
			case 405:
				e.header.message = i18n.t('responseError.code405');
				break;
			case 500:
				e.header.message = i18n.t('responseError.code500');
				break;
			case 501:
				console.log('接口[' + url + ']还未实现');
				e = error.response.data;
				break;
			case 503:
				e.header.message = i18n.t('responseError.code503');
				break;
		}
	} else {
		e.header.code = 600;
		e.header.message = (error.message === 'Network Error') ? i18n.t('responseError.netError') : i18n.t('responseError.code600');
	}
	responseHandler(e, options);
};