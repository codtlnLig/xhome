
package net.swa.index.web.action;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;
import net.swa.index.beans.entity.Dynamic;
import net.swa.index.beans.entity.DynamicImgs;
import net.swa.index.beans.vo.VoListPage;
import net.swa.index.service.DynamicService;
import net.swa.system.web.action.AbstractBaseAction;

/**
 * 提供客户端接口 Action
 * 动态信息
 * @author junyu
 *
 */
@Controller
@RequestMapping(value = "/dynamic")
public class DynamicAction extends AbstractBaseAction
{
    private DynamicService dynamicService;

    /**
     * 新增动态信息
     * 动态信息json格式
     * {"imgs":[{"imgUrl":"http://www.baidu.com/0.jpg"}],"text":"内容"}
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveDynamic")
    public void saveDynamic(String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        StringBuffer json = new StringBuffer();
        //json 格式数据收集
        BufferedReader reader = req.getReader();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            json.append(line);
        }
        JSONObject obj = JSONObject.fromObject(json.toString());
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("imgs", DynamicImgs.class);
        Dynamic model = (Dynamic) JSONObject.toBean(obj, Dynamic.class, classMap);
        // 解析json到对象
        //        Dynamic model = (Dynamic) getDTO(json.toString(), Dynamic.class);
        //动态只可以新增
        map = dynamicService.add(uid, model);
        outJson(map, rsp);
    }

    /**
     * 对一条动态添加评论
     * 会将新加的评论返回
     * @param pid  要评论的动态id
     * @param comments  评论内容
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveComment")
    public void saveComment(Long pid , String comments , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }

        //动态只可以新增
        map = dynamicService.addComment(uid, pid, comments);
        outJson(map, rsp);
    }

    /**
     * 评论的回复
     * @param pid 评论的id
     * @param targetUserId   回复目标人的id（没有默认回复评论的发表者）
     * @param reply  回复的内容
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveReply")
    public void saveReply(Long pid , Long targetUserId , String reply , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        //动态只可以新增
        map = dynamicService.addReply(uid, pid, targetUserId, reply);
        outJson(map, rsp);
    }

    /**
     * 根据动态id 查询动态详细信息
     * @param id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryDetail")
    public void queryDetail(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = dynamicService.queryById(uid, id);
        outJson(map, rsp);
    }

    /**
     * 查询动态信息列表，
     * @param currentId 上次结果最小的id
     * @param num       要查的最大数量
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryList")
    public void queryList(Long currentId, Integer num , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        VoListPage<Dynamic> json = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        json = dynamicService.queryPageList(uid, currentId, num);
        outJson(json, rsp);
    }
    
    @RequestMapping(value = "/queryMyList")
    public void queryMyList(Long currentId, Integer num , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        VoListPage<Dynamic> json = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        json = dynamicService.queryMyPageList(uid, currentId, num);
        outJson(json, rsp);
    }

    /**
     * 删除动态
     * @param id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteDynamic")
    public void deleteDynamic(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = dynamicService.deleteDynamic(uid, id);
        outJson(map, rsp);
    }

    /**
     * 删除动态评论（会将评论的回复内容一起删除）
     * @param id 评论id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteComment")
    public void deleteComment(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = dynamicService.deleteComment(uid, id);
        outJson(map, rsp);
    }

    /**
     * 删除动态评论的回复
     * @param id 回复id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteReply")
    public void deleteReply(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = dynamicService.deleteReply(uid, id);
        outJson(map, rsp);
    }

    public DynamicService getDynamicService()
    {
        return dynamicService;
    }

    @Resource
    @Required
    public void setDynamicService(DynamicService dynamicService)
    {
        this.dynamicService = dynamicService;
    }

}
