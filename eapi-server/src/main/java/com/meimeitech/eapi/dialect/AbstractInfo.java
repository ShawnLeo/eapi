package com.meimeitech.eapi.dialect;

import com.meimeitech.eapi.entity.DataModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class  AbstractInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String GENERATOR_NAME = "system-uuid";

    @Id
    @GenericGenerator(name = GENERATOR_NAME, strategy = "uuid")
    @GeneratedValue(generator = GENERATOR_NAME)
    @Column(name = "ID",length = 40,unique = true,nullable = false)
    protected String id;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "DATAMODEL_ID", nullable = false)
    private DataModel dataModel;


    @Column(name = "INTERFACE_ID",length = 40 ,nullable = false)
    private String interfaceId;

    /**
     * 必填项
     */
    @Column(name = "REQUIRED")
    private boolean required = true;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
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