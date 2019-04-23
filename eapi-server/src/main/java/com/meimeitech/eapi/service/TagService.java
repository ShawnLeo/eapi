package com.meimeitech.eapi.service;


import com.google.common.collect.Lists;
import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.Response;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.eapi.entity.Interface;
import com.meimeitech.eapi.entity.Tag;
import com.meimeitech.eapi.repository.InterfaceRepository;
import com.meimeitech.eapi.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TagService {
    private static Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private InterfaceRepository interfaceRepository;

    public Response list(String projectId) {
       return Response.success(tagRepository.findAllByProjectId(projectId));
    }

    public Response create(Tag tag) {
        UserSession user = UserContextHolder.getContext();
        tag.setCreater(user.getId().toString());
        tag.setCreaterUserName(user.getLoginName());
        tag.setCreateTime(new Date());
        return Response.success(tagRepository.save(tag));
    }

    public Response checkExists(Tag tag) {
        long count = tagRepository.count((Specification<Tag>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("name"), tag.getName()));
            predicates.add(criteriaBuilder.equal(root.get("projectId"), tag.getProjectId()));
            if ( tag.getId() != null ) {
                predicates.add(criteriaBuilder.notEqual(root.get("id"), tag.getId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return Response.success(count > 0);
    }

    public Response deleteInBatch(List<Tag> tags) {
        for (int i = 0; i < tags.size(); i++ ) {
            Tag tag = tags.get(i);
            long interfaces = interfaceRepository.count((Specification<Interface>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                SetJoin<Interface, Tag> join= root.join(root.getModel().getSet("tags", Tag.class), JoinType.LEFT);
                predicates.add( criteriaBuilder.equal(join.get("id"), tag.getId()));
                predicates.add( criteriaBuilder.equal(root.get("projectId"), tag.getProjectId()));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            });
            if (interfaces > 0) {
                return Response.error("[" + tag.getName() + "]标签下存在接口,无法删除");
            }
        }
        tagRepository.deleteInBatch(tags);
        return Response.success("SUCCESS");
    }

    public void deleteByProjectId(String projectId) {
        deleteInBatch(tagRepository.findAllByProjectId(projectId));
    }
}
