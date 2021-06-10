
package net.swa.index.beans.vo;

/**
 * 公共空间账单虚拟类
 * @author junyu
 *
 */
public class VoPrivateBillDate
{
    private Double price;

    private String createDate;

    private Long createUserId;

    private Long zoneId;

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
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
}
