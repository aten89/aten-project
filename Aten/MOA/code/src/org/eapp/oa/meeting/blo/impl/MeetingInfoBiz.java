package org.eapp.oa.meeting.blo.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.address.dao.IAddressListDAO;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.meeting.blo.IMeetingInfoBiz;
import org.eapp.oa.meeting.dao.IMeetingInfoDAO;
import org.eapp.oa.meeting.dao.IMeetingRoomDAO;
import org.eapp.oa.meeting.dto.MeetingInfoQueryParameters;
import org.eapp.oa.meeting.hbean.MeetingAttachment;
import org.eapp.oa.meeting.hbean.MeetingAttachmentId;
import org.eapp.oa.meeting.hbean.MeetingInfo;
import org.eapp.oa.meeting.hbean.MeetingParticipant;
import org.eapp.oa.meeting.hbean.MeetingRoom;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.mail.JMailProxy;
import org.eapp.util.mail.MailMessage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;


public class MeetingInfoBiz implements IMeetingInfoBiz {
	
	private static final Log log = LogFactory.getLog(MeetingInfoBiz.class);
	
	private IMeetingInfoDAO meetingInfoDAO;
	private IMeetingRoomDAO meetingRoomDAO;
//	private IDeviceDAO deviceDAO;
	private IAddressListDAO addressListDAO;
	
	public void setMeetingRoomDAO(IMeetingRoomDAO meetingRoomDAO) {
		this.meetingRoomDAO = meetingRoomDAO;
	}

//	public void setDeviceDAO(IDeviceDAO deviceDAO) {
//		this.deviceDAO = deviceDAO;
//	}
	
	// setter method
	public void setMeetingInfoDAO(IMeetingInfoDAO meetingInfoDAO) {
		this.meetingInfoDAO = meetingInfoDAO;
	}	

	public void setAddressListDAO(IAddressListDAO addressListDAO) {
		this.addressListDAO = addressListDAO;
	}

	@Override
	public ListPage<MeetingInfo> queryMeetingInfo(MeetingInfoQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		return meetingInfoDAO.queryMeetingInfo(qp);
	}

	@Override
	public MeetingInfo getMeetingInfoById(String id) throws OaException {
		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		MeetingInfo meetingInfo = meetingInfoDAO.findById(id);
		if (meetingInfo == null) {
			throw new OaException("会议信息不存在");
		}
		return meetingInfo;
	}

	@Override
	public MeetingInfo txSaveMeetingInfo(String meetingInfoId, String roomId, String applyMan,
			String groupName, String theme, Date beginTime, Date endTime,
			String remark, String[] devices, 
			String[] persons) throws OaException {
		if (roomId == null ||theme == null ||beginTime == null || endTime == null) {
			throw new IllegalArgumentException("非法参数:参数不能为空！");
		}
		MeetingInfo meetingInfo = null;
		if (meetingInfoId != null) {
			meetingInfo = meetingInfoDAO.findById(meetingInfoId);
			if (meetingInfo == null) {
				throw new IllegalArgumentException();
			}
		} else {
			meetingInfo = new MeetingInfo();
			meetingInfo.setApplyMan(applyMan);
			meetingInfo.setGroupName(groupName);
			meetingInfo.setReserveTime(new Date());
		}
//		Set<BorrowDevice> borrowDevices = meetingInfo.getBorrowDevices();
//		if (borrowDevices == null) {
//			borrowDevices = new HashSet<BorrowDevice>();
//		} else {
//			borrowDevices.clear();
//		}
//			
//		if(devices !=null){
//			for(String devStr : devices) {
//				String[] device = devStr.split("\\*\\*");
//				String deviceId = device[0];
//				int deviceNum = Integer.parseInt(device[1]);
//				if (deviceNum < 0) {
//					continue;
//				}
//				Device d  = deviceDAO.findById(deviceId);
//				if (d == null) {
//					throw new IllegalArgumentException("设备不存在，ID＝" + deviceId);
//				}
//				BorrowDevice bd = new BorrowDevice();
//				bd.setDeviceClass(Device.DEVICECLASS_MEET);
//				bd.setUseAccountID(applyMan);
//				bd.setDevice(d);
//				bd.setBeginTime(beginTime);
//				bd.setEndTime(endTime);
//				bd.setDeviceNum(deviceNum);
//				bd.setIsOrder(true);
//				bd.setRemarks("会议系统预订");
//				borrowDevices.add(bd);
//			}
//		}
		Set<MeetingParticipant> participants = meetingInfo.getParticipants();
		if (participants == null) {
			participants = new HashSet<MeetingParticipant>();
		} else {
			participants.clear();
		}
		for(String personStr : persons){
			String[] person = personStr.split("\\*\\*");
			String userName = person[0];
			String email =null;
			if(person.length ==2){
				email=person[1];	
			}
			MeetingParticipant mp = new MeetingParticipant();
			if(email!=null){
				mp.setName(userName);
				mp.setEmail(email);
				mp.setType(MeetingParticipant.TYPE_NOT_IN_ADDRESS_BOOK);
			}else{
				mp.setParticipant(userName);
				mp.setType(MeetingParticipant.TYPE_IN_ADDRESS_BOOK);
			}
			participants.add(mp);
		}
		MeetingRoom r = meetingRoomDAO.findById(roomId);
		meetingInfo.setMeetingRoom(r);
		
		meetingInfo.setTheme(theme);
		
		meetingInfo.setBeginTime(beginTime);
		meetingInfo.setEndTime(endTime);
		meetingInfo.setRemark(remark);
//		meetingInfo.setBorrowDevices(borrowDevices);
		meetingInfo.setParticipants(participants);		
		meetingInfoDAO.saveOrUpdate(meetingInfo);		
		return meetingInfo;
	}

