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
import io.swagger.models.*;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.meimeitech.eapi.consts.DataModelType.CUSTOM_TYPE;

@Component
public class Eapi2Swagger {

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


    public Map<String,Path> mapApiListings(String projectId) {
        Map<String, Path> map = Maps.newTreeMap();
        List<Interface> interfaces = interfaceRepository.findAllByProjectIdOrderByPath(projectId);
        interfaces.forEach( _interface -> {
            map.put(_interface.getPath(), mapOperations(_interface, map));
        });
        return map;
    }


    public Path mapOperations(Interface anInterface, Map<String, Path> map) {
        map.get(anInterface.getPath());
        Path path = map.get(anInterface.getPath());
        if (path == null) {
            path = new Path();
        }
        List<String> list_ = Lists.newArrayList();
        Set<Tag> tags = anInterface.getTags();
        tags.forEach(tag -> {
            list_.add(tag.getName());
        });
        List<Parameter> parameters = Lists.newArrayList();

        List<RequestInfo> requestInfos =  requestInfoRepository.findByInterfaceIdOrderByCreateTimeDesc(anInterface.getId());

        requestInfos.forEach(requestInfo -> {
            parameters.add(Parameters.parameter(requestInfo));
        });

        Operation operation = new Operation().operationId(anInterface.getOperationId()).tags(list_)
                .description(anInterface.getDescription()).summary(anInterface.getName());
//                .deprecated(anInterface.isDeprecated())
//                .defaultResponse(response);
        operation.setParameters(parameters);

        if(ParamInConsts.formData.name().equals(anInterface.getRequestType())){
            List<String> consumes = Lists.newArrayList();
            consumes.add("multipart/form-data");
            operation.setConsumes(consumes);
        }

        List<ResponseInfo> responseHeaders =  responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(anInterface.getId(), ResponseInConsts.header.name());

        Map<String, Property> headers = Maps.newHashMap();
        responseHeaders.forEach(responseInfo -> {
            headers.put(responseInfo.getDataModel().getName(), Properties.mapProperty(responseInfo.getDataModel()));
        });

        List<ResponseInfo> responseInfos =  responseInfoRepository
                .findByInterfaceIdAndResponseInOrderByCreateTimeDesc(anInterface.getId(), ResponseInConsts.schema.name());

        Map<String, Response> responses = Maps.newHashMap();
        responseInfos.forEach(responseInfo -> {
            Response response = new Response().headers(headers)
                    .description(responseInfo.getDescription())
                    .schema(Properties.mapProperty(responseInfo.getDataModel()));
            try{
                Map<String, Object> examples = Json.mapper().convertValue(responseInfo.getDataModel().getExample(), Json.mapper().getTypeFactory().constructMapType(Map.class, String.class, Object.class));
                response.setExamples(examples);
            }catch (Exception e) {
                response.setExamples(null);
            }
            responses.put(responseInfo.getDataModel().getName(), response);
        });

        operation.setResponses(responses);
        String method = anInterface.getMethod();
        switch (method) {
            case "get" :
                path.get(operation);
                break;
            case "post" :
                path.post(operation);
                break;
            case "put" :
                path.put(operation);
                break;
            case "delete" :
                path.delete(operation);
                break;
        }
        return path;
    }

    public List<io.swagger.models.Tag> tagSetToTagList(String projectId) {
        List<io.swagger.models.Tag> list_ = Lists.newArrayList();
        List<com.meimeitech.eapi.entity.Tag> tags = tagRepository.findAllByProjectId(projectId);
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

    public Map<String, Model> modelMap (String projectId){
        List<DataModel> dataModels =  dataModelRepository.findByTypeAndProjectIdOrderByName(CUSTOM_TYPE, projectId);
        Map<String, Model> map = Maps.newHashMap();
        dataModels.forEach(dataModel -> {
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
        });

        return map;
    }


    private Map<String, Property>  mapProperties(List<DataModel> properties) {

        Map<String, Property> mappedProperties = new LinkedHashMap<>();

        properties.forEach(propertie -> {
            mappedProperties.put(propertie.getName(), Properties.mapProperty(propertie));
        });

        return mappedProperties;
    }
}
