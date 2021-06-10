package net.swa.util;

import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class ReadZipFile
{
	/**
	 * 遍历zip中的内容
	 * 
	 * @param zipPathName
	 * @throws Exception
	 */
	public static void listJarFile(String zipPathName) throws Exception
	{
		ZipFile zf = new ZipFile("d:/Archive.zip", "gbk");
		System.out.println(zf.getEncoding());
		@SuppressWarnings("unchecked")
		Enumeration<ZipArchiveEntry> it = (Enumeration<ZipArchiveEntry>) zf.getEntries();
		while (it.hasMoreElements())
		{
			ZipArchiveEntry ze = it.nextElement();
			System.out.println("-" + ze.getName());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
	}

}
