
package net.swa.file.web.action;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import net.swa.file.beans.entity.Attachment;
import net.swa.index.util.ConstIndex;
import net.swa.system.beans.entity.User;
import net.swa.system.service.ICommonService;
import net.swa.system.web.action.AbstractBaseAction;
import net.swa.util.ConfigUtil;
import net.swa.util.Const;
import net.swa.util.EncryptTool;
import net.swa.util.FileUtil;
import net.swa.util.JsonResult;

@Controller
@RequestMapping("/file")
public class FileAction extends AbstractBaseAction
{
    private static final long serialVersionUID = -3566256767528230895L;

    private final Logger log = Logger.getLogger(FileAction.class);

    /**存放上传文件的目录**/
    private String uploadPath;

    /**存放删除文件目录*/
    private String gabagePath;

    /**文件服务器路径**/
    private String httpPath;

    private ICommonService commonService;

    public FileAction()
    {
        uploadPath = ConfigUtil.getProperty("uploadPath");
        gabagePath = ConfigUtil.getProperty("gabagePath");
        httpPath = ConfigUtil.getProperty("httpPath");
    }

    /**
     * 删除文件
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/removeFile")
    public void removeFile(boolean delete , String fileFileName , HttpServletResponse rsp) throws Exception
    {
        //        UploadFile file = commonService.commonFind(UploadFile.class, fileId);
        Attachment file = commonService.findByAttribute(Attachment.class, "uuid", fileFileName);
        if (null != file)
        {
            if (delete)
            {
                commonService.commonDelete("Attachment", file.getId());
                String fname = fileFileName.replace(httpPath, "").replace("/", "");
                String path = fileFileName.replace(httpPath, uploadPath); //部署时候可配置
                File target = new File(path);
                File gcFile = new File(gabagePath);
                if (!gcFile.exists())
                {
                    gcFile.mkdirs();
                }
                //将文件移到新文件里 
                File fnew = new File(gabagePath + File.separator + fname);
                target.renameTo(fnew);
                target.delete();
            }
            outSuccess(rsp);
        }
        else
        {
            outError("附件不存在", rsp);
        }
    }

    @RequestMapping(value = "/download")
    public String download(String fileFileName , HttpServletResponse response) throws Exception
    {
        String path = fileFileName.replace(httpPath, uploadPath); //部署时候可配置
        File target = new File(path);
        InputStream in = new FileInputStream(target);
        response.setHeader("Content-disposition", "attachment;filename=" + fileFileName);
        response.setIntHeader("Content-Length", in.available());
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try
        {
            bos = new BufferedOutputStream(response.getOutputStream());
            bis = new BufferedInputStream(in);
            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b)) > 0)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            bos.close();
            bis.close();
        }
        return null;
    }

    /**
     * 上传文件
     * 并且保存附件表信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/upload")
    public void upload(String folder , Integer size , HttpServletRequest request , HttpServletRequest req , HttpServletResponse response , HttpSession session) throws Exception
    {
        if (StringUtils.isBlank(folder) || "null".equals(folder))
        {
            folder = "remoteFile";
        }
        JsonResult<Object> json = new JsonResult<Object>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        if (null == file)
        {
            json.setSuccess(false);
            json.setMessage("附件不存在");
        }
        else
        {
            String fileFileName = file.getOriginalFilename();

            boolean mat = true;
            User u = (User) session.getAttribute(Const.CURRENT_USER);
            if (null == u)
            {
                log.debug("session 为空，应该是session过期，或移动端调用");
                mat = false;
                String mac = req.getParameter("mac");
                String token = req.getParameter("token");

                if (!StringUtils.isBlank(token))
                {
                    if (StringUtils.isBlank(mac))
                    {
                        mac = ConstIndex.DEFAULT_MAC;
                    }
                    EncryptTool tool;
                    tool = new EncryptTool(mac);
                    String sid = tool.decrypt(token);
                    mat = sid.matches("\\d+");
                    log.debug("移动端校验结果：" + mat);
                }
            }
            if (mat)
            {
                String path = uploadPath; //部署时候可配置
                String imgUrl = httpPath + folder + "/";
                File target = new File(path + File.separator + folder);
                if (!target.exists())
                {
                    target.mkdirs();
                }
                String sourceName = fileFileName;
                String ext = FileUtil.getExtension(fileFileName);
                if (!StringUtils.isBlank(ext))
                {
                    ext = "." + ext;
                }
                fileFileName = UUID.randomUUID().toString();
                File desc = new File(target.getAbsoluteFile() + File.separator + fileFileName);
                //                if (!(file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())))
                //                {
                //                    List<String> fileTypes = new ArrayList<String>();
                //                    fileTypes.add("jpg");
                //                    fileTypes.add("jpeg");
                //                    fileTypes.add("bmp");
                //                    fileTypes.add("gif");
                //                    fileTypes.add("png");
                //                    fileTypes.add("apk");
                //                    fileTypes.add("APK");
                //                    fileTypes.add("ios");
                //                    if (true)
                //                    {
                file.transferTo(desc); //保存上传的文件
                json.setSuccess(true);
                //保存到附件表
                Attachment attachment = new Attachment();
                attachment.setName(sourceName);
                attachment.setUuid(imgUrl + fileFileName);//此处也加路径？？
                attachment.setExt(ext);
                attachment.setPath(desc.getAbsolutePath());
                commonService.commonAdd(attachment);
                json.setAttribute("fileName", imgUrl + fileFileName);//此图片服务器上可以直接取？？
                // json.setAttribute("minfileName", imgUrl + "min/" + fileFileName);//此图片服务器上可以直接取？？
                json.setAttribute("sourceName", sourceName);

                //                    }
                //                    else
                //                    {
                //                        json.setSuccess(false);
                //                        json.setMessage("附件格式不符合要求" + ext);
                //                    }
                //                }
                //                else
                //                {
                //                    json.setSuccess(false);
                //                    json.setMessage("附件格式不符合要求,名称为空");
                //                }
            }
            else
            {
                json.setSuccess(false);
                json.setMessage("token 校验失败");
            }
        }
        outJson(json, response);
    }

    /**
     * 上传文件
     * 并且保存附件表信息
     * @return
     * @throws Exception
     */
    //    @RequestMapping(value = "/upload")
    //    public void upload(String folder , Integer width , Integer height , Double outputWidth , Double outputHeight , Integer size , HttpServletRequest request , HttpServletRequest req , HttpServletResponse response , HttpSession session) throws Exception
    //    {
    //        if (null == outputWidth || null == outputHeight)
    //        {
    //            outputHeight = 120d;
    //            outputWidth = 120d;
    //        }
    //        if (!StringUtils.isBlank(folder))
    //        {
    //            folder = "remoteFile";
    //        }
    //        JsonResult<Object> json = new JsonResult<Object>();
    //        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    //        MultipartFile file = multipartRequest.getFile("file");
    //
    //        if (null == file)
    //        {
    //            json.setSuccess(false);
    //            json.setMessage("附件不存在");
    //        }
    //        else
    //        {
    //            String fileFileName = file.getOriginalFilename();
    //
    //            boolean mat = true;
    //            User u = (User) session.getAttribute(Const.CURRENT_USER);
    //            if (null == u)
    //            {
    //                log.debug("session 为空，应该是session过期，或移动端调用");
    //                mat = false;
    //                String mac = req.getParameter("mac");
    //                String token = req.getParameter("token");
    //
    //                if (!StringUtils.isBlank(token))
    //                {
    //                    if (StringUtils.isBlank(mac))
    //                    {
    //                        mac = ConstIndex.DEFAULT_MAC;
    //                    }
    //                    EncryptTool tool;
    //                    tool = new EncryptTool(mac);
    //                    String sid = tool.decrypt(token);
    //                    mat = sid.matches("\\d+");
    //                    log.debug("移动端校验结果：" + mat);
    //                }
    //            }
    //            if (mat)
    //            {
    //                String path = uploadPath; //部署时候可配置
    //                String imgUrl = httpPath + folder + "/";
    //                File target = new File(path + File.separator + folder);
    //                if (!target.exists())
    //                {
    //                    target.mkdirs();
    //                }
    //                File mintarget = new File(path + File.separator + folder + "/min/");
    //                if (!mintarget.exists())
    //                {
    //                    mintarget.mkdirs();
    //                }
    //
    //                String sourceName = fileFileName;
    //                String ext = FileUtil.getExtension(fileFileName);
    //                fileFileName = UUID.randomUUID().toString() + "." + ext;
    //                File desc = new File(target.getAbsoluteFile() + File.separator + fileFileName);
    //                if (!(file.getOriginalFilename() == null || "".equals(file.getOriginalFilename())))
    //                {
    //                    List<String> fileTypes = new ArrayList<String>();
    //                    fileTypes.add("jpg");
    //                    fileTypes.add("jpeg");
    //                    fileTypes.add("bmp");
    //                    fileTypes.add("gif");
    //                    fileTypes.add("png");
    //                    fileTypes.add("apk");
    //                    fileTypes.add("APK");
    //                    fileTypes.add("ios");
    //                    if (true)
    //                    {
    //                        file.transferTo(desc); //保存上传的文件
    //                        if (!(validateImage(width, height, desc)))
    //                        {
    //                            json.setSuccess(false);
    //                            json.setMessage("图片尺寸不符合要求");
    //                            desc.delete();
    //                        }
    //                        else
    //                        {
    //                            json.setSuccess(true);
    //                            //保存到附件表
    //                            Attachment attachment = new Attachment();
    //                            attachment.setName(sourceName);
    //                            attachment.setUuid(imgUrl + fileFileName);//此处也加路径？？
    //                            attachment.setExt(ext);
    //                            attachment.setPath(desc.getAbsolutePath());
    //                            commonService.commonAdd(attachment);
    //                            json.setAttribute("fileName", imgUrl + fileFileName);//此图片服务器上可以直接取？？
    //                            // json.setAttribute("minfileName", imgUrl + "min/" + fileFileName);//此图片服务器上可以直接取？？
    //                            json.setAttribute("sourceName", sourceName);
    //                        }
    //                    }
    //                    else
    //                    {
    //                        json.setSuccess(false);
    //                        json.setMessage("附件格式不符合要求" + ext);
    //                    }
    //                }
    //                else
    //                {
    //                    json.setSuccess(false);
    //                    json.setMessage("附件格式不符合要求,名称为空");
    //                }
    //            }
    //            else
    //            {
    //                json.setSuccess(false);
    //                json.setMessage("token 校验失败");
    //            }
    //        }
    //        outJson(json, response);
    //    }

    /**
     * 校验图片规格
     * @return
     * @throws Exception
     */
    private boolean validateImage(Integer width , Integer height , File file) throws Exception
    {
        if ((null != width && width != 0) && (null != height && height != 0))
        {
            FileInputStream in = new FileInputStream(file);
            BufferedImage img = ImageIO.read(in);
            return ((img.getWidth() <= width) || (img.getHeight() <= height));
        }
        return true;
    }

    @RequestMapping(value = "/viewImg")
    public void viewImg(String imgUrl , HttpServletResponse response)
    {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try
        {
            URL url = new URL(imgUrl);
            URLConnection con = url.openConnection();
            int index = imgUrl.indexOf("/", 10);
            con.setRequestProperty("Host", index == -1 ? imgUrl.substring(7) : imgUrl.substring(7, index));
            con.setRequestProperty("Referer", null);
            InputStream is = con.getInputStream();
            BufferedImage image = ImageIO.read(is);
            ImageIO.write(image, "png", response.getOutputStream());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Resource
    @Required
    public void setCommonService(ICommonService commonService)
    {
        this.commonService = commonService;
    }

    public ICommonService getCommonService()
    {
        return commonService;
    }

}
