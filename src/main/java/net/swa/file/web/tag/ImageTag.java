
package net.swa.file.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

public class ImageTag extends TagSupport
{

    private static final long serialVersionUID = 4969976335926608773L;

    private String name;

    private String minName;

    private String fileName;

    private String minFileName;

    private Integer width;

    private Integer height;

    private String ext;

    private String title;

    private String viewTag;

    /** 上传图片的父文件夹 **/
    private String folder;

    private long size;

    public int doStartTag() throws JspException
    {
        try
        {
            JspWriter out = this.pageContext.getOut();
            StringBuilder html = new StringBuilder();
            long id = RandomUtils.nextLong();
            html.append("<input type=\"file\" name=\"file\" fname='f_" + name + "' id='f_" + id
                    + "' onchange=\"upload(this,'" + name + "','" + viewTag + "'," + width + "," + height + ",'" + ext
                    + "',false,'" + folder + "','" + minName + "')\" title='" + title + "'>");
            if (!StringUtils.isBlank(fileName))
            {
                fileName = fileName.trim();
                html.append(getFileItem(fileName, minFileName));
            }

            out.println(html);
        }
        catch (Exception e)
        {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    private String getFileItem(String fileName , String minFileName)
    {
        long id = RandomUtils.nextLong();
        String fileId = name.substring(name.indexOf(".") + 1, name.length());
        StringBuilder html = new StringBuilder();
        html.append("<div class='fileitemx' id='file_" + id + "'>");
        html.append("<input type='hidden' id='" + fileId + "' name='" + name + "' value='" + fileName+ "'/><input type='hidden' id='" + fileId + "min' name='" + minName + "' value='" + minFileName + "'/>");

        html.append("<a id='a_" + id + "' class='fancybox' onclick=\"viewImage('" + fileName + "')\">点击预览</a>");
        html.append("<a class='right' aname='a_" + name + "' txt='manager.photo' pid='file_" + id
                + "' onclick=\"clearFile(this,'" + folder + "','" + fileName + "')\">删除</a>");
        html.append("</div>");
        return html.toString();
    }

    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }

    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public String getExt()
    {
        return ext;
    }

    public void setExt(String ext)
    {
        this.ext = ext;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getViewTag()
    {
        return viewTag;
    }

    public void setViewTag(String viewTag)
    {
        this.viewTag = viewTag;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public String getFolder()
    {
        return folder;
    }

    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    public String getMinName()
    {
        return minName;
    }

    public void setMinName(String minName)
    {
        this.minName = minName;
    }

    public String getMinFileName()
    {
        return minFileName;
    }

    public void setMinFileName(String minFileName)
    {
        this.minFileName = minFileName;
    }

}