
package net.swa.system.web.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import net.swa.system.beans.entity.User;
import net.swa.system.service.ICommonService;
import net.swa.util.Const;

@Controller
@RequestMapping(value = "/log")
public class LogAction extends AbstractBaseAction
{
    private ICommonService commonService;

    private static final long serialVersionUID = -8971852540641555704L;

    @RequestMapping(value = "/listPage")
    public ModelAndView listPage(HttpSession session) throws Exception
    {
        User u = (User) session.getAttribute(Const.CURRENT_USER);
        ModelAndView mv = new ModelAndView("system/log/list");
        String loginName = "";
        boolean admin = false;
        if (Const.USER_TYPE_ADMIN == u.getUserType())
        {
            admin = true;
        }
        else
        {
            loginName = u.getLoginName();
            admin = false;
        }
        mv.addObject("loginName", loginName);
        mv.addObject("admin", admin);
        return mv;
    }

    /**
    * 修改日志记录状态方法
    * @return
    * @throws Exception 
    */
    @RequestMapping(value = "/ShiXiao")
    public void ShiXiao(Long[] ids , HttpServletResponse rsp) throws Exception
    {
        commonService.commonUpdateStatus("OperationLog", ids, Const.SHI_XIAO);
        outSuccess(rsp);
    }

    @Resource
    @Required
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }
}
