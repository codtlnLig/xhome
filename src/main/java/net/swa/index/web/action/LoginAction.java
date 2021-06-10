//
//package net.swa.index.web.action;
//
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Required;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import net.swa.index.service.IndexService;
//import net.swa.system.web.action.AbstractBaseAction;
//import net.swa.util.DateUtils;
//import net.swa.util.EncryptTool;
//
///**提供客户端接口 Action**/
//@Controller
//@RequestMapping(value = "/login")
//public class LoginAction extends AbstractBaseAction
//{
//
//    private IndexService indexService;
//
//    @RequestMapping(value = "/login")
//    public void login(String username , String password , String imei , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        map = indexService.login(username, password, imei);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/register")
//    public void register(String username , String password , String imei , String question1 , String answer1 , String question2 , String answer2 , String question3 , String answer3 , String type , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        map = indexService.saveRegister(username, password, imei, question1, answer1, question2, answer2, question3, answer3, type);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/modifyPassword")
//    public void modifyPassword(String oldPassword , String newPassword , String token , String imei , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//        map = indexService.modifyPassword(username, oldPassword, newPassword, imei);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/findPasswordQuestion")
//    public void findPasswordQuestion(String username , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        map = indexService.queryQuestions(username);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/findPassword")
//    public void findPassword(String username , String question1 , String answer1 , String question2 , String answer2 , String question3 , String answer3 , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        map = indexService.queryPassword(username, question1, answer1, question2, answer2, question3, answer3);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/resetPassword")
//    public void resetPassword(String username , String password , String secret , HttpServletResponse rsp) throws Exception
//    {
//        String token = null;
//        EncryptTool tool;
//        try
//        {
//            tool = new EncryptTool(DateUtils.getCurrDate("yyyy-MM-dd"));
//            token = tool.encrypt(username);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        if (null != token && null != secret && secret.equals(token))
//        {
//            Map<String, Object> map = null;
//            map = indexService.resetPassword(username, password);
//            outJson(map, rsp);
//        }
//        else
//        {
//            outError("安全secret校验失败", rsp);
//        }
//    }
//
//    @RequestMapping(value = "/myAccount")
//    public void myAccount(String token , String imei , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//        map = indexService.queryMyAccount(username);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/queryDetail")
//    public void queryDetail(String token , String imei , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//
//        map = indexService.queryDetail(username);
//        outJson(map, rsp);
//    }
//
//    /**
//     * 管理员向所有人发起充值或者缴费请求
//     * @param token
//     * @param imei
//     * @param type
//     * @param rsp
//     *  备注：type （0 - 充值 1-缴费
//     * @throws Exception
//     */
//    @RequestMapping(value = "/saveNewInfo")
//    public void saveNewInfo(String token , String imei , String type , Double money , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//        map = indexService.saveNewInfo(username, type, money);
//        outJson(map, rsp);
//    }
//
//    /**
//     * 管理员查询明细
//     * @param token
//     * @param imei
//     * @param type
//     * @param status
//     * @param rsp
//     * @throws Exception
//     */
//    @RequestMapping(value = "/queryInfos")
//    public void queryInfos(String token , String imei , String type , Integer status , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//        map = indexService.queryInfos(username, type, status);
//        outJson(map, rsp);
//    }
//
//    /**
//     * 管理员，回滚？？
//     */
//    @RequestMapping(value = "/updateMoneyByAdmin")
//    public void updateMoneyByAdmin(String token , String imei , Long id , HttpServletResponse rsp) throws Exception
//    {
//
//        //已经扣款的 返现
//        //已经充值的，扣除，余额不足的扣除失败
//    }
//
//    /**
//     * 查询充值或者缴费信息
//     * 用户查询管理员对他群发信息，待确认
//     * @param token
//     * @param imei
//     * @param rsp
//     * 备注：type （0 - 充值 1-缴费
//     * @throws Exception
//     */
//    @RequestMapping(value = "/queryNewInfo")
//    public void queryNewInfo(String token , String imei , String type , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//
//        map = indexService.queryNewInfo(username, type);
//        outJson(map, rsp);
//    }
//
//    /**
//     * 用户确认扣款充值信息信息
//     * @param token
//     * @param imei
//     * id 为查询的扣款或者充值的记录id
//     * @param type
//     * @param rsp
//     * @throws Exception
//     */
//    @RequestMapping(value = "/updateMoney")
//    public void updateMoney(String token , String imei , Long id , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//
//        map = indexService.updateMoney(username, id);
//        outJson(map, rsp);
//    }
//
//    /**
//     * 用户忽略扣款充值信息信息
//     * @param token
//     * @param imei
//     * id 为查询的扣款或者充值的记录id
//     * @param type
//     * @param rsp
//     * @throws Exception
//     */
//    @RequestMapping(value = "/ignoreEditMoney")
//    public void ignoreEditMoney(String token , String imei , Long id , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//
//        map = indexService.updateIgnoreMoney(username, id);
//        outJson(map, rsp);
//    }
//
//    public void queryNewVersion()
//    {
//
//    }
//
//    /***
//     * 判断某种类型用户是否存在
//     * @param type
//     * @param rsp
//     * @throws Exception
//     */
//    @RequestMapping(value = "/queryTypeExs")
//    public void queryTypeExs(String type , String username , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        map = indexService.queryTypeExs(type, username);
//        outJson(map, rsp);
//    }
//
//    //    queryDetail
//
//    /**
//     * 根据时间收费类型查询交易记录
//     * 
//     * 
//     */
//    @RequestMapping(value = "/queryUserDetail")
//    public void queryUserDetail(String token , String imei , String date , String type , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//
//        map = indexService.queryUserDetail(username, date, type);
//        outJson(map, rsp);
//    }
//
//    /**
//     * 根据时间收费类型查询交易记录
//     * 管理员查询
//     * 
//     */
//    @RequestMapping(value = "/queryByAdminDetail")
//    public void queryByAdminDetail(String token , String imei , String username , String date , String type , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String adminname = tool.decrypt(token);
//
//        map = indexService.queryByAdminDetail(adminname, username, date, type);
//        outJson(map, rsp);
//    }
//
//    @RequestMapping(value = "/updateLimit")
//    public void updateLimit(Double plusLimit , Double minusLimit , String token , String imei , HttpServletResponse rsp) throws Exception
//    {
//        Map<String, Object> map = null;
//        if (StringUtils.isBlank(imei))
//        {
//            imei = "";
//        }
//        EncryptTool tool = new EncryptTool(imei);
//        String username = tool.decrypt(token);
//        if (null == plusLimit)
//        {
//            plusLimit = 100d;
//        }
//        if (null == minusLimit)
//        {
//            minusLimit = 30d;
//        }
//        map = indexService.updateLimit(username, plusLimit, minusLimit);
//        outJson(map, rsp);
//    }
//
//    //    private String generateToken(String id , String imei)
//    //    {
//    //        String s = null;
//    //        if (StringUtils.isBlank(imei))
//    //        {
//    //            imei = "";
//    //        }
//    //        try
//    //        {
//    //            EncryptTool tool;
//    //            tool = new EncryptTool(imei);
//    //            s = tool.encrypt(id.toString());
//    //        }
//    //        catch (Exception e)
//    //        {
//    //            e.printStackTrace();
//    //        }
//    //        return s;
//    //    }
//
//    @Required
//    @Resource
//    public void setIndexService(IndexService indexService)
//    {
//        this.indexService = indexService;
//    }
//}
