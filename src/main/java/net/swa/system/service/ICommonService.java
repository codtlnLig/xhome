
package net.swa.system.service;

import java.util.List;
import net.swa.util.JsonResult;

public interface ICommonService
{
    /**
     * 
     * @param attrNames 要查询属性名，可以多个
     * @param attrValues 要查询属性值，数量必需和attrNames相同
     * @param operators 操作符号
     * @param type 实体类的全名例如：net.swa.entity.User
     * @param currentPage 第几页
     * @param pageSize 页大小
     * @return  封装好的查询结果
     * @throws Exception 异常抛出，统一处理
     */
    public <T> JsonResult<T> search(final String[] attrNames , final Object[] attrValues , final String[] operators , final Class<T> type , final int currentPage , final int pageSize , final String orderBy , final String orderType) throws Exception;

    /**
     * 通用状态更新方法
     * @return
     * @throws Exception
     */
    public boolean commonUpdateStatus(String type , Long[] ids , int status) throws Exception;

    /**
     * 通用删除方法
     * @param type
     * @param ids
     * @return
     * @throws Exception
     */
    public boolean commonDelete(String type , Long... ids) throws Exception;

    /**
     * 通用增加方法
     * @param obj
     * @throws Exception
     */
    public void commonAdd(Object obj) throws Exception;

    /**
     * 通用更新方法
     * @param obj
     * @throws Exception
     */
    public void commonUpdate(Object obj) throws Exception;

    /**
     * 通用根据ID查找对象方法
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    public <T> T commonFind(Class<T> type , long id) throws Exception;

    /**
     * 
     * @param <T>
     * @param attr
     * @param value
     * @param type
     * @return
     */
    public <T> List<T> search(String attr , Object value , Class<T> type);

    /**
     * 
     * @param <T>
     * @param attrNames
     * @param attrValue
     * @param type
     * @return
     */
    public <T> List<T> search(Class<T> type , String[] attrNames , Object attrValue[]);

    /**
     * 
     * @param attrNames 要查询属性名，可以多个
     * @param attrValues 要查询属性值，数量必需和attrNames相同
     * @param type 实体类的全名例如：net.swa.entity.User
     * @param currentPage 第几页
     * @param pageSize 页大小
     * @return  封装好的查询结果
     * @throws Exception 异常抛出，统一处理
     */
    public <T> JsonResult<T> search(String[] attrNames , Object[] attrValues , Class<T> type , int currentPage , int pageSize , String orderBy , String orderType) throws Exception;

    /**
     * 删除整个集合中的元素
     * @param list
     * @return
     * @throws Exception
     */
    public boolean batchDelete(List<?> list) throws Exception;

    public <T> JsonResult<T> searchBean(final String attrName , final Object attrValue , final Class<T> type) throws Exception;

    /**
     * 注销
     * @param type
     * @param stateName
     * @param ids
     * @param status
     * @return
     * @throws Exception
     */
    public boolean commonDelHisStatus(final String type , final String stateName , final Long[] ids , final String status) throws Exception;

    public <T> T findByAttribute(final Class<T> type , final String attrName , final Object val);

    public <T> List<T> search(final String attr , final Object value , final Class<T> type , final Integer count , final String orderBy , final String orderType);
}
