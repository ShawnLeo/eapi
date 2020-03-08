package com.shawn.sys.entity;

import com.shawn.sys.dialect.AbstractResetUser;
import com.shawn.sys.dialect.Schema.Tables;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = Tables.RESET_USER)
public class ResetUser extends AbstractResetUser {

    private static final long serialVersionUID = -5228484640414835579L;
    /**
     *邮箱
     */
    @Column(name = "EMAIL",length = 80)
    private String email;

    /**
     *创建时间
     */
    @Column(name = "LAST_SEND_TIME")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastSendTime = new Date();

    /**
     * 重置码
     */
    @Column(name = "RESET_CODE",length = 256)
    private String resetCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(Date lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }
}
