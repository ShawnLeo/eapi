package com.meimeitech.eapi.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meimeitech.eapi.consts.ParamInConsts;
import com.meimeitech.eapi.consts.ResponseInConsts;
import com.meimeitech.eapi.entity.*;
import com.meimeitech.eapi.entity.Tag;
import com.meimeitech.eapi.model.Parameters;
import com.meimeitech.eapi.model.Properties;
import com.meimeitech.eapi.repository.*;
import com.meimeitech.eapi.service.Swagger2Service;
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.utils.PropertyModelConverter;
import io.swagger.util.Json;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class Eapi2Swagger {

    @Autowired
    private DataModelRepository dataModelRepository;
    @Autowired
    private RequestInfoRepository requestInfoRepository;
    @Autowired
    private ResponseInfoRepository responseInfoRepository;

    public Map<String, Path> mapApiListings(List<Interface> interfaces, Project project,
                                            Swagger2Service.BuildType type) {
        Map<String, Path> map = Maps.newTreeMap();
        interfaces.forEach(_interface -> map.put(_interface.getPath(), mapOperations(_interface, map, project, type)));
        return map;
    }

    public Path mapOperations(Interface anInterface, Map<String, Path> map, Project project,
                              Swagger2Service.BuildType type) {

        map.get(anInterface.getPath());
        Path path = map.get(anInterface.getPath());
        if (path == null) {
            path = new Path();
        }
        List<String> list_ = Lists.newArrayList();
        Set<Tag> tags = anInterface.getTags();
        tags.forEach(tag -> list_.add(tag.getName()));
        List<Parameter> parameters = Lists.newArrayList();

        List<RequestInfo> requestInfos = requestInfoRepository.findByInterfaceIdOrderByCreateTimeDesc(anInterface.getId());

        requestInfos.forEach(requestInfo -> parameters.add(Parameters.parameter(requestInfo)));

        Operation operation = new Operation().operationId(anInterface.getOperationId()).tags(list_)
                .description(anInterface.getDescription()).summary(anInterface.getName())
                .deprecated(anInterface.getStatus() == (short) 500);
        operation.setParameters(parameters);

        if (ParamInConsts.formData.name().equals(anInterface.getRequestType())) {
            List<String> consumes = Lists.newArrayList();
            consumes.add("multipart/form-data");
            operation.setConsumes(consumes);
        }

        List<ResponseInfo> responseHeaders = responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(anInterface.getId(), ResponseInConsts.header.name());

        Map<String, Property> headers = Maps.newHashMap();
        responseHeaders.forEach(responseInfo -> {
            headers.put(responseInfo.getDataModel().getName(), Properties.mapProperty(responseInfo.getDataModel()));
        });

        List<ResponseInfo> responseInfos = responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(anInterface.getId(), ResponseInConsts.schema.name());

        Map<String, Response> responses = Maps.newHashMap();
        responseInfos.forEach(responseInfo -> {

            Property property;

            if (project.getCommonResponse() != null && project.getCommonResponse() &&
                    type.equals(Swagger2Service.BuildType.SWAGGER_UI)) {  // 是否开启通用响应
                Map<String, Property> properties = Maps.newHashMap();


                dataModelRepository.findById(project.getCommonResponseId()).get().getChildren().forEach(dataModel -> {
                    properties.put(dataModel.getName(), Properties.mapProperty(dataModel));
                });

                if (StringUtils.isEmpty(project.getCommonResponseField())) {
                    properties.put("data", Properties.mapProperty(responseInfo.getDataModel()));
                } else {
                    properties.put(project.getCommonResponseField(), Properties.mapProperty(responseInfo.getDataModel()));
                }

                property = new ObjectProperty().properties(properties).description(responseInfo.getDescription());

            } else {
                property = Properties.mapProperty(responseInfo.getDataModel());
            }

            Response response = new Response().headers(headers)
                    .description(responseInfo.getDescription());

            response.setResponseSchema(new PropertyModelConverter().propertyToModel(property));
            try {
                Map<String, Object> examples = Json.mapper().convertValue(responseInfo.getDataModel().getExample(),
                        Json.mapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class));
                response.setExamples(examples);
            } catch (Exception e) {
                response.setExamples(null);
            }
            responses.put(responseInfo.getDataModel().getName(), response);
        });

        operation.setResponses(responses);
        String method = anInterface.getMethod();
        switch (method) {
            case "get":
                path.get(operation);
                break;
            case "post":
                path.post(operation);
                break;
            case "put":
                path.put(operation);
                break;
            case "delete":
                path.delete(operation);
                break;
        }
        return path;
    }

    public List<io.swagger.models.Tag> tagSetToTagList(List<Tag> tags) {
        List<io.swagger.models.Tag> list_ = Lists.newArrayList();
        tags.forEach(tag -> {
            io.swagger.models.Tag newTag = new io.swagger.models.Tag().description(tag.getDescription()).name(tag.getName());
            list_.add(newTag);
        });
        return list_;
    }

    public Info apiInfo(Project project) {
        return new Info().title(project.getTitle())
                .description(project.getDescription())
                .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                .termsOfService("")
                .version(project.getVersion())
                .contact(new Contact().email(project.getContactEmail()));
    }

    public Map<String, Model> modelMap(List<DataModel> dataModels) {

        Map<String, Model> map = Maps.newHashMap();

        dataModels.forEach(dataModel -> modelMapItem(map, dataModel));

        return map;
    }

    public void modelMapItem(Map<String, Model> map, DataModel dataModel) {

        Model model;

        if (dataModel.getDataType().equals("array")
                && dataModel.getChildren() != null && dataModel.getChildren().size() > 0) {
            model = new ArrayModel().description(dataModel.getDescription())
                    .items(Properties.mapProperty(dataModel.getChildren().get(0)));
        } else {
            model = new ModelImpl().description(dataModel.getDescription())
                    .example(dataModel.getExample())
                    .name(dataModel.getName())
                    .type(dataModel.getDataType());
            Map<String, Property> modelProperties = mapProperties(dataModel.getChildren());
            model.setProperties(modelProperties);
        }

        map.put(dataModel.getName(), model);
    }

    private Map<String, Property> mapProperties(List<DataModel> properties) {

        Map<String, Property> mappedProperties = new LinkedHashMap<>();

        properties.forEach(propertie -> {
            mappedProperties.put(propertie.getName(), Properties.mapProperty(propertie));
        });

        return mappedProperties;
    }

}
