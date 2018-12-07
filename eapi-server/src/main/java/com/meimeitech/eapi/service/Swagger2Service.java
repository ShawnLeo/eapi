package com.meimeitech.eapi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meimeitech.eapi.consts.ResponseInConsts;
import com.meimeitech.eapi.entity.*;
import com.meimeitech.eapi.model.Parameters;
import com.meimeitech.eapi.model.Properties;
import com.meimeitech.eapi.repository.*;
import com.meimeitech.eapi.util.Swagger2Eapi;
import com.meimeitech.eapi.util.Eapi2Swagger;
import io.swagger.models.*;
import io.swagger.models.Tag;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class Swagger2Service {


    @Autowired
    private ProjectService projectService;

    @Autowired
    private Eapi2Swagger eapi2Swagger;

    @Autowired
    private Swagger2Eapi swagger2Eapi;

    /**
     *
     * 导出项目swagger.json文件
     *
     * @param projectId 项目编号
     * @return
     */
    public Swagger buildSwagger (String projectId) {

        Project project = (Project) projectService.findById(projectId).getBody();

        Swagger swagger = new Swagger();
        swagger.setHost(project.getHost());
        swagger.setBasePath( project.getBasePath() );

        List<Scheme> list = Lists.newArrayList();
        list.add(Scheme.HTTP);
        swagger.setSchemes( list );

        // 数据模型
        Map<String, Model> map_ = eapi2Swagger.modelMap(projectId);
        swagger.setDefinitions( map_ );

        // 接口信息
        swagger.setInfo(eapi2Swagger.apiInfo(project));

        // 接口列表
        Map<String, Path> map__ = eapi2Swagger.mapApiListings(projectId);
        swagger.setPaths( map__ );

        //
        Map<String, SecuritySchemeDefinition> map___ = Maps.newHashMap();
        swagger.setSecurityDefinitions( map___ );


        // 标签列表
        List<Tag> list_ = eapi2Swagger.tagSetToTagList(projectId);
        swagger.setTags( list_ );

//        List<String> list__ = Lists.newArrayList();
//
//        swagger.setConsumes(list__);


//        List<String> list___ = Lists.newArrayList();
//
//        swagger.setProduces(list___);

        return swagger;

    }

    /**
     *
     * 导如项目swagger.json文件
     *
     * @param swaggerJson 文件内容
     * @return
     */
    @Transactional
    public String importSwagger(String swaggerJson, String projectId){
        Swagger output  = new SwaggerParser().parse(swaggerJson);
//        Swagger output = new SwaggerParser().read("http://10.133.255.201:7050/v2/api-docs/8a808383670c3b64016776edc8540000");

        // 删掉原来项目
        projectService.deleteContent(projectId);

        // 添加标签
        swagger2Eapi.tagSetToTagList(output.getTags(), projectId, "");

        // 添加模块
        swagger2Eapi.modelMap(output.getDefinitions(), projectId, "");

        // 添加接口
        swagger2Eapi.mapInterfaceListings(output.getPaths(), projectId, null);

        return projectId;
    }


//    public static void main(String[] args) {
//        new Swagger2Service().importSwagger(null, null);
//    }



}
