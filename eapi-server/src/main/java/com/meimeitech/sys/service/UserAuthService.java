package com.meimeitech.sys.service;

import com.meimeitech.sys.entity.UserAuth;
import com.meimeitech.sys.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    /**
     * 根据用户登陆账号查询认证账户
     * @param authId 账号
     * @return
     */
    public UserAuth findByAuthId(String authId){
        UserAuth userAuth = this.userAuthRepository.findByAuthId(authId);
        return userAuth;
    }

}
