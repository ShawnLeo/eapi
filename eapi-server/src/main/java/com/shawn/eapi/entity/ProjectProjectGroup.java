package com.shawn.eapi.entity;

import com.shawn.eapi.dialect.AbstractProjectGroup;

import javax.persistence.*;
import com.shawn.eapi.dialect.Schema.Tables;

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
