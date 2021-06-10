
package net.swa.index.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.swa.index.beans.entity.Members;
import net.swa.index.beans.entity.PrivateBill;
import net.swa.index.beans.entity.PrivateGoods;
import net.swa.index.beans.entity.PrivateGoodsImgs;
import net.swa.index.beans.entity.PrivateTypes;
import net.swa.index.beans.vo.VoPrivateBillDate;
import net.swa.index.beans.vo.VoPrivateBillMonth;
import net.swa.index.service.PrivateService;
import net.swa.index.util.ConstIndex;
import net.swa.system.dao.HibernateDaoSupport;
import net.swa.util.DateUtils;
import net.swa.util.EncryptTool;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;

@Service("privateService")
public class PrivateServiceImpl extends HibernateDaoSupport implements PrivateService
{
    private static final Logger log = Logger.getLogger(PrivateServiceImpl.class);

    @Override
    public Map<String, Object> addOrUpdateType(Long uid , String imgUrl , String name , Long pid , Long id , String password , String question , String answer)
    {
        log.debug("addOrUpdateType 参数信息： uid is " + uid + " ,  imgUrl is " + imgUrl + " ,  name is " + name + ",  pid " + pid + ", id is " + id + " ,  password is " + password + ", question is " + question + " ,  answer is " + answer);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PrivateTypes pt = null;
                if (null != id)
                {
                    //更新
                    pt = (PrivateTypes) session.get(PrivateTypes.class, id);
                    if (null != pt)
                    {
                        pt.setCreateUserId(uid);
                        if (!StringUtils.isBlank(imgUrl))
                        {
                            pt.setImgUrl(imgUrl);
                        }
                        pt.setName(name);
                        pt.setUpdateTime(DateUtils.getCurrDate(null));
                        session.update(pt);
                        map.put("model", pt);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "更新分类成功");
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "该分类不存在，更新失败");
                    }
                }
                else
                {
                    log.debug("新增个人目录，or空间");
                    if (!StringUtils.isBlank(name))
                    {
                        pt = new PrivateTypes();
                        if (null == pid || 0 == pid)
                        {
                            log.debug("");
                            if (!StringUtils.isBlank(password))
                            {
                                pt.setPid(ConstIndex.PUBLIC_TYPE_ROOT);
                                //将父节点的空间id赋予子节点
                                pt.setCreateTime(DateUtils.getCurrDate(null));
                                pt.setCreateUserId(uid);
                                pt.setImgUrl(imgUrl);
                                pt.setName(name);
                                pt.setUpdateTime(DateUtils.getCurrDate(null));
                                pt.setPassword(EncryptTool.MD5(password));
                                pt.setQuestion(question);
                                pt.setAnswer(answer);
                                session.save(pt);
                                pt.setZoneId(pt.getId());
                                session.update(pt);
                            }
                            else
                            {
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_LOST);
                                map.put("message", "密码不能空");
                                return map;
                            }
                        }
                        else
                        {
                            //检测其父节点是否存在
                            PrivateTypes parent = (PrivateTypes) session.get(PrivateTypes.class, pid);
                            if (null == parent)
                            {
                                map.put("success", true);
                                map.put("errorCode", ConstIndex.ERR_SOURCE);
                                map.put("message", "该父类型id不存在");
                                return map;
                            }
                            pt.setPid(pid);
                            //将父节点的空间id赋予子节点
                            pt.setZoneId(parent.getZoneId());
                            pt.setCreateTime(DateUtils.getCurrDate(null));
                            pt.setCreateUserId(uid);
                            pt.setImgUrl(imgUrl);
                            pt.setName(name);
                            pt.setUpdateTime(DateUtils.getCurrDate(null));
                            if (!StringUtils.isBlank(password))
                            {
                                pt.setPassword(EncryptTool.MD5(password));
                            }
                            pt.setQuestion(question);
                            pt.setAnswer(answer);
                            session.save(pt);
                        }
                        map.put("model", pt);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "新增分类成功");
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_LOST);
                        map.put("message", "物品名称不能空");
                    }
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
    public Map<String, Object> deleteType(Long uid , Long id , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            if (null == id)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "目录ID不能为空");
                return map;
            }

            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                //                if (!StringUtils.isBlank(password))
                //                {
                PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, id);
                if (null != t && null != t.getId() && null != t.getZoneId())
                {
                    String cPassword = t.getPassword();
                    //默认不需要校验密码
                    boolean noCheckPsw = true;
                    if (t.getZoneId() != t.getId())
                    {
                        PrivateTypes zone = (PrivateTypes) session.get(PrivateTypes.class, t.getZoneId());
                        log.debug("此处查询空间id,must 存在");
                        cPassword = zone.getPassword();

                        //不是个人空间  cPassword是个人空间的密码
                    }
                    else
                    {
                        //需要校验密码
                        noCheckPsw = false;
                    }
                    if (noCheckPsw || (null != password && (EncryptTool.MD5(password)).equals(cPassword)))
                    {
                        //检测是否有子分类
                        String hCheckChildren = " from PrivateTypes where pid=:id ";
                        //检测是否有该类产品
                        String hCheckGoods = " from PrivateGoods where typeId=:id ";
                        q = session.createQuery(hCheckChildren);
                        q.setLong("id", id);
                        List<PrivateTypes> children = q.list();
                        if (null != children && children.size() > 0)
                        {
                            map.put("success", false);
                            map.put("errorCode", ConstIndex.ERR_NEED_PRE_RECURSIVE);
                            map.put("message", "请先删除子分类");
                        }
                        else
                        {
                            //检测该分类下是否有商品
                            q = session.createQuery(hCheckGoods);
                            q.setLong("id", id);
                            List<PrivateGoods> goods = q.list();
                            if (null != goods && goods.size() > 0)
                            {
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_NEED_PRE);
                                map.put("message", "该分类下已有商品");
                            }
                            else
                            {
                                session.delete(t);
                                map.put("success", true);
                                map.put("errorCode", ConstIndex.ERR_NO);
                                map.put("message", "删除分类成功");
                            }
                        }
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                        map.put("message", "密码错误");
                    }
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "该分类不存在");
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
    public Map<String, Object> deleteTypeGoods(Long uid , Long id , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            if (null == id)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "ID不能为空");
                return map;
            }

            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (StringUtils.isBlank(password))
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "密码不为空");
                }
                else
                {
                    PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, id);
                    if (null == t)
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "该分类不存在");
                    }
                    else
                    {
                        if (null != t.getId() && null != t.getZoneId())
                        {
                            String cPassword = t.getPassword();
                            if ((t.getZoneId()).equals(t.getId()))
                            {
                                PrivateTypes zone = (PrivateTypes) session.get(PrivateTypes.class, t.getZoneId());
                                log.debug("此处查询空间id,must 存在");
                                cPassword = zone.getPassword();
                            }
                            if (null != cPassword && (EncryptTool.MD5(password)).equals(cPassword))
                            {
                                //检测是否有子分类
                                String hCheckChildren = " from PrivateTypes where pid=:id ";
                                //删除该类产品图片
                                String himgs = " delete from PrivateGoodsImgs where goodId=:goodId ";
                                String hCheckGoods = " from PrivateGoods where typeId=:id ";
                                q = session.createQuery(hCheckChildren);
                                q.setLong("id", id);
                                List<?> children = q.list();
                                if (null != children && children.size() > 0)
                                {
                                    map.put("success", false);
                                    map.put("errorCode", ConstIndex.ERR_NEED_PRE_RECURSIVE);
                                    map.put("message", "请先删除子分类");
                                }
                                else
                                {
                                    //先删除该分类下商品
                                    q = session.createQuery(hCheckGoods);
                                    q.setLong("id", id);
                                    List<PrivateGoods> l = q.list();
                                    if (null != l && l.size() > 0)
                                    {
                                        for (PrivateGoods g : l)
                                        {
                                            q = session.createQuery(himgs);
                                            q.setLong("goodId", g.getId());
                                            q.executeUpdate();
                                            session.delete(g);
                                        }
                                    }
                                    session.delete(t);
                                    map.put("success", true);
                                    map.put("errorCode", ConstIndex.ERR_NO);
                                    map.put("message", "删除分类成功");
                                }
                            }
                            else
                            {
                                log.debug("ERROR :出现错误查许出的目录没有id or 空间id");
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_CANNOT_FOUND);
                                map.put("message", "ERROR");
                            }
                        }
                        else
                        {
                            map.put("success", false);
                            map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                            map.put("message", "密码错误");
                        }
                    }
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
    public Map<String, Object> queryTypeByPid(Long uid , Long pid , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null == pid || 0 == pid)
                {
                    //进入首页目录
                    //查询子节点
                    String qChildren = " from PrivateTypes where pid=:pid  and  createUserId=:createUserId  ";
                    q = session.createQuery(qChildren);
                    q.setLong("pid", ConstIndex.PUBLIC_TYPE_ROOT);
                    q.setLong("createUserId", uid);
                    List<?> l = q.list();
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("list", l);
                    map.put("message", "查询子分类成功");

                }
                else
                {
                    PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, pid);
                    if (null == t)
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "父目录被删除");
                        log.debug("父节点不存在");
                    }
                    else
                    {
                        if ((t.getId()).equals(t.getZoneId()))
                        {
                            //此时需要校验密码
                            if (!StringUtils.isBlank(password) && (!StringUtils.isBlank(t.getPassword())))
                            {
                                if ((EncryptTool.MD5(password)).equals(t.getPassword()))
                                {
                                    //查询子节点
                                    String qChildren = " from PrivateTypes where pid=:pid ";
                                    q = session.createQuery(qChildren);
                                    q.setLong("pid", pid);
                                    List<PrivateTypes> l = q.list();

                                    if (null != l && l.size() > 0)
                                    {
                                        //检测是否有子分类
                                        String hCheckChildren = " from PrivateTypes where pid=:id ";
                                        //检测是否有商品
                                        String hCheckGoods = " from PrivateGoods where typeId=:id ";
                                        for (PrivateTypes p : l)
                                        {
                                            p.setChildType(ConstIndex.CHILD_TYPE0);
                                            q = session.createQuery(hCheckGoods);
                                            q.setLong("id", p.getId());
                                            List<?> gl = q.list();
                                            if (null != gl && gl.size() > 0)
                                            {
                                                p.setChildType(ConstIndex.CHILD_TYPE1);
                                                log.debug(" 目录下有商品。" + p);

                                                q = session.createQuery(hCheckChildren);
                                                q.setLong("id", p.getId());
                                                List<?> tl = q.list();
                                                if (null != tl && tl.size() > 0)
                                                {
                                                    p.setChildType(ConstIndex.CHILD_TYPE3);
                                                    log.debug("ERROR 目录下有商品和目录。" + p);
                                                }

                                            }
                                            else
                                            {
                                                q = session.createQuery(hCheckChildren);
                                                q.setLong("id", p.getId());
                                                List<?> tl = q.list();
                                                if (null != tl && tl.size() > 0)
                                                {
                                                    p.setChildType(ConstIndex.CHILD_TYPE2);
                                                    log.debug("ERROR 目录下有目录。" + p);
                                                }
                                            }
                                        }
                                        map.put("list", l);
                                    }
                                    else
                                    {
                                        map.put("list", l);
                                    }
                                    map.put("success", true);
                                    map.put("errorCode", ConstIndex.ERR_NO);

                                    map.put("message", "查询子分类成功");
                                }
                                else
                                {
                                    map.put("success", false);
                                    map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                                    map.put("message", "密码错误");
                                }
                            }
                            else
                            {
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_SOURCE);
                                map.put("message", "需要访问密码");
                                log.debug("密码没有传or 该目录没有设置 密码");
                                log.debug("password is " + password);
                                log.debug("目录 :" + t);
                            }
                        }
                        else
                        {
                            //查询子节点
                            String qChildren = " from PrivateTypes where pid=:pid ";
                            q = session.createQuery(qChildren);
                            q.setLong("pid", pid);
                            List<?> l = q.list();
                            map.put("success", true);
                            map.put("errorCode", ConstIndex.ERR_NO);
                            map.put("list", l);
                            map.put("message", "查询子分类成功");
                        }
                    }
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
    public Map<String, Object> queryGoodsByPid(Long uid , Long pid , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null == pid || 0 == pid)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_LOST);
                    map.put("message", "父目录不存在");
                    log.debug("父节点不存在");
                }
                else
                {
                    PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, pid);
                    if (null == t)
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "父目录被删除");
                        log.debug("父节点不存在");
                    }
                    else
                    {
                        if ((t.getId()).equals(t.getZoneId()))
                        {
                            //此时需要校验密码
                            if (!StringUtils.isBlank(password) && (!StringUtils.isBlank(t.getPassword())))
                            {
                                if ((EncryptTool.MD5(password)).equals(t.getPassword()))
                                {
                                    //查询目录下物品
                                    String qChildren = " from PrivateGoods where typeId=:typeId ";
                                    q = session.createQuery(qChildren);
                                    q.setLong("typeId", pid);
                                    List<?> l = q.list();
                                    map.put("success", true);
                                    map.put("errorCode", ConstIndex.ERR_NO);
                                    map.put("list", l);
                                    map.put("message", "查询商品成功");
                                }
                                else
                                {
                                    map.put("success", false);
                                    map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                                    map.put("message", "密码错误");
                                }
                            }
                            else
                            {
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_SOURCE);
                                map.put("message", "需要访问密码");
                                log.debug("密码没有传or 该目录没有设置 密码");
                                log.debug("password is " + password);
                                log.debug("目录 :" + t);
                            }
                        }
                        else
                        {
                            //查询子节点
                            String qChildren = " from PrivateGoods where typeId=:typeId ";
                            q = session.createQuery(qChildren);
                            q.setLong("typeId", pid);
                            List<?> l = q.list();
                            map.put("success", true);
                            map.put("errorCode", ConstIndex.ERR_NO);
                            map.put("list", l);
                            map.put("message", "查询商品成功");
                            log.debug("个人空间子层目录 :" + t);
                        }
                    }
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
    public Map<String, Object> queryAllByPid(Long uid , Long pid , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null == pid || 0 == pid)
                {
                    //进入首页目录
                    //查询子节点
                    String qChildren = " from PrivateTypes where pid=:pid  and  createUserId=:createUserId  ";
                    q = session.createQuery(qChildren);
                    q.setLong("pid", ConstIndex.PUBLIC_TYPE_ROOT);
                    q.setLong("createUserId", uid);
                    List<?> l = q.list();
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("list", l);
                    map.put("message", "查询成功");
                    log.debug("该目录下不应该直接有商品");
                }
                else
                {
                    PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, pid);
                    if (null == t)
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "父目录被删除");
                        log.debug("父节点不存在");
                    }
                    else
                    {
                        if ((t.getId()).equals(t.getZoneId()))
                        {
                            //此时需要校验密码
                            if (!StringUtils.isBlank(password) && (!StringUtils.isBlank(t.getPassword())))
                            {
                                if ((EncryptTool.MD5(password)).equals(t.getPassword()))
                                {
                                    //查询子节点
                                    String qChildren = " from PrivateTypes where pid=:pid ";
                                    q = session.createQuery(qChildren);
                                    q.setLong("pid", pid);
                                    List<?> l = q.list();
                                    map.put("list", l);

                                    //查询目录下物品
                                    String qChildrenGoods = " from PrivateGoods where typeId=:typeId ";
                                    q = session.createQuery(qChildrenGoods);
                                    q.setLong("typeId", pid);
                                    List<?> goods = q.list();
                                    map.put("goods", goods);

                                    map.put("success", true);
                                    map.put("errorCode", ConstIndex.ERR_NO);
                                    map.put("message", "查询成功");
                                }
                                else
                                {
                                    map.put("success", false);
                                    map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                                    map.put("message", "密码错误");
                                }
                            }
                            else
                            {
                                map.put("success", false);
                                map.put("errorCode", ConstIndex.ERR_SOURCE);
                                map.put("message", "需要访问密码");
                                log.debug("密码没有传or 该目录没有设置 密码");
                                log.debug("password is " + password);
                                log.debug("目录 :" + t);
                            }
                        }
                        else
                        {
                            //查询子节点
                            String qChildren = " from PrivateTypes where pid=:pid ";
                            q = session.createQuery(qChildren);
                            q.setLong("pid", pid);
                            List<?> l = q.list();
                            map.put("list", l);
                            //查询目录下物品
                            String qChildrenGoods = " from PrivateGoods where typeId=:typeId ";
                            q = session.createQuery(qChildrenGoods);
                            q.setLong("typeId", pid);
                            List<?> goods = q.list();
                            map.put("goods", goods);

                            map.put("success", true);
                            map.put("errorCode", ConstIndex.ERR_NO);
                            map.put("message", "查询成功");
                        }
                    }
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
    public Map<String, Object> queryGoodDetail(Long uid , Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            if (null == id)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "ID不能为空");
                return map;
            }

            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PrivateGoods good = (PrivateGoods) session.get(PrivateGoods.class, id);
                if (null != good)
                {
                    String imghql = " from PrivateGoodsImgs where goodId=:id ";
                    q = session.createQuery(imghql);
                    q.setLong("id", id);
                    List<PrivateGoodsImgs> l = q.list();
                    good.setImgs(l);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("model", good);
                    map.put("message", "查询商品成功");
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "商品已经被删除");
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
    public Map<String, Object> addOrUpdate(Long uid , PrivateGoods model)
    {
        log.debug("user id is " + uid + " ,model is " + model);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null != model)
                {
                    if (null != model.getTypeId())
                    {
                        PrivateTypes type = (PrivateTypes) session.get(PrivateTypes.class, model.getTypeId());
                        if (null != type && (null != type.getZoneId()))
                        {
                            model.setZoneId(type.getZoneId());
                            model.setUpdateTime(DateUtils.getCurrDate(null));

                            if (null != model.getId())
                            {
                                String delhql = " delete from PrivateGoodsImgs where goodId=:id ";
                                Query q = session.createQuery(delhql);
                                q.setLong("id", model.getId());
                                q.executeUpdate();

                                if (null != model.getImgs() && model.getImgs().size() > 0)
                                {
                                    for (PrivateGoodsImgs img : model.getImgs())
                                    {
                                        img.setGoodId(model.getId());
                                        session.save(img);
                                    }
                                }
                                session.update(model);
                                log.debug("更新个人商品信息：" + model);
                                map.put("success", true);
                                //返回商品信息
                                map.put("model", model);
                                map.put("errorCode", ConstIndex.ERR_NO);
                                map.put("message", "物品更新成功");
                            }
                            else
                            {
                                log.debug("新增个人商品信息：" + model);
                                model.setCreateUserId(uid);
                                model.setCreateTime(DateUtils.getCurrDate(null));
                                Long id = (Long) session.save(model);
                                if (null != model.getImgs() && model.getImgs().size() > 0)
                                {
                                    for (PrivateGoodsImgs img : model.getImgs())
                                    {
                                        img.setGoodId(id);
                                        session.save(img);
                                    }
                                }
                                //返回商品信息
                                map.put("model", model);
                                map.put("success", true);
                                map.put("errorCode", ConstIndex.ERR_NO);
                                map.put("message", "物品新增成功");
                            }
                        }
                        else
                        {
                            map.put("success", false);
                            map.put("errorCode", ConstIndex.ERR_SOURCE);
                            map.put("message", "物品信息目录不存在");
                        }
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_LOST);
                        map.put("message", "物品信息中无目录id");
                    }
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "物品数据解析失败");
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
    public Map<String, Object> addBill(Long uid , Long id)
    {
        log.debug("addBill user id is " + uid + " ,id is " + id);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null != id)
                {
                    PrivateGoods good = (PrivateGoods) session.get(PrivateGoods.class, id);
                    if (null == good)
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "物品已经被删除");
                    }
                    else
                    {
                        //入账
                        PrivateBill bill = new PrivateBill();
                        bill.setBuyAddr(good.getBuyAddr());
                        bill.setBuyTime(good.getBuyTime());
                        bill.setCreateTime(DateUtils.getCurrDate(null));
                        //区分
                        bill.setCreateUserId(uid);

                        bill.setName(good.getName());
                        bill.setPid(good.getId());
                        bill.setPrice(good.getPrice());
                        bill.setUpdateTime(DateUtils.getCurrDate(null));

                        bill.setZoneId(good.getZoneId());

                        session.save(bill);
                        map.put("model", bill);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "添加成功");
                    }
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "物品id缺失");
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
    public Map<String, Object> queryBillReport(Long uid , String password , Long zoneId , String sMonth , String eMonth)
    {
        log.debug("addBill user id is " + uid + " ,zoneId is " + zoneId + ",sMonth is " + sMonth + ",eMonth is " + eMonth + ", password is " + password);
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            if (null == zoneId)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "zoneId不能为空");
                return map;
            }
            if (StringUtils.isBlank(password))
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "密码不能为空");
                return map;
            }

            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                Query q = null;
                PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, zoneId);
                if (null == t)
                {
                    //空间id要明确存在
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "空间不存在");
                    return map;
                }
                else
                {
                    String p = t.getPassword();
                    if (null == p)
                    {
                        p = "";
                    }
                    if (ConstIndex.PUBLIC_TYPE_ROOT == t.getPid())
                    {
                        log.debug("父节点0de是个人空间");
                        if (!(EncryptTool.MD5(password)).equals(p))
                        {
                            map.put("success", false);
                            map.put("errorCode", ConstIndex.ERR_SOURCE);
                            if ("".equals(p))
                            {
                                log.debug("空间未设置密码。。。。。。。。");
                                map.put("message", "空间未设置密码");
                            }
                            else
                            {
                                map.put("message", "密码不正确");
                            }
                            return map;
                        }
                    }
                    else
                    {
                        log.debug("父节点不是0不是个人空间");
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "空间不存在");
                        return map;
                    }
                }

                if (StringUtils.isBlank(sMonth) && StringUtils.isBlank(eMonth))
                {
                    //默认得到当前12个月以前
                    sMonth = DateUtils.queryMonthdBefore(11).substring(0, 7);
                    eMonth = DateUtils.getCurrDate(null).substring(0, 7);
                }
                String mSql = "select price,createMonth,createUserId,zoneId from v_month_private_bill where createUserId=:uid and zoneId=:zoneId";

                String dSql = "select price,createDate,createUserId,zoneId from v_day_private_bill where createUserId=:uid  and zoneId=:zoneId and createDate like :createDate";

                if (!StringUtils.isBlank(sMonth))
                {
                    mSql += " and  createMonth >=:sMonth ";
                }
                if (!StringUtils.isBlank(eMonth))
                {
                    mSql += " and  createMonth <=:eMonth ";
                }
                q = session.createSQLQuery(mSql).addScalar("price", StandardBasicTypes.DOUBLE).addScalar("createMonth", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).addScalar("zoneId", StandardBasicTypes.LONG).setResultTransformer(Transformers.aliasToBean(VoPrivateBillMonth.class));
                q.setLong("uid", uid);
                q.setLong("zoneId", zoneId);
                if (!StringUtils.isBlank(sMonth))
                {
                    q.setString("sMonth", sMonth);
                }
                if (!StringUtils.isBlank(eMonth))
                {
                    q.setString("eMonth", eMonth);
                }
                List<VoPrivateBillMonth> ml = q.list();
                for (int i = 0; i < ml.size(); i++)
                {
                    VoPrivateBillMonth o = ml.get(i);
                    q = session.createSQLQuery(dSql).addScalar("price", StandardBasicTypes.DOUBLE).addScalar("createDate", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).addScalar("zoneId", StandardBasicTypes.LONG).setResultTransformer(Transformers.aliasToBean(VoPrivateBillDate.class));
                    q.setLong("uid", uid);
                    q.setLong("zoneId", zoneId);
                    q.setString("createDate", o.getCreateMonth() + "%");
                    List<VoPrivateBillDate> dl = q.list();
                    o.setSubList(dl);
                }
                map.put("list", ml);
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_NO);
                map.put("message", "成功");
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
    public Map<String, Object> batchDelete(Long uid , List<Long> ids , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            if (StringUtils.isBlank(password))
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "密码不能为空");
                return map;
            }
            if (null == ids || ids.size() < 1)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "商品ID不存在");
                return map;
            }

            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                //删除成功的ids
                List<Long> sList = new ArrayList<Long>();
                int size = ids.size();
                for (Long id : ids)
                {
                    PrivateGoods good = (PrivateGoods) session.get(PrivateGoods.class, id);
                    if (null != good)
                    {
                        PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, good.getZoneId());
                        if (null != t)
                        {
                            if (EncryptTool.MD5(password).equals(t.getPassword()))
                            {
                                String imghql = "delete from PrivateGoodsImgs where goodId=:id ";
                                q = session.createQuery(imghql);
                                q.setLong("id", id);
                                q.executeUpdate();
                                session.delete(good);
                                sList.add(id);
                            }
                            else
                            {
                                size--;
                                log.debug("密码错误 batchDelete ==============");
                            }
                        }
                        else
                        {
                            size--;
                            log.debug("ERROR batchDelete ==============有的参数对于的商品不存在");
                        }
                    }
                    else
                    {
                        size--;
                        log.debug("存在商品id错误。id is " + id);
                    }
                }
                if (size > 0)
                {
                    if (size == ids.size())
                    {
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("successNum", size);
                        map.put("successList", sList);
                        map.put("message", "删除商品成功");
                    }
                    else
                    {
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("successNum", size);
                        map.put("successList", sList);
                        map.put("message", "部分商品删除成功");
                    }
                }
                else
                {
                    //密码错误
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("successNum", size);
                    map.put("successList", sList);
                    map.put("message", "密码错误");
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
    public Map<String, Object> deleteBill(Long uid , Long id , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            if (null == id)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "ID不能为空");
                return map;
            }
            if (StringUtils.isBlank(password))
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "密码不能为空");
                return map;
            }
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PrivateBill bill = (PrivateBill) session.get(PrivateBill.class, id);
                if (null == bill)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "目标数据不存在");
                }
                else
                {
                    Long zoneId = bill.getZoneId();
                    PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, zoneId);
                    if (EncryptTool.MD5(password).equals(t.getPassword()))
                    {
                        session.delete(bill);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "删除成功");
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                        map.put("message", "密码错误");
                    }
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
    public Map<String, Object> deleteGood(Long uid , Long id , String password)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            //            if (StringUtils.isBlank(password))
            //            {
            //                map.put("success", true);
            //                map.put("errorCode", ConstIndex.ERR_LOST);
            //                map.put("message", "密码不能为空");
            //                return map;
            //            }

            if (null == id)
            {
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_LOST);
                map.put("message", "商品ID不存在");
                return map;
            }
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PrivateGoods good = (PrivateGoods) session.get(PrivateGoods.class, id);
                if (null != good)
                {
                    PrivateTypes t = (PrivateTypes) session.get(PrivateTypes.class, good.getZoneId());
                    if (null != t)
                    {
                        //                        if (EncryptTool.MD5(password).equals(t.getPassword()))
                        //                        {
                        String imghql = "delete from PrivateGoodsImgs where goodId=:id ";
                        q = session.createQuery(imghql);
                        q.setLong("id", id);
                        q.executeUpdate();
                        session.delete(good);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "删除商品成功");
                        //                        }
                        //                        else
                        //                        {
                        //                            map.put("success", false);
                        //                            map.put("errorCode", ConstIndex.ERR_SOURCE);
                        //                            map.put("message", "密码错误");
                        //                            log.debug("密码错误 batchDelete ==============");
                        //                        }
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_SOURCE);
                        map.put("message", "ERROR商品空间不存在");
                        log.debug("ERROR batchDelete ==============有的参数对于的商品不存在");
                    }
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "商品不存在");
                    log.debug("存在商品id错误。id is " + id);
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
}