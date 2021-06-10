package net.swa.system.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.swa.system.beans.entity.Menu;
import net.swa.system.beans.entity.Role;
import net.swa.system.dao.HibernateDaoSupport;
import net.swa.system.service.IMenuService;

import org.hibernate.Query;
import org.springframework.stereotype.Service;


@Service("menuService")
public class MenuServiceImpl extends HibernateDaoSupport implements IMenuService
{

	@SuppressWarnings("unchecked")
	public List<Menu> getRootMenu(final long roleId) throws Exception
	{
		Query query = getCurrentSession().createQuery("from Menu");
		List<Menu> menus = query.list();
		List<Menu> roots = new ArrayList<Menu>();
		List<Menu> roleMenus = new ArrayList<Menu>();
		if (roleId != 0)
		{
			Role role = (Role) getCurrentSession().get(Role.class, roleId);
			roleMenus = role.getMenus();

		}
		for (Menu aMenu : menus)
		{
			for (Menu m : roleMenus)
			{
				if (aMenu.getId() == m.getId())
				{
					aMenu.setChecked(true);
				}
			}
			if (aMenu.getParent() == null)
			{
				try
				{
					aMenu.setSubMenus(getSubMenu(menus, aMenu));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				roots.add(aMenu);
			}
		}
		Collections.sort(roots);
		return roots;
	}

	/**
	 * 查看参数parent是否包含子菜单
	 * 
	 * @param menus
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	private boolean containSubMenu(List<Menu> menus, Menu parent) throws Exception
	{
		for (Menu aMenu : menus)
		{
			if (parent.equals(aMenu.getParent()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到parent的所有子菜单
	 * 
	 * @param menus
	 * @param parent
	 * @return
	 * @throws Exception
	 */
	private List<Menu> getSubMenu(List<Menu> menus, Menu parent) throws Exception
	{
		List<Menu> subMenus = new ArrayList<Menu>();
		for (Menu aMenu : menus)
		{
			if (parent.equals(aMenu.getParent()))
			{
				if (containSubMenu(menus, aMenu))
				{
					List<Menu> subs = getSubMenu(menus, aMenu);
					aMenu.setSubMenus(subs);
				}
				subMenus.add(aMenu);
			}
		}
		Collections.sort(subMenus);
		return subMenus;
	}

	public List<Menu> getRootsByRoleId(final long roleId) throws Exception
	{
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAaaaa  getCurrentSession" + getCurrentSession());
		List<Menu> roots = new ArrayList<Menu>();
		List<Menu> roleMenus = new ArrayList<Menu>();
		if (roleId != 0)
		{
			Role role = (Role) getCurrentSession().get(Role.class, roleId);
			roleMenus = role.getMenus();

		}
		for (Menu aMenu : roleMenus)
		{
			if (aMenu.getParent() == null)
			{
				try
				{
					aMenu.setSubMenus(getSubMenu(roleMenus, aMenu));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				roots.add(aMenu);
			}
		}
		Collections.sort(roots);
		return roots;
	}

}
