package org.eapp.oa.info.blo.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eapp.oa.info.blo.IInformationBiz;
import org.eapp.oa.info.dao.IInfoFormDAO;
import org.eapp.oa.info.dao.IInfoLayoutDAO;
import org.eapp.oa.info.dao.IInformationDAO;
import org.eapp.oa.info.dto.InformationQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.info.hbean.InfoLayoutAssign;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;
import org.hibernate.Hibernate;


public class InformationBiz implements IInformationBiz {
	private IInformationDAO informationDAO;
	private IInfoFormDAO infoFormDAO;
	private IInfoLayoutDAO infoLayoutDAO;

	/**
	 * @param informationDAO the informationDAO to set
	 */
	public void setInformationDAO(IInformationDAO informationDAO) {
		this.informationDAO = informationDAO;
	}

	public void setInfoFormDAO(IInfoFormDAO infoFormDAO) {
		this.infoFormDAO = infoFormDAO;
	}

	public void setInfoLayoutDAO(IInfoLayoutDAO infoLayoutDAO) {
		this.infoLayoutDAO = infoLayoutDAO;
	}

	@Override
	public Information getInformationById(String id) {
		if (id == null) {
			return null;
		}
		return informationDAO.findById(id);
	}
	
	@Override
	public ListPage<Information> queryInformation(InformationQueryParameters qp) {
		if(qp == null){
			throw new IllegalArgumentException("非法参数:OutlayListQueryParameters参数为null");
		}
		ListPage<Information> list = informationDAO.queryInformation(qp);
		return list;
	}

	/* (non-Javadoc)
	 */
	@Override
	public Information txUpdateState(String userAccountId, 
			List<String> groupNames, List<String> postNames, String id, int state) throws OaException {
		if(id == null){
			throw new IllegalArgumentException("非法参数:id参数为null");
		}
		
		Information information = informationDAO.findById(id);
		if(information == null){
			throw new OaException("未找到信息");
		}
		List<InfoLayout> ils = infoLayoutDAO.findAssignLayout(userAccountId, 
				groupNames, postNames, InfoLayoutAssign.FLAG_INFOMAN, information.getInfoLayout());
		if (ils == null || ils.size() == 0) {
			throw new OaException("没有操作权限");
		}
		information.setInfoProperty(state);
		informationDAO.saveOrUpdate(information);
		return information;
	}

	@Override
	public Set<Attachment> getInfoAttachments(String infoId) {
		if (infoId == null) {
			throw new IllegalArgumentException();
		}
		InfoForm infoFrom = infoFormDAO.findById(infoId);
		if (infoFrom == null) {
			throw new IllegalArgumentException("信息不存在");
		}
		Information info = infoFrom.getInformation();
		Hibernate.initialize(info.getAttachments());
		return info.getAttachments();
	}

	@Override
	public void txUpdateAttachment(String infoId, String[] deletedFiles,
			List<Attachment> files) {
		if (infoId == null) {
			throw new IllegalArgumentException();
		}
		InfoForm infoFrom = infoFormDAO.findById(infoId);
		if (infoFrom == null) {
			throw new IllegalArgumentException("信息不存在");
		}
		Information info = infoFrom.getInformation();
		
		//删除旧附件
		if (deletedFiles != null && deletedFiles.length > 0 
				&& info.getAttachments() != null && info.getAttachments().size() > 0) {
			List<String> delFileList = Arrays.asList(deletedFiles);
			//通过名称删除附件，前台控件只支持名称删除
			for (Attachment am : new ArrayList<Attachment>(info.getAttachments())) {
				if (delFileList.contains(am.getDisplayName() + am.getFileExt())) {
					info.getAttachments().remove(am);
					// 删除文件
		        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
					if (f != null) {
						f.delete();
					}
				}
			}
		}
		
		//添加新附件
		info.getAttachments().addAll(files);
		
		informationDAO.saveOrUpdate(info);
	}
	
	@Override
	public void txUpdateContentUrl(String infoId, String contentUrl) {
		if (infoId == null) {
			throw new IllegalArgumentException();
		}
		InfoForm infoFrom = infoFormDAO.findById(infoId);
		if (infoFrom == null) {
			throw new IllegalArgumentException("信息不存在");
		}
		
		infoFrom.getInformation().setContentUrl(contentUrl);
		informationDAO.saveOrUpdate(infoFrom.getInformation());
	}

	@Override
	public void deleteContentFile(String infoId) {
		if (infoId == null) {
			throw new IllegalArgumentException();
		}
		InfoForm infoFrom = infoFormDAO.findById(infoId);
		if (infoFrom == null) {
			throw new IllegalArgumentException("信息不存在");
		}
		String contentUrl = infoFrom.getInformation().getContentUrl();
		if (contentUrl == null) {
			return;
		}
		int index = contentUrl.lastIndexOf("/");
		if (index == -1) {
			return;
		}
		String dir = contentUrl.substring(0, index + 1);
		
		 // 删除文件
        List<File> delDirs = FileDispatcher.findDirs(FileDispatcher.getAbsPath(dir));
        if (delDirs != null && !delDirs.isEmpty()) {
        	//只会有一个目录
        	FileUtil.delDir(delDirs.get(0));
        }
//		FileUtil.delDir(FileDispatcher.getAbsPath(dir));
	}

	/* (non-Javadoc)
	 */
	@Override
	public Set<Attachment> getInfomationAttachments(String infoId) {
		if (infoId == null) {
			throw new IllegalArgumentException();
		}
		Information info = informationDAO.findById(infoId);
		if(info != null){
			Hibernate.initialize(info.getAttachments());
			return info.getAttachments();
		}
		return null;
	}

	@Override
	public List<String> getAllInfoLayout() {
		return informationDAO.findAllInfoLayout();
	}

	@Override
	public void addInformation(Information information) {
		informationDAO.saveOrUpdate(information);
	}
	
}
