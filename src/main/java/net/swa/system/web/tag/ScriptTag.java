package net.swa.system.web.tag;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import net.swa.util.DateUtils;

public class ScriptTag extends TagSupport {
	
	private static final long serialVersionUID = 7278706016067991155L;
	private String folder;
	public int doStartTag() throws JspException {
		String str=pageContext.getServletContext().getRealPath(folder);
		File dir=new File(str);
		File[] list=dir.listFiles();
		JspWriter out = this.pageContext.getOut();
		String s=DateUtils.dateToString(new Date(),"yyyy-MM-dd");//默认重新加载
		try {
		for(File f:list){
			out.println("<script type='text/javascript' src='"+folder+"/"+f.getName()+"?v="+s+"'></script>");
		}
		} catch (IOException e) {
		}
		return super.doStartTag();
	}

	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		return super.doEndTag();
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
 
}
