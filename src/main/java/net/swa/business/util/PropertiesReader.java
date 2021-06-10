
package net.swa.business.util;

import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class PropertiesReader
{
    private String fileName;

    public PropertiesReader(String fileName)
    {
        this.fileName = fileName;
    }

    public String readProperty(String name)
    {
        ClassPathResource res = new ClassPathResource(fileName);
        Properties p = new Properties();
        try
        {
            p.load(res.getInputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return p.getProperty(name);

    }

}
