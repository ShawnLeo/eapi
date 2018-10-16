package com.meimeitech.eapi.service;


import com.google.common.collect.Lists;
import com.meimeitech.common.vo.Response;
import com.meimeitech.eapi.consts.ParamInConsts;
import com.meimeitech.eapi.consts.ResponseInConsts;
import com.meimeitech.eapi.dialect.AbstractInfo;
import com.meimeitech.eapi.entity.*;
import com.meimeitech.eapi.model.InterfaceVo;
import com.meimeitech.eapi.repository.DataModelRepository;
import com.meimeitech.eapi.repository.InterfaceRepository;
import com.meimeitech.eapi.repository.RequestInfoRepository;
import com.meimeitech.eapi.repository.ResponseInfoRepository;
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

import static com.meimeitech.eapi.consts.DataModelType.UNIT_TYPE;
import static com.meimeitech.eapi.service.DataModelService.deepSetParent;

@Service
public class InterfaceService {
    private static Logger logger = LoggerFactory.getLogger(InterfaceService.class);

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private RequestInfoRepository requestInfoRepository;

    @Autowired
    private ResponseInfoRepository responseInfoRepository;

    @Autowired
    private DataModelRepository dataModelRepository;

    public Response list(String projectId) {
        return Response.success(interfaceRepository.findAllByProjectIdOrderByPath(projectId));
    }

    @Transactional
    public Response create(InterfaceVo interfaceVo) {
        Interface _interface = new Interface();
        BeanUtils.copyProperties(interfaceVo, _interface);
        _interface.setCreater("admin");
        _interface.setCreateTime(new Date());
        interfaceVo.getTagIds().forEach(tagId -> {
            Tag tag = new Tag();
            tag.setId(tagId);
            _interface.getTags().add(tag);
        });
        interfaceRepository.save(_interface);
        createDefaultResponse(_interface);
        return Response.success("success");
    }


