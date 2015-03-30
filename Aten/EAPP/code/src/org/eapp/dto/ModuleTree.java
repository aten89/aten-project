/**
 * 
 */
package org.eapp.dto;

import java.io.Serializable;
import java.util.List;

import org.eapp.hbean.Module;
import org.eapp.hbean.SubSystem;


/**
 * @version
 */
public class ModuleTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5731513126107987489L;
	private SubSystem subSystem;
	private List<Module> modules;
//	private String path;
	/**
	 * @param subSystem the subSystem to set
	 */
	public void setSubSystem(SubSystem subSystem) {
		this.subSystem = subSystem;
	}
	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
//		this.path = path;
	}

	@Override
	public String toString() {
//		String url = null;
//		if (StringUtils.isNotBlank(path)) {
//			url = path + "/m/module/moduletree?loadPath=" + path + "&moduleID=";
//		} else {
//			url = "m/module/moduletree?moduleID=";
//		}
		String url = "m/module/moduletree?moduleID=";
		StringBuffer out = new StringBuffer();
		if(subSystem != null){
			//树的根节点
			out.append("<li class=\"root\" id=\"" + subSystem.getSubSystemID() + "\" subSystemID=\"" + subSystem.getSubSystemID() + "\"><span class=\"text\" subSystemID=\"" + subSystem.getSubSystemID() + "\">"
					+ subSystem.getName() + "</span>");			
			if(modules != null && !modules.isEmpty()){
				out.append("<ul>");
				moduleHtml(out, modules, url);
				out.append("</ul>");
			}			
			out.append("</li>");
		} else {
			//树的下级节点
			if (modules != null && !modules.isEmpty()) {
				moduleHtml(out, modules, url);
			}
		}
		return out.toString();
	}
	
	private void moduleHtml(StringBuffer out, List<Module> modules, String url) {
		for (Module module : modules) {
			String moduleId = module.getModuleID();
			String moduleKey = module.getModuleKey();
			out.append("<li id=\"" + moduleId + "\" moduleId=\"" + moduleId + "\" moduleKey=\"" + moduleKey + "\"><span class=\"text\" moduleId=\"" + moduleId + "\" moduleKey=\"" + moduleKey + "\">"
					+ module.getName() + "</span>");
			int childModuleCount = module.getChildModules().size();
			if (childModuleCount > 0) {
				out.append("<ul class=\"ajax\">");
				out.append("<li>{url:" + url + moduleId + "}</li>");
				out.append("</ul>");
			}
			out.append("</li>");
		}
	}
}
