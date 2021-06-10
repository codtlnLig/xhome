
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
 * 共享空间商品
 * @author junyu
 *
 */
@Entity
@Table(name = "home_public_goods")
public class PublicGoods
{
    //PK
    private Long id;

    /**
     * 分类id
     */
    private Long typeId;

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

    /**
     * 详细信息中图片，不做数据库映射
     */
    private List<PublicGoodsImgs> imgs;

    /**
     * 图片的id,看情况可以不使用或者冗余
     */
    private Long imgId;

    private String createTime;

    private String updateTime;

    /**
     * 创建者id
     */
    private Long createUserId;

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

    public Long getImgId()
    {
        return imgId;
    }

    public void setImgId(Long imgId)
    {
        this.imgId = imgId;
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
    public List<PublicGoodsImgs> getImgs()
    {
        return imgs;
    }

    public void setImgs(List<PublicGoodsImgs> imgs)
    {
        this.imgs = imgs;
    }

    @Override
    public String toString()
    {
        return "PublicGoods [buyAddr=" + buyAddr + ", buyTime=" + buyTime + ", createTime=" + createTime + ", createUserId=" + createUserId + ", detail=" + detail + ", id=" + id + ", imgId=" + imgId + ", imgUrl=" + imgUrl + ", imgs=" + imgs + ", name=" + name + ", price=" + price + ", typeId=" + typeId + ", updateTime=" + updateTime + "]";
    }

}
