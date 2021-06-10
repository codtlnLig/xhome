
package net.swa.system.beans.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sys_role")
public class Role
{

    private Long id;

    private String name;

    private String description;

    private List<Menu> menus;

    private List<User> users;

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

    @Column(name = "name", length = 100, unique = true)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_menu", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "menuid") })
    public List<Menu> getMenus()
    {
        return menus;
    }

    public void setMenus(List<Menu> menus)
    {
        this.menus = menus;
    }

    @Column(name = "description", length = 100)
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String toXml()
    {
        StringBuilder buffer = new StringBuilder();
        if (null == id)
        {
            id = 0l;
        }
        buffer.append("<item text='" + name + "' id='" + id + "' im0='user.png' im1='user.png' im2='user.png'  >");
        if (null != users)
        {
            for (User u : users)
            {
                buffer.append(u.toXml());
            }
        }
        buffer.append("</item>");
        return buffer.toString();
    }

    /**
     * @return the users
     */
    @Transient
    public List<User> getUsers()
    {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<User> users)
    {
        this.users = users;
    }

}