	@Override
	public MeetingInfo txDelMeetingInfo(String id, String subject, String content, boolean sendEmailFlag)
			throws OaException {
		MeetingInfo meetingInfo = meetingInfoDAO.findById(id);
		
		//手动删除会议的所有附件
		Set<Attachment> allMeetAttach =  new HashSet<Attachment>();
		allMeetAttach.addAll(meetingInfo.getMeetingAttas());
		allMeetAttach.addAll(meetingInfo.getMeetingMinutes());
		allMeetAttach.addAll(meetingInfo.getMeetingContentImags());
		for(Attachment am : allMeetAttach) {
			//删除文件
			FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(am.getFilePath()));
			//删除关联表
			MeetingAttachment mam = meetingInfoDAO.findMeetingAttachmentById(new MeetingAttachmentId(meetingInfo, am));
			meetingInfoDAO.delete(mam);
			meetingInfoDAO.delete(am);
		}
		
		if(sendEmailFlag){
			//发送会议取消通知
			String toAddress = getToAddress(meetingInfo);
			if (toAddress == null) {
				throw new OaException("没有参会人员");
			}
			MailMessage msg = new MailMessage(toAddress, subject, content);	
			//抄送给预订者
			//2012-04-24修改：取邮箱地址改成从通讯录里面去，没有在用账号拼接邮箱地址
			String applyMan = meetingInfo.getApplyMan();
			String emaliAddr = addressListDAO.findEmailByAccountId(applyMan);
			if (StringUtils.isNotEmpty(emaliAddr)) {
				msg.setCopyToAddress(emaliAddr);
			} else {
				log.warn("通讯录无邮箱地址：" + applyMan);
			}
			//发送
			JMailProxy.daemonSend(msg);
		}
		meetingInfoDAO.delete(meetingInfo);
		return meetingInfo;
	}

	public void txUpdateAttachment(String referId, String[] deletedFiles,
			List<Attachment> files, int attchType) {
		MeetingInfo meetingInfo = meetingInfoDAO.findById(referId);
		// 删除旧附件
		if (deletedFiles != null && deletedFiles.length > 0) {
			List<Attachment> targetList = new ArrayList<Attachment>();
			if(MeetingAttachment.TYPE_ATTACH == attchType) {
				targetList.addAll(meetingInfo.getMeetingAttas());
			} else if(MeetingAttachment.TYPE_MINUTE == attchType) {
				targetList.addAll(meetingInfo.getMeetingMinutes());
			} else {
				throw new IllegalArgumentException("附件类型参数非法");
			}
			List<String> delFileList = Arrays.asList(deletedFiles);
			// 通过名称删除附件，前台控件只支持名称删除
			for (Attachment am : targetList) {
				if (delFileList.contains(am.getDisplayName() + am.getFileExt())) {
					meetingInfo.getMeetingMinutes().remove(am);
					// 删除文件
					FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(am.getFilePath()));
					
					MeetingAttachment mam = meetingInfoDAO.findMeetingAttachmentById(new MeetingAttachmentId(meetingInfo, am));
					meetingInfoDAO.delete(mam);
					meetingInfoDAO.delete(am);
					
				}
			}
		}
		
		//保存
		for (Attachment am : files) {
			meetingInfoDAO.save(am);
			MeetingAttachment mam = new MeetingAttachment(new MeetingAttachmentId(meetingInfo, am), attchType);
			meetingInfoDAO.save(mam);
		}
		
		if(MeetingAttachment.TYPE_ATTACH == attchType) {
			meetingInfo.getMeetingAttas().addAll(files);
		} else if(MeetingAttachment.TYPE_MINUTE == attchType) {
			meetingInfo.getMeetingMinutes().addAll(files);
		} else {
			throw new IllegalArgumentException("附件类型参数非法");
		}
	}
	public List<Attachment> getAttachments(String referId,int attachType) {
		MeetingInfo meetingInfo = meetingInfoDAO.findById(referId);
		if (meetingInfo == null) {
			return null;
		}
		if(MeetingAttachment.TYPE_ATTACH == attachType){
			return new ArrayList<Attachment>(meetingInfo.getMeetingAttas());
		} else if(MeetingAttachment.TYPE_MINUTE == attachType) {
			return new ArrayList<Attachment>(meetingInfo.getMeetingMinutes());
		} else {
			throw new IllegalArgumentException("attachType参数出错");
		}
	}
	
	/**
	 * 发送会议纪要
	 * 
	 * @param meetingInfo
	 * @throws OaException 
	 */
	public void csSendMeetingMinutes(String id, String subject, String content) throws OaException {
		MeetingInfo meetingInfo = getMeetingInfoById(id);
		String toAddress = getToAddress(meetingInfo);
		if (toAddress == null) {
			throw new OaException("没有参会人员");
		}

		try {
			MailMessage msg = new MailMessage(toAddress, subject, content);	
			for(Attachment am : meetingInfo.getMeetingMinutes()) {
				File file = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
				String attachmentName = am.getDisplayName() + am.getFileExt();
				msg.addAttachment(file, new String(attachmentName.getBytes(),"ISO-8859-1"));
			}
			//发送
			JMailProxy.daemonSend(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 取得参会人的EMAIL地址
	 * @param meetingInfo
	 * @return
	 */
	private String getToAddress(MeetingInfo meetingInfo) {
		if(meetingInfo == null) {
			throw new IllegalArgumentException();
		}
		if(meetingInfo.getParticipants().size() == 0) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		//获得通讯录
		List<AddressList> addressList = addressListDAO.findAllAddressList();
    	Map<String,String> addressMap = new HashMap<String,String>();
		if (addressList != null && !addressList.isEmpty()) {
			for (AddressList address : addressList) {
				addressMap.put(address.getUserAccountId(), address.getUserEmail());
			}
		}
		
		for(MeetingParticipant person : meetingInfo.getParticipants()){
			if(person.getType() == MeetingParticipant.TYPE_IN_ADDRESS_BOOK) {
				//系统用户
				//获取收件人
				String userMail = addressMap.get(person.getParticipant());
				//如果通讯录为空，进行邮箱拼接
				if(StringUtils.isBlank(userMail)){
					log.warn("通讯录无邮箱地址：" + person.getParticipant());
				} else {
					sb.append(userMail); 
					sb.append(",");						
				}

			} else if(person.getType() == MeetingParticipant.TYPE_NOT_IN_ADDRESS_BOOK){
				//手动添加
				sb.append(person.getEmail());
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public Collection<Attachment> getContentAttachments(String referId){
		if(referId ==null){
			throw new IllegalArgumentException();
		}
		MeetingInfo meetingInfo =meetingInfoDAO.findById(referId);
		if (meetingInfo == null) {
			throw new IllegalArgumentException();
		}
		return meetingInfo.getMeetingContentImags();
	}

	public void txSaveMeetingContent(String id, String title, String content, Collection<Attachment> atts, boolean flag, String emailContent) throws Exception{
		if(id==null){
			throw new IllegalArgumentException();
		}
		MeetingInfo meetingInfo =meetingInfoDAO.findById(id);
		meetingInfo.setContent(content);
		if(atts !=null && !atts.isEmpty()){
			Set<Attachment> set = meetingInfo.getMeetingContentImags();
			if (set == null) {
				set = new HashSet<Attachment>();
			}
			for(Attachment am : atts){
				String tempPath = FileDispatcher.getTempDir()+am.getId()+am.getFileExt();
				FileUtil.moveFile(new File(tempPath), new File(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(am.getFilePath()))));
				am.setId(null);
				set.add(am);
				meetingInfoDAO.save(am);
				MeetingAttachment mam = new MeetingAttachment(new MeetingAttachmentId(meetingInfo, am), MeetingAttachment.TYPE_CONTENTIMG);
				meetingInfoDAO.save(mam);
			}
			meetingInfo.setMeetingContentImags(set);
		}
		meetingInfoDAO.saveOrUpdate(meetingInfo);
		if(flag){
			sendMeetingNotice(meetingInfo,title, emailContent);
		}
	}
	
	/**
	 * 发送会议通知
	 * 
	 * @param meetingInfo
	 * @throws OaException 
	 */
	private void sendMeetingNotice(MeetingInfo meetingInfo,String subject, String emailContent) throws OaException {
		String toAddress = getToAddress(meetingInfo);
		if (toAddress == null) {
			throw new OaException("没有参会人员");
		}
		try {
			MailMessage msg = new MailMessage(toAddress, subject, emailContent);	
			//抄送会议室预订者
			//2012-04-24修改：取邮箱地址改成从通讯录里面去，没有在用账号拼接邮箱地址
            String applyMan = meetingInfo.getApplyMan();
            String emaliAddr = addressListDAO.findEmailByAccountId(applyMan);
            if (StringUtils.isNotBlank(emaliAddr)) {
            	msg.setCopyToAddress(emaliAddr);
            } else {
            	log.warn("通讯录无邮箱地址：" + applyMan);
            }
			
			
			for(Attachment am : meetingInfo.getMeetingAttas()) {
				File file = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
				String attachmentName = am.getDisplayName() + am.getFileExt();
				msg.addAttachment(file, new String(attachmentName.getBytes(),"ISO-8859-1"));
			}
			//发送
			JMailProxy.daemonSend(msg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public List<MeetingInfo> getMeetingInfos(String id, String roomId, Timestamp beginTime, Timestamp endTime){
		List<MeetingInfo> times = meetingInfoDAO.getMeetRoomOrderTimes(id, roomId, beginTime, endTime);
		
		return times;
	}
	
	public void txDelMeetingContentImg(String imgId, String referId){
		MeetingInfo meetingInfo =meetingInfoDAO.findById(referId);
		Set<Attachment> set =  meetingInfo.getMeetingContentImags();
		Attachment delAtt = null;
		for(Attachment att : set){
			if(att.getId().equals(imgId)){
				delAtt = att;
				break;
			}
		}
		if (delAtt != null) {
			FileUtil.delSimilarFiles(FileDispatcher.getAbsPath(delAtt.getFilePath()));	
			meetingInfo.getMeetingAttas().remove(delAtt);
			MeetingAttachment mam = meetingInfoDAO.findMeetingAttachmentById(new MeetingAttachmentId(meetingInfo, delAtt));
			meetingInfoDAO.delete(mam);
			meetingInfoDAO.delete(delAtt);
		}
	}
	
	public ListPage<MeetingInfo> queryMeetingOrderInfo(MeetingInfoQueryParameters qp) {
		if (qp == null) {
			throw new IllegalArgumentException();
		}
		return meetingInfoDAO.queryMeetingOrderInfo(qp);
	}

	@Override
	public Collection<MeetingParticipant> getParticipants(String id) throws OaException {
		MeetingInfo mi = getMeetingInfoById(id);
		return mi.getParticipants();
	}
}
