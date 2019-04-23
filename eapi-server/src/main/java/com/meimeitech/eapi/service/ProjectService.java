package com.meimeitech.eapi.service;


import com.google.common.collect.Lists;
import com.meimeitech.common.util.UserContextHolder;
import com.meimeitech.common.vo.Response;
import com.meimeitech.common.vo.UserSession;
import com.meimeitech.eapi.entity.Group;
import com.meimeitech.eapi.entity.Project;
import com.meimeitech.eapi.model.ProjectVo;
import com.meimeitech.eapi.repository.GroupRepository;
import com.meimeitech.eapi.repository.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService {
    private static Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Value("${swagger.host:10.133.255.201:7050}")
    private String swaggerHost;

    @Value("${swagger.contact-email:easyapi@163.com}")
    private String contactEmail;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private TagService tagService;
    @Autowired
    private DataModelService dataModelService;

    public Response list(String groupId) {
        UserSession user = UserContextHolder.getContext();
        List<Project> projects;
        if ("all".equals(groupId)) {
            projects = projectRepository.findAllGroups(user.getId().toString());
        } else {
            projects = projectRepository.findByGroupId(groupId);
        }
       return Response.success(projects);
    }

    public Response update(Project project) {
        if ((boolean)checkExists(project).getBody()) {
            return Response.error("项目名称已存在");
        }
        return Response.success(projectRepository.save(project));
    }

    public Response create(Project project) {
        UserSession user = UserContextHolder.getContext();

        project.setContactEmail(contactEmail);
        project.setHost(swaggerHost);
        project.setVersion("1.0.0");
        project.setCreateTime(new Date());
        project.setCreater(user.getId().toString());
        project.setCreaterUserName(user.getLoginName());
        project = projectRepository.save(project);
        project.setBasePath("/virtserver/" + project.getId() + "/1.0.0/");

        return Response.success(projectRepository.save(project));
    }

    public Response checkExists(Project project) {
        long count = projectRepository.count((Specification<Project>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("title"), project.getTitle()));
            if ( project.getId() != null ) {
                predicates.add(criteriaBuilder.notEqual(root.get("id"), project.getId()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
        return Response.success(count > 0);
    }

    public Response findById(String id) {

        if (StringUtils.isEmpty(id)) {
            return Response.error("请求数据异常");
        }

        ProjectVo projectVo = new ProjectVo();

        Project project = projectRepository.findById(id).get();

        if (project == null) {
            return Response.error("未找到数据");
        }

        BeanUtils.copyProperties(project, projectVo);

        Group group = groupRepository.findById(project.getGroupId()).get();

        if(group != null) {
            projectVo.setGroupName(group.getName());
        }

        return Response.success(projectVo);
    }

    @Transactional
    public Response delete(String projectId) {
        // 删掉项目下所有接口
        interfaceService.deleteByProjectId(projectId);
        // 删掉项目下所有数据模型
        dataModelService.deleteByProjectId(projectId);
        // 删掉所有的标签
        tagService.deleteByProjectId(projectId);
        // 删除掉该项目
        projectRepository.deleteById(projectId);
        return Response.success("success");
    }

    public Response publish(Project project) {
        project = projectRepository.findById(project.getId()).get();
        if (StringUtils.isEmpty(project.getDeployUrl())) {
            return Response.error("发布失败。请先填写项目发布地址");
        }
        // 触发自动构建
        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(project.getDeployUrl());
        try {
            HttpResponse hp = client.execute(get);
            int statusCode = hp.getStatusLine().getStatusCode();

            if(statusCode >= 200 && statusCode < 300){
                return Response.success("发布完成。");
            } else {
                return Response.error("发布失败。响应码：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.error("发布失败。");

    }

    public void deleteContent(String projectId) {
        // 删掉项目下所有接口
        interfaceService.deleteByProjectId(projectId);
        // 删掉项目下所有数据模型
        dataModelService.deleteByProjectId(projectId);
        // 删掉所有的标签
        tagService.deleteByProjectId(projectId);
    }

    /**
     *
     * @return
     */
    public List search(String title) {

        List<Project> projects = Lists.newArrayList();


        return projects;
    }

}
