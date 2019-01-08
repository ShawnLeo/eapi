package com.meimeitech.eapi.util;

import com.google.common.collect.Lists;
import com.meimeitech.eapi.consts.ParamInConsts;
import com.meimeitech.eapi.consts.ResponseInConsts;
import com.meimeitech.eapi.entity.DataModel;
import com.meimeitech.eapi.entity.Interface;
import com.meimeitech.eapi.entity.RequestInfo;
import com.meimeitech.eapi.entity.ResponseInfo;
import com.meimeitech.eapi.model.Parameters;
import com.meimeitech.eapi.model.Properties;
import com.meimeitech.eapi.repository.*;
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.meimeitech.eapi.consts.DataModelType.CUSTOM_TYPE;
import static com.meimeitech.eapi.consts.DataModelType.UNIT_TYPE;
import static com.meimeitech.eapi.util.JpaSpecUtils.eq;
import static com.meimeitech.eapi.util.JpaSpecUtils.in;
import static com.meimeitech.eapi.util.JpaSpecUtils.merge;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class Swagger2Eapi {



    @Autowired
    private DataModelRepository dataModelRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private InterfaceRepository interfaceRepository;
    @Autowired
    private RequestInfoRepository requestInfoRepository;
    @Autowired
    private ResponseInfoRepository responseInfoRepository;

    // Tag
    public void tagSetToTagList(List<Tag> tags, String projectId, String creater, String createrUserName) {
        if (tags == null) {
            return;
        }
        List<com.meimeitech.eapi.entity.Tag> newTags = Lists.newArrayList();

        tags.forEach((tag) -> {

            com.meimeitech.eapi.entity.Tag newTag = new com.meimeitech.eapi.entity.Tag();
            newTag.setName(tag.getName());
            newTag.setDescription(tag.getDescription());
            newTag.setProjectId(projectId);
            newTag.setCreateTime(new Date());
            newTag.setCreater(creater);
            newTag.setCreaterUserName(createrUserName);
            newTags.add(newTag);

        });

        tagRepository.saveAll(newTags);
    }

    // Datamodel
    public void modelMap(Map<String, Model> map, String projectId, String creater, String createrUserName) {
        if (map == null) {
           return;
        }
        List<DataModel> dataModels = Lists.newArrayList();

        for (Map.Entry<String, Model> entry : map.entrySet()) {
            if (StringUtils.isEmpty(entry.getKey())) {
                continue;
            }
            Model model = entry.getValue();

            DataModel dataModel = new DataModel();
            dataModel.setName(entry.getKey());
            dataModel.setType(CUSTOM_TYPE);

//            dataModel.setChildren();
            dataModel.setCreater(creater);
            dataModel.setCreaterUserName(createrUserName);
            dataModel.setCreateTime(new Date());

            dataModel.setDescription(model.getDescription());
//            dataModel.setDisplayOrder();
//            dataModel.setExample(model.getExample().toString());
//            dataModel.setParent();
            dataModel.setProjectId(projectId);

            if (model instanceof ModelImpl) {
                ModelImpl  modelImpl = (ModelImpl) model;
                dataModel.setDataType(modelImpl.getType());
                Map<String, Property> properties = modelImpl.getProperties();
                Properties.mapProperties(properties, dataModel);
//                dataModel.setRequired(((ModelImpl) model).required());
            }

            if (model instanceof ArrayModel) {
                ArrayModel  arrayModel = (ArrayModel) model;
                dataModel.setDataType(arrayModel.getType());
                Properties.mapPropertie(arrayModel.getItems(), dataModel);
            }
//            dataModel.setType(data);
//            dataModels.add()
            dataModels.add(dataModel);
        }
        dataModelRepository.saveAll(dataModels);
    }

    public void mapInterfaceListings(Map<String, Path> map, String projectId, String creater, String createrUserName) {

        int index = 0;

//        List<Interface> interfaces = Lists.newArrayList();

        List<RequestInfo> requestInfos = Lists.newArrayList();

        List<ResponseInfo> responseInfos = Lists.newArrayList();

        for (Map.Entry<String, Path> pathMap : map.entrySet()) {
//            System.out.println("Key = " + pathMap.getKey() + ", Value = " + pathMap.getValue());

            Path path = pathMap.getValue();
            Map<HttpMethod, Operation> operations =  path.getOperationMap();

            for (Map.Entry<HttpMethod, Operation> entry : operations.entrySet()) {
//                System.out.println("Key = " + entry.getKey().name() + ", Value = " + entry.getValue());
                Operation operation = entry.getValue();

                Interface _interface = new Interface();
                _interface.setName(operation.getSummary());
                _interface.setMethod(entry.getKey().name().toLowerCase());
                _interface.setCreater(creater);
                _interface.setCreaterUserName(createrUserName);
                _interface.setCreateTime(new Date());
                _interface.setDeprecated(operation.isDeprecated() == null ? false : operation.isDeprecated());
                _interface.setOperationId(operation.getOperationId());
                _interface.setDisplayOrder(index);
                _interface.setDescription(operation.getDescription());
                _interface.setPath(pathMap.getKey());
                _interface.setProjectId(projectId);
//                _interface.setRequestType();
                _interface.setStatus((short)100);

                List<com.meimeitech.eapi.entity.Tag> tags = tagRepository.findAll((Specification<com.meimeitech.eapi.entity.Tag>) (root, query, cb) -> cb.and(merge(
                        eq(cb, root.get("projectId"), projectId),
                        in(cb, root.get("name"), operation.getTags()))));
                _interface.setTags(new HashSet<>(tags));
//                interfaces.add(_interface);
                interfaceRepository.save(_interface);

                AtomicReference<String> paramIn = new AtomicReference<>(ParamInConsts.query.name());
                // 请求参数
                List<Parameter> parameters = operation.getParameters();
                parameters.forEach(parameter -> {
                    RequestInfo requestInfo = Parameters.parameter(parameter);
                    requestInfo.setInterfaceId(_interface.getId());
                    requestInfo.setCreater(creater);
                    requestInfo.setCreaterUserName(createrUserName);
                    if (requestInfo.getParamIn().equals(ParamInConsts.body.name())) {
                        paramIn.set(ParamInConsts.body.name());
                    } else if (requestInfo.getParamIn().equals(ParamInConsts.formData.name())){
                        paramIn.set(ParamInConsts.formData.name());
                    }
                    requestInfos.add(requestInfo);
                });
                _interface.setRequestType(paramIn.get());
                interfaceRepository.save(_interface);
                requestInfoRepository.saveAll(requestInfos);
                // 响应参数
                Map<String, Response> responses = operation.getResponses();

                for (Map.Entry<String, Response> responseEntry : responses.entrySet()) {
                    Response response = responseEntry.getValue();
                    ResponseInfo responseInfo = new ResponseInfo();
                    responseInfo.setResponseIn(ResponseInConsts.schema.name());
                    responseInfo.setDescription(response.getDescription());
                    responseInfo.setCreater(creater);
                    responseInfo.setCreaterUserName(createrUserName);
                    responseInfo.setCreateTime(new Date());

                    DataModel dataModel = new DataModel();
                    dataModel.setCreateTime(new Date());
                    dataModel.setCreater(creater);
                    dataModel.setCreaterUserName(createrUserName);
                    dataModel.setName(responseEntry.getKey());
                    dataModel.setDescription(responseInfo.getDescription());
                    dataModel.setType(UNIT_TYPE);

                    Property property = response.getSchema();

                    property.setName(responseEntry.getKey());

                    dataModel.setDataType(property.getType());
                    dataModel.setRequired(property.getRequired());

                    if (property.getExample() != null){
                        dataModel.setExample(property.getExample().toString());
                    }

                    if ( property instanceof ArrayProperty ){

                        ArrayProperty arrayProperty = (ArrayProperty) property;
                        dataModel.setDataType(arrayProperty.getType());
                        Properties.mapPropertie(arrayProperty, dataModel);

                    } else if (property instanceof ObjectProperty) {

                        ObjectProperty objectProperty = (ObjectProperty) property;
                        dataModel.setDataType(objectProperty.getType());
                        Properties.mapPropertie(objectProperty, dataModel);
                        dataModel = dataModel.getChildren().get(0);
                    } else if (property instanceof RefProperty) {
                        RefProperty refProperty = (RefProperty) property;
                        dataModel.setDataType(refProperty.getSimpleRef());
                    }

                    responseInfo.setInterfaceId(_interface.getId());
                    responseInfo.setDataModel(dataModel);
                    responseInfos.add(responseInfo);
                }
                index++;
                responseInfoRepository.saveAll(responseInfos);
            }
        }
    }

}
