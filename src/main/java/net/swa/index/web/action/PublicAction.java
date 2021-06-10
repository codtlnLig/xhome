
package net.swa.index.web.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.sf.json.JSONObject;
import net.swa.index.beans.entity.PublicGoods;
import net.swa.index.beans.entity.PublicGoodsImgs;
import net.swa.index.service.PublicService;
import net.swa.index.util.ConstIndex;
import net.swa.system.web.action.AbstractBaseAction;
import net.swa.util.ConfigUtil;

/**
 * 提供客户端接口 Action
 * 共享空间相关
 * @author junyu
 *
 */
@Controller
@RequestMapping(value = "/public")
public class PublicAction extends AbstractBaseAction
{

    /**存放上传文件的目录**/
    private String uploadPath;

    /**存放删除文件目录*/
    private String gabagePath;

    /**文件服务器路径**/
    private String httpPath;

    public PublicAction()
    {
        uploadPath = ConfigUtil.getProperty("uploadPath");
        gabagePath = ConfigUtil.getProperty("gabagePath");
        httpPath = ConfigUtil.getProperty("httpPath");
    }

    private PublicService publicService;

    /**
     * 新增or修改类型
     * @param imgUrl 图片链接，三级菜单无
     * @param name   分类名称
     * @param pid    父分类id ,如是顶级分类其父分类可以不传 or 0
     * @param id     分类的id，若存在则修改
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/saveType")
    public void saveType(String imgUrl , String name , Long pid , Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.addOrUpdateType(imgUrl, name, uid, pid, id);
        outJson(map, rsp);
    }

    /**
     * 目录删除，若存在下级目录，则不给删除；若存在商品不给删除
     * @param id 目录id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteType")
    public void deleteType(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.deleteType(uid, id);
        outJson(map, rsp);
    }

    /**
     * 确认后再删除，若类型有子类型无法删除会提示（仅叶子分类可以删除），否则先删除商品再删除分类
     * 
     * @param id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/suDeleteType")
    public void suDeleteType(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.deleteTypeGoods(uid, id);
        outJson(map, rsp);
    }

    /**
     * 根据父分类查询子分类信息
     * @param pid  父分类id,若不传表示查询一级目录
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryType")
    public void queryType(Long pid , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.queryTypeByPid(uid, pid);
        outJson(map, rsp);
    }

    /**
     * 查询typeId分类下所有物品
     * 若物品数量多可以令开发分页加载
     * @param typeId
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryGoods")
    public void queryGoods(Long typeId , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.queryGoodsByPid(uid, typeId);
        outJson(map, rsp);
    }

    /**
     * 根据目录查询所有子节点（包括子目录和商品）
     * @param pid 父目录id
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryAllByPid")
    public void queryAllByPid(Long pid , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.queryAllByPid(uid, pid);
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
        map = publicService.queryGoodDetail(uid, id);
        outJson(map, rsp);
    }

    /**
     * 保存商品信息
     * 需要传递json 数据
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

        JSONObject obj = JSONObject.fromObject(json.toString());
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("imgs", PublicGoodsImgs.class);
        PublicGoods model = (PublicGoods) JSONObject.toBean(obj, PublicGoods.class, classMap);

        // 解析json到对象
        //        PublicGoods model = (PublicGoods) getDTO(json.toString(), PublicGoods.class);
        map = publicService.addOrUpdate(uid, model);
        outJson(map, rsp);
    }

    /**
     * 批量删除
     * 删除数据格式json:[1,2,3]
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteGoods")
    public void deleteGoods(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        //        List<Long> ids = new ArrayList<Long>();
        //        ids.add(id);
        //        map = publicService.batchDelete(uid, ids);
        map = publicService.deleteGood(uid, id);
        outJson(map, rsp);
    }

    /**
     * 将物品计入账单
     * @param id 物品id
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
        map = publicService.addBill(uid, id);
        outJson(map, rsp);
    }

    /**
     * 删除加入账单的数据
     * 要删除的数据格式用json 传入 json:[1,2,3]
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/deleteBills")
    public void deleteBills(Long id , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        //        List<Long> ids = new ArrayList<Long>();
        //        ids.add(id);
        //        map = publicService.batchDeleteBill(uid, ids);

        map = publicService.deleteBill(uid, id);
        outJson(map, rsp);
    }

    /**
     * 查询消费账单统计信息，开始月结束月不传时候默认查询至今12个月数据
     * @param sMonth  开始月（选填）
     * @param eMonth  结束月（选填）
     * @param token
     * @param mac
     * @param req
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/queryBillReport")
    public void queryBillReport(String sMonth , String eMonth , String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = null;
        Long uid = getUserId(req);
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        map = publicService.queryBillReport(uid, sMonth, eMonth);
        outJson(map, rsp);
    }

    /**
     * 文件上传
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/upload")
    public void upload(String token , String mac , HttpServletRequest req , HttpServletResponse rsp) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Long uid = getUserId(req);
        System.out.println(req.getContentLength());
        System.out.println(req.getContentType());
        if (null == uid && (!StringUtils.isBlank(token)))
        {
            uid = getUserId(token, mac);
        }
        if (null != uid)
        {
            boolean f = publicService.queryCheckUid(uid);
            if (f)
            {
                try
                {
                    String url = uploadFile("remoteFile/", req);
                    map.put("success", true);
                    map.put("url", url);
                    System.out.println(url);
                    map.put("errorCode", ConstIndex.ERR_NO);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    map.put("success", false);
                    map.put("msg", e.getMessage());
                    map.put("errorCode", ConstIndex.ERR_IO);
                }
            }
            else
            {
                map.put("success", false);
                map.put("errorCode", ConstIndex.ERR_TOKEN);
                map.put("message", "用户不存在");
            }
        }
        else
        {
            map.put("success", false);
            map.put("errorCode", ConstIndex.ERR_TOKEN);
            map.put("message", "token校验失败");
        }

        outJson(map, rsp);
    }

    private String uploadFile(String imgDir , HttpServletRequest req) throws Exception
    {
        String savePath = uploadPath + imgDir;
        String serverUrl = httpPath + imgDir;
        UUID uuid = UUID.randomUUID();
        String filename = uuid.toString().replace("-", "");
        File dir = new File(savePath);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        File file = new File(savePath + File.separator + filename);
        InputStream in = req.getInputStream();
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        bos = new BufferedOutputStream(new FileOutputStream(file));
        bis = new BufferedInputStream(in);

        byte[] b = new byte[1024];
        int len;
        while ((len = bis.read(b)) > 0)
        {
            bos.write(b, 0, len);
        }
        bos.flush();
        bos.close();
        bis.close();
        return serverUrl + filename;
    }

    public PublicService getPublicService()
    {
        return publicService;
    }

    @Resource
    @Required
    public void setPublicService(PublicService publicService)
    {
        this.publicService = publicService;
    }

}
