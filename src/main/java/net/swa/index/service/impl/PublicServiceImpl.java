
package net.swa.index.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.swa.index.beans.entity.Members;
import net.swa.index.beans.entity.PublicBill;
import net.swa.index.beans.entity.PublicGoods;
import net.swa.index.beans.entity.PublicGoodsImgs;
import net.swa.index.beans.entity.PublicTypes;
import net.swa.index.beans.vo.VoPublicBillDate;
import net.swa.index.beans.vo.VoPublicBillMonth;
import net.swa.index.service.PublicService;
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

@Service("publicService")
public class PublicServiceImpl extends HibernateDaoSupport implements PublicService
{

    private static final Logger log = Logger.getLogger(PublicServiceImpl.class);

    @Override
    public Map<String, Object> addOrUpdateType(String imgUrl , String name , Long uid , Long pid , Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicTypes pt = null;
                if (null != id)
                {
                    //更新
                    pt = (PublicTypes) session.get(PublicTypes.class, id);
                    if (null != pt)
                    {
                        pt.setCreateUserId(uid);
                        if (!StringUtils.isBlank(imgUrl))
                        {
                            //图片有了就更新
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
                    if (!StringUtils.isBlank(name))
                    {
                        pt = new PublicTypes();
                        if (null == pid || 0 == pid)
                        {
                            log.debug("因为根节点都有了所有，到此处说明缺失参数");
                            pt.setPid(ConstIndex.PUBLIC_TYPE_ROOT);
                        }
                        else
                        {
                            //检测其父节点是否存在
                            PublicTypes parent = (PublicTypes) session.get(PublicTypes.class, pid);
                            if (null == parent)
                            {
                                map.put("success", true);
                                map.put("errorCode", ConstIndex.ERR_SOURCE);
                                map.put("message", "该父类型id不存在");
                                return map;
                            }
                            pt.setPid(pid);
                        }
                        pt.setCreateTime(DateUtils.getCurrDate(null));
                        pt.setCreateUserId(uid);
                        pt.setImgUrl(imgUrl);
                        pt.setName(name);

                        pt.setUpdateTime(DateUtils.getCurrDate(null));
                        session.save(pt);
                        map.put("model", pt);
                        map.put("success", true);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "新增分类成功");
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("errorCode", ConstIndex.ERR_LOST);
                        map.put("message", "分类名称不能空");
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
    public Map<String, Object> deleteType(Long uid , Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == id)
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "id不存在");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicTypes t = (PublicTypes) session.get(PublicTypes.class, id);
                if (null == t)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "该分类不存在");
                }
                else
                {
                    //检测是否有子分类
                    String hCheckChildren = " from PublicTypes where pid=:id ";
                    //检测是否有该类产品
                    String hCheckGoods = " from PublicGoods where typeId=:id ";
                    q = session.createQuery(hCheckChildren);
                    q.setLong("id", id);
                    List<PublicTypes> children = q.list();
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
                        List<PublicGoods> goods = q.list();
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
    public Map<String, Object> deleteTypeGoods(Long uid , Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == id)
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_SOURCE);
            map.put("message", "分类id不为null");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicTypes t = (PublicTypes) session.get(PublicTypes.class, id);
                if (null == t)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "该分类不存在");
                }
                else
                {
                    //检测是否有子分类
                    String hCheckChildren = " from PublicTypes where pid=:id ";
                    //删除该类产品图片
                    String himgs = " delete from PublicGoodsImgs where goodId=:goodId ";
                    String hCheckGoods = " from PublicGoods where typeId=:id ";
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
                        List<PublicGoods> l = q.list();
                        if (null != l && l.size() > 0)
                        {
                            for (PublicGoods g : l)
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
    public Map<String, Object> queryTypeByPid(Long uid , Long pid)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                //查询子节点
                String qChildren = " from PublicTypes where pid=:pid ";
                if (null == pid || ConstIndex.PUBLIC_TYPE_ROOT == pid)
                {
                    qChildren = qChildren + " and  createUserId=:createUserId ";
                    pid = ConstIndex.PUBLIC_TYPE_ROOT;
                }
                q = session.createQuery(qChildren);
                q.setLong("pid", pid);
                if (null == pid || ConstIndex.PUBLIC_TYPE_ROOT == pid)
                {
                    q.setLong("createUserId", uid);
                }
                List<PublicTypes> l = q.list();
                if (null != l && l.size() > 0)
                {
                    //检测是否有子分类
                    String hCheckChildren = " from PublicTypes where pid=:id ";
                    //检测是否有商品
                    String hCheckGoods = " from PublicGoods where typeId=:id ";
                    for (PublicTypes p : l)
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
                map.put("list", l);
                map.put("message", "查询子分类成功");
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
    public Map<String, Object> queryGoodsByPid(Long uid , Long typeId)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (null == typeId)
                {
                    typeId = ConstIndex.PUBLIC_TYPE_ROOT;
                }
                //查询目录下物品
                String qChildren = " from PublicGoods where typeId=:typeId  ";
                q = session.createQuery(qChildren);
                q.setLong("typeId", typeId);
                List<?> l = q.list();
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_NO);
                map.put("list", l);
                map.put("message", "查询商品成功");
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
        if (null == id)
        {
            map.put("success", true);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "商品id不能为空");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicGoods good = (PublicGoods) session.get(PublicGoods.class, id);
                if (null != good)
                {
                    String imghql = " from PublicGoodsImgs where goodId=:id ";
                    q = session.createQuery(imghql);
                    q.setLong("id", id);
                    List<PublicGoodsImgs> l = q.list();
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
    public Map<String, Object> addOrUpdate(Long uid , PublicGoods model)
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
                    model.setUpdateTime(DateUtils.getCurrDate(null));
                    if (null != model.getId())
                    {
                        String delhql = " delete from PublicGoodsImgs where goodId=:id ";
                        Query q = session.createQuery(delhql);
                        q.setLong("id", model.getId());
                        q.executeUpdate();

                        if (null != model.getImgs() && model.getImgs().size() > 0)
                        {
                            for (PublicGoodsImgs img : model.getImgs())
                            {
                                img.setGoodId(model.getId());
                                session.save(img);
                            }
                        }
                        session.update(model);
                        map.put("success", true);
                        //返回商品信息
                        map.put("model", model);
                        map.put("errorCode", ConstIndex.ERR_NO);
                        map.put("message", "物品更新成功");
                    }
                    else
                    {
                        model.setCreateTime(DateUtils.getCurrDate(null));
                        model.setCreateUserId(uid);
                        Long id = (Long) session.save(model);
                        if (null != model.getImgs() && model.getImgs().size() > 0)
                        {
                            for (PublicGoodsImgs img : model.getImgs())
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
        if (null == id)
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_SOURCE);
            map.put("message", "物品id不能为空");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicGoods good = (PublicGoods) session.get(PublicGoods.class, id);
                if (null == good)
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "物品已经被删除");
                }
                else
                {
                    //入账
                    PublicBill bill = new PublicBill();
                    bill.setBuyAddr(good.getBuyAddr());
                    bill.setBuyTime(good.getBuyTime());
                    bill.setCreateTime(DateUtils.getCurrDate(null));
                    //区分
                    bill.setCreateUserId(uid);

                    bill.setName(good.getName());
                    bill.setPid(good.getId());
                    bill.setPrice(good.getPrice());
                    bill.setUpdateTime(DateUtils.getCurrDate(null));
                    session.save(bill);
                    map.put("model", bill);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "物品成功");
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
    public Map<String, Object> queryAllByPid(Long uid , Long pid)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                //查询子节点
                String qChildren = " from PublicTypes where pid=:pid ";
                //查询节点下
                String qChildrenGoods = " from PublicGoods where typeId=:typeId ";

                if (null == pid || ConstIndex.PUBLIC_TYPE_ROOT == pid)
                {
                    qChildren = qChildren + " and  createUserId=:createUserId ";

                    qChildrenGoods = qChildrenGoods + " and  createUserId=:createUserId ";

                    pid = ConstIndex.PUBLIC_TYPE_ROOT;
                }
                q = session.createQuery(qChildren);
                q.setLong("pid", pid);
                if (null == pid || ConstIndex.PUBLIC_TYPE_ROOT == pid)
                {
                    q.setLong("createUserId", uid);
                }
                List<?> l = q.list();

                q = session.createQuery(qChildrenGoods);
                q.setLong("typeId", pid);
                if (null == pid || ConstIndex.PUBLIC_TYPE_ROOT == pid)
                {
                    q.setLong("createUserId", uid);
                    log.debug("LOG物品可以直挂最外层时候再执行。。");
                }
                else
                {
                    //默认共享空间不直接挂物品，如可以将此块外移
                    List<?> goods = q.list();
                    map.put("goods", goods);
                }
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_NO);
                map.put("list", l);
                map.put("message", "查询子分类成功");
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
    public Map<String, Object> queryBillReport(Long uid , String sMonth , String eMonth)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (StringUtils.isBlank(sMonth) && StringUtils.isBlank(eMonth))
                {
                    //默认得到当前12个月以前
                    sMonth = DateUtils.queryMonthdBefore(11).substring(0, 7);
                    eMonth = DateUtils.getCurrDate(null).substring(0, 7);
                }
                String mSql = "select price,createMonth,createUserId  from v_month_public_bill where createUserId=:uid ";

                String dSql = "select price,createDate,createUserId  from v_day_public_bill where createUserId=:uid  and createDate like :createDate";

                if (!StringUtils.isBlank(sMonth))
                {
                    mSql += " and  createMonth >=:sMonth ";
                }
                if (!StringUtils.isBlank(eMonth))
                {
                    mSql += " and  createMonth <=:eMonth ";
                }
                q = session.createSQLQuery(mSql).addScalar("price", StandardBasicTypes.DOUBLE).addScalar("createMonth", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).setResultTransformer(Transformers.aliasToBean(VoPublicBillMonth.class));
                q.setLong("uid", uid);
                if (!StringUtils.isBlank(sMonth))
                {
                    q.setString("sMonth", sMonth);
                }
                if (!StringUtils.isBlank(eMonth))
                {
                    q.setString("eMonth", eMonth);
                }
                List<VoPublicBillMonth> ml = q.list();
                for (int i = 0; i < ml.size(); i++)
                {
                    VoPublicBillMonth o = ml.get(i);
                    q = session.createSQLQuery(dSql).addScalar("price", StandardBasicTypes.DOUBLE).addScalar("createDate", StandardBasicTypes.STRING).addScalar("createUserId", StandardBasicTypes.LONG).setResultTransformer(Transformers.aliasToBean(VoPublicBillDate.class));
                    q.setLong("uid", uid);
                    q.setString("createDate", o.getCreateMonth() + "%");
                    List<VoPublicBillDate> dl = q.list();
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
    public Map<String, Object> batchDelete(Long uid , List<Long> ids)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == ids || ids.size() < 1)
        {
            map.put("success", true);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "商品ID不存在");
            return map;
        }

        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                int size = ids.size();
                //删除成功的ids
                List<Long> sList = new ArrayList<Long>();
                for (Long id : ids)
                {
                    PublicGoods good = (PublicGoods) session.get(PublicGoods.class, id);
                    if (null != good)
                    {
                        String imghql = "delete from PublicGoodsImgs where goodId=:id ";
                        q = session.createQuery(imghql);
                        q.setLong("id", id);
                        q.executeUpdate();
                        session.delete(good);
                        sList.add(id);
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
                    //ids全部不存在
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("successNum", size);
                    map.put("successList", sList);
                    map.put("message", "目标商品不存在");
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
    public Map<String, Object> batchDeleteBill(Long uid , List<Long> ids)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        if (null == ids || ids.size() < 1)
        {
            map.put("success", true);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "账单的ID不存在");
            return map;
        }

        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                int size = ids.size();
                for (Long id : ids)
                {
                    PublicBill bill = (PublicBill) session.get(PublicBill.class, id);
                    if (null != bill)
                    {
                        session.delete(bill);
                    }
                    else
                    {
                        size--;
                        log.debug("存在账单id错误。id is " + id);
                    }
                }
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_NO);
                map.put("successNum", size);
                map.put("message", "删除账单成功");
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
    public Map<String, Object> deleteBill(Long uid , Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == id)
        {
            map.put("success", true);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "账单的ID不存在");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicBill bill = (PublicBill) session.get(PublicBill.class, id);
                if (null != bill)
                {
                    session.delete(bill);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "删除账单成功");
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "账单不存在");

                    log.debug("存在账单id错误。id is " + id);
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
    public Map<String, Object> deleteGood(Long uid , Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == id)
        {
            map.put("success", true);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "商品ID不存在");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Query q = null;
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                PublicGoods good = (PublicGoods) session.get(PublicGoods.class, id);
                if (null != good)
                {
                    String imghql = "delete from PublicGoodsImgs where goodId=:id ";
                    q = session.createQuery(imghql);
                    q.setLong("id", id);
                    q.executeUpdate();
                    session.delete(good);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "删除商品成功");
                }
                else
                {
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "商品不存在");
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
    public boolean queryCheckUid(Long uid)
    {
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                return true;
            }
        }
        return false;
    }

}