    public Response checkExists(InterfaceVo interfaceVo) {
        long count = interfaceRepository.count((Specification<Interface>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("path"), interfaceVo.getPath()));
            predicates.add(criteriaBuilder.equal(root.get("method"), interfaceVo.getMethod()));
            predicates.add(criteriaBuilder.equal(root.get("projectId"), interfaceVo.getProjectId()));
            if ( interfaceVo.getId() != null ) {
                predicates.add(criteriaBuilder.notEqual(root.get("id"), interfaceVo.getId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return Response.success(count > 0);
    }

    @Transactional
    public Response deleteInBatch(List<Interface> iterfaces) {
        interfaceRepository.deleteInBatch(iterfaces);
        iterfaces.forEach(anInterface -> {
            deleteInfos(anInterface);
        });
        return Response.success("success");
    }

    public Response getById(String id) {
        Interface _interface = interfaceRepository.findById(id).get();
        InterfaceVo interfaceVo = new InterfaceVo();
        BeanUtils.copyProperties(_interface, interfaceVo);
        _interface.getTags().forEach(tag -> {
            interfaceVo.getTagIds().add(tag.getId());
        });
        getRequestInfos(id, interfaceVo);
        getResponseInfos(id, interfaceVo);
        return Response.success(interfaceVo);
    }

    @Transactional
    public Response update(InterfaceVo interfaceVo) {
        Interface _interface = new Interface();
        BeanUtils.copyProperties(interfaceVo, _interface);
        interfaceVo.getTagIds().forEach(tagId -> {
            Tag tag = new Tag();
            tag.setId(tagId);
            _interface.getTags().add(tag);
        });
        // 请求数据更新
        saveRequestInfo(interfaceVo);
        // 响应数据更新
        saveResponseInfo(interfaceVo);

        interfaceRepository.save(_interface);

        return Response.success("success");
    }


    public Response deleteRequestInfosInBatch(List<RequestInfo> requestInfos) {
        requestInfoRepository.deleteInBatch(requestInfos);
        requestInfos.forEach(requestInfo -> {
            deepDelete(requestInfo.getDataModel().getChildren());
            dataModelRepository.delete(requestInfo.getDataModel());
        });
        return Response.success("SUCCESS");
    }

    public Response deleteResponsetInfosInBatch(List<ResponseInfo> responseInfos) {
        responseInfoRepository.deleteInBatch(responseInfos);
        responseInfos.forEach(responseInfo -> {
            deepDelete(responseInfo.getDataModel().getChildren());
            dataModelRepository.delete(responseInfo.getDataModel());
        });
        return Response.success("SUCCESS");
    }

    /**
     * 保存请求参数
     *
     * @param interfaceVo
     */
    private void saveRequestInfo(InterfaceVo interfaceVo) {
        List<RequestInfo> pathParams = interfaceVo.getPathParams();
        if (!pathParams.isEmpty()) { // 路径参数
            saveDataModel(pathParams);
            requestInfoRepository.saveAll(pathParams);
        }
        List<RequestInfo> headers = interfaceVo.getHeaders();
        if (!headers.isEmpty()) { // 头信息
            saveDataModel(headers);
            requestInfoRepository.saveAll(headers);
        }
        List<RequestInfo> querys = interfaceVo.getQuerys();
        if (!querys.isEmpty()) { // query
            saveDataModel(querys);
            requestInfoRepository.saveAll(querys);
        }
        List<RequestInfo> body = interfaceVo.getBody();
        if (!body.isEmpty()) { // body
            saveDataModel(body);
            requestInfoRepository.saveAll(body);
        }
        List<RequestInfo> formDatas = interfaceVo.getFormDatas();
        if (!formDatas.isEmpty()) { // formData
            saveDataModel(formDatas);
            requestInfoRepository.saveAll(formDatas);
        }
    }

    /**
     * 保存响应参数
     *
     * @param interfaceVo
     */
    private void saveResponseInfo(InterfaceVo interfaceVo) {
        List<ResponseInfo> responseHeader = interfaceVo.getResponseHeader();
        if (!responseHeader.isEmpty()) { // 路径参数
            saveDataModel(responseHeader);
            responseInfoRepository.saveAll(responseHeader);
        }
        List<ResponseInfo> responseBody = interfaceVo.getResponseBody();
        if (!responseBody.isEmpty()) { // 头信息
            saveDataModel(responseBody);
            responseInfoRepository.saveAll(responseBody);
        }
    }

    private void createDefaultResponse(Interface _interface){
        ResponseInfo responseInfo = new ResponseInfo();
        DataModel dataModel = new DataModel();
        dataModel.setName("default");
        dataModel.setDataType("string");
        responseInfo.setDataModel(dataModel);
        responseInfo.setResponseIn(ResponseInConsts.schema.name());
        responseInfo.setDescription("successful operation");
        responseInfo.setInterfaceId(_interface.getId());
        List<ResponseInfo> responseInfos = Lists.newArrayList(responseInfo);
        saveDataModel(responseInfos);
        responseInfoRepository.saveAll(responseInfos);
    }

    private <T extends AbstractInfo> void saveDataModel(List<T> infos) {
        infos.forEach(info -> {
            DataModel dataModel = info.getDataModel();
            dataModel.setCreater("admin");
            dataModel.setType(UNIT_TYPE);
            dataModel.setCreateTime(new Date());
            dataModelRepository.save(dataModel);
            deepSetParent(dataModel, dataModel.getChildren());
            dataModelRepository.save(dataModel);
        });
    }

    private void getRequestInfos(String id, InterfaceVo interfaceVo) {
        interfaceVo.setHeaders(requestInfoRepository
                .findByInterfaceIdAndParamInOrderByCreateTimeDesc(id, ParamInConsts.header.name()));
        interfaceVo.setPathParams(requestInfoRepository
                .findByInterfaceIdAndParamInOrderByCreateTimeDesc(id, ParamInConsts.path.name()));
        interfaceVo.setQuerys(requestInfoRepository
                .findByInterfaceIdAndParamInOrderByCreateTimeDesc(id, ParamInConsts.query.name()));
        interfaceVo.setBody(requestInfoRepository
                .findByInterfaceIdAndParamInOrderByCreateTimeDesc(id, ParamInConsts.body.name()));
        interfaceVo.setFormDatas(requestInfoRepository
                .findByInterfaceIdAndParamInOrderByCreateTimeDesc(id, ParamInConsts.formData.name()));
    }

    private void getResponseInfos(String id, InterfaceVo interfaceVo) {
        interfaceVo.setResponseHeader(responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(id, ResponseInConsts.header.name()));
        interfaceVo.setResponseBody(responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(id, ResponseInConsts.schema.name()));
    }

    private void deleteInfos(Interface anInterface) {
        deleteRequestInfosInBatch(requestInfoRepository
                .findByInterfaceIdOrderByCreateTimeDesc(anInterface.getId()));
        deleteResponsetInfosInBatch(responseInfoRepository
                .findByInterfaceIdOrderByCreateTimeDesc(anInterface.getId()));
    }

    // 迭代删除
    private void deepDelete(List<DataModel> children) {
        if (children == null) {
            return;
        }
        children.forEach(dataModel -> {
            if (dataModel.getChildren() != null && dataModel.getChildren().size() > 0) {
                deepDelete(dataModel.getChildren());
            }
            dataModelRepository.delete(dataModel);
        });
    }

    public void deleteByProjectId(String projectId) {
        deleteInBatch(interfaceRepository.findAllByProjectIdOrderByPath(projectId));
    }
}
