package net.swa.file.beans.entity;

public class Entry {
	private long fid;
	private int id;
	private String name;

	private long size;

	private String ext;

	private boolean dir;
	
	private Entry parent;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public boolean isDir() {
		return dir;
	}

	public void setDir(boolean dir) {
		this.dir = dir;
	}

	public String toXml(){
		StringBuilder html=new StringBuilder();
		
		html.append("<tr id='node-"+id+"' "+(parent==null?"":"class='child-of-node-"+parent.getId()+"'")+">");
		html.append("<td>\n");
		if(!dir){
			html.append("<span class='file'>");
			if(name.indexOf("/")>0){
				name=name.substring(name.lastIndexOf("/")+1,name.length());
			}
			html.append("<a class='fancybox-buttons' data-fancybox-group='button"+fid+"' href='file/viewEntry/"+fid+"/"+id+".png' title='"+name+"'>"+name+"</a> \n");
			html.append("</span>");
		}else{
			String folder=name.substring(0,name.length()-1);
			if(folder.indexOf("/")>0){
				folder=folder.substring(folder.lastIndexOf("/")+1,folder.length());
			}
			html.append("<span class='folder'>"+folder+"</span>\n");
		}
		html.append("</td>\n");
		
		Double val=(size/10.24);
		size=val.intValue();
		
		html.append("<td>"+(dir?"-":((size/100.0)+"KB"))+" </td>\n");
		html.append("<td>"+(dir?"文件夹":ext)+"</td></tr>\n");
	
		return html.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Entry getParent() {
		return parent;
	}

	public void setParent(Entry parent) {
		this.parent = parent;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}
	
}
