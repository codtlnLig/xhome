
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 个人空间分类
 * @author junyu
 *
 */
@Entity
@Table(name = "home_private_types")
public class PrivateTypes
{
    //PK
    private Long id;

    /**
     * 父节点id
     */
    private Long pid;

    /**
     * 空间id ，若是一级节点便是自身id
     */
    private Long zoneId;

    /**
     * 目录名称
     */
    private String name;

    /**
     * 图片的url
     */
    private String imgUrl;

    /**
     * 密码
     */
    private String password;

    /**
     * 提示问题
     */
    private String question;

    /**
     * 回答
     */
    private String answer;

    /**
     * 创建者id
     */
    private Long createUserId;

    private String createTime;

    private String updateTime;

    //0无子节点 1 有商品 2 有目录
    private String childType;

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getPid()
    {
        return pid;
    }

    public void setPid(Long pid)
    {
        this.pid = pid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public Long getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId)
    {
        this.createUserId = createUserId;
    }

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    @Transient
    public String getChildType()
    {
        return childType;
    }

    public void setChildType(String childType)
    {
        this.childType = childType;
    }

}
