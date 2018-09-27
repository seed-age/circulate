package com.sunnsoft.sloa.db.vo;
// Generated by Hibernate Tools 3.4.0.CR1


import java.util.List;

/**
 * Menu generated by hbm2java
 */
public class Menu  implements java.io.Serializable {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*Name:菜单ID
	*Comment:
	*/
     private Long menuId;
     private Menu menu;
	/**
	*Name:菜单名称
	*Comment:
	*/
     private String menuText;
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/
     private String icon;
	/**
	*Name:ACTION路径
	*Comment:
	*/
     private String actionPath;
	/**
	*Name:EXT_ID
	*Comment:
	*/
     private String extId;
	/**
	*Name:布局
	*Comment:
	*/
     private String layout;
	/**
	*Name:是否叶节点
	*Comment:
	*/
     private boolean leaf;
	/**
	*Name:是否展开
	*Comment:
	*/
     private boolean expanded;
	/**
	*Name:排序
	*Comment:
	*/
     private int indexValue;
	/**
	*Name:是否停用
	*Comment:
	*/
     private boolean enabled;
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/
     private String iconCls;
     private List<Role> roles = new ArrayList<Role>(0);
     private List<Menu> menus = new ArrayList<Menu>(0);

    public Menu() {
    }

	
    public Menu(String menuText, boolean leaf, boolean expanded, int indexValue, boolean enabled) {
        this.menuText = menuText;
        this.leaf = leaf;
        this.expanded = expanded;
        this.indexValue = indexValue;
        this.enabled = enabled;
    }
    public Menu(Menu menu, String menuText, String icon, String actionPath, String extId, String layout, boolean leaf, boolean expanded, int indexValue, boolean enabled, String iconCls, List<Role> roles, List<Menu> menus) {
       this.menu = menu;
       this.menuText = menuText;
       this.icon = icon;
       this.actionPath = actionPath;
       this.extId = extId;
       this.layout = layout;
       this.leaf = leaf;
       this.expanded = expanded;
       this.indexValue = indexValue;
       this.enabled = enabled;
       this.iconCls = iconCls;
       this.roles = roles;
       this.menus = menus;
    }
   
	/**
	*Name:菜单ID
	*Comment:
	*/
    public Long getMenuId() {
        return this.menuId;
    }
    
	/**
	*Name:菜单ID
	*Comment:
	*/
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
    public Menu getMenu() {
        return this.menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
	/**
	*Name:菜单名称
	*Comment:
	*/
    public String getMenuText() {
        return this.menuText;
    }
    
	/**
	*Name:菜单名称
	*Comment:
	*/
    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/
    public String getIcon() {
        return this.icon;
    }
    
	/**
	*Name:图标路径
	*Comment:图标class和图标路径只能填写其中一种。
	*/
    public void setIcon(String icon) {
        this.icon = icon;
    }
	/**
	*Name:ACTION路径
	*Comment:
	*/
    public String getActionPath() {
        return this.actionPath;
    }
    
	/**
	*Name:ACTION路径
	*Comment:
	*/
    public void setActionPath(String actionPath) {
        this.actionPath = actionPath;
    }
	/**
	*Name:EXT_ID
	*Comment:
	*/
    public String getExtId() {
        return this.extId;
    }
    
	/**
	*Name:EXT_ID
	*Comment:
	*/
    public void setExtId(String extId) {
        this.extId = extId;
    }
	/**
	*Name:布局
	*Comment:
	*/
    public String getLayout() {
        return this.layout;
    }
    
	/**
	*Name:布局
	*Comment:
	*/
    public void setLayout(String layout) {
        this.layout = layout;
    }
    public boolean isLeaf() {
        return this.leaf;
    }
    
	/**
	*Name:是否叶节点
	*Comment:
	*/
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
    public boolean isExpanded() {
        return this.expanded;
    }
    
	/**
	*Name:是否展开
	*Comment:
	*/
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
	/**
	*Name:排序
	*Comment:
	*/
    public int getIndexValue() {
        return this.indexValue;
    }
    
	/**
	*Name:排序
	*Comment:
	*/
    public void setIndexValue(int indexValue) {
        this.indexValue = indexValue;
    }
    public boolean isEnabled() {
        return this.enabled;
    }
    
	/**
	*Name:是否停用
	*Comment:
	*/
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/
    public String getIconCls() {
        return this.iconCls;
    }
    
	/**
	*Name:图标class
	*Comment:图标class和图标路径只能填写其中一种。
	*/
    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }
    public List<Role> getRoles() {
        return this.roles;
    }
    
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    public List<Menu> getMenus() {
        return this.menus;
    }
    
    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }




}


