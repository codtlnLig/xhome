
package net.swa.system.web.action;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import net.swa.system.beans.entity.Menu;
import net.swa.system.beans.entity.Role;
import net.swa.system.service.ICommonService;

@Controller
@RequestMapping(value = "/role")
public class RoleAction extends AbstractBaseAction
{

    private static final long serialVersionUID = 6771957130989908200L;

    private ICommonService commonService;

    @RequestMapping(value = "/listPage")
    public String listPage() throws Exception
    {
        return "system/role/list";
    }

    @RequestMapping(value = "/rootTree")
    public void rootTree(HttpServletResponse rsp) throws Exception
    {
        String[] attrNames = new String[1];
        Object[] attrValues = new Object[1];
        StringBuilder buffer = new StringBuilder();
        buffer.append("<item text='root' id='0' open='1'  im0='user.png' im1='user.png' im2='user.png'  >");
        List<Role> roles = commonService.search(Role.class, attrNames, attrValues);
        for (Role role : roles)
        {
            buffer.append(role.toXml());
        }
        buffer.append("</item>");

        outString(buffer.toString(), rsp);
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit(@ModelAttribute Role role,Long id) throws Exception
    {
        ModelAndView mv = new ModelAndView("system/role/edit");
        if (id == null||0l==id)
        {
            role = new Role();
        }
        else
        {
            role = commonService.commonFind(Role.class,id);
        }
        mv.addObject("role", role);
        if (null != role.getMenus())
        {
            mv.addObject("menus", getJson(role.getMenus()));
        }
        else
        {
            mv.addObject("menus", "{}");
        }
        return mv;
    }

    @RequestMapping(value = "/save")
    public void save(@ModelAttribute Role role , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        String[] menuIds = req.getParameterValues("menuId");
        List<Menu> menus = new ArrayList<Menu>();
        if (menuIds != null)
        {
            for (String s : menuIds)
            {
                Menu menu = commonService.commonFind(Menu.class, Long.parseLong(s));
                menus.add(menu);
            }
        }
        role.setMenus(menus);
        if (null==role.getId())
        {
            commonService.commonAdd(role);
        }
        else
        {
            commonService.commonUpdate(role);
        }
        outSuccess(rsp);
    }

    @Required
    @Resource
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }
}
