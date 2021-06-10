
package net.swa.file.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

public class SingleJcropTag extends TagSupport
{

    private static final long serialVersionUID = 4969976335926608773L;

    /** 上传图片的父文件夹 **/
    private String folder;

    private String name;

    private String fileName;

    private Integer width;

    private Integer height;

    private Integer minwidth;

    private Integer minheight;

    private Integer limitwidth;

    private Integer limitheight;

    public int doStartTag() throws JspException
    {
        try
        {
            JspWriter out = this.pageContext.getOut();
            StringBuilder html = new StringBuilder();
            html.append("<div id='imgCutPane'>");
            if (!StringUtils.isBlank(fileName))
            {
                html.append("<div style='width:" + minwidth + "px;height:" + minheight + "px;display:inline-block;  border: 1px rgba(0,0,0,.4) solid;background-color: white;-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;-webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);-moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);'> ");
                html.append("<img id='minImg' src=\'" + fileName + "\' style='width:" + minwidth + "px;height:" + minheight + "px;display:inline-block;' /></div>");
                html.append("</div><input type='hidden' name='" + name + "' value='" + fileName + "'  id='model_imageUrl'/>");
            }
            else
            {
                html.append("</div><input type='hidden' name='" + name + "'  id='model_imageUrl'/>");
            }
            html.append("<input type='file' name='file' id='loadFile' onchange=\"uploadPreview(this,'" + folder + "','model_imageUrl'," + width + "," + height + "," + minwidth + "," + minheight + "," + limitwidth + "," + limitheight + ",\'uploadSpan\');\" value='选择图片'/>");

            if (!StringUtils.isBlank(fileName))
            {
                html.append("<span id='uploadSpan'><input type='button' value='删除'  style='width: 90px;' onclick=\"deleteJcropImg(this,'imgCutPane','model_imageUrl');\" /></span>");
            }
            else
            {
                html.append("<span id='uploadSpan'></span>");
            }
            out.println(html);
        }
        catch (Exception e)
        {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
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

    public String getFolder()
    {
        return folder;
    }

    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    public Integer getMinwidth()
    {
        return minwidth;
    }

    public void setMinwidth(Integer minwidth)
    {
        this.minwidth = minwidth;
    }

    public Integer getMinheight()
    {
        return minheight;
    }

    public void setMinheight(Integer minheight)
    {
        this.minheight = minheight;
    }

    public Integer getLimitwidth()
    {
        return limitwidth;
    }

    public void setLimitwidth(Integer limitwidth)
    {
        this.limitwidth = limitwidth;
    }

    public Integer getLimitheight()
    {
        return limitheight;
    }

    public void setLimitheight(Integer limitheight)
    {
        this.limitheight = limitheight;
    }

}
