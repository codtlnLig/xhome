
package net.swa.file.web.action;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import net.swa.file.beans.entity.Attachment;
import net.swa.system.service.ICommonService;
import net.swa.system.web.action.AbstractBaseAction;
import net.swa.util.ConfigUtil;
import net.swa.util.FileUtil;
import net.swa.util.JsonResult;

/***
 * 上传图片裁剪action
 * @author junyu
 *
 */
@Controller
@RequestMapping("/jcrop")
public class JcropImgAction extends AbstractBaseAction
{
    private static final long serialVersionUID = -3566256767528230895L;

    private Logger log = Logger.getLogger(JcropImgAction.class);

    /** 公用方法接口 */
    private ICommonService commonService;

    /**存放上传文件的目录**/
    private String uploadPath;

    /**存放删除文件目录*/
    private String gabagePath;

    /**文件服务器路径**/
    private String httpPath;

    public JcropImgAction()
    {
        uploadPath = ConfigUtil.getProperty("uploadPath");
        gabagePath = ConfigUtil.getProperty("gabagePath");
        httpPath = ConfigUtil.getProperty("httpPath");
        log.debug("gabagePath is no use :"+gabagePath);
    }

    /***
     * 上传原图像并返回图片服务路径和位置
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadImg")
    public void uploadImg(String folder , Integer limitwidth , Integer limitheight , String fileFileName , HttpServletRequest request , HttpServletResponse rsp) throws Exception
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");

        fileFileName = file.getOriginalFilename();
        JsonResult<Object> json = new JsonResult<Object>();
        String path = uploadPath; //部署时候可配置
        String imgUrl = httpPath + folder + "/" + "resource/";
        File f = new File(path + File.separator + folder);
        if (!f.exists())
        {
            f.mkdirs();
        }
        File target = new File(path + File.separator + folder + File.separator + "resource");//默认存放元图片路径
        if (!target.exists())
        {
            target.mkdirs();
        }
        String sourceName = fileFileName;
        String ext = FileUtil.getExtension(fileFileName);
        fileFileName = UUID.randomUUID().toString() + "." + ext;

        if (!(file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())))
        {
            List<String> fileTypes = new ArrayList<String>();
            fileTypes.add("jpg");
            fileTypes.add("jpeg");
            fileTypes.add("bmp");
            fileTypes.add("gif");
            fileTypes.add("png");
            if (fileTypes.contains(ext))
            {
                File desc = new File(target.getAbsoluteFile() + File.separator + fileFileName);
                try
                {
                    file.transferTo(desc); //保存上传的文件

                    if (validateFile(desc, limitwidth, limitheight))
                    {

                        json.setSuccess(true);
                        //保存到附件表
                        //                            Attachment attachment = new Attachment();
                        //                            attachment.setName(sourceName);
                        //                            attachment.setUuid(imgUrl + fileFileName);//此处也加路径？？
                        //                            attachment.setExt(ext);
                        //                            attachment.setPath(desc.getAbsolutePath());
                        //                            commonService.commonAdd(attachment);
                        json.setAttribute("fileName", imgUrl + fileFileName);//此图片服务器上可以直接取？？
                        json.setAttribute("fileSourceName", path + folder + "/resource/" + fileFileName);//此图片服务器上可以直接取？？
                        json.setAttribute("sourceName", sourceName);
                    }
                    else
                    {
                        desc.delete();
                        log.debug("上传的源图片长宽不符合要求");
                        json.setSuccess(false);
                        json.setMessage("上传的源图片长宽不符合要求");
                    }

                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                    json.setSuccess(false);
                    json.setMessage("出现异常" + e);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    json.setSuccess(false);
                    json.setMessage("出现异常" + e);
                }
            }
            else
            {
                log.debug("上传的源图片文件格式不符合要求");
                json.setSuccess(false);
                json.setMessage("附件文件格式不符合要求");
            }
        }
        else
        {
            log.debug("上传的源图片文件error");
            json.setSuccess(false);
            json.setMessage("附件文件error");
        }
        outJson(json, rsp);
    }

    /**
     * 裁剪图片返回裁剪后的图片名称
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/cutImg")
    public void cutImg(Double x , Double y , Double w , Double h , Double sw , Double sh , Integer limitwidth , Integer limitheight , String imageUrl , HttpServletResponse rsp) throws Exception
    {
        log.debug("开始剪切图片文件");
        JsonResult<Object> json = new JsonResult<Object>();
        if (!StringUtil.isBlank(imageUrl))
        {
            String ext = FileUtil.getExtension(imageUrl);
            String dir = imageUrl.substring(0, imageUrl.indexOf("resource"));//目标目录
            String fname = imageUrl.substring(imageUrl.indexOf("resource") + 9);//目标目录
            String netPath = imageUrl.replace("resource/", "").replace(uploadPath, httpPath);
            File f = new File(imageUrl.trim());
            FileInputStream is = null;
            ImageInputStream iis = null;
            File mindesc = new File(dir + fname);
            if (!mindesc.exists())
            {
                mindesc.delete();
            }
            try
            {
                // 读取图片文件 
                is = new FileInputStream(f);
                Image img = ImageIO.read(f);
                /* 
                 * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader 
                 * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 .
                 *（例如 "jpeg" 或 "tiff"）等 。 
                */
                String imgType = getImgType(ext);

                Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(imgType);
                ImageReader reader = it.next();
                // 获取图片流  
                iis = ImageIO.createImageInputStream(is);
                /*  
                 * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
                 * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
                 *  避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
                */
                reader.setInput(iis, true);
                /*  
                 * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O 
                 * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
                 * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回 
                 * ImageReadParam 的实例。  
                */
                ImageReadParam param = reader.getDefaultReadParam();
                int rwidth = img.getWidth(null);
                int rheight = img.getHeight(null);

