
package net.swa.system.web.action;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import net.swa.system.beans.entity.Dict;
import net.swa.system.beans.entity.Role;
import net.swa.system.beans.entity.User;
import net.swa.system.service.ICommonService;
import net.swa.util.Const;
import net.swa.util.DateUtils;
import net.swa.util.EncryptTool;
import net.swa.util.HtmlUtil;
import net.swa.util.StringUtil;

@Controller
@RequestMapping(value = "/user")
public class UserAction extends AbstractBaseAction
{
    private static final long serialVersionUID = 8631793382515907988L;

    private ICommonService commonService;

    @RequestMapping(value = "/listPage")
    public String listPage() throws Exception
    {
        return "system/user/list";
    }

    @RequestMapping(value = "/checkUserId")
    public void checkUserId(@ModelAttribute User user , HttpServletResponse rsp) throws Exception
    {
        List<User> list = commonService.search("loginName", user.getLoginName(), User.class);
        outString((list.size() == 0) + "", rsp);
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit(@ModelAttribute User user , Long id , HttpSession session) throws Exception
    {
        ModelAndView mv = new ModelAndView("system/user/edit");
        List<Dict> userTypes = null;
        if (id == null || 0 == id)
        {
            user = new User();
            // 如果当前用户是服务站，则相当于为当前服务站再增加用户。
            User cuser = (User) session.getAttribute(Const.CURRENT_USER);
            if (null != cuser)
            {
                userTypes = commonService.search("title", "用户类型", Dict.class);
            }
        }
        else
        {
            userTypes = commonService.search("title", "用户类型", Dict.class);
            user = commonService.commonFind(User.class, id);
        }
        mv.addObject("user", user);
        mv.addObject("userTypes", getJson(userTypes));
        return mv;
    }

    @RequestMapping(value = "/save")
    public void save(@ModelAttribute User user , HttpServletResponse rsp) throws Exception
    {
        user.setRole(commonService.commonFind(Role.class, user.getRole().getId()));
        if (user.getId() == 0)
        {
            user.setRegDate(DateUtils.getCurrDate(null));
            user.setPassword("888888");// StringUtil.getRandomString(6)
            // sendMail(user);
            EncryptTool tool = new EncryptTool(user.getLoginName());
            user.setPassword(tool.encrypt(user.getPassword()));
            commonService.commonAdd(user);
        }
        else
        {
            commonService.commonUpdate(user);
        }
        outSuccess(rsp);
    }

    @Resource
    @Required
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }

    public static void main(String[] args)
    {
        User user = new User();
        user.setPassword(StringUtil.getRandomString(6));
        try
        {
            String html = HtmlUtil.parseHtmlFile("net/swa/util/templete.html", user);
            System.out.println(html);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
