/**
 * 
 */
package org.eapp.dto;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eapp.comobj.ModuleMenuTree;
import org.eapp.comobj.ModuleMenuTree.ModuleMenu;
import org.eapp.util.web.DataFormatUtil;

/**
 * 生成模块菜单的XML
 * @author zsy
 * @version 
 */
public class ModuleMenuXml {
	private static final String DEFAULT_ENCODING = "utf-8";
	ModuleMenuTree tree;
	
	public ModuleMenuXml(ModuleMenuTree tree) {
		this.tree = tree;
	}
	
	/**
	 * @return the moduleActions
	 */
	public ModuleMenuTree getModuleMenuTree() {
		return tree;
	}

	/**
	 * @param moduleActions the moduleActions to set
	 */
	public void setModuleMenuTree(ModuleMenuTree tree) {
		this.tree = tree;
	}

	/**
	 *生成模块菜单的XML信息，格式如下：
	 * <?xml version="1.0" encoding="utf-8"?>
	 * <root>
	 * 	<message code="1"/>
	 * 	<content>
	 * 	  <module ID="d08ded7a-3be2-4a6d-afa6-221a29cffbab">
	 *  	<module-key>interface_service</module-key> 
	 *  	<module-name>接口服务</module-name> 
	 *  	<module-url /> 
	 *  	<module ID="37e796fe-a509-4298-aa6b-ca7fd810b838">
	 *  	  <module-key>service_config</module-key> 
	 *  	  <module-name>服务配置</module-name> 
	 *  	  <module-url /> 
	 *  	</module>
	 * 	  </module>
	 * 	</content>
	 * </root>
	 * @return
	 */
	public Document toDocument() {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding(DEFAULT_ENCODING);
		Element root = doc.addElement("root");
		root.addElement("message").addAttribute("code", "1");

		if (tree == null || tree.getSubModuleMenu() == null || tree.getSubModuleMenu().size() < 1) {
			return doc;
		}
		Element contentEle = root.addElement("content");
		for (ModuleMenuTree st : tree.getSubModuleMenu().values()) {
			buildModule(contentEle, st);
		}
		return doc;
	}
	
	private void buildModule(Element root, ModuleMenuTree tree) {
		if (tree == null) {
			return;
		}
		ModuleMenu m = tree.getModuleMenu();
		Element ele = root.addElement("module");
		ele.addAttribute("id", m.getModuleID());
		ele.addElement("module-key").setText(DataFormatUtil.noNullValue(m.getModuleKey()));
		ele.addElement("module-name").setText(DataFormatUtil.noNullValue(m.getName()));
		ele.addElement("module-url").setText(DataFormatUtil.noNullValue(m.getUrl()));
		for (ModuleMenuTree st : tree.getSubModuleMenu().values()) {
			buildModule(ele, st);
		}
	}
	
	public String toString() {
		return toDocument().asXML();
	}
	
/*	private Element buildModule(Element parentEle, Module m) {
		Element existEle = parentEle.elementByID(m.getModuleID());
		if (existEle != null) {
			return existEle;
		}

		Element ele = DocumentHelper.createElement("module");
		ele.addAttribute("ID", m.getModuleID());
		ele.addElement("module-key").setText(DataFormatUtil.noNullValue(m.getModuleKey()));
		ele.addElement("module-name").setText(DataFormatUtil.noNullValue(m.getName()));
		ele.addElement("module-url").setText(DataFormatUtil.noNullValue(m.getUrl()));
		
		if (m.getParentModule() != null) {
			Element tem = buildModule(parentEle, m.getParentModule());
			tem.add(ele);
			return ele;
			
		} else {
			parentEle.add(ele);
		}
		return ele;
	}*/
}
