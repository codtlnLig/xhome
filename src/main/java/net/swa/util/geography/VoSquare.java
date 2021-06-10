
package net.swa.util.geography;

/***
 * 虚拟矩形的四个点
 * 实际上只有四个值
 * @author dawei
 *
 */
public class VoSquare
{
    private Double maxLat;

    private Double maxLng;

    private Double minLat;

    private Double minLng;

    public Double getMaxLat()
    {
        return maxLat;
    }

    public void setMaxLat(Double maxLat)
    {
        this.maxLat = maxLat;
    }

    public Double getMaxLng()
    {
        return maxLng;
    }

    public void setMaxLng(Double maxLng)
    {
        this.maxLng = maxLng;
    }

    public Double getMinLat()
    {
        return minLat;
    }

    public void setMinLat(Double minLat)
    {
        this.minLat = minLat;
    }

    public Double getMinLng()
    {
        return minLng;
    }

    public void setMinLng(Double minLng)
    {
        this.minLng = minLng;
    }

    @Override
    public String toString()
    {
        return "VoSquare [maxLat=" + maxLat + ", maxLng=" + maxLng + ", minLat=" + minLat + ", minLng=" + minLng + "]";
    }

}
