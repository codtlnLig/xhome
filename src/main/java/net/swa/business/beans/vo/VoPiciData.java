
package net.swa.business.beans.vo;

/**同批次产品**/
public class VoPiciData
{
    //PK
    private Long id;

    private String chanpinName;

    private String yuanliaoName;

    private String pici;

    private String shengchanGsi;

    private String traceCode;

    private String name;

    private String birthDate;

    private String zhibaoDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getShengchanGsi()
    {
        return shengchanGsi;
    }

    public void setShengchanGsi(String shengchanGsi)
    {
        this.shengchanGsi = shengchanGsi;
    }

    public String getTraceCode()
    {
        return traceCode;
    }

    public void setTraceCode(String traceCode)
    {
        this.traceCode = traceCode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(String birthDate)
    {
        this.birthDate = birthDate;
    }

    public String getZhibaoDate()
    {
        return zhibaoDate;
    }

    public void setZhibaoDate(String zhibaoDate)
    {
        this.zhibaoDate = zhibaoDate;
    }

    public String getYuanliaoName()
    {
        return yuanliaoName;
    }

    public void setYuanliaoName(String yuanliaoName)
    {
        this.yuanliaoName = yuanliaoName;
    }

    public String getPici()
    {
        return pici;
    }

    public void setPici(String pici)
    {
        this.pici = pici;
    }

    public String getChanpinName()
    {
        return chanpinName;
    }

    public void setChanpinName(String chanpinName)
    {
        this.chanpinName = chanpinName;
    }

}