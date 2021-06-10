
package net.swa.index.beans.vo;

import java.util.List;

/**
 * 公共空间账单虚拟类
 * @author junyu
 * v_month_public_bill
 *
 */
public class VoPublicBillMonth
{
    private Double price;

    private String createMonth;

    private Long createUserId;

    private List<VoPublicBillDate> subList;

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getCreateMonth()
    {
        return createMonth;
    }

    public void setCreateMonth(String createMonth)
    {
        this.createMonth = createMonth;
    }

    public Long getCreateUserId()
    {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId)
    {
        this.createUserId = createUserId;
    }

    public List<VoPublicBillDate> getSubList()
    {
        return subList;
    }

    public void setSubList(List<VoPublicBillDate> subList)
    {
        this.subList = subList;
    }

}
