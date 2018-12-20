package com.meimeitech.sys.service;

import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.sys.entity.User;
import com.meimeitech.sys.repository.UserRepository;
import com.meimeitech.sys.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserVO getUserInfo() {
        UserSession userSession = UserContextHolder.getContext();

        UserVO userVO = new UserVO();
        User user = userRepository.getOne(userSession.getId());
        BeanUtils.copyProperties(userVO,user);
        userVO.setId(userSession.getId());
        userVO.setAuthId(userSession.getLoginName());
        userVO.setRealName(user.getRealName());
        return userVO;
    }
}