                int newWidth = (int) (rwidth * (w / sw));
                int newHeight = (int) (rheight * (h / sh));

                x = x * rwidth / sw;
                y = y * rheight / sh;
                /* 
                 * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
                 * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。 
                */
                Rectangle rect = new Rectangle(x.intValue(), y.intValue(), newWidth, newHeight);

                // 提供一个 BufferedImage，将其用作解码像素数据的目标。  
                param.setSourceRegion(rect);

                /* 
                 * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
                 * 它作为一个完整的 BufferedImage 返回。
                  */
                BufferedImage bi = reader.read(0, param);

                // 保存新图片  
                ImageIO.write(bi, imgType, mindesc);

                json.setSuccess(true);
                //保存到附件表
                //                Attachment attachment = new Attachment();
                //                attachment.setName(sourceName);
                //                attachment.setUuid(imgUrl + fileFileName);//此处也加路径？？
                //                attachment.setExt(ext);
                //                attachment.setPath(desc.getAbsolutePath());
                //                commonService.commonAdd(attachment);
                json.setAttribute("fileName", netPath);//此图片服务器上可以直接取？？
            }
            finally
            {
                if (is != null)
                    is.close();
                if (iis != null)
                    iis.close();
            }
        }
        else
        {
            log.debug("要剪切图片文件丢失");
            json.setSuccess(false);
            json.setMessage("图片不存在");
        }
        outJson(json, rsp);
    }

    private String getImgType(String ext)
    {
        if (!StringUtil.isBlank(ext))
        {
            if (ext.trim().contains("jpg"))
            {
                log.debug("获取图片格式jpg");
                return "jpg";
            }
            else if (ext.trim().contains("png"))
            {
                log.debug("获取图片格式png");
                return "png";
            }
        }
        log.debug("获取图片格式失败" + ext);
        return ext;
    }

    private boolean validateFile(File file , Integer limitwidth , Integer limitheight)
    {
        Image img;
        try
        {
            img = ImageIO.read(file);
            if (null != limitheight && limitheight != 0)
            {
                if (img.getHeight(null) > limitheight)
                {
                    return false;
                }
            }
            if (null != limitwidth && limitwidth != 0)
            {
                if (img.getWidth(null) > limitwidth)
                {
                    return false;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除文件
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/removeFile")
    public void removeFile(String fileFileName , HttpServletResponse rsp) throws Exception
    {
        //        UploadFile file = commonService.commonFind(UploadFile.class, fileId);
        Attachment file = commonService.findByAttribute(Attachment.class, "uuid", fileFileName);
        if (null != file)
        {
            outSuccess(rsp);
        }
        else
        {
            outError("附件不存在", rsp);
        }
    }

    public ICommonService getCommonService()
    {
        return commonService;
    }

    @Required
    @Resource
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }
}
