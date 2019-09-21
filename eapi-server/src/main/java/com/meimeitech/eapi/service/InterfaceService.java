package com.meimeitech.eapi.service;


import com.meimeitech.common.RetCode;
import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.Response;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.eapi.entity.*;
import com.meimeitech.eapi.model.InterfaceVo;
import com.meimeitech.eapi.repository.InterfaceRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InterfaceService {
    private static Logger logger = LoggerFactory.getLogger(InterfaceService.class);

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private InfoService infoService;

    public Response list(String projectId) {
        return Response.success(interfaceRepository.findAllByProjectIdOrderByPath(projectId));
    }

    /**
     * 创建接口
     *
     * @param interfaceVo
     * @return
     */
    @Transactional
    public Response create(InterfaceVo interfaceVo) {
        UserSession user = UserContextHolder.getContext();

        Interface _interface = new Interface();
        BeanUtils.copyProperties(interfaceVo, _interface);
        _interface.setCreater(user.getId().toString());
        _interface.setCreaterUserName(user.getLoginName());
        _interface.setCreateTime(new Date());
        interfaceVo.getTagIds().forEach(tagId -> {
            Tag tag = new Tag();
            tag.setId(tagId);
            _interface.getTags().add(tag);
        });
        interfaceRepository.save(_interface);
        infoService.createDefaultResponse(_interface);
        return Response.success("success");
    }

    /**
     * 检查接口是否存在
     *
     * @param interfaceVo
     * @return
     */
    public Response checkExists(InterfaceVo interfaceVo) {
        long count = interfaceRepository.count((Specification<Interface>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("path"), interfaceVo.getPath()));
            predicates.add(criteriaBuilder.equal(root.get("method"), interfaceVo.getMethod()));
            predicates.add(criteriaBuilder.equal(root.get("projectId"), interfaceVo.getProjectId()));
            if (interfaceVo.getId() != null) {
                predicates.add(criteriaBuilder.notEqual(root.get("id"), interfaceVo.getId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return Response.success(count > 0);
    }

    /**
     * 批量删除
     *
     * @param iterfaces
     * @return
     */
    @Transactional
    public Response deleteInBatch(List<Interface> iterfaces) {
        interfaceRepository.deleteInBatch(iterfaces);
        iterfaces.forEach(anInterface -> {
            infoService.deleteInfos(anInterface);
        });
        return Response.success("success");
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Response getById(String id) {
        Interface _interface = interfaceRepository.findById(id).get();
        InterfaceVo interfaceVo = new InterfaceVo();
        BeanUtils.copyProperties(_interface, interfaceVo);
        _interface.getTags().forEach(tag -> {
            interfaceVo.getTagIds().add(tag.getId());
        });
        infoService.getRequestInfos(id, interfaceVo);
        infoService.getResponseInfos(id, interfaceVo);
        return Response.success(interfaceVo);
    }

    /**
     * 根据Id查询
     *
     * @param interfaceVo
     * @return
     */
    @Transactional
    public Response update(InterfaceVo interfaceVo) {
        Interface _interface = new Interface();
        BeanUtils.copyProperties(interfaceVo, _interface);
        interfaceVo.getTagIds().forEach(tagId -> {
            Tag tag = new Tag();
            tag.setId(tagId);
            _interface.getTags().add(tag);
        });
//
        // 删除请求数据项
        infoService.deleteRequestInfo(interfaceVo.getRequestInfos(), interfaceVo.getId());

        // 请求数据更新
        infoService.saveRequestInfo(interfaceVo);

        // 删除响应数据项
        infoService.deleteResponseInfo(interfaceVo.getResponseInfos(), interfaceVo.getId());
        // 响应数据更新
        infoService.saveResponseInfo(interfaceVo);

        interfaceRepository.save(_interface);

        return Response.success("success");
    }

    /**
     * 根据Ids删除
     *
     * @param projectId
     */
    public void deleteByProjectId(String projectId) {
        deleteInBatch(interfaceRepository.findAllByProjectIdOrderByPath(projectId));
    }

    /**
     * 修改状态
     *
     * @param interfaceVos
     * @return
     */
    @Transactional
    public Response changeStatus(List<InterfaceVo> interfaceVos) {

        interfaceVos.forEach(interfaceVo -> {
            interfaceRepository.changeStatus(interfaceVo.getId(), interfaceVo.getStatus());
        });

        return Response.success("");
    }

    @Transactional
    public Response copy(InterfaceVo interfaceVo) {

        if (StringUtils.isEmpty(interfaceVo.getId())) {
            return Response.response(RetCode.VALIDATEERROR);
        }

        String copyId = interfaceVo.getId();

        UserSession user = UserContextHolder.getContext();

        Interface _interface = new Interface();

        BeanUtils.copyProperties(interfaceVo, _interface);
        _interface.setId(null);
        _interface.setCreater(user.getId().toString());
        _interface.setCreaterUserName(user.getLoginName());
        _interface.setCreateTime(new Date());
        interfaceVo.getTagIds().forEach(tagId -> {
            Tag tag = new Tag();
            tag.setId(tagId);
            _interface.getTags().add(tag);
        });
        interfaceRepository.save(_interface);

//        InterfaceVo copyFrom = (InterfaceVo) this.getById(copyId).getBody();

        infoService.copyRequestInfo(copyId, _interface.getId());

        infoService.copyResponseInfo(copyId, _interface.getId());

        return Response.success("");
    }

}
