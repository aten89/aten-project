package org.eapp.oa.doc.blo.impl;

import java.util.List;

import org.eapp.oa.doc.blo.IDocClassAssignBiz;
import org.eapp.oa.doc.dao.impl.DocClassAssignDAO;
import org.eapp.oa.doc.dao.impl.DocClassDAO;
import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.doc.hbean.DocClassAssign;
import org.eapp.oa.system.exception.OaException;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassAssignBiz implements IDocClassAssignBiz {

	private DocClassAssignDAO docClassAssignDAO;
	private DocClassDAO docClassDAO;

	@Override
	public List<DocClassAssign> getBindingGroups(String id) {
		if (id == null) {
			return null;
		}
		else {
			return docClassAssignDAO.findBindById(id, DocClassAssign.TYPE_GROUP);
		}
	}

	@Override
	public List<DocClassAssign> getBindingPosts(String id) {
		if (id == null) {
			return null;
		}
		else {
			return docClassAssignDAO.findBindById(id, DocClassAssign.TYPE_POST);
		}
	}

	@Override
	public List<DocClassAssign> getBindingUsers(String id) {
		if (id == null) {
			return null;
		}
		else {
			return docClassAssignDAO.findBindById(id, DocClassAssign.TYPE_USER);
		}
	}

	@Override
	public void txBindingGroups(String id, String[] groupIDs)
			throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		docClassAssignDAO.delBindById(id, DocClassAssign.TYPE_GROUP);
		//如果要绑定的机构为空，直接返回
		if(groupIDs == null || groupIDs.length == 0){
			return;
		}
		DocClass docClass = docClassDAO.findById(id);
		if(docClass == null){
			throw new OaException("类别不存在!");
		}
		//保存绑定用户
		for (String groupId : groupIDs) {
			DocClassAssign docClassAssign = new DocClassAssign();
			docClassAssign.setType(DocClassAssign.TYPE_GROUP);
//			docClassAssign.setFlag(flag);
			docClassAssign.setAssignKey(groupId);
			docClassAssign.setDocClass(docClass);
			
			docClassAssignDAO.save(docClassAssign);
		}
	}

	@Override
	public void txBindingPosts(String id, String[] postIDs)
			throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		docClassAssignDAO.delBindById(id, DocClassAssign.TYPE_POST);
		//如果要绑定的职位为空，直接返回
		if(postIDs == null || postIDs.length == 0){
			return;
		}
		DocClass docClass = docClassDAO.findById(id);
		if(docClass == null){
			throw new OaException("类别不存在!");
		}
		//保存绑定用户
		for (String postId : postIDs) {
			DocClassAssign docClassAssign = new DocClassAssign();
			docClassAssign.setType(DocClassAssign.TYPE_POST);
//			docClassAssign.setFlag(flag);
			docClassAssign.setAssignKey(postId);
			docClassAssign.setDocClass(docClass);
			
			docClassAssignDAO.save(docClassAssign);
		}

	}

	@Override
	public void txBindingUsers(String id, String[] userIDs)
			throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		docClassAssignDAO.delBindById(id, DocClassAssign.TYPE_USER);
		//如果要绑定的用户为空，直接返回
		if(userIDs == null || userIDs.length == 0){
			return;
		}
		DocClass docClass = docClassDAO.findById(id);
		if(docClass == null){
			throw new OaException("类别不存在!");
		}
		//保存绑定用户
		for (String userId : userIDs) {
			DocClassAssign docClassAssign = new DocClassAssign();
			docClassAssign.setType(DocClassAssign.TYPE_USER);
//			docClassAssign.setFlag(flag);
			docClassAssign.setAssignKey(userId);
			docClassAssign.setDocClass(docClass);
			
			docClassAssignDAO.save(docClassAssign);
		}

	}

	

	public void setDocClassAssignDAO(DocClassAssignDAO docClassAssignDAO) {
		this.docClassAssignDAO = docClassAssignDAO;
	}

	public void setDocClassDAO(DocClassDAO docClassDAO) {
		this.docClassDAO = docClassDAO;
	}
}
