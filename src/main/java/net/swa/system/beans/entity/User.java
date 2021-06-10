
package net.swa.system.beans.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sys_user")
public class User
{
    /** 主键 */
    private long id;

    /** 登录名 */
    private String loginName;

    /** 密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 用户角色 */
    private Role role;

    /** 用户状态 */
    private int status;

    private int weight;

    /** 注册时间 */
    private String regDate;

    /** 邮箱 */
    private String email;

    /** 登录日期 */
    private String loginDate;

    /**用户类型：0管理员，1是服务站*/
    private Short userType;

    //    /**省**/
    //    private String province;
    //
    //    /**市*/
    //    private String city;
    //
    //    /**县**/
    //    private String town;

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Column(name = "userid", unique = true, length = 100)
    public String getLoginName()
    {
        return loginName;
    }

    @Column(name = "pwd", length = 100)
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    @Column(name = "realName")
    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleid")
    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    @Transient
    public String toXml()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<item text='" + realName + "' id='" + id + "'  im0='user.png' im1='user.png' im2='user.png'  >");
        buffer.append("</item>");
        return buffer.toString();
    }

    public String getRegDate()
    {
        return regDate;
    }

    public void setRegDate(String regDate)
    {
        this.regDate = regDate;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(String loginDate)
    {
        this.loginDate = loginDate;
    }

    public Short getUserType()
    {
        return userType;
    }

    public void setUserType(Short userType)
    {
        this.userType = userType;
    }
}
