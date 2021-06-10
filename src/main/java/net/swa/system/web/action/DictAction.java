
package net.swa.system.web.action;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import net.swa.system.beans.entity.Dict;
import net.swa.system.service.ICommonService;
import net.swa.system.service.IDictService;
import net.swa.util.JsonResult;

/**
 * 字典action
 * 
 * 
 */
@Controller
@RequestMapping(value = "/dict")
public class DictAction extends AbstractBaseAction
{
    private static final long serialVersionUID = -985277678565361630L;

    private ICommonService commonService;

    private IDictService dictService;

    /**
     * 列表页面
     * 
     * @return
     */
    @RequestMapping(value = "/listPage")
    public ModelAndView listPage() throws Exception
    {
        ModelAndView mv = new ModelAndView("system/dict/list");
        List<Dict> typeList = dictService.getDictType();
        mv.addObject("typeList", getJson(typeList));
        return mv;
    }

    /**
    * 列表页面
    * 
    * @return
    */
    @RequestMapping(value = "/listPage2")
    public ModelAndView listPage2() throws Exception
    {
        ModelAndView mv = new ModelAndView("system/dict2/list");
        List<Dict> typeList = dictService.getDictType2();
        mv.addObject("typeList", getJson(typeList));
        return mv;
    }

    /**
     * 编辑、新建操作
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit(@ModelAttribute Dict dict) throws Exception
    {
        ModelAndView mv = new ModelAndView("system/dict/edit");
        if (dict == null)
        {
            dict = new Dict();
        }
        else
        {
            dict = commonService.commonFind(Dict.class, dict.getId());
        }
        mv.addObject("dict", dict);
        return mv;
    }

    /**
     * 编辑、新建操作
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit2")
    public ModelAndView edit2(@ModelAttribute Dict dict , String type) throws Exception
    {
        ModelAndView mv = new ModelAndView("system/dict2/edit");
        if (dict == null)
        {
            dict = new Dict();
            if (!StringUtils.isBlank(type))
            {
                dict.setTitle(type);
            }
        }
        else
        {
            dict = commonService.commonFind(Dict.class, dict.getId());
        }
        mv.addObject("dict", dict);
        return mv;
    }

    /**
     * 保存操作
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public void save(@ModelAttribute Dict dict , HttpServletResponse rsp) throws Exception
    {
        JsonResult<Dict> json = new JsonResult<Dict>();
        if (dict.getId() == 0)
        {
            commonService.commonAdd(dict);
        }
        else
        {
            commonService.commonUpdate(dict);
        }
        outJson(json, rsp);
    }

    /**
     * 保存操作
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/save2")
    public void save2(@ModelAttribute Dict dict , HttpServletResponse rsp) throws Exception
    {
        JsonResult<Dict> json = new JsonResult<Dict>();
        if (!StringUtils.isBlank(dict.getValue()))
        {
            if ("不限".equals(dict.getValue().trim()))
            {
                dict.setKey("");
            }
            else
            {
                dict.setKey(dict.getValue());
            }
        }
        if (dict.getId() == 0)
        {
            commonService.commonAdd(dict);
        }
        else
        {
            commonService.commonUpdate(dict);
        }
        outJson(json, rsp);
    }

    /**
     * 保存排序
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/savesort")
    public void savesort(Long[] dicIds , Long[] dicNums , HttpServletResponse rsp) throws Exception
    {
        dictService.updateDicNum(dicIds, dicNums);
        JsonResult<Dict> json = new JsonResult<Dict>();
        outJson(json, rsp);
    }

    @RequestMapping(value = "/delete")
    public void delete(Long[] ids , HttpServletResponse rsp) throws Exception
    {
        JsonResult<String> json = dictService.openSessiondelete(ids);
        outJson(json, rsp);
    }

    @Resource
    @Required
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }

    @Resource
    @Required
    public void setDictService(IDictService dictService)
    {
        this.dictService = dictService;
    }

}
