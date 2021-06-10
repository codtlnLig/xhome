
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 动态评论的回复
 * @author dawei
 */
@Entity
@Table(name = "home_dynamic_comments_reply")
public class DynamicComReply
{
    //PK
    private Long id;

    //评论的id
    private Long pid;

    /**
     * 回复的内容
     */
    private String reply;

    //0发表，1删除
    private Integer status;

    /**
     * 目标用户，即对谁的回复
     */
    private Long targetUserId;

    /**
     * 目标用户的昵称，不做数据库映射
     */
    private String targetNick;

    /**
     * 回复人的昵称，不做数据库映射
     */
    private String replyNick;

    /**
     * 创建者id，回复的评论人的用户
     */
    private Long replyUserId;

    private String createTime;

    private String updateTime;

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

    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Long getTargetUserId()
    {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId)
    {
        this.targetUserId = targetUserId;
    }

    public Long getReplyUserId()
    {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId)
    {
        this.replyUserId = replyUserId;
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
    public String getTargetNick()
    {
        return targetNick;
    }

    public void setTargetNick(String targetNick)
    {
        this.targetNick = targetNick;
    }

    @Transient
    public String getReplyNick()
    {
        return replyNick;
    }

    public void setReplyNick(String replyNick)
    {
        this.replyNick = replyNick;
    }

}
