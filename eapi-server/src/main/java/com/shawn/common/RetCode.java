package com.shawn.common;

/**
 * <B>系统名称：</B>美美金融平台<BR>
 * <B>模块名称：</B>平台公共模块<BR>
 * <B>中文类名：</B>返回码<BR>
 * <B>概要说明：</B><BR>
 *
 * @author 北京鹏润美美科技有限公司
 * @since 2017年6月30日
 */
public enum RetCode {

    SUCCESS("0", "success"),ACCESSDENY("-1", "访问拒绝"), VALIDATEERROR("-2", "验证错误"), REMEMBERMELOGIN("-8", "记住密码自动登陆失败") , INTERNALEXCEP("-9", "内部异常"), TIMEOUT("-100", "超时"), NOLOGIN("-101", "请登录"), LOGINED("-102",
            "您的账号已在其他地方登录，当前账号被登出"), USERORPWDERR("-103", "用户名或密码错误"),
    // USERLOCKED("-104","账户被锁定"),
    // LOCKEDPLEASE("-105","账号已锁定，12小时后自动解锁立即解锁，请联系客服：400-0666-088\n"),
    VALIDATEFAIL("-106", "验证失败"), COMMITDUP("-107", "重复提交"), RELEASEDUP("-108", "重放"), DECRYPTEXCEP("-109", "解密异常"), ENCRYPTEXCEP("-110",
            "加密异常"), SHOWCAPTCHA("-111", "显示图形验证码"), CAPTCHAERR("-112",
                    "图形验证码输入有误"), DENYFORMORE("-113", "请求次数超限，操作被拒绝"), NOPERMISSION("-114", "权限不足"), GOMEBINDFAIL("-120", "Gome用户绑定失败"),

    NOACCOUNTINFO("-200", "未查到账户信息"), NOUSERINVESTSURVER("-111", "未查到用户风险评估信息"), NOPAYPASSWORD("-201", "未设置交易密码"), EMPTYPAYPASSWORD("-202",
            "用户交易密码为空"), CONSUMPWDONEERR("-203", "交易密码输错1次"), CONSUMPWDTWOERR("-204", "交易密码输错2次"), USERLOCKED("-205", "账户被锁定"), LOCKEDPLEASE("-206",
                    "账号已锁定，12小时后自动解锁立即解锁，请联系客服：400-0666-088\n"), SENDSMSFAIL("-211", "1分钟内已发送过验证码"), NOSMSTEMPLATE("-212",
                            "没找到短信模板"), VERIFYSMSVERIFYCODEFAIL("-213", "请输入正确验证码"), EMPTYVVERIFYCODEFAIL("-214", "验证码为空"),

    BALANCEINSUFFICIENT("-1001", "账户余额不足"), NOTBINDCARD("-1002", "用户未开户"), CREDITLINEINSUFFICIENT("-2001", "授信额度不足"), LEVELLOW("-2002", "用户等级不足"),
    // CONSUMPWDONEERR("-2003","消费密码输错1次"),
    // CONSUMPWDTWOERR("-2004","消费密码输错2次"),
    CREDITUSERLOCKED("-2005", "授信账户锁定");

    private String code;

    private String message;

    private RetCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
