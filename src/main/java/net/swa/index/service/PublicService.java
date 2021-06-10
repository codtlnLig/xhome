
package net.swa.index.service;

import java.util.List;
import java.util.Map;

import net.swa.index.beans.entity.PublicGoods;

public interface PublicService
{

    /**
     * * 添加公共空间分类
     * 当前设计用图片链接
     * @param imgUrl 图片的连接
     * @param name   分类名称
     * @param uid    操作用户id
     * @param pid    父分类id
     * @param id     分类id,若存在则更新
     * @return
     */
    Map<String, Object> addOrUpdateType(String imgUrl , String name , Long uid , Long pid , Long id);

    /**
     * 删除目录
     * @param uid
     * @param id 分类目录id
     * @return
     */
    Map<String, Object> deleteType(Long uid , Long id);

    /**
     * 删除目录和商品
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> deleteTypeGoods(Long uid , Long id);

    /**
     * 查询子目录信息
     * @param uid
     * @param pid
     * @return
     */
    Map<String, Object> queryTypeByPid(Long uid , Long pid);

    /**
     * 查询目录下物品
     * @param uid
     * @param typeId
     * @return
     */
    Map<String, Object> queryGoodsByPid(Long uid , Long pid);

    /**
     * 查询物品详细信息
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> queryGoodDetail(Long uid , Long id);

    /**
     * 保存商品数据
     * @param uid
     * @param model
     * @return
     */
    Map<String, Object> addOrUpdate(Long uid , PublicGoods model);

    /**
     * 将物品计入账单
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> addBill(Long uid , Long id);

    /**
     * 根据目录查询所有子节点（包括子目录和商品）
     * @param uid
     * @param pid
     * @return
     */
    Map<String, Object> queryAllByPid(Long uid , Long pid);

    /**
     * 查询消费账单统计信息，开始月结束月不传时候默认查询至今12个月数据
     * @param uid
     * @param sMonth
     * @param eMonth
     * @return
     */
    Map<String, Object> queryBillReport(Long uid , String sMonth , String eMonth);

    /**
     * 批量删除商品
     * @param uid
     * @param ids
     * @return
     */
    Map<String, Object> batchDelete(Long uid , List<Long> ids);

    /**
     * 删除添加到账单的数据
     * @param uid
     * @param ids
     * @return
     */
    Map<String, Object> batchDeleteBill(Long uid , List<Long> ids);

    /**
     * 根据id删除商品
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> deleteGood(Long uid , Long id);

    /**
     * 根据id删除账单数据
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> deleteBill(Long uid , Long id);

    /**
     * 检测用户是否存在
     * @param uid
     * @return
     */
    boolean queryCheckUid(Long uid);

}
