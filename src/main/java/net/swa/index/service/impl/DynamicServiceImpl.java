
package net.swa.index.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.swa.index.beans.entity.Dynamic;
import net.swa.index.beans.entity.DynamicComReply;
import net.swa.index.beans.entity.DynamicComments;
import net.swa.index.beans.entity.DynamicImgs;
import net.swa.index.beans.entity.Members;
import net.swa.index.beans.vo.VoListPage;
import net.swa.index.service.DynamicService;
import net.swa.index.util.ConstIndex;
import net.swa.system.dao.HibernateDaoSupport;
import net.swa.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

@Service("dynamicService")
public class DynamicServiceImpl extends HibernateDaoSupport implements DynamicService
{
    private static final Logger log = Logger.getLogger(DynamicServiceImpl.class);

    @Override
    public Map<String, Object> add(Long uid , Dynamic model)
    {
        log.debug("add： user id is " + uid + " ,model is " + model);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null != model)
                {
                    List<DynamicImgs> imgs = model.getImgs();
                    model.setCreateTime(DateUtils.getCurrDate(null));
                    model.setCreateUserId(uid);
                    model.setUpdateTime(DateUtils.getCurrDate(null));
                    model.setStatus(ConstIndex.STATUS_SHOW);
                    session.save(model);
                    if (null != imgs && imgs.size() > 0)
                    {
                        for (DynamicImgs img : imgs)
                        {
                            img.setPid(model.getId());
                            session.save(img);
                        }
                    }
                    map.put("model", model);
                    map.put("success", true);

                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "新增动态成功");
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "动态数据解析失败");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> deleteDynamic(Long uid , Long id)
    {
        log.debug("delteDynamic： user id is " + uid + " ,id is " + id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                Query q = null;
                String delImg = " delete from DynamicImgs where pid=:id ";
                //评论要查询出来,先删除评论再删自己
                String queryComments = " from DynamicComments where pid=:id ";
                String delComments = " delete from DynamicComments where pid=:id ";
                String delRply = "delete  from DynamicComReply where pid=:cid ";
                Dynamic d = (Dynamic) session.get(Dynamic.class, id);
                if (null != d)
                {
                    //删除图片
                    q = session.createQuery(delImg);
                    q.setLong("id", id);
                    q.executeUpdate();
                    //查询出评论列表,将评论的回复删除
                    q = session.createQuery(queryComments);
                    q.setLong("id", id);
                    List<DynamicComments> l = q.list();
                    for (int i = 0; i < l.size(); i++)
                    {
                        DynamicComments c = l.get(i);
                        q = session.createQuery(delRply);
                        q.setLong("cid", c.getId());
                        q.executeUpdate();
                    }
                    //删除评论
                    q = session.createQuery(delComments);
                    q.setLong("id", id);
                    q.executeUpdate();

                    //删除动态
                    session.delete(d);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "删除成功");
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "动态已经被删除");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryById(Long uid , Long id)
    {
        log.debug("queryById： user id is " + uid + " ,id is " + id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                Query q = null;
                String queryImg = " from DynamicImgs where pid=:id ";
                //评论要查询出来,先删除评论再删自己
                String queryComments = " from DynamicComments where pid=:id ";
                String queryRply = " from DynamicComReply where pid=:cid ";

                StringBuffer sqlb = new StringBuffer();

                sqlb.append("SELECT d.id,d.text,d.`status`,d.createTime,d.updateTime,d.createUserId,u.nickName,u.imgUrl  from home_dynamic d  JOIN home_members u on u.id=d.createUserId where d.id=:id ");
                q = session.createSQLQuery(sqlb.toString()).addScalar("id", StandardBasicTypes.LONG).addScalar("text", StandardBasicTypes.STRING).addScalar("status", StandardBasicTypes.INTEGER).addScalar("createTime", StandardBasicTypes.STRING).addScalar("updateTime", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).addScalar("nickName", StandardBasicTypes.STRING).addScalar("imgUrl", StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(Dynamic.class));
                q.setLong("id", id);
                Dynamic d = (Dynamic) q.uniqueResult();
                if (null != d)
                {
                    d.setImgUrl(m.getImgUrl());

                    //查询图片
                    q = session.createQuery(queryImg);
                    q.setLong("id", id);
                    List<DynamicImgs> imgs = q.list();
                    d.setImgs(imgs);

                    //查询出评论列表,将评论带上回复
                    q = session.createQuery(queryComments);
                    q.setLong("id", id);
                    List<DynamicComments> l = q.list();
                    for (int i = 0; i < l.size(); i++)
                    {
                        DynamicComments c = l.get(i);
                        if (null != c.getCreateUserId())
                        {
                            //评论的用户
                            Members cUser = (Members) session.get(Members.class, c.getCreateUserId());
                            if (null != cUser)
                            {
                                c.setCreateNick(cUser.getNickName());
                            }
                        }
                        q = session.createQuery(queryRply);
                        q.setLong("cid", c.getId());
                        List<DynamicComReply> replys = q.list();
                        for (int j = 0; j < replys.size(); j++)
                        {
                            DynamicComReply r = replys.get(j);
                            if (null != r.getTargetUserId())
                            {
                                //对谁回复
                                Members rtUser = (Members) session.get(Members.class, r.getTargetUserId());
                                if (null != rtUser)
                                {
                                    r.setTargetNick(rtUser.getNickName());
                                }
                            }
                            if (null != r.getReplyUserId())
                            {
                                //回复的人
                                Members rcUser = (Members) session.get(Members.class, r.getReplyUserId());
                                if (null != rcUser)
                                {
                                    r.setReplyNick(rcUser.getNickName());
                                }
                            }
                        }
                        //将评论加上回复
                        c.setReplys(replys);
                    }
                    //将动态加上评论
                    d.setComments(l);
                    map.put("success", true);
                    map.put("model", d);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "查询成功");
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "动态已经被删除");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @Override
    public Map<String, Object> addComment(Long uid , Long pid , String comments)
    {
        log.debug("add： user id is " + uid + " ,pid is " + pid + ",comments is " + comments);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if ((!StringUtils.isBlank(comments)) && (null != pid))
                {
                    Dynamic dynamic = (Dynamic) session.get(Dynamic.class, pid);
                    if (null == dynamic)
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "动态不存在");
                    }
                    else
                    {
                        Members m2 = (Members) session.get(Members.class, dynamic.getCreateUserId());
                        DynamicComments model = new DynamicComments();
                        model.setPid(pid);
                        model.setComments(comments);
                        model.setCreateNick(m.getNickName());
                        model.setCreateTime(DateUtils.getCurrDate(null));
                        model.setUpdateTime(DateUtils.getCurrDate(null));
                        model.setCreateUserId(uid);
                        model.setStatus(ConstIndex.STATUS_SHOW);
                        session.save(model);
                        //保存后返回加nick
                        model.setCreateNick(m2.getNickName());
                        map.put("success", true);
                        map.put("model", model);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "新增评论成功");
                    }
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_LOST);
                    map.put("message", "必填参数为空");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @Override
    public Map<String, Object> addReply(Long uid , Long pid , Long targetUserId , String reply)
    {
        log.debug("add： user id is " + uid + " ,pid is " + pid + ",targetUserId is " + targetUserId + "  ,reply is " + reply);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if ((!StringUtils.isBlank(reply)) && (null != pid))
                {
                    DynamicComments comment = (DynamicComments) session.get(DynamicComments.class, pid);
                    if (null != comment)
                    {
                        DynamicComReply replyModel = new DynamicComReply();
                        replyModel.setCreateTime(DateUtils.getCurrDate(null));
                        replyModel.setPid(pid);
                        replyModel.setReply(reply);
                        replyModel.setReplyNick(m.getNickName());
                        replyModel.setReplyUserId(uid);
                        replyModel.setStatus(ConstIndex.STATUS_SHOW);
                        replyModel.setUpdateTime(DateUtils.getCurrDate(null));

                        if (null != targetUserId)
                        {
                            Members targetUser = (Members) session.get(Members.class, targetUserId);
                            if (null != targetUser)
                            {
                                replyModel.setTargetUserId(targetUserId);
                                replyModel.setTargetNick(targetUser.getNickName());
                            }
                            else
                            {
                                log.debug("尼玛这是要回复谁");
                            }
                        }
                        //如果还没有查到要回复谁
                        if (null == replyModel.getTargetUserId())
                        {
                            Members targetUser = (Members) session.get(Members.class, comment.getCreateUserId());//默认
                            if (null != targetUser)
                            {
                                replyModel.setTargetUserId(comment.getCreateUserId());
                                replyModel.setTargetNick(targetUser.getNickName());
                            }

                            else
                            {
                                log.debug("尼玛这是要回复谁");
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_SOURCE);
                                map.put("message", "ERROR");
                                return map;
                            }
                        }
                        session.save(replyModel);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("model", replyModel);
                        map.put("message", "回复成功");
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "评论被删除");
                    }

                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_LOST);
                    map.put("message", "必填参数为空");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public VoListPage<Dynamic> queryPageList(Long uid , Long currentId , Integer num)
    {
        log.debug("queryById： user id is " + uid + " ,currentId is " + currentId + " ,num is " + num);
        VoListPage<Dynamic> result = new VoListPage<Dynamic>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                Query q = null;
                if (null == num)
                {
                    num = 10;
                }
                StringBuffer sqlb = new StringBuffer();
                StringBuffer cb = new StringBuffer();

                sqlb.append("SELECT d.id,d.text,d.`status`,d.createTime,d.updateTime,d.createUserId,u.nickName,u.imgUrl  from home_dynamic d  JOIN home_members u on u.id=d.createUserId where d.`status`=1  ");
                cb.append("SELECT count(*) counNum from home_dynamic d  JOIN home_members u on u.id=d.createUserId where d.`status`=1 ");
                if (null != currentId)
                {
                    sqlb.append(" and id<:currentId ");
                }
                sqlb.append(" order by id desc ");
                q = session.createSQLQuery(sqlb.toString()).addScalar("id", StandardBasicTypes.LONG).addScalar("text", StandardBasicTypes.STRING).addScalar("status", StandardBasicTypes.INTEGER).addScalar("createTime", StandardBasicTypes.STRING).addScalar("updateTime", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).addScalar("nickName", StandardBasicTypes.STRING).addScalar("imgUrl", StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(Dynamic.class));
                q.setMaxResults(num);
                if (null != currentId)
                {
                    q.setLong("currentId", currentId);
                }
                List<Dynamic> dl = q.list();
                if (null != dl && dl.size() > 0)
                {
                    String queryImg = " from DynamicImgs where pid=:id ";
                    String queryComments = " from DynamicComments where pid=:id ";
                    String queryRply = " from DynamicComReply where pid=:cid ";
                    for (Dynamic d : dl)
                    {
                        Long id = d.getId();
                        //查询图片
                        q = session.createQuery(queryImg);
                        q.setLong("id", id);
                        List<DynamicImgs> imgs = q.list();
                        d.setImgs(imgs);

                        //查询出评论列表,将评论带上回复
                        q = session.createQuery(queryComments);
                        q.setLong("id", id);
                        List<DynamicComments> l = q.list();
                        for (int i = 0; i < l.size(); i++)
                        {
                            DynamicComments c = l.get(i);
                            if (null != c.getCreateUserId())
                            {
                                //评论的用户
                                Members cUser = (Members) session.get(Members.class, c.getCreateUserId());
                                if (null != cUser)
                                {
                                    c.setCreateNick(cUser.getNickName());
                                }
                            }
                            q = session.createQuery(queryRply);
                            q.setLong("cid", c.getId());
                            List<DynamicComReply> replys = q.list();
                            for (int j = 0; j < replys.size(); j++)
                            {
                                DynamicComReply r = replys.get(j);
                                if (null != r.getTargetUserId())
                                {
                                    //对谁回复
                                    Members rtUser = (Members) session.get(Members.class, r.getTargetUserId());
                                    if (null != rtUser)
                                    {
                                        r.setTargetNick(rtUser.getNickName());
                                    }
                                }
                                if (null != r.getReplyUserId())
                                {
                                    //回复的人
                                    Members rcUser = (Members) session.get(Members.class, r.getReplyUserId());
                                    if (null != rcUser)
                                    {
                                        r.setReplyNick(rcUser.getNickName());
                                    }
                                }
                            }
                            //将评论加上回复
                            c.setReplys(replys);
                        }
                        //将动态加上评论
                        d.setComments(l);
                    }
                    if (num == dl.size())
                    {
                        result.setHasNext(true);
                    }
                    result.setCurrentId(dl.get(dl.size() - 1).getId());
                    q = session.createSQLQuery(cb.toString()).addScalar("counNum", StandardBasicTypes.INTEGER);
                    int totalCount = (Integer) q.uniqueResult();
                    result.setTotalSize(totalCount);
                }
                result.setResult(dl);
                result.setSuccess(true);
                result.setErrorCode(ConstIndex.ERR_NO);
            }
            else
            {
                result.setSuccess(false);
                result.setErrorCode(ConstIndex.ERR_TOKEN);
                result.setMessage("用户校验失败");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorCode(ConstIndex.ERR_TOKEN);
            result.setMessage("token校验失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteComment(Long uid , Long id)
    {
        log.debug("deleteComment： user id is " + uid + " ,id is " + id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                Query q = null;
                if (null == id)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_LOST);
                    map.put("message", "评论id不能为空");
                    return map;
                }
                //评论查询
                DynamicComments p = (DynamicComments) session.get(DynamicComments.class, id);
                if (null == p)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "评论不存在");
                }
                else
                {
                    String delRply = "delete  from DynamicComReply where pid=:cid ";
                    q = session.createQuery(delRply);
                    q.setLong("cid", p.getId());
                    q.executeUpdate();
                    session.delete(p);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "删除成功");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @Override
    public Map<String, Object> deleteReply(Long uid , Long id)
    {
        log.debug("deleteReply： user id is " + uid + " ,id is " + id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null == id)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_LOST);
                    map.put("message", "回复id不能为空");
                    return map;
                }
                //查询
                DynamicComReply r = (DynamicComReply) session.get(DynamicComReply.class, id);
                if (null == r)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "回复不存在");
                }
                else
                {
                    session.delete(r);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "删除成功");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户校验失败");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }
        return map;
    }

    @Override
    public VoListPage<Dynamic> queryMyPageList(Long uid , Long currentId , Integer num)
    {
        log.debug("queryById： user id is " + uid + " ,currentId is " + currentId + " ,num is " + num);
        VoListPage<Dynamic> result = new VoListPage<Dynamic>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                Query q = null;
                if (null == num)
                {
                    num = 10;
                }
                StringBuffer sqlb = new StringBuffer();
                StringBuffer cb = new StringBuffer();

                sqlb.append("SELECT d.id,d.text,d.`status`,d.createTime,d.updateTime,d.createUserId,u.nickName,u.imgUrl  from home_dynamic d  JOIN home_members u on u.id=d.createUserId where d.`status`=1 and createUserId=:createUserId  ");
                cb.append("SELECT count(*) counNum from home_dynamic d  JOIN home_members u on u.id=d.createUserId where d.`status`=1 and createUserId=:createUserId ");
                if (null != currentId)
                {
                    sqlb.append(" and id<:currentId ");
                }

                sqlb.append(" order by id desc ");
                q = session.createSQLQuery(sqlb.toString()).addScalar("id", StandardBasicTypes.LONG).addScalar("text", StandardBasicTypes.STRING).addScalar("status", StandardBasicTypes.INTEGER).addScalar("createTime", StandardBasicTypes.STRING).addScalar("updateTime", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).addScalar("nickName", StandardBasicTypes.STRING).addScalar("imgUrl", StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(Dynamic.class));
                q.setMaxResults(num);
                q.setLong("createUserId", uid);
                if (null != currentId)
                {
                    q.setLong("currentId", currentId);
                }
                List<Dynamic> dl = q.list();
                if (null != dl && dl.size() > 0)
                {
                    String queryImg = " from DynamicImgs where pid=:id ";
                    String queryComments = " from DynamicComments where pid=:id ";
                    String queryRply = " from DynamicComReply where pid=:cid ";
                    for (Dynamic d : dl)
                    {
                        Long id = d.getId();
                        //查询图片
                        q = session.createQuery(queryImg);
                        q.setLong("id", id);
                        List<DynamicImgs> imgs = q.list();
                        d.setImgs(imgs);

                        //查询出评论列表,将评论带上回复
                        q = session.createQuery(queryComments);
                        q.setLong("id", id);
                        List<DynamicComments> l = q.list();
                        for (int i = 0; i < l.size(); i++)
                        {
                            DynamicComments c = l.get(i);
                            if (null != c.getCreateUserId())
                            {
                                //评论的用户
                                Members cUser = (Members) session.get(Members.class, c.getCreateUserId());
                                if (null != cUser)
                                {
                                    c.setCreateNick(cUser.getNickName());
                                }
                            }
                            q = session.createQuery(queryRply);
                            q.setLong("cid", c.getId());
                            List<DynamicComReply> replys = q.list();
                            for (int j = 0; j < replys.size(); j++)
                            {
                                DynamicComReply r = replys.get(j);
                                if (null != r.getTargetUserId())
                                {
                                    //对谁回复
                                    Members rtUser = (Members) session.get(Members.class, r.getTargetUserId());
                                    if (null != rtUser)
                                    {
                                        r.setTargetNick(rtUser.getNickName());
                                    }
                                }
                                if (null != r.getReplyUserId())
                                {
                                    //回复的人
                                    Members rcUser = (Members) session.get(Members.class, r.getReplyUserId());
                                    if (null != rcUser)
                                    {
                                        r.setReplyNick(rcUser.getNickName());
                                    }
                                }
                            }
                            //将评论加上回复
                            c.setReplys(replys);
                        }
                        //将动态加上评论
                        d.setComments(l);
                    }
                    if (num == dl.size())
                    {
                        result.setHasNext(true);
                    }
                    result.setCurrentId(dl.get(dl.size() - 1).getId());
                    q = session.createSQLQuery(cb.toString()).addScalar("counNum", StandardBasicTypes.INTEGER);
                    q.setLong("createUserId", uid);
                    int totalCount = (Integer) q.uniqueResult();
                    result.setTotalSize(totalCount);
                }
                result.setResult(dl);
                result.setSuccess(true);
                result.setErrorCode(ConstIndex.ERR_NO);
            }
            else
            {
                result.setSuccess(false);
                result.setErrorCode(ConstIndex.ERR_TOKEN);
                result.setMessage("用户校验失败");
            }
        }
        else
        {
            result.setSuccess(false);
            result.setErrorCode(ConstIndex.ERR_TOKEN);
            result.setMessage("token校验失败");
        }
        return result;
    }

}