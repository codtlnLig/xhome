
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 公共账单
 * @author junyu
 *
 */
@Entity
@Table(name = "home_public_bill")
public class PublicBill
{
    //PK
    private Long id;

    /**
     * 商品id
     */
    private Long pid;

    private String name;

    private String buyAddr;

    private String buyTime;

    /**
     * 金额
     */
    private Double price;

    /**
     * 创建者id
     */
    private Long createUserId;

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

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
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

}
