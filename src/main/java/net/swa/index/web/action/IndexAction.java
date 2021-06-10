
package net.swa.index.web.action;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.swa.index.beans.entity.Members;
import net.swa.index.service.IndexService;
import net.swa.system.web.action.AbstractBaseAction;

/**
 * 提供客户端接口 Action
 * 客户端用户信息相关
 * @author junyu
 *
 */
@Controller
@RequestMapping(value = "/index")
public class IndexAction extends AbstractBaseAction
{

    private IndexService indexService;

    /**
     * 登陆 
     * @param email 邮箱       （若采用手机登陆，便不传）
     * @param mobi  手机号码（若采用邮箱登陆，便不传）
     * @param password 密码
     * @param mac     硬件地址(在登陆后操作中不会变化)
     * @throws Exception
     */
    @RequestMapping(value = "/login")
    public void login(String email , String mobi , String password , String mac , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        map = indexService.login(email, mobi, password, mac);
        if (null != map.get("user"))
        {
            Members o = (Members) map.get("user");
            o.setPassword(null);
            map.put("user", o);
        }
        outJson(map, rsp);
    }

    /**
     * 验证码发送，暂无短信发送仅仅生成验证码
     * @param mobi 手机号码
     * @param mac  硬件唯一地址（考虑扩展绑定，暂时可以不传）
     * @throws Exception
     */
    @RequestMapping(value = "/generateCode")
    public void generateCode(String mobi , String mac , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        map = indexService.saveCode(mobi, mac);
        outJson(map, rsp);
    }

    /**
     * 注册
     * @param mobi 手机号码（若采用邮箱注册，便不传）
     * @param code 验证码     （若采用邮箱注册，便不传）
     * @param email 邮箱       （若采用手机注册，便不传）
     * @param nickname  昵称
     * @param password  密码
     * @param mac       硬件唯一地址（考虑扩展绑定，暂时可以不传）
     * @throws Exception
     */
    @RequestMapping(value = "/register")
    public void register(String mobi , String code , String email , String nickname , String password , String mac , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        map = indexService.saveRegister(mobi, code, email, nickname, password, mac);
        outJson(map, rsp);
    }

    /**
     * 修改密码
     * @param password 密码
     * @param token   校验使用登陆时候返回
     * @param mac
     * @throws Exception
     */
    @RequestMapping(value = "/changePassword")
    public void changePassword(String newpassword , String oldpassword , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = indexService.updatePassword(uid, newpassword, oldpassword);
        outJson(map, rsp);
    }

    /**
     * 修改昵称
     * @param nickname 昵称
     * @param token   校验使用登陆时候返回
     * @param mac
     * @throws Exception
     */
    @RequestMapping(value = "/changeNickname")
    public void changeNickname(String nickname , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;

        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = indexService.updateNick(uid, nickname);
        outJson(map, rsp);
    }

    @RequestMapping(value = "/changePhoto")
    public void changePhoto(String imgUrl , Long imgId , String mac , String token , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;

        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = indexService.updatePhoto(uid, imgUrl);
        outJson(map, rsp);
    }

    /**
     * 修改头像
     * @param imgUrl
     * @param mac
     * @param token
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/updatePhoto")
    public void updatePhoto(String imgUrl , String mac , String token , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;

        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = indexService.updatePhoto(uid, imgUrl);
        outJson(map, rsp);
    }

    @Required
    @Resource
    public void setIndexService(IndexService indexService)
    {
        this.indexService = indexService;
    }
}
