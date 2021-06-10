
package net.swa.index.service;

import java.util.List;
import java.util.Map;
import net.swa.index.beans.entity.PrivateGoods;

public interface PrivateService
{

    /**
     * 新建目录分类
     * @param uid
     * @param imgUrl
     * @param name
     * @param pid
     * @param id
     * @param password
     * @param question
     * @param answer
     * @return
     */
    Map<String, Object> addOrUpdateType(Long uid , String imgUrl , String name , Long pid , Long id , String password , String question , String answer);

    /**
     * 输入密码删除目录，若有子目录or商品则不删除返回提醒
     * @param uid
     * @param id
     * @param password
     * @return
     */
    Map<String, Object> deleteType(Long uid , Long id , String password);

    /**
     * 确认后再删除，若类型有子类型无法删除会提示（仅叶子分类可以删除），否则先删除商品再删除分类
     * @param uid
     * @param id
     * @param password
     * @return
     */
    Map<String, Object> deleteTypeGoods(Long uid , Long id , String password);

    /**
     * 根据父分类查询子分类信息
     * @param pid 父分类id,若不传表示查询一级目录
     * @param password  若打开私人文件夹，需要传密码（其他情况不需要传）
     * @return
     */
    Map<String, Object> queryTypeByPid(Long uid , Long pid , String password);

    /**
     * 查询目录下商品
     * @param uid
     * @param typeId
     * @param password
     * @return
     */
    Map<String, Object> queryGoodsByPid(Long uid , Long typeId , String password);

    /**
     * 根据父分类查询子分类和商品信息
     * @param uid
     * @param pid
     * @param password
     * @return
     */
    Map<String, Object> queryAllByPid(Long uid , Long pid , String password);

    /**
     * 查询物品详细信息
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> queryGoodDetail(Long uid , Long id);

    /**
     * 保存商品信息
     * @param uid
     * @param model
     * @return
     */
    Map<String, Object> addOrUpdate(Long uid , PrivateGoods model);

    /**
     * 将商品加入账单
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> addBill(Long uid , Long id);

    /**
     * 个人空间查询消费账单统计信息，开始月结束月不传时候默认查询至今12个月数据
     * @param uid
     * @param password 
     * @param zoneId
     * @param sMonth
     * @param eMonth
     * @return
     */
    Map<String, Object> queryBillReport(Long uid , String password , Long zoneId , String sMonth , String eMonth);

    /**
     * 输入密码删除
     * @param uid
     * @param ids
     * @return
     */
    Map<String, Object> batchDelete(Long uid , List<Long> ids , String password);

    /**
     * 根据id删除账单数据
     * @param uid
     * @param id
     * @param password
     * @return
     */
    Map<String, Object> deleteBill(Long uid , Long id , String password);

    /**
     * 根据id删除商品
     * @param uid
     * @param id
     * @param password
     * @return
     */
    Map<String, Object> deleteGood(Long uid , Long id , String password);

}
