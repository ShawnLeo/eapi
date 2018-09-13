package com.meimeitech.eapi.entity;

import com.meimeitech.eapi.dialect.AbstractProjectGroup;

import javax.persistence.*;
import com.meimeitech.eapi.dialect.Schema.Tables;

@Entity
@Table(name = Tables.PROJECT_GROUP)
public class ProjectProjectGroup extends AbstractProjectGroup {

    /**
     * 项目组名称
     */
    @Column(name = "NAME",length = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
