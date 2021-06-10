
package net.swa.system.web.action;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;
import net.swa.system.beans.entity.Menu;
import net.swa.system.service.ICommonService;
import net.swa.system.service.IMenuService;

/**
 * 功能菜单action
 * 
 * 
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuAction extends AbstractBaseAction
{
    private static final long serialVersionUID = -3395155013072080770L;

    private ICommonService commonService;

    private IMenuService menuService;

    /**
     * 返回菜单列表页面
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listPage")
    public String listPage() throws Exception
    {
        return "system/menu/list";
    }

    @RequestMapping(value = "/rootTree")
    public void rootTree(HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        String str = req.getParameter("value");
        long roleId = 0;
        if (!StringUtils.isEmpty(str))
        {
            roleId = Long.parseLong(str);
        }
        List<Menu> rootList = menuService.getRootMenu(roleId);
        Menu root = new Menu();
        root.setId(0l);
        root.setTitle("功能菜单");
        root.setSubMenus(rootList);
        
        outString(root.toXml(), rsp);
    }

    /**
     * 新建、编辑菜单操作
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit(@ModelAttribute Menu menu , Long id) throws Exception
    {
        ModelAndView mv = new ModelAndView("system/menu/edit");
        if (id != null && id != 0)
        {// 编辑操作
            menu = commonService.commonFind(Menu.class, id);
        }
        else
        {// 新建操作
            menu = new Menu();
        }
        mv.addObject("menu", menu);
        return mv;
    }

    /**
     * 菜单保存操作
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public void save(@ModelAttribute Menu menu , HttpServletResponse rsp) throws Exception
    {
        if (menu.getParent() != null && (null==menu.getParent().getId()||menu.getParent().getId()==0))
        {
            menu.setParent(null);
        }
        if (null==menu.getId()||0==menu.getId())
        {

            commonService.commonAdd(menu);
        }
        else
        {
            commonService.commonUpdate(menu);
        }
        outSuccess(rsp);
    }

    @RequestMapping(value = "/delete")
    public void delete(@ModelAttribute Menu menu , Long id , HttpServletResponse rsp) throws Exception
    {
        List<Menu> subMenus = commonService.search("parent.id", id, Menu.class);
        if (subMenus.size() > 0)
        {
            outError("该菜单下含有子菜单，请先删除子菜单！", rsp);
        }
        else
        {
            commonService.commonDelete("Menu", menu.getId());
            outSuccess(rsp);
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
