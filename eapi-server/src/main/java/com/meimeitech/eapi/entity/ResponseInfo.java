package com.meimeitech.eapi.entity;

import com.meimeitech.eapi.dialect.AbstractInfo;
import com.meimeitech.eapi.dialect.Schema;

import javax.persistence.*;

@Entity
@Table(name = Schema.Tables.RESPONSE_INFO)
public class ResponseInfo extends AbstractInfo{

    /**
     *  请求数据的数据类型
     *  schema、headers
     */
    @Column(name = "RESPONSE_IN")
    private String responseIn;

    /**
     * 描述
     */
    @Column(name = "DESCRIPTION",length = 2000)
    private String description;

    public String getResponseIn() {
        return responseIn;
    }

    public void setResponseIn(String responseIn) {
        this.responseIn = responseIn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
