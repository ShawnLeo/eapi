package com.shawn.sys.swagger.service;

import com.shawn.sys.swagger.model.DefaultResponse;
import com.shawn.sys.swagger.model.EmailActivateReq;
import com.shawn.sys.swagger.model.EmailActivateSendReq;
import com.shawn.sys.swagger.model.EmailResetReq;
import com.shawn.sys.swagger.model.EmailResetSendReq;
import com.shawn.sys.swagger.model.RegisterReq;
import com.shawn.sys.swagger.model.UserInfoResponse;

public interface UserService{

    DefaultResponse activateEmail(EmailActivateReq emailActivateReq );

    UserInfoResponse info();

    DefaultResponse register(RegisterReq registerReq );

    DefaultResponse resetPass(EmailResetReq emailResetReq );

    DefaultResponse sendActivateEmail(EmailActivateSendReq emailActivateSendReq );

    DefaultResponse sendResetEmail(EmailResetSendReq emailResetSendReq );
}
