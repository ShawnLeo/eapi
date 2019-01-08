package com.meimeitech.eapi.service;


import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.Response;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.eapi.entity.DataModel;
import com.meimeitech.eapi.repository.DataModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static com.meimeitech.eapi.consts.DataModelType.*;

@Service
public class DataModelService {
    private static Logger logger = LoggerFactory.getLogger(DataModelService.class);

    @Autowired
    DataModelRepository dataModelRepository;

    public Response findByTypeAndProjectId(String type, String projectId) {
        if (type.equals(SYSTEM_TYPE)) {
            return Response.success(dataModelRepository.findByTypeOrderByName(type));
        }
        return Response.success(dataModelRepository.findByTypeAndProjectIdOrderByName(type, projectId));
    }

    public Response create(DataModel dataModel) {
        UserSession user = UserContextHolder.getContext();

        dataModel.setType(CUSTOM_TYPE);
        dataModel.setCreater(user.getId().toString());
        dataModel.setCreaterUserName(user.getLoginName());
        dataModel.setCreateTime(new Date());
        dataModelRepository.save(dataModel);
        deepSetParent(dataModel, dataModel.getChildren());
        dataModelRepository.save(dataModel);
        return Response.success("SUCCESS");
    }

    public Response checkExists(DataModel dataModel) {
        long count = dataModelRepository.count((Specification<DataModel>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("name"), dataModel.getName()));
            predicates.add(criteriaBuilder.equal(root.get("type"), CUSTOM_TYPE));
            predicates.add(criteriaBuilder.equal(root.get("projectId"), dataModel.getProjectId()));
            if ( dataModel.getId() != null ) {
                predicates.add(criteriaBuilder.notEqual(root.get("id"), dataModel.getId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return Response.success(count > 0);
    }

    @Transactional
    public Response update(DataModel dataModel) {
        // delete old datamodel
        DataModel oldModel = dataModelRepository.findById(dataModel.getId()).get();
        this.deleteInBatch(oldModel.getChildren());

        // deep set
        deepSetParent(dataModel, dataModel.getChildren());

        // update
        dataModelRepository.save(dataModel);

        return Response.success("SUCCESS");
    }

    public Response deleteInBatch(List<DataModel> dataModels) {
        if (dataModels == null || dataModels.size() < 1) {
            return Response.error("数据不存在");
        }
        for (int i = 0; i < dataModels.size(); i++ ) {
            DataModel dataModel = dataModels.get(i);
            long count = dataModelRepository.count((Specification<DataModel>) (root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.equal(root.get("dataType"), dataModel.getId());
                return criteriaBuilder.and(predicate);
            });
            if (count > 0) {
                return Response.error("[" + dataModel.getName() + "] 被引用 ,无法删除");
            }
        }
        deepDelete(dataModels);
        return Response.success("SUCCESS");
    }

    // 迭代添加
    public static void deepSetParent(DataModel parent, List<DataModel> children){
        if (children == null) {
            return;
        }
        children.forEach(dataModel -> {
            dataModel.setParent(parent);
            dataModel.setType(UNIT_TYPE);
            dataModel.setProjectId(parent.getProjectId());
            dataModel.setCreater("admin");
            dataModel.setCreateTime(new Date());
            if(dataModel.getChildren() != null && dataModel.getChildren().size() > 0){
                deepSetParent(dataModel, dataModel.getChildren());
            }
        });
    }

    // 迭代删除
    private void deepDelete(List<DataModel> children){
        if (children == null) {
            return;
        }
        children.forEach(dataModel -> {
            if(dataModel.getChildren() != null && dataModel.getChildren().size() > 0){
                deepDelete(dataModel.getChildren());
            }
            dataModelRepository.deleteById(dataModel.getId());
        });
    }

    public Response findById(String id) {
        return Response.success(dataModelRepository.findById(id));
    }

    public void  deleteByProjectId(String projectId) {
        deleteInBatch(dataModelRepository.findByTypeAndProjectIdOrderByName(CUSTOM_TYPE, projectId));
    }
}
