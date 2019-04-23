package com.meimeitech.eapi.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meimeitech.common.BizException;
import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.eapi.entity.*;
import com.meimeitech.eapi.model.ProjectVo;
import com.meimeitech.eapi.util.Swagger2Eapi;
import com.meimeitech.eapi.util.Eapi2Swagger;
import io.swagger.models.*;
import io.swagger.models.Tag;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
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

        ProjectVo project = (ProjectVo) projectService.findById(projectId).getBody();

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
        Swagger output  = new SwaggerParser().parse(swaggerJson);

        if (output == null) {
            throw new BizException("Unavailable Swagger Json File");
        }

        this.importSwagger(output, projectId);
    }

    /**
     *  导如项目swagger.json文件
     *
     * @param output
     * @param projectId
     */

    public void importSwagger(Swagger output, String projectId){

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

}
