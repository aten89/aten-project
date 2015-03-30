package org.eapp.oa.info.blo;

import java.util.List;
import java.util.Set;

import org.eapp.oa.info.dto.InformationQueryParameters;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;

public interface IInformationBiz {
	
	/**
	 * 通过ID查找
	 * @param id
	 * @return
	 */
	public Information getInformationById(String id);
	
	/**
	 * 翻页查询
	 * @param qp
	 * @return
	 */
	public ListPage<Information> queryInformation(InformationQueryParameters qp);
	
	/**
	 * 更新信息状态
	 * @param id
	 * @param state
	 * @return
	 * @throws OaException
	 */
	public Information txUpdateState(String userAccountId, 
			List<String> groupNames, List<String> postNames,String id,int state)throws OaException;
	
	
	/**
	 * 更新信息的附件
	 * @param infoId 
	 * @param deletedFiles 删除的附件名称
	 * @param files 新增的附件列表
	 */
	void txUpdateAttachment(String infoId, String[] deletedFiles, List<Attachment> files);
	
	/**
	 * 取得信息的附件
	 * @param infoId
	 * @return
	 */
	Set<Attachment> getInfoAttachments(String infoId);
	
	/**
	 * 更新信息内容地址
	 * @param infoId
	 * @param url
	 */
	void txUpdateContentUrl(String infoId, String url);
	
	/**
	 * 删除内容地址下所有文件
	 * @param infoId
	 */
	void deleteContentFile(String infoId);
	/**
	 * 取得已发布的附件信息
	 * @param infoId
	 * @return
	 */
	Set<Attachment> getInfomationAttachments(String infoId);
	
	/**
	 * 查找所有已有信息的板块
	 * @return
	 */
	List<String> getAllInfoLayout();
	
	/**
	 * 添加公告信息
	 * @param information
	 * @return
	 */
	public void addInformation(Information information);
}
