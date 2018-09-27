package com.sunnsoft.sloa.actions.system.menus;

import com.opensymphony.xwork2.ActionSupport;
import com.sunnsoft.sloa.db.vo.Menu;
import com.sunnsoft.sloa.helper.MenuHelper;
import com.sunnsoft.sloa.service.MenuService;
import com.sunnsoft.sloa.util.UserUtils;
import com.sunnsoft.util.struts2.Results;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class MoveMenu extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int indexValue;//所在新的父类菜单的位置
	private long newParentId;//新的父类菜单ID
	private long oldParentId;//旧的父类的ID，有可能等于新的父类ID
	private long nodeId;//被拖动的节点本身的id
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private MenuService menuService;

	public int getIndexValue() {
		return indexValue;
	}

	public void setIndexValue(int indexValue) {
		this.indexValue = indexValue;
	}

	public long getNewParentId() {
		return newParentId;
	}

	public void setNewParentId(long newParentId) {
		this.newParentId = newParentId;
	}

	public long getOldParentId() {
		return oldParentId;
	}

	public void setOldParentId(long oldParentId) {
		this.oldParentId = oldParentId;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	@Override
	public String execute() throws Exception {
		if(!UserUtils.getCurrentUser().isAdmin()){
			this.msg = "当前用户不是管理员，不能移动菜单";
			return Results.GLOBAL_FAILURE_JSON;
		}
		Menu menu = this.menuService.findById(nodeId);
		if(menu != null){
			if(newParentId != oldParentId ){//表示移动到新的父类下
				if(newParentId != 0){//新父类不是根节点
					Menu newParent = this.menuService.findById(newParentId);
					MenuHelper helper = this.menuService.createHelper();
					
					List<Menu> sibilingMenus = helper.getMenu().Eq(newParent).getIndexValue().Asc().list();//获取新的节点列表。
					menu.setMenu(newParent);//设置新父类,注意设置新父类一定要在获取新父类的节点列表之后，否则，会出现重复数据，因为menu.setMenu(newParent);已经执行更新语句了。
					sibilingMenus.add(indexValue, menu);//把节点塞入指定位置。
					int index = 0;
					for (Iterator iterator = sibilingMenus.iterator(); iterator
							.hasNext();) {
						Menu indexMenu = (Menu) iterator.next();
						indexMenu.setIndexValue(index++);
						this.menuService.update(indexMenu);//重新更新排序。
					}
					//旧列表不用排序，因为indexValue只是按大小作为排序依据，单个indexValue的值是具体位置无关。
					
				}else{
					
					MenuHelper helper = this.menuService.createHelper();
					
					List<Menu> sibilingMenus = helper.getMenu().IsNull().getIndexValue().Asc().list();//获取新的节点列表。
					menu.setMenu(null);
					sibilingMenus.add(indexValue, menu);//把节点塞入指定位置。
					int index = 0;
					for (Iterator iterator = sibilingMenus.iterator(); iterator
							.hasNext();) {
						Menu indexMenu = (Menu) iterator.next();
						indexMenu.setIndexValue(index++);
						this.menuService.update(indexMenu);//重新更新排序。
					}
				}
			}else{//仅仅改变了顺序，并没有改变父类。
				MenuHelper helper = this.menuService.createHelper();
				if(newParentId != 0){//父类不是根节点
					Menu parent = this.menuService.findById(oldParentId);
					List<Menu> sibilingMenus = helper.getMenu().Eq(parent).getIndexValue().Asc().list();//获取新的节点列表。
					for (Iterator iterator = sibilingMenus.iterator(); iterator
						.hasNext();) {
						Menu indexMenu = (Menu) iterator.next();
						if(indexMenu.getMenuId().equals(this.nodeId)){
							iterator.remove(); 
							break;
						}
						
					}
					sibilingMenus.add(indexValue, menu);//把节点塞入指定位置。
					int index = 0;
					for (Iterator iterator = sibilingMenus.iterator(); iterator
					.hasNext();) {
						Menu indexMenu = (Menu) iterator.next();
						indexMenu.setIndexValue(index++);
						this.menuService.update(indexMenu);//重新更新排序。
					}
				}else{
					List<Menu> sibilingMenus = helper.getMenu().IsNull().getIndexValue().Asc().list();//获取新的节点列表。
					for (Iterator iterator = sibilingMenus.iterator(); iterator
						.hasNext();) {
						Menu indexMenu = (Menu) iterator.next();
						if(indexMenu.getMenuId().equals(this.nodeId)){
							iterator.remove(); 
							break;
						}
						
					}
					sibilingMenus.add(indexValue, menu);//把节点塞入指定位置。
					int index = 0;
					for (Iterator iterator = sibilingMenus.iterator(); iterator
					.hasNext();) {
						Menu indexMenu = (Menu) iterator.next();
						indexMenu.setIndexValue(index++);
						this.menuService.update(indexMenu);//重新更新排序。
					}
				}
				
			}
		}
		return Results.GLOBAL_SUCCESS_JSON;
	}
	
	
}
