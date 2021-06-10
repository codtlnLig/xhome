
package net.swa.util;

import java.math.BigDecimal;

public class Const
{

    public static final String CURRENT_USER = "cuser";

    public static double add(double v1 , double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 用户类型：0管理员，
     *
     */
    public static final short USER_TYPE_ADMIN = 0;

    /**
     * 用户类型：1公司管理员
     */
    public static final short USER_TYPE_FUWUZHAN = 1;

    /**
     * 用户类型：2普通员工
     */
    public static final short USER_TYPE_EMP = 2;

    /**
    * 用户操作的日志 -1代表数据失效  0 代表当前数据有效
    */
    public static final int SHI_XIAO = -1;

    public static final int YOU_XIAO = 0;

    public static final String MENDIAN_ROLE = "门店用户";

    /**
     * 订单状态0已经下单
     */
    public static final String DINGDAN_STATES0 = "0";

    /**
     * 订单状态1已完成
     */
    public static final String DINGDAN_STATES1 = "1";

    /**
     * 订单状态2已指派
     */
    public static final String DINGDAN_STATES2 = "2";

    /**
     * 订单状态3已作废
     */
    public static final String DINGDAN_STATES3 = "3";

    /**
     * 订单状态4已超期
     */
    public static final String DINGDAN_STATES4 = "4";

    /**
     * 订单状态5已核实
     */
    public static final String DINGDAN_STATES5 = "5";

    /**角色名称：食品公司***/
    public static final String ROLE_FUWUZHAN = "食品公司";

    /**角色名称：职工***/
    public static final String ROLE_EMP = "普通员工";

    public static final int MAX_RESULT = 60000;

    /**
     * 当前完成进度
     */
    public static final String CURRENT_PROCESS = "prcocess";

    /**
     * 线程池中线程数量
     */
    public static final int NUM_THREAD = 8;

    /***
     * 计算5分钟之前时间
     */
    public static final int MINUTE_FIVE = 5;

    /***
     * 计算15分钟之前时间
     */
    public static final int MINUTE_FIFTEN = 15;

    public static final String TOKEN = "access_token";

    /**微营销平台，获取用户未知(亚软)url**/
    public static final String HTTP_GETCLIENT_LOCATION = "http://java.softweare.net/wxapi/api/getUserRecentlyLocation.json";

    /**公众号appid*/
    public static final String APPID = "wx924ac7d11803f9c1";

    /**公众号密码*/
    public static final String SECRET = "f28f2ba41173c1407cb3a1dfedb4ae02";

    //    /**公众号appid*/
    //    public static final String APPID = "wx7919cd165cd0c660";
    //    /**公众号密码*/
    //    public static final String SECRET = "b06088975f9f7edd63389cf6fce856a7";
    //    /**微营销平台，获取用户未知url**/
    //    public static final String HTTP_GETCLIENT_LOCATION = "http://112.124.107.150/wxapi/api/getUserRecentlyLocation.json";
    /**oauth2 鉴权*/
    public static final String HTTP_WX__WEB_OAUTH_CHECK = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /***
     * 微信公众平台获取用户详细信息url
     * https://api.weixin.qq.com/cgi-bin/user/info
     */
    public static final String HTTP_WX_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";

    /***
     * 微信公众平台获取token
     */
    public static final String HTTP_WX_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

    /***
     * 微信公众平台获取用户List
     */
    public static final String HTTP_WX_USER_LIST = "https://api.weixin.qq.com/cgi-bin/user/get";

    /***
     * 微信公众平台获取用户List
     */
    public static final String HTTP_STATIC_IMAGE_URL = "https://api.weixin.qq.com/cgi-bin/user/get";

    /***
     * 百度地图纠偏接口
     */
    public static final String HTTP_BAIDU_CONVERT_URL = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4";

    /***
     * 百度地图纠偏接口
     * 谷歌地图坐标
     */
    public static final String HTTP_BAIDU_CONVERT_URL2 = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4";

    /***
     * GPS--谷歌地图坐标 转换接口
     */
    public static final String HTTP_BAIDU_CONVERT_URL3 = "http://map.yanue.net/gpsApi.php?";

    /**微信 关注状态1***/
    public static final Integer WX_GUANZHU = 1;

    /**微信 取消关注状态2***/
    public static final Integer WX_QUXIAO = 2;

    /**有效状态**/
    public static final int STATUS_YES = 1;

    /**无效状态**/
    public static final int STATUS_NO = 0;

    public static final int MAXNUM = 1000;

    /**系统配置信息类别名称**/
    public static final Object CONFIG_INFO = "configInfo";

    /**系统配置信息：电池上门更换服务费**/
    public static final Object CONFIG_SERVER_PRICE = "serverPrice";

    /**48小时**/
    public static final int HOUR48 = 48;

    /**格式化字符串"yyyy-MM-dd HH:mm:ss"**/
    public static final String FORMAT_STRING1 = "yyyy-MM-dd HH:mm:ss";

    /**格式化字符串"yyyy年MM月dd日"**/
    public static final String FORMAT_STRING2 = "yyyy年MM月dd日";

    /**车型展示状态**/
    public static final String CHEXING_STATE_SHOW = "1";

    /**车型不展示状态**/
    public static final String CHEXING_STATE_HIDDEN = "0";

    public static final String DEWEI_NAME = "得威";

    public static final String HTTP_BACKUP_MYSQL = "http://localhost/mysql_backup.php";

    /**
     * 高德geo接口
     */
    public static final String HTTP_MAP_GEO_GAODE = "http://restapi.amap.com/v3/geocode/regeo?key=e4e68a5cf6df32793a5c7f00e7d17253&radius=500";

    /**
     * google geo接口
     */
    public static final String HTTP_MAP_GEO_GOOGLE = "https://maps.google.com/maps/api/geocode/json";

    public static final String DEFAULT_KEY = "dewei_default_key";

    public static final String CODE_UTF8 = "utf-8";

    public static final String ZTREE_AREA_NAME = "区域信息";

    public static final String ZTREE_OPEN = "open";

    /***
     * 价格上调
     */
    public static final String PRICE_CHANGE_TYPE_PLUS = "1";

    /***
     * 价格下调
     */
    public static final String PRICE_CHANGE_TYPE_MINUS = "-1";

    /**
     * 调整统一价格
     */
    public static final String PRICE_CHANGE_ZONE_ALL = "all";

    /**
     * 调区域价格
     */
    public static final String PRICE_CHANGE_ZONE_PART = "part";

    public static final String FOODM2STATE1 = "1";

    public static final String FOODM2STATE2 = "2";

}