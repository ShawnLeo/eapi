package com.meimeitech.sys.service;

import com.meimeitech.sys.entity.UserAuth;
import com.meimeitech.sys.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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

    public List<UserAuth> findByUsername(String username, List<String> groupUserIds) {

         Page page = userAuthRepository.findAll((Specification<UserAuth>) (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(root.get("authId"), "%" + username + "%"));
            if (!CollectionUtils.isEmpty(groupUserIds)) { // 不包含已存在的用户
                predicates.add(criteriaBuilder.isTrue(root.get("user").get("id").in(groupUserIds).not()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));

        }, PageRequest.of(0, 20));

        return page.getContent();

    }
}
