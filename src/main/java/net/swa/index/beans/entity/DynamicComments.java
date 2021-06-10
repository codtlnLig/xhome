
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
 * 动态评论
 * @author dawei
 */
@Entity
@Table(name = "home_dynamic_comments")
public class DynamicComments
{
    //PK
    private Long id;

    //动态id
    private Long pid;

    /**
     * 评论内容
     */
    private String comments;

    //0发表，1删除
    private Integer status;

    /**
     * 创建者id
     */
    private Long createUserId;

    /**
     * 创建者昵称，不做数据库映射
     */
    private String createNick;

    private String createTime;

    private String updateTime;

    /**
     * 评论的回复，不做数据库映射
     */
    private List<DynamicComReply> replys;

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

    public Long getPid()
    {
        return pid;
    }

    public void setPid(Long pid)
    {
        this.pid = pid;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    @Transient
    public String getCreateNick()
    {
        return createNick;
    }

    public void setCreateNick(String createNick)
    {
        this.createNick = createNick;
    }

    @Transient
    public List<DynamicComReply> getReplys()
    {
        return replys;
    }

    public void setReplys(List<DynamicComReply> replys)
    {
        this.replys = replys;
    }

}
