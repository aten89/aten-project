package org.eapp.oa.info.blo.impl;

import java.util.List;

import org.eapp.oa.info.blo.IInfoLayoutAssignBiz;
import org.eapp.oa.info.dao.IInfoLayoutAssignDAO;
import org.eapp.oa.info.dao.IInfoLayoutDAO;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.system.exception.OaException;

public class InfoLayoutAssignBiz implements IInfoLayoutAssignBiz {
	
	private IInfoLayoutAssignDAO infoLayoutAssignDAO;
	private IInfoLayoutDAO infoLayoutDAO;
	/**
	 * @return the infoLayoutAssignDAO
	 */
	public IInfoLayoutAssignDAO getInfoLayoutAssignDAO() {
		return infoLayoutAssignDAO;
	}

	/**
	 * @param infoLayoutAssignDAO the infoLayoutAssignDAO to set
	 */
	public void setInfoLayoutAssignDAO(IInfoLayoutAssignDAO infoLayoutAssignDAO) {
		this.infoLayoutAssignDAO = infoLayoutAssignDAO;
	}

	@Override
	public List<InfoLayoutAssign> getBindingGroups(String id, int flag) {
		if (id == null) {
			return null;
		}
		return infoLayoutAssignDAO.findBindById(id, InfoLayoutAssign.TYPE_GROUP, flag);
	}

	@Override
	public List<InfoLayoutAssign> getBindingPosts(String id, int flag) {
		if (id == null) {
			return null;
		}
		return infoLayoutAssignDAO.findBindById(id, InfoLayoutAssign.TYPE_POST, flag);
	}

	@Override
	public List<InfoLayoutAssign> getBindingUsers(String id, int flag) {
		if (id == null) {
			return null;
		}
		return infoLayoutAssignDAO.findBindById(id, InfoLayoutAssign.TYPE_USER, flag);
	}

	@Override
	public void txBindingUsers(String id, String[] userIDs, int flag) throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		infoLayoutAssignDAO.delBindById(id, InfoLayoutAssign.TYPE_USER, flag);
		//如果要绑定的用户为空，直接返回
		if(userIDs == null || userIDs.length == 0){
			return;
		}
		InfoLayout layout = infoLayoutDAO.findById(id);
		if(layout == null){
			throw new OaException("板块不存在!");
		}
		//保存绑定用户
		for (String userId : userIDs) {
			InfoLayoutAssign ia = new InfoLayoutAssign();
			ia.setType(InfoLayoutAssign.TYPE_USER);
			ia.setFlag(flag);
			ia.setAssignKey(userId);
			ia.setInfoLayout(layout);
			infoLayoutAssignDAO.save(ia);
		}
	}

	@Override
	public void txBindingGroups(String id, String[] groupIDs, int flag)throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		infoLayoutAssignDAO.delBindById(id, InfoLayoutAssign.TYPE_GROUP,flag);
		//如果要绑定的机构为空，直接返回
		if(groupIDs == null || groupIDs.length == 0){
			return;
		}
		InfoLayout layout = infoLayoutDAO.findById(id);
		if(layout == null){
			throw new OaException("板块不存在!");
		}
		//保存绑定用户
		for (String groupId : groupIDs) {
			InfoLayoutAssign ia = new InfoLayoutAssign();
			ia.setType(InfoLayoutAssign.TYPE_GROUP);
			ia.setFlag(flag);
			ia.setAssignKey(groupId);
			ia.setInfoLayout(layout);
			infoLayoutAssignDAO.save(ia);
		}
		
	}

	@Override
	public void txBindingPosts(String id, String[] postIDs, int flag) throws OaException{
		if (id == null) {
			throw new IllegalArgumentException("非法参数：id不能为空！");
		}
		infoLayoutAssignDAO.delBindById(id, InfoLayoutAssign.TYPE_POST,flag);
		//如果要绑定的职位为空，直接返回
		if(postIDs == null || postIDs.length == 0){
			return;
		}
		InfoLayout layout = infoLayoutDAO.findById(id);
		if(layout == null){
			throw new OaException("板块不存在!");
		}
		//保存绑定用户
		for (String postId : postIDs) {
			InfoLayoutAssign ia = new InfoLayoutAssign();
			ia.setType(InfoLayoutAssign.TYPE_POST);
			ia.setFlag(flag);
			ia.setAssignKey(postId);
			ia.setInfoLayout(layout);
			infoLayoutAssignDAO.save(ia);
		}
	}

	/**
	 * @return the infoLayoutDAO
	 */
	public IInfoLayoutDAO getInfoLayoutDAO() {
		return infoLayoutDAO;
	}

	/**
	 * @param infoLayoutDAO the infoLayoutDAO to set
	 */
	public void setInfoLayoutDAO(IInfoLayoutDAO infoLayoutDAO) {
		this.infoLayoutDAO = infoLayoutDAO;
	}
	
}
