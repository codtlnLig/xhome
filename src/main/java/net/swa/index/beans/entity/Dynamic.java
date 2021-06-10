
package net.swa.index.beans.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 动态信息类
 * @author dawei
 *
 */
@Entity
@Table(name = "home_dynamic")
public class Dynamic
{
    //PK
    private Long id;

    private String text;

    //0发表，1删除
    private Integer status;

    /**
     * 创建者id
     */
    private Long createUserId;

    /**
     * 创建者昵称，不做数据库映射
     */
    private String nickName;

    /**
     * 创建者头像url，不做数据库映射
     */
    private String imgUrl;

    @Transient
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    private String createTime;

    private String updateTime;

    /**
     * 动态的图片，不做数据库映射
     */
    private List<DynamicImgs> imgs;

    /**
     * 动态的评论，不做数据库映射
     */
    private List<DynamicComments> comments;

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

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Long getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId)
    {
        this.createUserId = createUserId;
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

    @Transient
    public List<DynamicImgs> getImgs()
    {
        return imgs;
    }

    public void setImgs(List<DynamicImgs> imgs)
    {
        this.imgs = imgs;
    }

    @Transient
    public List<DynamicComments> getComments()
    {
        return comments;
    }

    public void setComments(List<DynamicComments> comments)
    {
        this.comments = comments;
    }

    @Transient
    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

}
