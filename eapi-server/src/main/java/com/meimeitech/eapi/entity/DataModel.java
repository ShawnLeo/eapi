package com.meimeitech.eapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meimeitech.eapi.dialect.AbstractDataModel;
import com.meimeitech.eapi.dialect.Schema;
import com.meimeitech.eapi.model.DataModelVo;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = Schema.Tables.DATAMODEL)
public class DataModel extends AbstractDataModel {

    /**
     * 名称
     */
    @Column(name = "NAME",length = 100)
    private String name;


    /**
     * 基本数据类型 （boolean、array、null、 number、 string、 file、 object）
     ）
     */
    @Column(name = "DATA_TYPE",length = 32, nullable = false)
    private String dataType;

//    @ManyToOne(cascade = CascadeType.REFRESH)
//    @JoinColumn(name = "REF_ID")
//    private DataModel ref;

    /**
     * 类型 (system-系统类型  custom-用户定义 unit-被包含子项)
     */
    @Column(name = "TYPE",length = 32)
    private String type;

    /**
     * 子
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.EAGER)
    private List<DataModel> children;

    /**
     * 父
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private DataModel parent;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION",length = 2000)
    private String description;

    /**
     * 必填项
     */
    @Column(name = "REQUIRED")
    private boolean required = true;

    /**
     * 样例
     */
    @Column(name = "EXAMPLE")
    private String example;

    /**
     * 排序
     */
    @Column(name = "DISPLAYORDER",length = 10)
    private Integer displayOrder;

    /**
     * 项目Id
     */
    @Column(name = "PROJECT_ID",length = 40)
    private String projectId;

    /**
     * 创建人
     */
    @Column(name = "CREATER",length = 40)
    private String creater;

    /**
     * 创建人昵称
     */
    @Column(name = "CREATER_USERNAME",length = 64)
    private String createrUserName;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public DataModel getRef() {
//        return ref;
//    }
//
//    public void setRef(DataModel ref) {
//        this.ref = ref;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<DataModel> getChildren() {
        return children;
    }

    public void setChildren(List<DataModel> children) {
        this.children = children;
    }

    public DataModel getParent() {
        return parent;
    }

    public void setParent(DataModel parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
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
}
