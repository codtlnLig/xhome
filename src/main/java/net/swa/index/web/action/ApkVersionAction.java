
package net.swa.index.web.action;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import net.swa.index.beans.entity.ApkVersion;
import net.swa.index.service.ApkVersionService;
import net.swa.system.web.action.AbstractBaseAction;

/**Action**/
@Controller
@RequestMapping(value = "/version")
public class ApkVersionAction extends AbstractBaseAction
{
    private static final long serialVersionUID = -1541842392065452147L;

    private final Logger log = Logger.getLogger(ApkVersionAction.class);

    private ApkVersionService apkVersionService;

    @RequestMapping(value = "/index")
    public ModelAndView index(@ModelAttribute ApkVersion model)
    {
        ModelAndView mv = new ModelAndView("index/index");
        model = apkVersionService.queryLastVersion();
        if (null == model)
        {
            model = new ApkVersion();
        }
        mv.addObject("model", model);
        return mv;
    }

    @RequestMapping(value = "/save")
    public void save(@ModelAttribute ApkVersion model , HttpServletRequest request , HttpServletResponse rsp) throws Exception
    {
        log.debug("保存微信版本信息");
        Map<String, Object> map = apkVersionService.save(model);
        outJson(map, rsp);
    }

    @Required
    @Resource
    public void setApkVersionService(ApkVersionService apkVersionService)
    {
        this.apkVersionService = apkVersionService;
    }
}
