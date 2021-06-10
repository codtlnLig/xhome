
package net.swa.index.beans.vo;

import java.util.List;

/**
 * 公共空间账单虚拟类
 * @author junyu
 *
 */
public class VoPrivateBillMonth
{
    private Double price;

    private String createMonth;

    private Long createUserId;

    private Long zoneId;

    private List<VoPrivateBillDate> subList;

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

    public String getCreateMonth()
    {
        return createMonth;
    }

    public void setCreateMonth(String createMonth)
    {
        this.createMonth = createMonth;
    }

    public Long getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(Long zoneId)
    {
        this.zoneId = zoneId;
    }

    public List<VoPrivateBillDate> getSubList()
    {
        return subList;
    }

    public void setSubList(List<VoPrivateBillDate> subList)
    {
        this.subList = subList;
    }

}
