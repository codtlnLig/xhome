
package net.swa.interceptor;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import net.swa.system.beans.entity.OperationLog;
import net.swa.system.beans.entity.User;
import net.swa.system.service.ICommonService;
import net.swa.system.util.annotation.Log;
import net.swa.util.Const;
import net.swa.util.DateUtils;
import net.swa.util.http.HttpServletUtil;

public class AuthorizeInterceptor extends HandlerInterceptorAdapter
{

    private final Logger log = Logger.getLogger(AuthorizeInterceptor.class);

    private List<String> includeAdmURLs;//后台要验证的列表为空时默认全要验证

    private List<String> includeFrontURLs;//前台要验证，验证规则不同于后台

    private List<String> exludeURLs;//排除的url,不要验证

    private ICommonService commonService;

    @SuppressWarnings("deprecation")
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 后台session控制  
        String uri = request.getRequestURI();
        boolean goon = true;//继续校验

        if (null != exludeURLs && exludeURLs.size() > 0)
        {
            for (String s : exludeURLs)
            {
                if (uri.indexOf(s) != -1)
                {
                    goon = false;
                    break;
                }
            }
        }
        if (goon && (null != includeFrontURLs) && includeFrontURLs.size() > 0)
        {
            for (String s : includeFrontURLs)
            {
                if (uri.indexOf(s) != -1)
                {
                    goon = false;
                    break;
                }
            }
            if (!goon)//前台要拦截
            {
                String u = (String) request.getSession().getAttribute("wxid");
                if (StringUtils.isBlank(u))
                {
                    String contextPath = request.getContextPath();
                    //                String url = request.getServletPath().toString();
                    response.sendRedirect(contextPath + "/jumpToLogin.jsp?redirectURL=" + URLEncoder.encode("index/main.do"));
                    log.debug("session wxid 过期。 " + uri);
                    return false;
                }
                else
                {
                    // 添加日志  
                    log.debug("拦截器拦截到有人访问微信端页面");
                }
            }
        }
        if (goon && (null != includeAdmURLs) && includeAdmURLs.size() > 0)
        {
            for (String s : includeAdmURLs)
            {
                if (uri.indexOf(s) != -1)
                {
                    goon = false;
                    break;
                }
            }
            if (!goon)//前台要拦截
            {
                User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
                if (null == user)
                {

                    String contextPath = request.getContextPath();
                    response.sendRedirect(contextPath + "/jumpToLogin.jsp?redirectURL=" + URLEncoder.encode("main.do"));
                    log.debug("session 过期。 " + uri);
                    return false;
                }
                String adminActions = "RoleActionMenuActionUserActionMenuAction";
                if (adminActions.contains(uri))
                {
                    if (((User) user).getUserType() == Const.USER_TYPE_ADMIN)
                    {
                    }
                    else
                    {
                        return false;
                    }
                }
            }
        }
        if (goon)
        {
            log.debug(uri + "====到死也没有拦截到");
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request , HttpServletResponse response , Object handler , Exception ex) throws Exception
    {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Log log = method.getAnnotation(Log.class);
        if (null != log)
        {
            String descript = log.description();

            OperationLog oLog = new OperationLog();
            String uri = request.getRequestURI();

            User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
            if (null != user)
            {
                oLog.setUserid(user.getLoginName());
            }
            oLog.setUpdateDate(DateUtils.getCurrDate(null));
            String ip = HttpServletUtil.getIpFromRequest(request);
            oLog.setIp(ip);
            oLog.setUri(uri.toString());
            oLog.setDescript(descript);
            commonService.commonAdd(oLog);
        }
        super.afterCompletion(request, response, handler, ex);
    }

    public ICommonService getCommonService()
    {
        return commonService;
    }

    @Required
    @Resource(name = "commonService")
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }

    public void setIncludeAdmURLs(List<String> includeAdmURLs)
    {
        this.includeAdmURLs = includeAdmURLs;
    }

    public void setIncludeFrontURLs(List<String> includeFrontURLs)
    {
        this.includeFrontURLs = includeFrontURLs;
    }

    public void setExludeURLs(List<String> exludeURLs)
    {
        this.exludeURLs = exludeURLs;
    }

}
