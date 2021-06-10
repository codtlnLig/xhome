
package net.swa.system.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 用户操作日志
 * @author 陈晓伟
 *
 */
@Entity
@Table(name = "sys_log")
public class OperationLog
{
    private long id;

    private String userid;

    private String uri;

    private String descript;

    //客户机器ip
    private String ip;

    private String updateDate;

    private long status;

    // 主键 ：@Id 主键生成方式：strategy = "assigned"
    // 映射表中id这个字段，不能为空，并且是唯一的
    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public String getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(String updateDate)
    {
        this.updateDate = updateDate;
    }

    public long getStatus()
    {
        return status;
    }

    public void setStatus(long status)
    {
        this.status = status;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

}
