package com.meimeitech.eapi.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.google.common.collect.Lists;
import com.meimeitech.common.vo.Response;
import com.meimeitech.eapi.consts.ParamInConsts;
import com.meimeitech.eapi.consts.ResponseInConsts;
import com.meimeitech.eapi.dialect.AbstractInfo;
import com.meimeitech.eapi.entity.DataModel;
import com.meimeitech.eapi.entity.Interface;
import com.meimeitech.eapi.entity.RequestInfo;
import com.meimeitech.eapi.entity.ResponseInfo;
import com.meimeitech.eapi.model.InterfaceVo;
import com.meimeitech.eapi.repository.DataModelRepository;
import com.meimeitech.eapi.repository.RequestInfoRepository;
import com.meimeitech.eapi.repository.ResponseInfoRepository;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.meimeitech.eapi.consts.DataModelType.UNIT_TYPE;
import static com.meimeitech.eapi.service.DataModelService.deepResetCopyModel;
import static com.meimeitech.eapi.service.DataModelService.deepSetParent;

@Service
public class InfoService {
    private static Logger logger = LoggerFactory.getLogger(InfoService.class);

    @Autowired
    private RequestInfoRepository requestInfoRepository;

    @Autowired
    private ResponseInfoRepository responseInfoRepository;

    @Autowired
    private DataModelRepository dataModelRepository;

    public Response deleteRequestInfosInBatch(List<RequestInfo> requestInfos) {

        requestInfoRepository.deleteInBatch(requestInfos);

        List<DataModel> dataModels = Lists.newArrayList();

        for (int i = 0; i < requestInfos.size(); i++ ) {
            RequestInfo requestInfo = requestInfos.get(i);

            deepDelete(requestInfo.getDataModel().getChildren());
            dataModels.add(requestInfo.getDataModel());
//            dataModelRepository.deleteById(.getId());
        }
        dataModelRepository.deleteInBatch(dataModels);

        return Response.success("SUCCESS");
    }

    public Response deleteResponsetInfosInBatch(List<ResponseInfo> responseInfos) {

        responseInfoRepository.deleteInBatch(responseInfos);

        List<DataModel> dataModels = Lists.newArrayList();

        responseInfos.forEach(responseInfo -> {
            deepDelete(responseInfo.getDataModel().getChildren());

            dataModels.add(responseInfo.getDataModel());
//            dataModelRepository.deleteById(responseInfo.getDataModel().getId());
        });

        dataModelRepository.deleteInBatch(dataModels);

        return Response.success("SUCCESS");
    }

    // 迭代删除
    private void deepDelete(List<DataModel> children) {
        if (children == null) {
            return;
        }
        List<DataModel> dataModels = Lists.newArrayList();
        children.forEach(dataModel -> {
            if (dataModel.getChildren() != null && dataModel.getChildren().size() > 0) {
                deepDelete(dataModel.getChildren());
            }
            dataModels.add(dataModel);
        });
        dataModelRepository.deleteInBatch(dataModels);
    }


    public void deleteInfos(Interface anInterface) {
        deleteRequestInfosInBatch(requestInfoRepository
                .findByInterfaceIdOrderByCreateTimeDesc(anInterface.getId()));
        deleteResponsetInfosInBatch(responseInfoRepository
                .findByInterfaceIdOrderByCreateTimeDesc(anInterface.getId()));
    }

