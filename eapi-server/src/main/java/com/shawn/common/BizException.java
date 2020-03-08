package com.shawn.common;

/**
 * <B>系统名称：</B>美美金融平台<BR>
 * <B>模块名称：</B>通用模块<BR>
 * <B>中文类名：</B>业务异常<BR>
 * <B>概要说明：</B>业务异常<BR>
 *
 * @author 北京鹏润美美科技有限公司
 * @since 2017年07月21日 09时49分
 */
public class BizException extends Exception{

    public BizException(){

    }

    public BizException(String message){
        super(message);
    }

    public BizException(String message,Throwable e){
        super(message,e);
    }

}
