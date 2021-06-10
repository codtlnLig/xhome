
package net.swa.index.service;

import java.util.Map;

import net.swa.index.beans.entity.Dynamic;
import net.swa.index.beans.vo.VoListPage;

public interface DynamicService
{

    /**
     * 新增动态信息
     * @param uid
     * @param model
     * @return
     */
    Map<String, Object> add(Long uid , Dynamic model);

    /**
     * 删除动态信息
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> deleteDynamic(Long uid , Long id);

    /**
     * 根据id查看动态详情
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> queryById(Long uid , Long id);

    /**
     * uid 用户对动态pid 评论comments
     * @param uid
     * @param pid
     * @param comments
     * @return
     */
    Map<String, Object> addComment(Long uid , Long pid , String comments);

    /**
     * 添加回复
     * @param uid
     * @param pid
     * @param targetUserId
     * @param reply
     * @return
     */
    Map<String, Object> addReply(Long uid , Long pid , Long targetUserId , String reply);

    /**
     * 逐次查询动态数据
     * @param uid
     * @param currentId
     * @param num
     * @return
     */
    VoListPage<Dynamic> queryPageList(Long uid , Long currentId , Integer num);

    /**
     * 删除动态评论（会将评论的回复内容一起删除）
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> deleteComment(Long uid , Long id);

    /**
     * 删除动态评论的回复
     * @param uid
     * @param id
     * @return
     */
    Map<String, Object> deleteReply(Long uid , Long id);

    /**
     * 逐次查询我的动态数据
     * @param uid
     * @param currentId
     * @param num
     * @return
     */
    VoListPage<Dynamic> queryMyPageList(Long uid , Long currentId , Integer num);

}
