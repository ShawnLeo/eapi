package com.shawn.common;

/**
 * <B>系统名称：</B>美美金融平台<BR>
 * <B>模块名称：</B>平台公共模块<BR>
 * <B>中文类名：</B>请求参数对象<BR>
 * <B>概要说明：公共参数</B><BR>
 *
 * @author 北京鹏润美美科技有限公司
 * @since 2017年07月28日 16时38分
 */
public class ComParams {

    // 请求应用代码 如: mmt-finance-ios
    public static final String X_APPLICATION = "xApplication";

    // 请求应用版本号 如：2.4.1
    public static final String X_VERSION = "xVersion";

    // 移动应用发布市场渠道
    public static final String X_CHANNEL = "xChannel";

    // 客户端类型，浏览器等同User-Agent
    public static final String X_CLIENT = "xClient";

    // 设备标识码，网页端可以为空
    public static final String X_DEVICE = "xDevice";

    // 初始服务路径，用于服务嵌套后跟踪溯源
    public static final String X_SERVIVE = "xService";

    // 请求令牌，通过登录时获取，原SessionId
    public static final String X_TOKEN = "xToken";

    // 用户id
    public static final String X_USERID = "xUserId";

    // 用户角色码
    public static final String X_ROLECODE = "xRoleCode";

    //用户手机号
    public static final String X_MOBILE = "xMobile";

    // 请求发送时的时间戳
    public static final String X_TIMESTAMP = "xTimestamp";

    // 重复发送次数
    public static final String X_REPEAT = "xRepeat";

    // 数据签名，目前暂时预留为空
    public static final String X_SIGNATURE = "xSignature";


    // 登陆认证用户名
    public static final String X_LOGINNAME = "xLoginName";

}
