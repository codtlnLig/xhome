
package net.swa.main.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import net.swa.system.beans.entity.Menu;
import net.swa.system.beans.entity.User;
import net.swa.system.service.ICommonService;
import net.swa.system.service.IMenuService;
import net.swa.system.web.action.AbstractBaseAction;
import net.swa.util.Const;
import net.swa.util.EncryptTool;
import net.swa.util.JsonResult;

/**
 * 后台管理
 * 
 * @author Administrator
 * 
 */
@Controller
public class MainAction extends AbstractBaseAction
{

    private static final long serialVersionUID = -8445520563685861470L;

    private ICommonService commonService;

    private IMenuService menuService;

    private static final Logger log = Logger.getLogger(MainAction.class);
    /**
     * 主页
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/main")
    public ModelAndView main(HttpServletRequest request , HttpSession session) throws Exception
    {
        log.debug("进入管理后台。。。");
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user)
        {
            // 跳转到后台登陆页面
            return new ModelAndView("main/login");
        }
        else
        {
            List<Menu> roots = menuService.getRootsByRoleId(((User) session.getAttribute(Const.CURRENT_USER)).getRole().getId());
            ModelAndView mv = new ModelAndView("main/main");
            mv.addObject("roots", getJsonWithCiecle(roots));
            mv.addObject("user", user);
            return mv;
        }
    }

    /**
     * 登陆页面
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/adminLogin")
    public void adminLogin(String code , @ModelAttribute User user , HttpSession session , HttpServletResponse response) throws Exception
    {
        Object vc = session.getAttribute("validateCode");
        String validateCode = "";
        if (vc != null)
        {
            validateCode = vc.toString();
        }
        JsonResult<User> json = new JsonResult<User>();
        if (!validateCode.equalsIgnoreCase(code))
        {
            json.setSuccess(false);
            json.setMessage("1");
        }
        else
        {
            List<User> list = commonService.search("loginName", user.getLoginName(), User.class);
            if (list.size() == 0)
            {
                json.setSuccess(false);
                json.setMessage("用户不存在");
            }
            else
            {
                User dbUser = list.get(0);
                EncryptTool tool = new EncryptTool(user.getLoginName());
                String password = tool.decrypt(dbUser.getPassword());
                if (password.equals(user.getPassword()))
                {
                    session.setAttribute(Const.CURRENT_USER, dbUser);
                }
                else
                {
                    json.setSuccess(false);
                    json.setMessage("用户名或密码错误");
                }
            }
        }
        outJson(json, response);
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session)
    {
        session.removeAttribute(Const.CURRENT_USER);
        return "redirect:/main.do";
    }

    /**
     * 进入修改密码页面
     * 
     * @return
     */
    @RequestMapping(value = "/profile")
    public String profile()
    {
        return "main/updatePwd";
    }

    /**
     * 确定修改密码
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/resetPwd")
    public void resetPwd(String oldpwd , String loginName , String password , HttpSession session , HttpServletResponse response) throws Exception
    {
        User u = (User) session.getAttribute(Const.CURRENT_USER);
        if (null != u)
        {
            u = commonService.findByAttribute(User.class, "loginName", u.getLoginName());
            if (null != u)
            {
                EncryptTool tool = new EncryptTool(u.getLoginName());
                String psw = tool.encrypt(oldpwd); // 将页面输入的密码进行加密，跟数据库中的加密密码进行
                // 比较（也可以吧数据库中的解密后比较（详细参考EncryptTool加密类））
                if (psw.equals(u.getPassword()))// 将登陆页面上的16位进制的密码值转换成byte数组
                {
                    u.setPassword(tool.encrypt(password));// 在放入session前，把用户密码设置成null
                    // 保证在session取不到用户密码(安全)
                    commonService.commonUpdate(u);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("success", true);
                    map.put("loginName", loginName);
                    map.put("message", "");
                    outJson(map, response);
                }
                else
                {
                    outError("密码输入错误", response);
                }
            }
            else
            {
                outError("ERROR", response);
            }
        }
        else
        {
            outError("请重新登录", response);
        }
    }

    @Resource
    @Required
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }

    public IMenuService getMenuService()
    {
        return menuService;
    }

    @Resource
    @Required
    public void setMenuService(IMenuService menuService)
    {
        this.menuService = menuService;
    }
}
