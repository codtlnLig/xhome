
package net.swa.index.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.swa.index.beans.entity.Members;
import net.swa.index.service.IndexService;
import net.swa.index.util.ConstIndex;
import net.swa.system.dao.HibernateDaoSupport;
import net.swa.util.DateUtils;
import net.swa.util.EncryptTool;
import net.swa.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service("indexService")
public class IndexServiceImpl extends HibernateDaoSupport implements IndexService
{
    private static final Logger log = Logger.getLogger(IndexServiceImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> login(String email , String mobi , String password , String mac)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (((!StringUtils.isBlank(email)) || (!StringUtils.isBlank(mobi))) && (!StringUtils.isBlank(password)))
        {
            Query q = null;
            Session session = getCurrentSession();
            StringBuilder hql = new StringBuilder();
            hql.append("from Members where 1=1 ");
            if (!StringUtils.isBlank(email))
            {
                hql.append(" and email=:email ");
            }
            else
            {
                hql.append(" and mobi=:mobi ");
            }
            q = session.createQuery(hql.toString());
            if (!StringUtils.isBlank(email))
            {
                q.setString("email", email);
            }
            else
            {
                q.setString("mobi", mobi);
            }
            List l = q.list();
            if (null != l && l.size() == 1)
            {
                Members o = (Members) l.get(0);
                if ((EncryptTool.MD5(password)).equals(o.getPassword()))
                {
                    if (o.getLocked())
                    {
                        map.put("success", false);
                        map.put("token", null);
                        map.put("user", null);
                        map.put("message", "用状态锁定");
                    }
                    else
                    {
                        o.setFirstLogin(false);
                        session.update(o);
                        map.put("success", false);
                        map.put("token", null);
                        map.put("user", o);
                        map.put("message", "获取token失败");
                        if (StringUtils.isBlank(mac))
                        {
                            mac = ConstIndex.DEFAULT_MAC;
                        }
                        EncryptTool tool;
                        try
                        {
                            tool = new EncryptTool(mac);
                            String token = tool.encrypt("" + o.getId());
                            map.put("success", true);
                            map.put("token", token);
                            map.put("message", "");
                            map.put("errorCode", ConstIndex.ERR_NO);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    map.put("success", false);
                    map.put("message", "密码错误");
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                }
            }
            else
            {
                map.put("success", false);
                map.put("message", "用户名不存在");
                map.put("errorCode", ConstIndex.ERR_CANNOT_FOUND);
            }
        }
        else
        {
            map.put("success", false);
            map.put("message", "账号密码不能为空");
            map.put("errorCode", ConstIndex.ERR_LOST);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> saveRegister(String mobi , String code , String email , String nickname , String password , String mac)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Long uid = null;
        if ((!StringUtils.isBlank(password)))
        {
            Query q = null;
            Session session = getCurrentSession();
            String hql = null;

            if ((!StringUtils.isBlank(mobi)))
            {
                //优先手机
                if ((!StringUtils.isBlank(code)))
                {
                    hql = "from Members where mobi=:mobi and code=:code ";
                    q = session.createQuery(hql);
                    q.setString("mobi", mobi);
                    q.setString("code", code);
                    //                    q.setString("loginType", "mobi");
                    List<Members> list = q.list();
                    if (null != list && list.size() > 0)
                    {
                        Members m = list.get(0);
                        if (m.getLocked())
                        {
                            m.setCreateTime(DateUtils.getCurrDate(null));
                            m.setNickName(nickname);
                            m.setPassword(EncryptTool.MD5(password));
                            m.setLocked(false);
                            m.setErrorTimes(0);//密码输错次数
                            m.setLockedTime(null);
                            m.setMac(mac);
                            m.setUuid(UUID.randomUUID().toString());
                            m.setFirstLogin(true);
                            session.update(m);
                            map.put("success", true);
                            map.put("message", "手机注册成功");
                            map.put("errorCode", ConstIndex.ERR_NO);
                            uid = m.getId();
                        }
                        else
                        {
                            //这种几率较小
                            map.put("success", false);
                            map.put("message", "该手机号码已经使用");
                            map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                        }
                    }
                    else
                    {
                        map.put("success", false);
                        map.put("message", "验证码错误，注册失败");
                        map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                    }
                }
            }
            else if ((!StringUtils.isBlank(email)))
            {
                //邮箱注册
                hql = "from Members where email=:email ";
                q = session.createQuery(hql);
                q.setString("email", email);
                List<Members> list = q.list();
                if (null != list && list.size() > 0)
                {
                    map.put("success", false);
                    map.put("message", "该邮箱邮已经注册，注册失败");
                    map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
                }
                else
                {
                    Members m = new Members();
                    m.setEmail(email);
                    m.setLoginType(ConstIndex.REGISTE_BY_EMAIL);
                    m.setLocked(false);
                    m.setCreateTime(DateUtils.getCurrDate(null));
                    m.setNickName(nickname);
                    m.setPassword(EncryptTool.MD5(password));
                    m.setErrorTimes(0);//密码输错次数
                    m.setLockedTime(null);
                    m.setMac(mac);
                    m.setUuid(UUID.randomUUID().toString());
                    m.setFirstLogin(true);
                    session.save(m);
                    log.debug("创建共享空间 前必须确认帐户存在 m is " + m);

                    map.put("success", true);
                    map.put("message", "email注册成功");
                    map.put("errorCode", ConstIndex.ERR_NO);
                    uid = m.getId();
                }
            }
            else
            {
                map.put("success", false);
                map.put("message", "缺失参数，注册失败");
                map.put("errorCode", ConstIndex.ERR_LOST);
            }
        }
        else
        {
            map.put("success", false);
            map.put("message", "密码不能为空");
            map.put("errorCode", ConstIndex.ERR_LOST);
        }
        Boolean success = (Boolean) map.get("success");
        if (null == success)
        {
            success = false;
        }
        if (success && null != uid)
        {
            map.put("success", false);
            map.put("token", null);
            map.put("errorCode", ConstIndex.ERR_FATAL);
            if (StringUtils.isBlank(mac))
            {
                mac = ConstIndex.DEFAULT_MAC;
            }
            EncryptTool tool;
            try
            {
                tool = new EncryptTool(mac);
                String token = tool.encrypt("" + uid);
                map.put("success", true);
                map.put("token", token);
                map.put("errorCode", ConstIndex.ERR_NO);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                map.put("token", null);
                map.put("message", "生成token异常");
            }
        }
        else
        {
            map.put("success", false);
            map.put("token", null);
            map.put("errorCode", ConstIndex.ERR_FATAL);
            map.put("message", "生成token时候发生致命性错误");
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> saveCode(String mobi , String mac)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if ((!StringUtils.isBlank(mobi)))
        {
            String code = StringUtil.getRandomString(4);
            //TODO 发送
            Query q = null;
            Session session = getCurrentSession();
            Members m = null;
            String hql = "from Members where mobi=:mobi";
            q = session.createQuery(hql);
            q.setString("mobi", mobi);
            List<Members> list = q.list();
            if (null != list && 1 == list.size())
            {
                m = list.get(0);
            }
            else
            {
                m = new Members();
                m.setLocked(true);
                m.setLockedTime(DateUtils.getCurrDate(null));
            }
            if (m.getLocked())
            {
                m.setCode(code);
                m.setMobi(mobi);
                m.setLoginType(ConstIndex.REGISTE_BY_MOBI);
                session.saveOrUpdate(m);
                map.put("success", true);
                //TODO 真正发 不会返回code
                map.put("code", code);
                map.put("message", "验证码发送成功");
                map.put("errorCode", ConstIndex.ERR_NO);
            }
            else
            {
                map.put("success", false);
                map.put("message", "手机号已经注册使用");
                map.put("errorCode", ConstIndex.ERR_FORBIDDEN);
            }
        }
        else
        {
            map.put("success", false);
            map.put("message", "手机号码空");
            map.put("errorCode", ConstIndex.ERR_LOST);
        }
        return map;
    }

    @Override
    public Map<String, Object> updatePassword(Long uid , String password , String oldpassword)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(password))
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(oldpassword))
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "原密码不能为空");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                if (EncryptTool.MD5(oldpassword).equals(m.getPassword()))
                {
                    m.setPassword(EncryptTool.MD5(password));
                    session.update(m);
                    map.put("success", true);
                    map.put("errorCode", ConstIndex.ERR_NO);
                    map.put("message", "修改密码成功");
                }
                else
                {
                    map.put("success", false);
                    map.put("errorCode", ConstIndex.ERR_SOURCE);
                    map.put("message", "原密码错误");
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_CANNOT_FOUND);
                map.put("message", "用户不存在");
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
    public Map<String, Object> updateNick(Long uid , String nickname)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(nickname))
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "昵称不能为空");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                m.setNickName(nickname);
                session.update(m);
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_NO);
                map.put("message", "修改昵称成功");
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_CANNOT_FOUND);
                map.put("message", "用户不存在");
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
    public Map<String, Object> updatePhoto(Long uid , String imgUrl)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(imgUrl))
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_LOST);
            map.put("message", "照片不能为空");
            return map;
        }
        if (null != uid)
        {
            Session session = getCurrentSession();
            Members m = (Members) session.get(Members.class, uid);
            if (null != m)
            {
                m.setImgUrl(imgUrl);
                session.update(m);
                map.put("success", true);
                map.put("errorCode", ConstIndex.ERR_NO);
                map.put("message", "修改头像成功");
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_CANNOT_FOUND);
                map.put("message", "token校验失败，用户不存在");
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