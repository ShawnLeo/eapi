package com.shawn.eapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.shawn.eapi.dialect.AbstractInfo;
import com.shawn.eapi.entity.RequestInfo;
import com.shawn.eapi.entity.ResponseInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InterfaceVo  implements Serializable{

    private String id;
    /**
     * 名称
     */
    private String name;

    /**
     * 方法
     */
    private String method;

    /**
     * 路径
     */
    private String path;

    /**
     * 标签
     */
    private List<String> tagIds = new ArrayList<>();

    /**
     * 操作方法
     */
    private String operationId;

    /**
     * 描述
     */
    private String description;

    /**
     * 弃用
     */
    private boolean deprecated;

    /**
     * 状态  100-未开始 200-开发中 300-测试中 400-已完成
     */
    private short status;

    /**
     * 排序
     */
    private Integer displayOrder;

    /**
     * 项目Id
     */
    private String projectId;
    /**
     * 创建人
     */
    private String creater;

    private String createrUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 路径参数
     */
    private List<RequestInfo> pathParams = new ArrayList<>();

    /**
     * 头参数
     */
    private List<RequestInfo> headers = new ArrayList<>();

    /**
     * query参数
     */
    private List<RequestInfo> querys = new ArrayList<>();

    /**
     * body参数
     */
    private List<RequestInfo> body = new ArrayList<>();

    /**
     * 表单提交参数
     */
    private List<RequestInfo> formDatas = new ArrayList<>();

    /**
     * 响应头
     */
    private List<ResponseInfo> responseHeader = new ArrayList<>();

    /**
     * 响应体
     */
    private List<ResponseInfo> responseBody = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<RequestInfo> getPathParams() {
        return pathParams;
    }

    public void setPathParams(List<RequestInfo> pathParams) {
        this.pathParams = pathParams;
    }

    public List<RequestInfo> getHeaders() {
        return headers;
    }

    public void setHeaders(List<RequestInfo> headers) {
        this.headers = headers;
    }

    public List<RequestInfo> getQuerys() {
        return querys;
    }

    public void setQuerys(List<RequestInfo> querys) {
        this.querys = querys;
    }

    public List<RequestInfo> getBody() {
        return body;
    }

    public void setBody(List<RequestInfo> body) {
        this.body = body;
    }

    public List<RequestInfo> getFormDatas() {
        return formDatas;
    }

    public void setFormDatas(List<RequestInfo> formDatas) {
        this.formDatas = formDatas;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterUserName() {
        return createrUserName;
    }

    public void setCreaterUserName(String createrUserName) {
        this.createrUserName = createrUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ResponseInfo> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(List<ResponseInfo> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<ResponseInfo> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(List<ResponseInfo> responseBody) {
        this.responseBody = responseBody;
    }

    @JsonIgnore
    public List<AbstractInfo> getRequestInfos() {
        List<AbstractInfo> requestInfos = Lists.newArrayList();
        requestInfos.addAll(this.getBody());
        requestInfos.addAll(this.getFormDatas());
        requestInfos.addAll(this.getPathParams());
        requestInfos.addAll(this.getHeaders());
        requestInfos.addAll(this.getQuerys());
        return requestInfos;
    }

    @JsonIgnore
    public List<AbstractInfo> getResponseInfos() {
        List<AbstractInfo> responseInfo = Lists.newArrayList();
        responseInfo.addAll(this.getResponseBody());
        responseInfo.addAll(this.getResponseHeader());
        return responseInfo;
    }

    @Override
    public String toString() {
        return "InterfaceVo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", tagIds=" + tagIds +
                ", operationId='" + operationId + '\'' +
                ", description='" + description + '\'' +
                ", deprecated=" + deprecated +
                ", status=" + status +
                ", displayOrder=" + displayOrder +
                ", projectId='" + projectId + '\'' +
                ", creater='" + creater + '\'' +
                ", createrUserName='" + createrUserName + '\'' +
                ", createTime=" + createTime +
                ", requestType='" + requestType + '\'' +
                ", pathParams=" + pathParams +
                ", headers=" + headers +
                ", querys=" + querys +
                ", body=" + body +
                ", formDatas=" + formDatas +
                ", responseHeader=" + responseHeader +
                ", responseBody=" + responseBody +
                '}';
    }
}
