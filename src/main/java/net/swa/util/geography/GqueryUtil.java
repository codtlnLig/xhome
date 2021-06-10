
package net.swa.util.geography;

/***
 * 空间查询方法类
 * @author dawei
 *
 */
public class GqueryUtil
{
    //地球半径
    public final static double EARTH_RADIUS = 6371;

    public static VoSquare querySquarePoint(double lng , double lat , double distance)
    {

        double dlng = 2 * Math.asin(Math.sin(distance / (2 * EARTH_RADIUS)) / Math.cos(deg2rad(lat)));
        dlng = rad2deg(dlng);
        double dlat = distance / EARTH_RADIUS;
        dlat = rad2deg(dlat);
        VoSquare v = new VoSquare();
        v.setMaxLat(lat + dlat);
        v.setMinLng(lng - dlng);
        v.setMaxLng(lng + dlng);
        v.setMinLat(lat - dlat);
        return v;
    }

    //将角度转换为弧度
    public static double deg2rad(double degree)
    {
        return degree / 180 * Math.PI;
    }

    //将弧度转换为角度
    public static double rad2deg(double radian)
    {
        return radian * 180 / Math.PI;
    }
}
