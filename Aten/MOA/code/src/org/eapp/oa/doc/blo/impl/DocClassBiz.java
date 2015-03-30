package org.eapp.oa.doc.blo.impl;

import java.io.File;
import java.util.List;

import org.eapp.oa.doc.blo.IDocClassBiz;
import org.eapp.oa.doc.dao.IDocClassDAO;
import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassBiz implements IDocClassBiz {

	private IDocClassDAO docClassDAO;

	public void setDocClassDAO(IDocClassDAO docClassDAO) {
		this.docClassDAO = docClassDAO;
	}
	
	@Override
	public List<DocClass> getAllDocClass() {
		List<DocClass> list = docClassDAO.findByDisplayorder();
		return list;
	}

	@Override
	public DocClass addDocClass(String name, String flowClass,Integer fileClass,
			String description) throws OaException {
		if (null == name) {
			throw new OaException("非法参数:分类名称不能为空！");
		}

		// 判断分类名称是否重复
		isDocClassNameRepeat(name);

		DocClass docClass = new DocClass();
		docClass.setFileClass(fileClass);
		docClass.setName(name);
//		docClass.setAssignType(isAssign);
		docClass.setFlowClass(flowClass);
		docClass.setDescription(description);
		int displayOrder = docClassDAO.getDisplayOrder() + 1;
		docClass.setDisplayOrder(displayOrder);
		docClassDAO.save(docClass);

		return docClass;
	}

	/**
	 * 验证公文类别名称是否重复
	 * 
	 * @param name
	 * @throws OaException
	 */
	private void isDocClassNameRepeat(String name) throws OaException {
		List<DocClass> docClasses = docClassDAO.findByName(name);
		if (docClasses != null && docClasses.size() > 0) {
			throw new OaException("模板名称不能重复!");
		}

	}

	@Override
	public DocClass deleteDocClass(String id) throws OaException {
		DocClass docClass = docClassDAO.findById(id);
		if (docClass == null) {
			throw new OaException("公文类别不存在");
		}
		
		Attachment bodyTemplate = docClass.getBodyTemplate();
		if (bodyTemplate != null) {
			// 删除文件
			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(bodyTemplate.getFilePath()));
			if (f != null) {
				f.delete();
			}
		}
		docClassDAO.delete(docClass);
		return docClass;
	}

	@Override
	public DocClass modifyDocClass(String id, String name, String flowClass,Integer fileClass,
			String description) throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		if (name == null) {
			throw new IllegalArgumentException("非法参数:infoName不能为空！");
		}
		DocClass docClass = docClassDAO.findById(id);
		if (docClass == null) {
			throw new OaException("公文类别信息不存在");
		}
		// 判断分类名称是否重复
		if (!docClass.getName().equals(name)) {
			isDocClassNameRepeat(name);
		}
		docClass.setName(name);
		docClass.setFlowClass(flowClass);
		docClass.setFileClass(fileClass);
//		docClass.setAssignType(isAssign);
		docClass.setDescription(description);
		docClassDAO.saveOrUpdate(docClass);

		return docClass;
	}

	@Override
	public void txSaveOrder(String[] docClassIds) {
		int order = 1;
		for (int i = 0; i < docClassIds.length; i++) {
			if (docClassIds[i] == null || "".equals(docClassIds[i].trim())) {
				continue;
			} else {
				DocClass docClass = docClassDAO.findById(docClassIds[i]);
				if (docClass == null) {
					continue;
				}
				docClass.setDisplayOrder(order);
				docClassDAO.saveOrUpdate(docClass);
			}
			order++;
		}
	}

	@Override
	public DocClass getDocClassById(String id) {
		return docClassDAO.findById(id);
	}

	@Override
	public void addBodyTemplate(String docClassId, Attachment am) {
		if (docClassId == null) {
			throw new IllegalArgumentException();
		}
		DocClass docClass = docClassDAO.findById(docClassId);
		if (docClass == null) {
			throw new IllegalArgumentException("公文类别");
		}

		Attachment bodyTemplate = docClass.getBodyTemplate();
		
		if (bodyTemplate != null) {
			// 删除文件
			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(bodyTemplate.getFilePath()));
			if (f != null) {
				f.delete();
			}
			docClassDAO.delete(bodyTemplate);
		}
		docClass.setBodyTemplate(am);
		docClassDAO.saveOrUpdate(docClass);
	}

	@Override
	public List<DocClass> getAssignClass(String userAccountId,
			List<String> groupNames, List<String> postNames,int fileClass) {
		return docClassDAO.findAssignClass(userAccountId, groupNames,
				postNames, null, fileClass);
	}

	@Override
	public DocClass getAssignDocClassByName(String userAccountId,
			List<String> groupNames, List<String> postNames, String name,int fileClass) {
		List<DocClass> dcs = docClassDAO.findAssignClass(userAccountId, groupNames,
				postNames, name, fileClass);
		if (dcs != null && dcs.size() > 0) {
			return dcs.get(0);
		}
		return null;
	}
	
	
	@Override
	public DocClass getDocClassByName(String name) {
		if (name == null) {
			return null;
		}
		List<DocClass> list = docClassDAO.findByName(name);
		if (list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}
	
	public List<DocClass> getByFileClass(Integer fileType){
		return docClassDAO.findAll(fileType);
	}

}
