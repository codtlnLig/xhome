
package net.swa.index.web.action;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;
import net.swa.index.beans.entity.PrivateGoods;
import net.swa.index.beans.entity.PrivateGoodsImgs;
import net.swa.index.beans.entity.PrivateTypes;
import net.swa.index.service.PrivateService;
import net.swa.system.web.action.AbstractBaseAction;

/**
 * 提供客户端接口 Action
 * 个人空间相关
 * @author junyu
 *
 */
@Controller
@RequestMapping(value = "/private")
public class PrivateAction extends AbstractBaseAction
{
    private static final Logger log = Logger.getLogger(PrivateAction.class);

    private PrivateService privateService;

    /**
     * * 新增or修改个人空间类型
     * @param imgUrl 图片链接，三级菜单无
     * @param name   分类名称
     * @param pid    父分类id ,如是顶级分类其父分类可以不传 or 0
     * @param id     分类的id，若存在则修改
     * @param password  密码（个人的第一层目录设置，子目录可以不传）
     * @param question  密码问题（个人的第一层目录设置，子目录可以不传）
     * @param answer    密码问题答案（个人的第一层目录设置，子目录可以不传）
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveType")
    public void saveType(String imgUrl , String name , Long pid , Long id , String password , String question , String answer , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.addOrUpdateType(uid, imgUrl, name, pid, id, password, question, answer);
        if (null != map.get("model"))
        {
            PrivateTypes o = (PrivateTypes) map.get("model");
            //将密码屏蔽
            o.setPassword(null);
        }
        outJson(map, rsp);
    }

    /**
     * 输入密码删除目录，若有子目录or商品则不删除返回提醒
     * 
     * @param id 目录id
     * @param password  密码
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteType")
    public void deleteType(Long id , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.deleteType(uid, id, password);
        outJson(map, rsp);
    }

    /**
     * 确认后再删除，若类型有子类型无法删除会提示（仅叶子分类可以删除），否则先删除商品再删除分类
     * @param id 目录id
     * @param password  密码
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/suDeleteType")
    public void suDeleteType(Long id , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.deleteTypeGoods(uid, id, password);
        outJson(map, rsp);
    }

    /**
     * 根据父分类查询子分类信息
     * @param pid 父分类id,若不传表示查询一级目录
     * @param password  若打开私人文件夹，需要传密码（其他情况不需要传）
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryType")
    public void queryType(Long pid , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.queryTypeByPid(uid, pid, password);
        outJson(map, rsp);
    }

    /**
     * 查询目录下商品
     * @param typeId 目录
     * @param password 密码，若打开私人文件夹，需要传密码（其他情况不需要传）
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryGoods")
    public void queryGoods(Long typeId , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.queryGoodsByPid(uid, typeId, password);
        outJson(map, rsp);
    }

    /**
     * 根据父目录id查物品和子目录
     * @param pid 父母路id
     * @param password 密码，若打开私人文件夹，需要传密码（其他情况不需要传）
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryAllByPid")
    public void queryAllByPid(Long pid , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.queryAllByPid(uid, pid, password);
        outJson(map, rsp);
    }

    /**
     * 查询物品详细信息
     * @param id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryGoodDetail")
    public void queryGoodDetail(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.queryGoodDetail(uid, id);
        outJson(map, rsp);
    }

    /**
     * 保存商品信息
     * 商品信息格式json
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveGood")
    public void saveGood(String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        StringBuffer json = new StringBuffer();
        //json 格式数据收集
        BufferedReader reader = req.getReader();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            json.append(line);
        }
        log.debug("json is " + json);
        JSONObject obj = JSONObject.fromObject(json.toString());
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("imgs", PrivateGoodsImgs.class);
        PrivateGoods model = (PrivateGoods) JSONObject.toBean(obj, PrivateGoods.class, classMap);
        // 解析json到对象
        //        PrivateGoods model = (PrivateGoods) getDTO(json.toString(), PrivateGoods.class);
        map = privateService.addOrUpdate(uid, model);
        outJson(map, rsp);
    }

    /**
     * 删除个人空间下商品
     * @param password 密码
     * id  要删除的id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteGoods")
    public void deleteGoods(Long id , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        //        List<Long> ids = new ArrayList<Long>();
        //        ids.add(id);
        //        map = privateService.batchDelete(uid, ids, password);
        map = privateService.deleteGood(uid, id, password);
        outJson(map, rsp);
    }

    /**
     * 将商品加入账单
     * @param id 商品id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveBill")
    public void saveBill(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.addBill(uid, id);
        outJson(map, rsp);
    }

    /**
     * 个人空间账单数据删除
     * @param id  账单id
     * @param password  密码
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteBills")
    public void deleteBills(Long id , String password , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.deleteBill(uid, id, password);
        outJson(map, rsp);
    }

    /**
     * 个人空间查询消费账单统计信息，开始月结束月不传时候默认查询至今12个月数据
     * @param zoneId  空间id
     * @param sMonth  开始月（选填）
     * @param eMonth  结束月（选填）
     * @param password  密码
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryBillReport")
    public void queryBillReport(Long zoneId , String password , String sMonth , String eMonth , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = privateService.queryBillReport(uid, password, zoneId, sMonth, eMonth);
        outJson(map, rsp);
    }

    public PrivateService getPrivateService()
    {
        return privateService;
    }

    @Resource
    @Required
    public void setPrivateService(PrivateService privateService)
    {
        this.privateService = privateService;
    }

}
