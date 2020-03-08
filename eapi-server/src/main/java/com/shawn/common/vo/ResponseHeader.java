package com.shawn.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <B>系统名称：</B>美美金融平台<BR>
 * <B>模块名称：</B>平台公共模块<BR>
 * <B>中文类名：</B>用请求响应头对象<BR>
 * <B>概要说明：</B><BR>
 *
 * @author 北京鹏润美美科技有限公司
 * @since 2017年6月30日
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHeader {

    //返回响应代码
    private String code;

    //错误信息说明内容，无错误为空
    private String message;

    //错误信息详细内容
    private String desc;

    //返回响应时的时间戳
    private Long timestamp = System.currentTimeMillis();

    //重复处理次数
    private int repeat = 0;

    //数据签名，目前暂时预留为空
    private String signature;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
