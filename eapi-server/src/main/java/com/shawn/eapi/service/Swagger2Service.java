package com.shawn.eapi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shawn.common.BizException;
import com.shawn.common.util.UserContextHolder;
import com.shawn.common.vo.UserSession;
import com.shawn.eapi.dialect.AbstractInfo;
import com.shawn.eapi.entity.*;
import com.shawn.eapi.repository.*;
import com.shawn.eapi.util.Swagger2Eapi;
import com.shawn.eapi.util.Eapi2Swagger;
import io.swagger.models.*;
import io.swagger.models.Tag;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.shawn.eapi.consts.DataModelType.CUSTOM_TYPE;

@Service
public class Swagger2Service {

    public enum BuildType {
        SWAGGER_UI, SWAGGER_JSON
    }

    final public static List<String> baseDataTypes = Lists.newArrayList("string", "integer", "array", "file",
            "object", "boolean", "bigdecimal", "double", "float", "long");

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private Eapi2Swagger eapi2Swagger;

    @Autowired
    private Swagger2Eapi swagger2Eapi;

    @Autowired
    private InterfaceRepository interfaceRepository;

    @Autowired
    private DataModelRepository dataModelRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RequestInfoRepository requestInfoRepository;

    @Autowired
    private ResponseInfoRepository responseInfoRepository;

    /**
     * 导出项目swagger.json文件
     *
     * @param projectId 项目编号
     * @return
     */
    public Swagger buildSwagger(String projectId, BuildType type) {

        Project project = projectRepository.findById(projectId).get();

        Swagger swagger = new Swagger();
        swagger.setHost(project.getHost());
        swagger.setBasePath(project.getBasePath());

        List<Scheme> list = Lists.newArrayList();
        list.add(Scheme.HTTP);
        swagger.setSchemes(list);

        // 接口信息
        swagger.setInfo(eapi2Swagger.apiInfo(project));

        // 数据模型
        List<DataModel> dataModels = dataModelRepository.findByTypeAndProjectIdOrderByName(CUSTOM_TYPE, projectId);
        Map<String, Model> map_ = eapi2Swagger.modelMap(dataModels);
        swagger.setDefinitions(map_);


        // 接口列表
        List<Interface> interfaces = interfaceRepository.findAllByProjectIdOrderByPath(project.getId());
        Map<String, Path> map__ = eapi2Swagger.mapApiListings(interfaces, project, type);
        swagger.setPaths(map__);


        // 标签列表
        List<com.shawn.eapi.entity.Tag> tags = tagRepository.findAllByProjectId(projectId);
        List<Tag> list_ = eapi2Swagger.tagSetToTagList(tags);
        swagger.setTags(list_);

        //
        Map<String, SecuritySchemeDefinition> map___ = Maps.newHashMap();
        swagger.setSecurityDefinitions(map___);
//        List<String> list__ = Lists.newArrayList();
//
//        swagger.setConsumes(list__);


//        List<String> list___ = Lists.newArrayList();
//
//        swagger.setProduces(list___);

        return swagger;

    }

    @Transactional
    public void importSwaggerFromUrl(String swaggerUrl, String projectId) throws BizException {

        Swagger output = new SwaggerParser().read(swaggerUrl);

        if (output == null) {
            throw new BizException("Unavailable URL");
        }

        this.importSwagger(output, projectId);
    }

    @Transactional
    public void importSwaggerFromFile(String swaggerJson, String projectId) throws BizException {
        Swagger output = new SwaggerParser().parse(swaggerJson);

        if (output == null) {
            throw new BizException("Unavailable Swagger Json File");
        }

        this.importSwagger(output, projectId);
    }

    /**
     * 导如项目swagger.json文件
     *
     * @param output
     * @param projectId
     */

    public void importSwagger(Swagger output, String projectId) {

        UserSession user = UserContextHolder.getContext();

        // 删掉原来项目
        projectService.deleteContent(projectId);

        // 添加标签
        swagger2Eapi.tagSetToTagList(output.getTags(), projectId, user.getId().toString(), user.getLoginName());

        // 添加模块
        swagger2Eapi.modelMap(output.getDefinitions(), projectId, user.getId().toString(), user.getLoginName());

        // 添加接口
        swagger2Eapi.mapInterfaceListings(output.getPaths(), projectId, user.getId().toString(), user.getLoginName());

    }


    public Swagger buildSwagger(String projectId, List<String> interfaceIds, BuildType type) {

        Project project = projectRepository.findById(projectId).get();

        Swagger swagger = new Swagger();
        swagger.setHost(project.getHost());
        swagger.setBasePath(project.getBasePath());

        List<Scheme> list = Lists.newArrayList();
        list.add(Scheme.HTTP);
        swagger.setSchemes(list);

        // 接口信息
        swagger.setInfo(eapi2Swagger.apiInfo(project));

        // 数据模型
        List<DataModel> dataModels = getRefDataModel(interfaceIds, projectId);
        Map<String, Model> map_ = eapi2Swagger.modelMap(dataModels);
        swagger.setDefinitions(map_);

        // 接口列表
        List<Interface> interfaces = interfaceRepository.findAllById(interfaceIds);
        Map<String, Path> map__ = eapi2Swagger.mapApiListings(interfaces, project, type);
        swagger.setPaths(map__);

        // 标签项
        List<com.shawn.eapi.entity.Tag> tags = Lists.newArrayList();
        interfaces.forEach(anInterface -> tags.addAll(anInterface.getTags()));
        // 去重
        List<com.shawn.eapi.entity.Tag> tagsNew = new ArrayList<>(new HashSet(tags));

        List<Tag> list_ = eapi2Swagger.tagSetToTagList(tagsNew);
        swagger.setTags(list_);

        //
        Map<String, SecuritySchemeDefinition> map___ = Maps.newHashMap();
        swagger.setSecurityDefinitions(map___);

        return swagger;

    }

    private List<DataModel> getRefDataModel(List<String> interfaceIds, String projectId) {

        List<DataModel> dataModels = Lists.newArrayList();

        List<AbstractInfo> abstractInfos = Lists.newArrayList();
        abstractInfos.addAll(responseInfoRepository.findByInterfaceIdInOrderByCreateTimeDesc(interfaceIds));
        abstractInfos.addAll(requestInfoRepository.findByInterfaceIdInOrderByCreateTimeDesc(interfaceIds));

        abstractInfos.forEach(requestInfo -> {
            DataModel dataModel = requestInfo.getDataModel();
            getDataModel(dataModels, dataModel, projectId);

        });

        return dataModels;
    }


    private void childrenRefDataModel(List<DataModel> children, List<DataModel> dataModels, String projectId) {
        if (children == null && children.size() < 1) {
            return;
        }
        children.forEach(dataModel -> {
            this.getDataModel(dataModels, dataModel, projectId);
        });
    }


    private void getDataModel(List<DataModel> dataModels, DataModel dataModel, String projectId) {
        if (!baseDataTypes.contains(dataModel.getDataType())) { // 不是基础数据类型
            DataModel refDataModel = dataModelRepository.findByTypeAndProjectIdAndName(CUSTOM_TYPE, projectId, dataModel.getDataType());
            if (refDataModel == null) {
                return;
            }
            dataModels.add(refDataModel);
            childrenRefDataModel(refDataModel.getChildren(), dataModels, projectId);
        } else if (dataModel.getDataType().equals("array") ||
                dataModel.getDataType().equals("object")) {
            // 查询子项
            childrenRefDataModel(dataModel.getChildren(), dataModels, projectId);
        }
    }
}
