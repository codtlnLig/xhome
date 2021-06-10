
package net.swa.file.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

public class MultiJcropTag extends TagSupport
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
            html.append("<div style='display: block;float: left;width: 100%;'>");
            if (!StringUtils.isBlank(fileName))
            {
                html.append("<ul id='list2' style='margin:0px; padding:0px; margin-left:20px;width:100%; list-style-type:none; margin:0px;'>");
                String[] s = fileName.split(",");
                for (int i = 0; i < s.length; i++)
                {
                    if (!StringUtils.isBlank(s[i]))
                    {
                        html.append("<li style='float:left; padding:5px; width:"+(minwidth+20)+"px;height:"+(minheight+20)+"px;'>");
                        html.append("<div style='top: 18px; left: 58px; z-index: 11; display: block;-moz-border-bottom-colors: none;-moz-border-left-colors: none;-moz-border-right-colors: none;-moz-border-top-colors: none;border-color: #F9F9F9;border-image: none;border-radius: 4px;border-style: solid;border-width: 7px 7px 7px; box-shadow: 1px 1px 5px #555555;'>");
                        html.append("<img style='width:"+minwidth+"px;height:"+minheight+"px;display:inline-block; ' src='" + s[i] + "'>");
                        html.append("<input type='hidden' value='" + s[i] + "' name='" + name + "'>");
                        html.append("<span class='delete'  onclick='removeLi(this)'>&nbsp;</span></div></li>");
                    }
                }
            }
            else
            {
                html.append("<ul id='list2' style='display:none;margin:0px; padding:0px; margin-left:20px;width:100%; list-style-type:none; margin:0px;'>");
            }
            html.append("</ul></div>");
            
            html.append("<div style='display: block;float: left;'><div><div id='imgCutPane'></div><input type='hidden'  id='model_imageUrl'/>");
            html.append("<input id='loadFile' type='file' value='选择图片' onchange=\"mulUploadPreview(this,'"+folder+"','model_imageUrl','list2',"+width+","+height+","+minwidth+","+minheight+","+limitwidth+","+limitheight+",'uploadSpan','"+name+"');\" name='file'>");
            html.append("<span id='uploadSpan'></span><span  style='color:red;'> 图片建议尺寸(像素)宽*高:450*310.大小不超过100KB</span>");
            html.append("</div></div>");
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
