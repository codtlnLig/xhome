
package net.swa.business.web.action;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import net.swa.system.service.ICommonService;
import net.swa.system.web.action.AbstractBaseAction;
import net.swa.util.JsonResult;

@Controller
@RequestMapping(value = "/comm")
public class CommonAction extends AbstractBaseAction
{
    private static final long serialVersionUID = 523073008569799989L;

    private ICommonService commonService;

    /**
     * 通用状态更新方法
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/updateStatus")
    public void updateStatus(String type , Long[] ids , HttpServletRequest request , HttpServletResponse response) throws Exception
    {
        JsonResult<String> json = new JsonResult<String>();
        int status = Integer.parseInt(request.getParameter("status"));
        commonService.commonUpdateStatus(type, ids, status);
        outJson(json, response);
    }

    /**
     * 通用删除记录方法
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public void delete(String type , Long[] ids , HttpServletRequest request , HttpServletResponse response) throws Exception
    {
        JsonResult<String> json = new JsonResult<String>();
        try
        {
            commonService.commonDelete(type, ids);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            json.setSuccess(false);
            json.setMessage(e.getMessage());
        }
        outJson(json, response);
    }

    /**
     * 通用查询分页方法
     * 
     * @throws Exception
     */
    @RequestMapping("/search")
    public void search(Integer currentPage , Integer pageSize , String orderBy , String orderType , String type , Long[] ids , HttpServletRequest request , HttpServletResponse response) throws Exception
    {
        if (null == currentPage)
        {
            currentPage = 1;
        }
        if (null == pageSize)
        {
            pageSize = 1;
        }
        currentPage = currentPage == 0 ? 1 : currentPage;
        pageSize = pageSize == 0 ? 20 : pageSize;
        List<String> attrValues = null;
        List<String> operators = null;
        String[] attrNames = request.getParameterValues("attrNames");
        if (attrNames == null)
        {
            attrNames = new String[] {};
        }
        List<String> paramName = new ArrayList<String>();
        attrValues = new ArrayList<String>();
        operators = new ArrayList<String>();

        for (int i = 0; i < attrNames.length; i++)
        {
            String[] value = request.getParameterValues(attrNames[i]);
            if (!paramName.contains(attrNames[i]))
            {

                if (value != null)
                {
                    if (value.length > 1)
                    {
                        String[] opers = request.getParameterValues(attrNames[i] + "_operator");
                        for (int j = 0; j < value.length; j++)
                        {
                            attrValues.add(value[j]);
                            paramName.add(attrNames[i]);
                            operators.add(opers[j]);
                        }
                    }
                    else if (value.length == 1)
                    {
                        paramName.add(attrNames[i]);
                        attrValues.add(value[0]);
                        operators.add(request.getParameter(attrNames[i] + "_operator"));
                    }
                    else
                    {

                    }
                }

            }
        }
        Class<?> cLz = Class.forName(type);
        String[] searchAttr = new String[paramName.size()];
        paramName.toArray(searchAttr);
        String[] searchVal = new String[paramName.size()];
        attrValues.toArray(searchVal);
        String[] searchOper = new String[paramName.size()];
        operators.toArray(searchOper);

        JsonResult<?> json = commonService.search(searchAttr, searchVal, searchOper, cLz, currentPage, pageSize, orderBy, orderType);
        json.setCurrentPage(currentPage);
        outJson(json, response);
    }

    public ICommonService getCommonService()
    {
        return commonService;
    }

    @Resource
    @Required
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }
}
