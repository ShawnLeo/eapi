/** 常量文件 */
import {fetch} from './fetch';

const INTERFACE = '/interface';

const DATAMODEL = '/datamodel';

const PROJECT = '/project';

const TAG = '/tag';


const USER = '/user';

const GENERATOR = '/generator';


/** ******* 项目-start ******** **/
// 获取项目列表
export const getProjectList = (data, callback) => fetch(PROJECT + '/list', {callback, reqParams: data}, 'GET');
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
export const exportSwaggerJson = (data, callback) => fetch('/swagger/export/' + data.projectId, {callback}, 'GET', 'blob');
/** ******* 项目-start ******** **/

/** ******* 接口-start ******** **/
// 更新接口
export const updateInterface = (data, callback) => fetch(INTERFACE + '/update', {callback, reqBody: data}, 'POST');
// 修改接口状态
export const changeStatus = (data, callback) => fetch(INTERFACE + '/status/change', {callback, reqBody: data}, 'POST');
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
// 复制接口
export const copyInterface = (data, callback) => fetch(INTERFACE + '/copy', {callback, reqBody: data}, 'POST');
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
// 注册
export const register = (data, callback) => fetch(USER + '/register', {callback, reqBody: data}, 'POST');
// 激活邮件
export const emailActivate = (data, callback) => fetch(USER + '/email/activate', {callback, reqBody: data, doNotToast: true}, 'POST');
// 发送激活邮件
export const emailActivateSend = (data, callback) => fetch(USER + '/email/activate/send', {callback, reqBody: data}, 'POST');
// 发送重置邮件
export const emailResetSend = (data, callback) => fetch(USER + '/email/reset/send', {callback, reqBody: data}, 'POST');
// 重置密码
export const emailReset = (data, callback) => fetch(USER + '/email/reset', {callback, reqBody: data}, 'POST');
/** ******* 用户-end ******** **/

/** ******* 项目组-start ******** **/
// 查询项目组
export const groupGet = (data, callback) => fetch('/group', {callback, reqParams: data}, 'GET');
// 添加项目组
export const groupAdd = (data, callback) => fetch('/group', {callback, reqBody: data}, 'POST');
// 修改项目组
export const groupUpdate = (data, callback) => fetch('/group', {callback, reqBody: data}, 'PUT');
// 删除项目组
export const groupDelete = (data, callback) => fetch('/group', {callback, reqParams: data}, 'DELETE');
// 我的项目组列表
export const groupList = (callback) => fetch('/group/list', {callback}, 'GET');
// 创建者转交项目组
export const groupTransfer = (data, callback) => fetch('/group/transfer', {callback, reqParams: data}, 'GET');
// 添加项目组成员
export const groupUserAdd = (data, callback) => fetch('/group/user', {callback, reqBody: data}, 'POST');
// 项目组成员列表
export const groupUserList = (data, callback) => fetch('/group/user/list', {callback, reqParams: data}, 'GET');
// 退出项目组
export const groupUserQuit = (data, callback) => fetch('/group/user/quit', {callback, reqParams: data}, 'GET');
// 退出项目组
export const groupUserUpdate = (data, callback) => fetch('/group/user/update', {callback, reqBody: data}, 'POST');
// 查询用户
export const groupUserQuery = (data, callback) => fetch('/group/user/query/' + data.username, {callback, reqParams: {groupId: data.groupId}}, 'GET');
// 当前用户在某项目组下的角色
export const getCurrUserRole = (data, callback) => fetch('/group/user/role', {callback, reqParams: data}, 'GET');
/** ******* 项目组-end ******** **/

export const generatorDatabaseAll = (data, callback) => fetch(GENERATOR + '/database/all', {callback, reqBody: data}, 'POST');

export const generatorDatabaseTest = (data, callback) => fetch(GENERATOR + '/database/test', {callback, reqBody: data}, 'POST');

export const generatorDatabaseGen = (data, callback) => fetch(GENERATOR + '/database/gen', {callback, reqBody: data}, 'POST');

export const generatorDatabaseDownload = (data, callback) => fetch(GENERATOR + '/database/gen', {callback, reqParams: data}, 'GET','blob');

export const generatorSwaggerGen = (data, callback) => fetch(GENERATOR + '/swagger/gen', {callback, reqBody: data}, 'POST');

export const generatorSwaggerDownload = (data, callback) => fetch(GENERATOR + '/swagger/gen', {callback, reqParams: data}, 'GET','blob');

export const generateTable = (name, rowNum, data, callback) => fetch(GENERATOR + `/vue/table/${name}/${rowNum}`, {callback, reqBody: data}, 'POST');

export const getEntityData = (data,callback) => fetch(GENERATOR + `/vue/json`, {callback, reqParams: data}, 'POST');
