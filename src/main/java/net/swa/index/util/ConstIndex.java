
package net.swa.index.util;

/**
 * 对外接口常量类
 * @author junyu
 *
 */
public class ConstIndex
{
    public static final String REGISTE_BY_EMAIL = "email";

    public static final String REGISTE_BY_MOBI = "mobi";

    public static final String DEFAULT_MAC = "abc";

    /**
     * token校验失败
     */
    public static final int ERR_TOKEN = 401;

    /**
     * 禁止访问
     */
    public static final int ERR_FORBIDDEN = 404;

    /***
     * 成功消息
     */
    public static final int ERR_NO = 0;

    /**
     * 自定义错误：必填参数缺失
     */
    public static final int ERR_LOST = 1;

    /**
     * 自定义错误：元数据错误
     * 
     */
    public static final int ERR_SOURCE = 2;

    /**
     * 查找失败,例如用户不存在
     */
    public static final int ERR_CANNOT_FOUND = 3;

    /**
     * 自定义错误：需要前置操作
     */
    public static final int ERR_NEED_PRE = 4;
    
    /**
     * 自定义错误：需要前置递归操作，如删除文件夹下有子文件夹
     */
    public static final int ERR_NEED_PRE_RECURSIVE = 40;
    
    /**
     * 自定义错误：致命错误
     */
    public static final int ERR_FATAL = 5;
    
    
    /**
     * 自定义错误：IO致命错误
     */
    public static final Object ERR_IO = 6;

    /**
     * 公共空间根分类
     */
    public static final long PUBLIC_TYPE_ROOT = 0;

    /**
     * 显示状态
     */
    public static final Integer STATUS_SHOW = 1;

    /**
     * 目录无子节点
     */
    public static final String CHILD_TYPE0 = "0";
    
    /**
     * 目录下只有有商品
     */
    public static final String CHILD_TYPE1= "1";
    
    /**
     * 目录下只有目录
     */
    public static final String CHILD_TYPE2 = "2";
    /**
     * 目录下有目录也有商品，不会出现
     */
    public static final String CHILD_TYPE3 = "3";


}
