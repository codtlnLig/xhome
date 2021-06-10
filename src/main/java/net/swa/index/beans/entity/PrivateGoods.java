
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
 * 个人空间商品
 * @author junyu
 *
 */
@Entity
@Table(name = "home_private_goods")
public class PrivateGoods
{
    //PK
    private Long id;

    /**
     * 分类id
     */
    private Long typeId;

    /**
     * 空间id ，个人顶级目录 id
     */
    private Long zoneId;

    /**
     * 名称/品牌
     */
    private String name;

    private String buyAddr;

    private String buyTime;

    private Double price;

    private String detail;

    /**
     * 图片的url
     */
    private String imgUrl;

    private String createTime;

    private String updateTime;

    /**
     * 创建者id
     */
    private Long createUserId;

    /***
     * 商品图片，不做数据库映射
     */
    private List<PrivateGoodsImgs> imgs;

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

    public Long getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBuyAddr()
    {
        return buyAddr;
    }

    public void setBuyAddr(String buyAddr)
    {
        this.buyAddr = buyAddr;
    }

    public String getBuyTime()
    {
        return buyTime;
    }

    public void setBuyTime(String buyTime)
    {
        this.buyTime = buyTime;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
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

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    @Transient
    public List<PrivateGoodsImgs> getImgs()
    {
        return imgs;
    }

    public void setImgs(List<PrivateGoodsImgs> imgs)
    {
        this.imgs = imgs;
    }

}
