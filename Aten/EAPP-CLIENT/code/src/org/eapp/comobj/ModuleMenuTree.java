package org.eapp.comobj;

import java.util.TreeMap;

/**
 * 模块的排序树模型
 * @author zsy
 * @version
 */
public class ModuleMenuTree implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8202672317657934469L;
	//当前结点的模块对象
	private ModuleMenu moduleMenu;
	//子结点的Map
	private TreeMap<ModuleMenu, ModuleMenuTree> subModuleMenu = new TreeMap<ModuleMenu, ModuleMenuTree>();
	
	public ModuleMenuTree(ModuleMenu moduleMenu) {
		this.moduleMenu = moduleMenu;
	}
	
	/**
	 * 创建根结点
	 * @return
	 */
	public static ModuleMenuTree createRoot() {
		return new ModuleMenuTree(new ModuleMenu());
	}
	/**
	 * @return the module
	 */
	public ModuleMenu getModuleMenu() {
		return moduleMenu;
	}

	/**
	 * @return the subModule
	 */
	public TreeMap<ModuleMenu, ModuleMenuTree> getSubModuleMenu() {
		return subModuleMenu;
	}

	/**
	 * ModuleMenuTree对像会被返回到hessian客户端，所以对Module进一步封装
	 * @author zsy
	 * @version
	 */
	public static class ModuleMenu implements java.io.Serializable, Comparable<ModuleMenu> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7668951158462849400L;
		private String moduleID;
		private String moduleKey;
		private String name;
		private String url;
		private Integer displayOrder;
		public ModuleMenu() {
			
		}
		
		public ModuleMenu(String moduleID, String moduleKey, String name, String url, Integer displayOrder) {
			this.moduleID = moduleID;
			this.moduleKey = moduleKey;
			this.name = name;
			this.url = url;
			this.displayOrder = displayOrder;
		}
		public String getModuleID() {
			return moduleID;
		}
		public void setModuleID(String moduleID) {
			this.moduleID = moduleID;
		}
		public String getModuleKey() {
			return moduleKey;
		}
		public void setModuleKey(String moduleKey) {
			this.moduleKey = moduleKey;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		public Integer getDisplayOrder() {
			return displayOrder;
		}

		public void setDisplayOrder(Integer displayOrder) {
			this.displayOrder = displayOrder;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((moduleID == null) ? 0 : moduleID.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final ModuleMenu other = (ModuleMenu) obj;
			if (moduleID == null) {
				if (other.moduleID != null)
					return false;
			} else if (!moduleID.equals(other.moduleID))
				return false;
			return true;
		}

		public int compareTo(ModuleMenu module) {
			if (this.equals(module)) {
				return 0;
			}
			if (getDisplayOrder() > module.getDisplayOrder()) {
				return 1;
//			} else if (getDisplayOrder() == module.getDisplayOrder()) {
//				return 0;
			} else {
				return -1;
			}
		}
	}
}
