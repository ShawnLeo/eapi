/**
 * 配置编译环境和线上环境之间的切换
 */
const baseUrl = process.env.VUE_APP_SERVER_URL; // 接口域名
const context = process.env.VUE_APP_CONTEXT;

const getRealBaseUrl = () => {
  let base = baseUrl;

  if (base.endsWith('/')) { //  去掉最后一个 /
    base = base.substring(0, base.length - 2);
  }

  if (!context.startsWith('/')) {  // context第一个元素添加 /
    return base + '/' + context;
  }
  return base + context;
};

const realBaseUrl = getRealBaseUrl();

export {baseUrl, context, realBaseUrl};
