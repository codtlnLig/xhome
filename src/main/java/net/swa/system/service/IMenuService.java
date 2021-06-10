package net.swa.system.service;

import java.util.List;
import net.swa.system.beans.entity.Menu;

/**
 * 菜单相关业务操作
 *
 */
public interface IMenuService {
	/**
	 * 得到所有根菜单和根菜单下的子菜单
	 * @return 根菜单列表
	 * @throws Exception 
	 */
	public List<Menu> getRootMenu(long roleId)throws Exception;
	
	public List<Menu> getRootsByRoleId(long roleId)throws Exception;
}
