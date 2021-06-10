
package net.swa.index.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 会员信息
 * @author junyu
 *
 */
@Entity
@Table(name = "home_members")
public class Members
{
    //PK
    private Long id;

    /**
     * 图片的url
     */
    private String imgUrl;

    private String uuid;

    private String nickName;

    private String email;

    private String mobi;

    //手机注册时候的验证码
    private String code;

    private String password;

    private String mac;

    private String createTime;

    private String loginTime;

    private Boolean locked;

    private String lockedTime;

    private Integer errorTimes;

    /**是否首次登陆，创建后默认true**/
    private Boolean firstLogin;

    //注册类型名称，1：email,2:mobi
    private String loginType;

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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(String loginTime)
    {
        this.loginTime = loginTime;
    }

    public Boolean getLocked()
    {
        return locked;
    }

    public void setLocked(Boolean locked)
    {
        this.locked = locked;
    }

    public String getLockedTime()
    {
        return lockedTime;
    }

    public void setLockedTime(String lockedTime)
    {
        this.lockedTime = lockedTime;
    }

    public Integer getErrorTimes()
    {
        return errorTimes;
    }

    public void setErrorTimes(Integer errorTimes)
    {
        this.errorTimes = errorTimes;
    }

    public String getMac()
    {
        return mac;
    }

    public void setMac(String mac)
    {
        this.mac = mac;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public Boolean getFirstLogin()
    {
        return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin)
    {
        this.firstLogin = firstLogin;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getMobi()
    {
        return mobi;
    }

    public void setMobi(String mobi)
    {
        this.mobi = mobi;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getLoginType()
    {
        return loginType;
    }

    public void setLoginType(String loginType)
    {
        this.loginType = loginType;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString()
    {
        return "Members [code=" + code + ", createTime=" + createTime + ", email=" + email + ", errorTimes=" + errorTimes + ", firstLogin=" + firstLogin + ", id=" + id + ", imgUrl=" + imgUrl + ", locked=" + locked + ", lockedTime=" + lockedTime + ", loginTime=" + loginTime + ", loginType=" + loginType + ", mac=" + mac + ", mobi=" + mobi + ", nickName=" + nickName + ", password=" + password + ", uuid=" + uuid + "]";
    }

}
