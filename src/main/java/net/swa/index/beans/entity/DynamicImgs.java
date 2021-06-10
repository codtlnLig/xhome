
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 动态信息类
 * @author dawei
 *
 */
@Entity
@Table(name = "home_dynamic_imgs")
public class DynamicImgs
{
    //PK
    private Long id;

    //动态id
    private Long pid;

    /**
     * 图片的url
     */
    private String imgUrl;

    /**
     * 图片的id,看情况可以不使用或者冗余
     */
    private Long imgId;

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

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public Long getImgId()
    {
        return imgId;
    }

    public void setImgId(Long imgId)
    {
        this.imgId = imgId;
    }

}
