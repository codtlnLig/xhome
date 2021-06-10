
package net.swa.system.web.action;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import net.swa.index.util.ConstIndex;
import net.swa.util.EncryptTool;
import net.swa.util.JsonResult;

public abstract class AbstractBaseAction
{
    private static final Logger log = Logger.getLogger(AbstractBaseAction.class);

    protected void outError(String msg , HttpServletResponse response) throws Exception
    {
        JsonResult<Object> json = new JsonResult<Object>();
        json.setSuccess(false);
        json.setMessage(msg);
        outJson(json, response);
    }

    protected Object getDTO(String jsonString , Class<?> clazz)
    {
        log.debug("json string is " + jsonString);
        JSONObject jsonObject = null;
        try
        {
            setDataFormat2JAVA();
            jsonObject = JSONObject.fromObject(jsonString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return JSONObject.toBean(jsonObject, clazz);
    }

    private void setDataFormat2JAVA()
    {
        //设定日期转换格式 
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }));
    }

    protected String getTokenFromHeader(HttpServletRequest req)
    {
        return req.getHeader("token");
    }

    protected String getMacFromHeader(HttpServletRequest req)
    {
        return req.getHeader("mac");
    }

    protected Long getUserId(HttpServletRequest req)
    {
        String mac = req.getHeader("mac");
        String token = req.getHeader("token");
        Long id = null;
        if (StringUtils.isBlank(mac))
        {
            mac = ConstIndex.DEFAULT_MAC;
        }
        EncryptTool tool;
        try
        {
            tool = new EncryptTool(mac);
            String userid = tool.decrypt(token);
            if (!StringUtils.isBlank(userid))
            {
                id = Long.parseLong(userid);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }

    protected Long getUserId(String token , String mac)
    {
        Long id = null;
        if (StringUtils.isBlank(mac))
        {
            mac = ConstIndex.DEFAULT_MAC;
        }
        EncryptTool tool;
        try
        {
            tool = new EncryptTool(mac);
            String userid = tool.decrypt(token);
            if (!StringUtils.isBlank(userid))
            {
                id = Long.parseLong(userid);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }

    protected void outSuccess(HttpServletResponse response) throws Exception
    {
        JsonResult<Object> json = new JsonResult<Object>();
        outJson(json, response);
    }

    protected void outString(String obj , HttpServletResponse response) throws Exception
    {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println(obj);
        response.getWriter().flush();
    }

    public void outJson(Object obj , HttpServletResponse response) throws Exception
    {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        String json = null;
        if (obj instanceof Collection<?>)
        {
            JSONArray ja = JSONArray.fromObject(obj);
            json = ja.toString();
        }
        else
        {
            json = JSONObject.fromObject(obj).toString();
        }
        response.getWriter().println(json);
        response.getWriter().flush();
    }

    public String getJson(Object obj)
    {
        String json = null;
        if (obj instanceof Collection<?>)
        {
            JSONArray ja = JSONArray.fromObject(obj);
            json = ja.toString();
        }
        else
        {
            json = JSONObject.fromObject(obj).toString();
        }
        return json;
    }

    /***
     * 转换json 忽略循环
     * @param obj
     * @return
     */
    public String getJsonWithCiecle(Object obj)
    {
        String json = null;
        if (obj instanceof Collection<?>)
        {
            JsonConfig jsonConfig = new JsonConfig(); //建立配置文件
            jsonConfig.setIgnoreDefaultExcludes(false); //设置默认忽略
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            //此处是亮点，不过经过测试，第2种方法有些悲剧，虽然可以使用，但其结果貌似循环数次，至于为啥，还请高人指点。
            JSONArray ja = JSONArray.fromObject(obj, jsonConfig);
            json = ja.toString();
        }
        else
        {
            json = JSONObject.fromObject(obj).toString();
        }
        return json;
    }
}
