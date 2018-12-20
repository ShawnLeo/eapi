/** 常量文件 */
import {fetch} from './fetch';

const INTERFACE = '/interface';

const DATAMODEL = '/datamodel';

const PROJECT = '/project';

const TAG = '/tag';


const USER = '/user';

/** ******* 项目-start ******** **/
// 获取项目列表
export const getProjectList = (callback) => fetch(PROJECT + '/list', {callback}, 'GET');
// 根据ID获取项目
export const getProjectById = (data, callback) => fetch(PROJECT + '/' + data.id, {callback}, 'GET');
// 创建项目
export const createProject = (data, callback) => fetch(PROJECT + '/create', {callback, reqBody: data}, 'POST');
// 检查项目名称是否已存在
export const checkProjectExists = (data, callback) => fetch(PROJECT + '/check', {callback, reqBody: data}, 'POST');
// 更新项目
export const updateProject = (data, callback) => fetch(PROJECT + '/update', {callback, reqBody: data}, 'POST');
// 删除项目
export const deleteProjectById = (data, callback) => fetch(PROJECT + '/' + data.id, {callback}, 'DELETE');
// 发布项目
export const publishProject = (data, callback) => fetch(PROJECT + '/publish', {callback, reqBody: data}, 'POST');
// url导入Swagger项目
export const importFromSwaggerUrl = (data, callback) => fetch('/swagger/import/url/' + data.projectId, {callback, reqParams: {
	swaggerUrl: data.swaggerUrl
}}, 'POST');

/** ******* 项目-start ******** **/

/** ******* 接口-start ******** **/
// 更新接口
export const updateInterface = (data, callback) => fetch(INTERFACE + '/update', {callback, reqBody: data}, 'POST');
// 根据ID获取接口
export const getInterfaceById = (data, callback) => fetch(INTERFACE + '/' + data.id, {callback}, 'GET');
// 根据ID删除接口
export const deleteInterfaceById = (data, callback) => fetch(INTERFACE + '/' + data.id, {callback}, 'DELETE');
// 批量删除接口
export const deleteInterfaceInBatch = (data, callback) => fetch(INTERFACE + '/batch', {callback, reqBody: data}, 'DELETE');
// 获取接口列表
export const getInterfaceList = (data, callback) => fetch(INTERFACE + '/list', {callback, reqParams: data}, 'GET');
// 创建接口
export const createInterface = (data, callback) => fetch(INTERFACE + '/create', {callback, reqBody: data}, 'POST');
// 检查接口名称是否已存在
export const checkInterfaceExists = (data, callback) => fetch(INTERFACE + '/check', {callback, reqBody: data}, 'POST');
// 批量删除请求数据
export const deleteRequestInBatch = (data, callback) => fetch(INTERFACE + '/request/batch', {callback, reqBody: data}, 'DELETE');
// 批量删除相应数据
export const deleteResponseInBatch = (data, callback) => fetch(INTERFACE + '/response/batch', {callback, reqBody: data}, 'DELETE');
// 根据ID获取接口请求参数
// export const getRequestInfos = (data, callback) => fetch(INTERFACE + '/request/infos/' + data.id, {callback}, 'GET');
/** ******* 接口-end **********/

/** ******* 数据模型-start ******** **/
// 更新数据模型
export const updateDataModel = (data, callback) => fetch(DATAMODEL + '/update', {callback, reqBody: data}, 'POST');
// 根据ID获取数据模型
export const getDataModelById = (data, callback) => fetch(DATAMODEL + '/' + data.id, {callback}, 'GET');
// 根据ID删除数据模型
export const deleteDataModelInBatch = (data, callback) => fetch(DATAMODEL + '/batch', {callback, reqBody: data}, 'DELETE');
// 获取数据模型
export const getDataModelList = (data, callback) => fetch(DATAMODEL + '/' + data.type + '/list', {callback}, 'GET');
export const getCustomDataModelList = (data, callback) => fetch(DATAMODEL + '/custom/list', {callback, reqParams: data}, 'GET');
// 创建数据模型
export const createDataModel = (data, callback) => fetch(DATAMODEL + '/create', {callback, reqBody: data}, 'POST');
// 检查数据名称是否已存在
export const checkExists = (data, callback) => fetch(DATAMODEL + '/check', {callback, reqBody: data}, 'POST');
/** ******* 数据模型-end ******** **/

/** ******* 标签-start ******** **/
// 获取标签
export const getTagList = (data, callback) => fetch(TAG + '/list', {callback, reqParams: data}, 'GET');
// 创建标签
export const createTag = (data, callback) => fetch(TAG + '/create', {callback, reqBody: data}, 'POST');
// 批量删除接口
export const deleteTagInBatch = (data, callback) => fetch(TAG + '/batch', {callback, reqBody: data}, 'DELETE');
// 检查数据名称是否已存在
export const checkTagExists = (data, callback) => fetch(TAG + '/check', {callback, reqBody: data}, 'POST');
/** ******* 标签-end ******** **/

/** ******* 用户-start ******** **/
// 登陆
export const login = (data, callback) => fetch('/login', {callback, reqParams: data}, 'GET');
// 创建标签
export const getLoginUser = (callback) => fetch(USER + '/info', {callback}, 'GET');
/** ******* 标签-end ******** **/
