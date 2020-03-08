package com.shawn.eapi.entity;

import com.shawn.eapi.dialect.AbstractInfo;
import com.shawn.eapi.dialect.Schema;

import javax.persistence.*;

@Entity
@Table(name = Schema.Tables.REQUEST_INFO)
public class RequestInfo extends AbstractInfo {

    /**
     *  请求数据的数据类型
     *  body、header、formData、query、path
     */
    @Column(name = "PARAM_IN")
    private String paramIn;



    public String getParamIn() {
        return paramIn;
    }

    public void setParamIn(String paramIn) {
        this.paramIn = paramIn;
    }


}
