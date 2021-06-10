
package net.swa.util.http;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServlet工具类
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 * @author author
 * @version 1.0, 2012-1-30
 * @see
 * @since V1.0
 */
public final class HttpServletUtil
{
    /**
     * 获取IP地址
     * @param request
     * @return
     */
    public static String getIpFromRequest(HttpServletRequest request)
    {
        if (null == request)
        {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (null == ip || 0 == ip.length() || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
