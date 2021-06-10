
package net.swa.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * IO工具类
 * [一句话功能简述]<p>
 * [功能详细描述]<p>
 */
public class IOUitl
{
    /**
    * Logger for this class
    */
    private static final Logger log = Logger.getLogger(IOUitl.class);

    /**
     * 拷贝文件
     * @param srcFileName 带路径的源文件名
     * @param destFileName 带路径的目标文件名
     * @param replace 是否替换，如果true，则替换，否则不替换
     * @throws IOException IO异常
     * @throws SysException 系统异常
     */
    public static void copy(String srcFileName , String destFileName , boolean replace) throws Exception
    {
        if ((null == srcFileName) || (0 == srcFileName.trim().length()) || (null == destFileName)
                || (0 == destFileName.trim().length()))
        {
            //抛出文件名为空的系统异常
            throw new Exception();
        }
        File file = new File(srcFileName);
        if (false == file.exists())
        {
            //抛出文件不存在的系统异常
            throw new Exception();
        }
        file = new File(destFileName);
        if ((file.exists()) && (replace))
        {
            file.delete();
        }
        FileInputStream input = null;
        FileOutputStream output = null;
        try
        {
            input = new FileInputStream(srcFileName);
            output = new FileOutputStream(destFileName);
            IOUtils.copyLarge(input, output);
        }
        finally
        {
            closeWithWarnLog(input);
            closeWithWarnLog(output);
        }
    }

    public static void closeWithWarnLog(InputStream input)
    {
        if (null != input)
        {
            try
            {
                input.close();
                input = null;
            }
            catch (IOException ex)
            {
                if (log.isEnabledFor(Level.WARN))
                {
                    log.warn("关闭文件输入流异常：", ex);
                }
            }
        }
    }

    public static void closeWithWarnLog(OutputStream output)
    {
        if (null != output)
        {
            try
            {
                output.close();
                output = null;
            }
            catch (IOException ex)
            {
                if (log.isEnabledFor(Level.WARN))
                {
                    log.warn("关闭文件输出流异常：", ex);
                }
            }
        }
    }
}
