package com.sunnsoft.sloa.actions.system.roles;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Menu;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.util.struts2.Results;
import org.gteam.util.FastJSONUtils;

import java.util.*;

@SuppressWarnings("unchecked")
public class LoadRoleMenusData extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long roleId;
	private MenuService menuService;
	
	private String json;
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public MenuService getMenuService() {
		return menuService;
	}
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
	@Override
	public String execute() throws Exception {
		MenuHelper helper = this.menuService.createHelper();
		List<Menu> selectedMenus = helper.enterRoles().getRoleId().Eq(roleId).back2Menus().list();
		List<Menu> menus = helper.newOne().getMenu().IsNull().getIndexValue().Asc().list();
		List<Map> beanList = process(selectedMenus, menus);
 		json = FastJSONUtils.getJsonHelper().toJSONString(beanList, true);
		return Results.GLOBAL_JSON;
	}
	
	private List<Map> process(List<Menu> selectedMenus, List<Menu> menus) {
		List<Map> beanList = new ArrayList<Map>();
        for (Iterator<Menu> iterator = menus.iterator(); iterator.hasNext();) {
			Menu menu = iterator.next();
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("text", menu.getMenuText());
			if(menu.isEnabled()){
				map.put("iconCls", "icon-accept");
			}else{
				map.put("iconCls", "icon-cancel");
			}
			List<Menu> subMenus = menu.getMenus();
			if(subMenus.size()>0){
				map.put("leaf", false);
				map.put("expanded", true);
				map.put("children", process(selectedMenus,subMenus));
			}else{
				map.put("leaf", true);
			}
			if(selectedMenus.contains(menu)){
				map.put("checked", false);
			}else{
				map.put("checked", false);
			}
			map.put("id", menu.getMenuId());
			beanList.add(map);
		}
		return beanList;
	}
	

}
