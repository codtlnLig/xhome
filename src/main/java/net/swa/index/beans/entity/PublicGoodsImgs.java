
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 共享空间商品的图片信息表
 * @author junyu
 *
 */
@Entity
@Table(name = "home_public_goods_img")
public class PublicGoodsImgs
{
    //PK
    private Long id;

    /**
     * 商品id
     */
    private Long goodId;

    /**
     * 图片的url
     */
    private String imgUrl;

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

    public Long getGoodId()
    {
        return goodId;
    }

    public void setGoodId(Long goodId)
    {
        this.goodId = goodId;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

}
