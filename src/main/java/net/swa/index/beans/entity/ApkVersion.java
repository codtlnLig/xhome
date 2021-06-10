
package net.swa.index.beans.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * APK实体类
 * @author dawei
 *
 */
@Entity
@Table(name = "file_apk_version")
public class ApkVersion
{
    //PK
    private Long id;

    private String name;

    private String verCode;

    private String verName;

    private String packageName;

    private String verPath;

    private String detail;

    private List<String> feature;

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVerCode()
    {
        return verCode;
    }

    public void setVerCode(String verCode)
    {
        this.verCode = verCode;
    }

    public String getVerName()
    {
        return verName;
    }

    public void setVerName(String verName)
    {
        this.verName = verName;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getVerPath()
    {
        return verPath;
    }

    public void setVerPath(String verPath)
    {
        this.verPath = verPath;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public void setFeature(List<String> feature)
    {
        this.feature = feature;
    }

    @Transient
    public List<String> getFeature()
    {
        return feature;
    }

}
