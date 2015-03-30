package org.eapp.oa.meeting.blo;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eapp.oa.meeting.dto.MeetingInfoQueryParameters;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 * @author sds
 * @version Jul 6, 2009
 */
public interface IMeetingInfoBiz {

	/**
	 * 新增会议信息
	 * 
	 * @param roomId
	 * @param applyMan
	 * @param groupName
	 * @param theme
	 * @param beginTime
	 * @param endTime
	 * @param remark
	 * @param content
	 * @param borrowDevices
	 * @param participants
	 * @return
	 * @throws OaException
	 */
	public MeetingInfo txSaveMeetingInfo(String meetingInfoId, String roomId, String applyMan,
			String groupName, String theme, Date beginTime, Date endTime,
			String remark, String[] devices, 
			String[] persons) throws OaException;

	/**
	 * 删除会议信息
	 * 
	 * @param id
	 * @param subject
	 * @param content
	 * @param sendFlag 是否发送取消会议通知
	 * @return
	 * @throws OaException
	 */
	public MeetingInfo txDelMeetingInfo(String id, String subject, String content, boolean sendFlag) throws OaException;

	/**
	 * 分页模糊条件查询 默认查询所有数据
	 * 
	 * @param qp
	 * @return
	 */
	public ListPage<MeetingInfo> queryMeetingInfo(MeetingInfoQueryParameters qp);

	/**
	 * 根据ID查看会议信息
	 * 
	 * @param id
	 * @return
	 * @throws OaException 
	 */
	public MeetingInfo getMeetingInfoById(String id) throws OaException;
	
	/**
	 * 上传附件
	 * 
	 * @param referId
	 * @param deletedFiles
	 * @param files
	 * @param attchType
	 */
	public void txUpdateAttachment(String referId, String[] deletedFiles, List<Attachment> files, int attchType);

	/**
	 * 获取附件列表
	 * 
	 * @param referId
	 * @param attachType
	 * @return
	 */
	public List<Attachment> getAttachments(String referId,int attachType);
	
	/**
	 * 发送会议纪要
	 * 
	 * @param id
	 * @param subject
	 * @param content
	 * @throws OaException
	 */
	public void csSendMeetingMinutes(String id, String subject, String content) throws OaException ;

	/**
	 * 会议内容图片
	 * 
	 * @param referId
	 * @return
	 */
	public Collection<Attachment> getContentAttachments(String referId);
	
	/**
	 * 保存会议内容
	 * @param id
	 * @param title
	 * @param content
	 * @param flag
	 * @param atts
	 * @throws Exception
	 */
	public void txSaveMeetingContent(String id, String title, String content,
			Collection<Attachment> atts, boolean flag, String emailContent) throws Exception;
	
	/**
	 * 查检会议室预订是否有冲突 
	 * @param id
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<MeetingInfo> getMeetingInfos(String id, String roomId, Timestamp beginTime, Timestamp endTime);
	
	/**
	 * 删除会议内容图片
	 * @param imgId
	 * @param referId
	 */
	public void txDelMeetingContentImg(String imgId, String referId);
	
	/**
	 * 翻页查询
	 * @param qp
	 * @return
	 */
	public ListPage<MeetingInfo> queryMeetingOrderInfo(MeetingInfoQueryParameters qp);
	
	/**
	 * 取得会议预订信息的参会人员
	 * @param id
	 * @return
	 * @throws OaException 
	 */
	public Collection<MeetingParticipant> getParticipants(String id) throws OaException;
}
