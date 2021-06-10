
package net.swa.index.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.swa.system.web.action.AbstractBaseAction;
import org.apache.log4j.Logger;

/**
 * 前台界面抽象action 基类
 * @author dawei chen
 *
 */
public abstract class AbstractBaseIndexAction extends AbstractBaseAction
{

    private static final long serialVersionUID = 3625388438776145908L;

    private final Logger log = Logger.getLogger(AbstractBaseIndexAction.class);

    protected void addCookie(HttpServletResponse resp , String name , String value)
    {
        try
        {
            value = URLEncoder.encode(value, "UTF-8");
            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(60 * 60 * 24 * 365);
            resp.addCookie(cookie);
        }
        catch (UnsupportedEncodingException e)
        {
            log.debug("UnsupportedEncodingException 转化中文");
            e.printStackTrace();
        }
    }

    /**
     * Cookie取得
     * @return
     * @throws Exception
     */
    protected String getCookie(String name , HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals(name))
                {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
