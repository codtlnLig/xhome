
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 公共空间分类
 * @author junyu
 *
 */
@Entity
@Table(name = "home_public_types")
public class PublicTypes
{
    //PK
    private Long id;

    /**
     * 父节点id
     */
    private Long pid;

    /**
     * 目录名称
     */
    private String name;

    private String imgUrl;

    //    private Long imgId;

    private String createTime;

    private String updateTime;

    /**
     * 创建者id
     */
    private Long createUserId;

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
