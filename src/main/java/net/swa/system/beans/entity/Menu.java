
package net.swa.system.beans.entity;

import java.util.List;

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

/**
 * 功能菜单实体类
 * 
 * 
 */
@Entity
@Table(name = "sys_menu")
public class Menu implements Comparable<Menu>
{
    /**
     * 主键
     */
    private Long id;

    /**
     * 菜单名
     */
    private String title;

    /**
     * 父菜单对象
     */
    private Menu parent;

    /**
     * 菜单事件
     */
    private String event;

    /**
     * 菜单权值
     */
    private int weight;

    /**
     * 子菜单集合,不做hibernate映射
     */
    private List<Menu> subMenus;

    private boolean checked;

    // 主键 ：@Id 主键生成方式：
    // 映射表中id这个字段，不能为空，并且是唯一的
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

    @Transient
    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    @Column(name = "title", length = 50)
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    // 不延迟加载：多对一方式
    // 关联信息：外键name = "category_id"
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentid")
    public Menu getParent()
    {
        return parent;
    }

    public void setParent(Menu parent)
    {
        this.parent = parent;
    }

    @Column(name = "event", length = 100)
    public String getEvent()
    {
        return event;
    }

    public void setEvent(String event)
    {
        this.event = event;
    }

    @Column(name = "weight")
    public int getWeight()
    {
        return weight;
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    @Transient
    public List<Menu> getSubMenus()
    {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus)
    {
        this.subMenus = subMenus;
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        Menu menu = (Menu) obj;
        return id == menu.getId();
    }

    public int compareTo(Menu o)
    {
        if (o == null)
        {
            return 0;
        }
        int val = weight - o.getWeight();
        if (val > 0)
        {
            return 1;
        }
        if (val < 0)
        {
            return -1;
        }
        return 0;
    }

    @Transient
    public String toXml()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<item text='" + title + "' id='" + id + "'  im0='tombs.gif' im1='tombs.gif' im2='iconSafe.gif' " + (checked ? "checked='1'" : "") + ">\n");
        if (subMenus != null)
        {
            for (Menu menu : subMenus)
            {
                buffer.append(menu.toXml());
            }
        }
        buffer.append("</item>");
        return buffer.toString();
    }

}
