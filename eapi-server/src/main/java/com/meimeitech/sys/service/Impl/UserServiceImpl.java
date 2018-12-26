package com.meimeitech.sys.service.Impl;

import com.google.common.collect.Maps;
import com.meimeitech.common.RetCode;
import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.security.crypto.MD5;
import com.meimeitech.sys.entity.RegUser;
import com.meimeitech.sys.entity.ResetUser;
import com.meimeitech.sys.entity.User;
import com.meimeitech.sys.entity.UserAuth;
import com.meimeitech.sys.repository.RegUserRepository;
import com.meimeitech.sys.repository.ResetUserRepository;
import com.meimeitech.sys.repository.UserAuthRepository;
import com.meimeitech.sys.repository.UserRepository;
import com.meimeitech.sys.service.MailService;
import com.meimeitech.sys.swagger.model.*;
import com.meimeitech.sys.swagger.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${mail.register:/#/user/active}")
    private String registerUrl;

    @Value("${mail.forget:/#/user/reset}")
    private String forgetUrl;

    @Value("${host.server: http://localhost:8080}")
    private String serverUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private RegUserRepository regUserRepository;

    @Autowired
    private ResetUserRepository resetUserRepository;

    @Autowired
    private MailService mailService;

    @Override
    public UserInfoResponse info() {
        UserSession userSession = UserContextHolder.getContext();

        UserVO userVO = new UserVO();
        User user = userRepository.getOne(userSession.getId());
        BeanUtils.copyProperties(userVO,user);
        userVO.setId(userSession.getId().toString());
        userVO.setAuthId(userSession.getLoginName());
        userVO.setRealName(user.getRealName());
        UserInfoResponse response = new UserInfoResponse();

        ResponseHeader header = new ResponseHeader();
        header.code(RetCode.SUCCESS.getCode());
        response.header(header).body(userVO);

        return response;
    }

    @Override
    @Transactional
    public DefaultResponse register(RegisterReq registerReq) {

        DefaultResponse response = new DefaultResponse();
        ResponseHeader header = new ResponseHeader();

        if (isEmailRegister(registerReq.getEmail())) {
            logger.info("Email [{}] has registered.", registerReq.getEmail());

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("Email [" + registerReq.getEmail() + "] has registered.");
            response.header(header);

            return response;
        }

        RegUser regUser = regUserRepository.findByEmail(registerReq.getEmail());

        if (regUser == null) {
            regUser = new RegUser();
            regUser.setEmail(registerReq.getEmail());
            regUser.setCreateTime(new Date());
            regUser.setPassword(MD5.encodeByMd5AndSalt(registerReq.getPassword()));
//            regUser.setActivateCode(UUID.randomUUID().toString());
//            regUserRepository.save(regUser);
        }
        regUser.setActivateCode(UUID.randomUUID().toString());
        regUser.setPassword(MD5.encodeByMd5AndSalt(registerReq.getPassword()));

        regUserRepository.save(regUser);

        sendRegisterEmail(registerReq.getEmail(), regUser.getActivateCode());

        header.code(RetCode.SUCCESS.getCode());
        response.header(header);

        return response;
    }

    @Override
    public DefaultResponse resetPass(EmailResetReq emailResetReq) {

        DefaultResponse response = new DefaultResponse();
        ResponseHeader header = new ResponseHeader();

        ResetUser resetUser = resetUserRepository.findByResetCode(emailResetReq.getResetCode());

        if (resetUser == null) {
            logger.warn("resetcode [{}] not exists.", emailResetReq.getResetCode());

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("resetcode ["+ emailResetReq.getResetCode() +"] not exists.");

            response.header(header);
            return response;
        }


        long currentTime = System.currentTimeMillis() - 30 * 60 * 1000;

        Date date = new Date(currentTime);

        if (resetUser.getLastSendTime().compareTo(date) < 0) {
            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("resetcode has expired.");
            response.header(header);
            return response;
        }


        UserAuth userAuth = userAuthRepository.findByAuthId(resetUser.getEmail());


        String enPassword = MD5.encodeByMd5AndSalt(emailResetReq.getPassword());

        if (enPassword.equals(userAuth.getAuthPass())) {
            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("The new password cannot be the same as the original password.");
            response.header(header);

            return response;
        }


        userAuth.setAuthPass(enPassword);

        userAuthRepository.save(userAuth);

        resetUserRepository.delete(resetUser);

        header.code(RetCode.SUCCESS.getCode());
        response.header(header);

        return response;
    }

    @Override
    @Transactional
    public DefaultResponse activateEmail(EmailActivateReq emailActivateReq) {

        DefaultResponse response = new DefaultResponse();
        ResponseHeader header = new ResponseHeader();

        if (StringUtils.isEmpty(emailActivateReq.getActivateCode())) {
            logger.warn("activate failure activateCode is null.");

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("activate failure activateCode is null.");
            response.header(header);
            return response;
        }

        RegUser regUser = regUserRepository.findByActivateCode(emailActivateReq.getActivateCode());

        if (regUser == null) {
            logger.warn("activate failure activateCode not exists.");

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("activate failure activateCode not exists.");
            response.header(header);
            return response;
        }

        User user = convertToUser(regUser);

        UserAuth userAuth = convertToUseAuth(regUser);

        userRepository.save(user);

        userAuth.setUser(user);

        userAuthRepository.save(userAuth);

        header.code(RetCode.SUCCESS.getCode());
        header.message(RetCode.SUCCESS.getMessage());
        response.header(header);
        return response;
    }

    @Override
    @Transactional
    public DefaultResponse sendActivateEmail(EmailActivateSendReq emailActivateSendReq) {

        DefaultResponse response = new DefaultResponse();
        ResponseHeader header = new ResponseHeader();

        if (isEmailRegister(emailActivateSendReq.getEmail())) {
            logger.warn("email [{}] has registered.", emailActivateSendReq.getEmail());

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("email " + emailActivateSendReq.getEmail() + " has registered.");
            response.header(header);

            return response;
        }

        RegUser regUser = regUserRepository.findByEmail(emailActivateSendReq.getEmail());

        if (regUser == null) {
            logger.warn("email [{}] not exists.", emailActivateSendReq.getEmail());

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("email [" + emailActivateSendReq.getEmail() + "] not exists.");
            response.header(header);

            return response;
        } else {
            regUser.setActivateCode(UUID.randomUUID().toString());
            regUserRepository.save(regUser);

            sendRegisterEmail(emailActivateSendReq.getEmail(), regUser.getActivateCode());

            header.code(RetCode.SUCCESS.getCode());
            response.header(header);
        }

        return response;
    }

    @Override
    public DefaultResponse sendResetEmail(EmailResetSendReq emailResetSendReq) {

        DefaultResponse response = new DefaultResponse();
        ResponseHeader header = new ResponseHeader();

        String email = emailResetSendReq.getEmail();

        UserAuth userAuth = userAuthRepository.findByAuthId(email);

        if (userAuth == null) {
            logger.warn("Email [{}] not exists.", email);

            header.code(RetCode.VALIDATEERROR.getCode());
            header.message("Email [" + email + "]  not exists.");
            response.header(header);

            return response;
        }

        ResetUser resetUser = resetUserRepository.findByEmail(email);

        if (resetUser == null) {
            resetUser = new ResetUser();
            resetUser.setEmail(email);
        }

        resetUser.setLastSendTime(new Date());
        resetUser.setResetCode(UUID.randomUUID().toString());
        resetUserRepository.save(resetUser);

        sendResetEmail(email, resetUser.getResetCode());

        header.code(RetCode.SUCCESS.getCode());
        response.header(header);

        return response;
    }

    private User convertToUser(RegUser regUser) {
        User user = new User();
        user.setEmail(regUser.getEmail());
        user.setStatus("1");
        user.setCreateTime(new Date());
        return user;
    }

    private UserAuth convertToUseAuth(RegUser regUser) {
        UserAuth userAuth = new UserAuth();
        userAuth.setAuthId(regUser.getEmail());
        userAuth.setAuthPass(regUser.getPassword());
        return userAuth;
    }

    private boolean isEmailRegister(String email) {
        UserAuth auth = userAuthRepository.findByAuthId(email);
        return auth != null;
    }

    private void sendRegisterEmail(String email, String activateCode) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("activate_link", serverUrl + registerUrl + "?activateCode=" + activateCode);
        Locale local = new Locale("zh_CN");
        mailService.send(new String[] {email}, "激活账号", "activate_account", local, context);
    }

    private void sendResetEmail(String email, String activateCode) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("resetpwd_link", serverUrl + forgetUrl + "?activateCode=" + activateCode);
        Locale local = new Locale("zh_CN");
        mailService.send(new String[] {email}, "重置密码", "reset_password", local, context);
    }
}
