package com.meimeitech.sys.swagger.service;

import com.meimeitech.sys.swagger.model.DefaultResponse;
import com.meimeitech.sys.swagger.model.EmailActivateReq;
import com.meimeitech.sys.swagger.model.EmailActivateSendReq;
import com.meimeitech.sys.swagger.model.EmailResetReq;
import com.meimeitech.sys.swagger.model.EmailResetSendReq;
import com.meimeitech.sys.swagger.model.RegisterReq;
import com.meimeitech.sys.swagger.model.UserInfoResponse;

public interface UserService{

    DefaultResponse activateEmail(EmailActivateReq emailActivateReq );

    UserInfoResponse info();

    DefaultResponse register(RegisterReq registerReq );

    DefaultResponse resetPass(EmailResetReq emailResetReq );

    DefaultResponse sendActivateEmail(EmailActivateSendReq emailActivateSendReq );

    DefaultResponse sendResetEmail(EmailResetSendReq emailResetSendReq );
}
