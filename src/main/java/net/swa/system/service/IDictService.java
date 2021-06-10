
package net.swa.system.service;

import java.util.List;

import net.swa.system.beans.entity.Dict;
import net.swa.util.JsonResult;

public interface IDictService
{
    /**
     * 得到所有根菜单和根菜单下的子菜单
     * @return 根菜单列表
     * @throws Exception 
     */
    public List<Dict> getDictType() throws Exception;

    public void updateDicNum(Long[] dicIds , Long[] dicNums);

    /**查询型号类别**/
    public List<Dict> getDictType2();

    public JsonResult<String> openSessiondelete(Long[] ids);
}