    /**
     * 保存请求参数
     *
     * @param interfaceVo
     */
    public void saveRequestInfo(InterfaceVo interfaceVo) {
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
    public void saveResponseInfo(InterfaceVo interfaceVo) {
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


    public <T extends AbstractInfo> void saveDataModel(List<T> infos) {
        infos.forEach(info -> {
            DataModel dataModel = info.getDataModel();
//            dataModel.setCreater("admin");
            dataModel.setType(UNIT_TYPE);
            dataModel.setCreateTime(new Date());
//            dataModelRepository.save(dataModel);
            deepSetParent(dataModel, dataModel.getChildren());
            dataModelRepository.save(dataModel);
        });
    }

    public void createDefaultResponse(Interface _interface){
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

    public void deleteRequestInfo(List<AbstractInfo> infos, String interfaceId) {
        requestInfoRepository.findByInterfaceIdOrderByCreateTimeDesc(interfaceId).forEach(requestInfo -> {
            compareDelete(requestInfo, infos);
        });
    }


    public void deleteResponseInfo(List<AbstractInfo> infos, String interfaceId) {
        responseInfoRepository.findByInterfaceIdOrderByCreateTimeDesc(interfaceId).forEach(responseInfo -> {
            compareDelete(responseInfo, infos);
        });
    }

    private void compareDelete(AbstractInfo info, List<AbstractInfo> infos){

        AtomicBoolean isDeleted = new AtomicBoolean(true);
        AtomicReference<AbstractInfo> sameNode =  new AtomicReference();

        infos.forEach(newResponse -> {

            if (info.getId().equals(newResponse.getId())) {
                isDeleted.set(false);
                sameNode.set(newResponse);
            }
        });

        if (isDeleted.get()) {  // 被删除
            if  (info instanceof ResponseInfo){
                ResponseInfo responseInfo = (ResponseInfo) info;
                responseInfoRepository.delete(responseInfo);
            }
            if  (info instanceof RequestInfo){
                RequestInfo requestInfo = (RequestInfo) info;
                requestInfoRepository.delete(requestInfo);
            }

            logger.info("节点[{}]-[{}]，被删除",info.getId(),info.getDataModel().getName());
        } else {
            deleteDataModel(info.getDataModel().getChildren(), sameNode.get().getDataModel().getChildren());
        }
    }

    // 迭代判断子节点
    private void deleteDataModel(List<DataModel> originDataModles, List<DataModel> newDatamodels){

        originDataModles.forEach(dataModel -> {

            AtomicBoolean isDeleted = new AtomicBoolean(true);

            AtomicReference<DataModel> sameNode = new AtomicReference();

            newDatamodels.forEach(newDataModel -> {

                if (dataModel.getId().equals(newDataModel.getId())) {
                    isDeleted.set(false);
                    sameNode.set(newDataModel);
                }
            });

            if (isDeleted.get()) {  // 被删除

                dataModelRepository.delete(dataModel);
                logger.info("节点[{}]-[{}]，被删除",dataModel.getId(),dataModel.getName());
            } else {
                // 迭代下一级
                deleteDataModel(dataModel.getChildren(), sameNode.get().getChildren());
            }
        });
    }

    public void getRequestInfos(String id, InterfaceVo interfaceVo) {
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

    public void getResponseInfos(String id, InterfaceVo interfaceVo) {
        interfaceVo.setResponseHeader(responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(id, ResponseInConsts.header.name()));
        interfaceVo.setResponseBody(responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(id, ResponseInConsts.schema.name()));
    }

    /**
     * 复制请求
     *
     * @param copyId
     * @param interfaceId
     */
    public void copyRequestInfo(String copyId, String interfaceId) {
        List<RequestInfo> requestInfos = requestInfoRepository.findByInterfaceIdOrderByCreateTimeDesc(copyId);

        if (requestInfos.isEmpty()) {
            return;
        }

        List<RequestInfo> newRequestInfos = Lists.newArrayList();
        requestInfos.forEach(requestInfo -> {
            DataModel dataModel = new DataModel();
            BeanUtils.copyProperties(requestInfo.getDataModel(), dataModel);
            dataModel.setId(null);
            dataModel.setCreateTime(new Date());
            deepResetCopyModel(dataModel, dataModel.getChildren());
            dataModelRepository.save(dataModel);

            RequestInfo newRequsetInfo = new RequestInfo();
            newRequsetInfo.setParamIn(requestInfo.getParamIn());
            newRequsetInfo.setDataModel(dataModel);
            newRequsetInfo.setInterfaceId(interfaceId);
            newRequsetInfo.setCreateTime(new Date());
            newRequestInfos.add(newRequsetInfo);
        });

        requestInfoRepository.saveAll(newRequestInfos);
    }

    /**
     * 复制响应
     *
     * @param copyId
     * @param interfaceId
     */
    public void copyResponseInfo(String copyId, String interfaceId) {

        List<ResponseInfo> responseInfos = responseInfoRepository.findByInterfaceIdOrderByCreateTimeDesc(copyId);

        if (responseInfos.isEmpty()) {
            return;
        }

        List<ResponseInfo> newResponseInfos = Lists.newArrayList();

        responseInfos.forEach(responseInfo -> {

            DataModel dataModel = new DataModel();
            BeanUtils.copyProperties(responseInfo.getDataModel(), dataModel);
            dataModel.setId(null);
            dataModel.setCreateTime(new Date());
            deepResetCopyModel(dataModel, dataModel.getChildren());
            dataModelRepository.save(dataModel);

            ResponseInfo newRequestInfo = new ResponseInfo();
            newRequestInfo.setResponseIn(responseInfo.getResponseIn());
            newRequestInfo.setDescription(responseInfo.getDescription());
            newRequestInfo.setDataModel(dataModel);
            newRequestInfo.setInterfaceId(interfaceId);
            newRequestInfo.setCreateTime(new Date());
            newResponseInfos.add(newRequestInfo);
        });

        responseInfoRepository.saveAll(newResponseInfos);
    }

}
