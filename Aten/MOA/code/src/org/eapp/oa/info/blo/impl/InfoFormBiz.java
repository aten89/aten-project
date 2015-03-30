package org.eapp.oa.info.blo.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.info.blo.IInfoFormBiz;
import org.eapp.oa.info.dao.IInfoFormDAO;
import org.eapp.oa.info.dto.InfoFormQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.AttachmentHelper;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;


/**
 * 公告信息业务逻辑实现
 * 
 */
public class InfoFormBiz implements IInfoFormBiz {
    /**
     * 公告信息数据层访问接口
     */
    private IInfoFormDAO infoFormDAO;
    /**
     * 任务数据层访问接口
     */
    private ITaskDAO taskDAO;
//    /**
//     * 信息流程配置业务逻辑访问接口
//     */
//    private IInfoLayoutBiz infoLayoutBiz;
    /**
     * 通讯录模块中的我的资料子模块的业务逻辑处理接口
     */
//    private IAddressListBiz addressListBiz;

    /**
     * 日志
     */
    private static final Log LOG = LogFactory.getLog(InfoFormBiz.class);

    /**
     * 设置infoFormDAO为infoFormDAO
     * 
     * @param infoFormDAO the infoFormDAO to set
     */
    public void setInfoFormDAO(IInfoFormDAO infoFormDAO) {
        this.infoFormDAO = infoFormDAO;
    }

    /**
     * 设置taskDAO为taskDAO
     * 
     * @param taskDAO the taskDAO to set
     */
    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    /**
     * 设置infoLayoutBiz为infoLayoutBiz
     * 
     * @param infoLayoutBiz the infoLayoutBiz to set
     */
//    public void setInfoLayoutBiz(IInfoLayoutBiz infoLayoutBiz) {
//        this.infoLayoutBiz = infoLayoutBiz;
//    }

    /**
     * 设置addressListBiz为addressListBiz
     * 
     * @param addressListBiz the addressListBiz to set
     */
//    public void setAddressListBiz(IAddressListBiz addressListBiz) {
//        this.addressListBiz = addressListBiz;
//    }

    @Override
    public void addInfoForm(InfoForm infoForm) {
        infoFormDAO.saveOrUpdate(infoForm);
    }

    @Override
    public InfoForm getInfoFormById(String id) {
        if (id == null) {
            return null;
        }
        return infoFormDAO.findById(id);
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public List<InfoForm> getDealingInfoFrom(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        return infoFormDAO.queryDealingInfoForm(userId);
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public ListPage<InfoForm> getTrackInfoFrom(InfoFormQueryParameters qp, String userAccountId) {
        if (userAccountId == null) {
            throw new IllegalArgumentException();
        }

        ListPage<InfoForm> page = infoFormDAO.queryTrackInfoForm(qp, userAccountId);
        if (page != null && page.getDataList() != null) {
            List<InfoForm> list = page.getDataList();
            for (InfoForm r : list) {
                // 取得最后结束的一个任务
                List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceId());
                StringBuffer sb = new StringBuffer();
                if (tasks != null && !tasks.isEmpty()) {
                    for (Task t : tasks) {
                        if (sb.length() > 0) {
                        	sb.append("<BR>");
                        }
                        if (t.getTaskState().equals(TaskInstance.PROCESS_STATE_CREATE)) {
                            sb.append("<b>" + t.getTransactorDisplayName() + "</b>");
                        } else {
                            sb.append(t.getTransactorDisplayName());
                        }
                    }
                    Task task = tasks.get(0);
                    task.setTransactor(sb.toString());
                    r.setTask(task);
                }
            }
        }
        return page;
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public ListPage<InfoForm> getArchInfoFrom(InfoFormQueryParameters qp, String userAccountId) {
        if (userAccountId == null) {
            throw new IllegalArgumentException();
        }
        return infoFormDAO.queryArchInfoForm(qp, userAccountId);
    }

    @Override
    public List<InfoForm> getInfoForm(String userAccountId, int infoStatus) {
        if (userAccountId == null) {
            return null;
        }
        List<InfoForm> list = infoFormDAO.findInfoForm(userAccountId, infoStatus);
        if (list != null) {
            for (InfoForm f : list) {
                // 初始化公告信息
                Hibernate.initialize(f.getInformation());
            }
        }
        return list;
    }

    @Override
    public InfoForm addOrModifyInfoForm(String infoFormId, String usreAccountId, String subject, String subjectColor,
            String infoLayout, String infoClass, String groupName, int displayMode, String content) throws OaException {
        InfoForm infoForm = null;
        Information info = null;
        if (infoFormId != null) {
            infoForm = infoFormDAO.findById(infoFormId);
            if (infoForm == null) {
                throw new OaException("该信息已不存在");
            }
        }
        // 不存在则新增一条
        if (infoForm == null) {
            infoForm = new InfoForm();
            info = new Information();
            // 设置Information
            infoForm.setInformation(info);
            // 设置InfoStatus
            info.setInfoStatus(Information.STATUS_UNPUBLISH);
            // 设置InfoProperty
            info.setInfoProperty(Information.PROPERTY_COMMON);
            // 设置起草人
            info.setDraftsMan(usreAccountId);
            // 设置起草时间
            info.setDraftDate(new Timestamp(System.currentTimeMillis()));
        } else {
            info = infoForm.getInformation();
        }
        // 设置标题
        info.setSubject(subject);
        // 设置颜色
        info.setSubjectColor(subjectColor);
        // 设置InfoLayout
        info.setInfoLayout(infoLayout);
        // 设置InfoClass
        info.setInfoClass(infoClass);
        // 设置部门
        info.setGroupName(groupName);
        // 设置DisplayMode
        if (displayMode == Information.DISPLAYMODE_CONTENT) {
        	info.setDisplayMode(Information.DISPLAYMODE_CONTENT);
        	//设置Content
            info.setContent(content);
        } else {
        	info.setDisplayMode(Information.DISPLAYMODE_URL);
        }
        

        infoFormDAO.saveOrUpdate(infoForm);
        return infoForm;
    }

    @Override
    public void txSaveContent(String infoFormId, String content) {
    	InfoForm infoForm = infoFormDAO.findById(infoFormId);
    	if (infoForm == null) {
    		throw new IllegalArgumentException();
    	}
        Information info = infoForm.getInformation();
        info.setContent(content);
        infoFormDAO.update(info);
    }
    
    
    @Override
    public void addContentDoc(String infoId, Attachment am) {
        if (infoId == null) {
            throw new IllegalArgumentException();
        }
        InfoForm infoForm = infoFormDAO.findById(infoId);
        if (infoForm == null) {
            throw new IllegalArgumentException("信息表单不存在");
        }

        Attachment oam = infoForm.getContentDoc();

        if (oam != null) {
        	// 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(oam.getFilePath()));
			if (f != null) {
				f.delete();
			}
            infoFormDAO.delete(oam);
        }
        infoForm.setContentDoc(am);
        infoFormDAO.saveOrUpdate(infoForm);

    }

    @Override
    public Attachment getContentDoc(String infoId) {
        if (infoId == null) {
            throw new IllegalArgumentException();
        }
        InfoForm infoForm = infoFormDAO.findById(infoId);
        if (infoForm == null) {
            throw new IllegalArgumentException("信息表单不存在");
        }
        return infoForm.getContentDoc();
    }

    @Override
    public void deleteInfoForm(String id, String accountId) throws OaException {
        if (id == null) {
            return;
        }
        InfoForm infoForm = infoFormDAO.findById(id);
        if (infoForm == null) {
            return;
        }
        if (Information.STATUS_UNPUBLISH != infoForm.getInformation().getInfoStatus()) {
            throw new OaException("只能删除未发布的消息");
        } else if (!accountId.equals(infoForm.getInformation().getDraftsMan())) {
            throw new OaException("只能删除自己发布的消息");
        }

        if (infoForm.getContentDoc() != null) {
        	// 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(infoForm.getContentDoc().getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (infoForm.getInformation().getAttachments() != null) {
            for (Attachment am : new ArrayList<Attachment>(infoForm.getInformation().getAttachments())) {
            	// 删除文件
	        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
				if (f != null) {
					f.delete();
				}
            }
        }

        infoFormDAO.delete(infoForm);
    }

    @Override
    public InfoForm txStartFlow(String id, String flowKey) throws OaException {
        if (id == null) {
            return null;
        }
        InfoForm infoForm = infoFormDAO.findById(id);
        if (infoForm == null) {
            return null;
        }
        // 判断信息内容是否存在
        if (infoForm.getContentDoc() == null && infoForm.getInformation().getContentUrl() == null
                && infoForm.getInformation().getContent() == null) {
            throw new OaException("信息内容不存在");
        }
        // 不启动流程，直接发布
        if (flowKey == null) {
            Date now = new Date();
            // 设置Passed
            infoForm.setPassed(true);
            // 设置归档时间
            infoForm.setArchiveDate(now);
            // 设置状态
            infoForm.getInformation().setInfoStatus(Information.STATUS_PUBLISH);
            // 设置发布时间
            infoForm.getInformation().setPublicDate(now);
            // 删除原先的信息和TASK
            if (infoForm.getCopyInfoFormId() != null) {
                deleteOldInfoFormAndTask(infoForm.getCopyInfoFormId());
            }

            // 2013-3-4 添加发送邮件通知
//            try {
//                txSendEmail(id);
//            } catch (IOException e) {
//                LOG.error("txSendEmail failed: ", e);
//            } catch (RpcAuthorizationException e) {
//                LOG.error("txSendEmail failed: ", e);
//            }

        } else {
            // 启动流程
            infoForm.getInformation().setInfoStatus(Information.STATUS_APPROVAL);

            WfmContext context = WfmConfiguration.getInstance().getWfmContext();
            try {
                // 设置流程上下文变量中，并启动流程
                Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
                // 把表单ID
                ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID,
                        ContextVariable.DATATYPE_STRING, infoForm.getId());
                contextVariables.put(cv.getName(), cv);
                // 发起人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                        infoForm.getInformation().getDraftsMan());
                contextVariables.put(cv.getName(), cv);
                // 部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME, ContextVariable.DATATYPE_STRING, infoForm
                        .getInformation().getGroupName());
                contextVariables.put(cv.getName(), cv);
                // 任务描述
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                        infoForm.getInformation().getInfoLayout() + "《" + infoForm.getInformation().getSubject() + "》");
                contextVariables.put(cv.getName(), cv);
                FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
                flowInstance.signal();
                context.save(flowInstance);

                String oldFlowInstanceId = infoForm.getFlowInstanceId();
                // 设置表单视图的流程实例ID
                infoForm.setFlowInstanceId(flowInstance.getId());

                // 判断该信息是否是进行修改的信息,如果是进行修改的则把源信息的TSAK内容重新COPY一份设置它的FlowInstanceID和FormID.
                if (infoForm.getCopyInfoFormId() != null) {
                    // 进行COPY
                    txCopyTasks(infoForm, oldFlowInstanceId);
                }
            } catch (Exception e) {
                LOG.error("txStartFlow failed: ", e);
                context.rollback();
                throw new OaException(e);
            } finally {
                context.close();
            }
        }

        infoFormDAO.saveOrUpdate(infoForm);
        return infoForm;
    }

    @Override
    public List<Task> getEndedTasks(String formId) {
        InfoForm infoForm = infoFormDAO.findById(formId);
        if (infoForm == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(infoForm.getFlowInstanceId());
    }

    @Override
    public void txCancellationInfoForm(String infoFormId) throws OaException {
        if (infoFormId == null) {
            throw new IllegalArgumentException("非法参数:infoFormId");
        }
        InfoForm infoForm = infoFormDAO.findById(infoFormId);
        // 设置Passed
        infoForm.setPassed(false);
        // 设置归档时间
        infoForm.setArchiveDate(new Date());
        // 设置状态
        infoForm.getInformation().setInfoStatus(Information.STATUS_CANCELLATION);
        infoFormDAO.saveOrUpdate(infoForm);
    }

    /*
     * (non-Javadoc)
     * 
     */
    /*
     * @Override public Attachment copyAttachment(Attachment attachment) throws OaException { if(attachment == null){
     * return null; } try { //获得该正文附件的相对路径 String path = FileDispatcher.getAbsPath(attachment.getFilePath()); //获得文件
     * File file = FileDispatcher.findFile(path); SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/"); String dir =
     * SysConstants.INFO_ATTACHMENT_DIR + sdf.format(new Date()); FileInputStream in = new FileInputStream(file);
     * Attachment attachmentCopy = new Attachment(); attachmentCopy.setDisplayName(attachment.getDisplayName());
     * attachmentCopy.setFileExt(attachment.getFileExt()); String attachmentPath = dir + SerialNoCreater.createUUID() +
     * attachment.getFileExt(); attachmentCopy.setFilePath(attachmentPath);
     * attachmentCopy.setSize(attachment.getSize()); attachmentCopy.setUploadDate(new Date());
     * FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(attachmentPath)), in); return
     * attachmentCopy; } catch(IOException e) { e.printStackTrace(); throw new OaException("附件保存出错"); } }
     */

    /*
     * (non-Javadoc)
     */
    @Override
    public InfoForm getInfoFormByCopyId(String infoFormId) {
        List<InfoForm> infoForm = infoFormDAO.findByCopyInfoFormId(infoFormId);
        if (infoForm == null || infoForm.size() < 1) {
            return null;
        }
        return infoForm.get(0);
    }

    /*
     * (non-Javadoc)
     */
    private void txCopyTasks(InfoForm infoForm, String oldFlowInstanceId) {
        // 调用TASKDAO方法 COPY 并且设置新的flowInstanceId 和 FormId
        List<Task> tasks = taskDAO.findEndedTasks(oldFlowInstanceId);
        if (tasks == null || tasks.size() < 1) {
            return;
        }
        // 拷贝任务
        for (Task t : tasks) {
            Task tempTask = new Task();
            // 设置Comment
            tempTask.setComment(t.getComment());
            // 设置CreateTime
            tempTask.setCreateTime(t.getCreateTime());
            // 设置Description
            tempTask.setDescription(t.getDescription());
            // 设置EndTime
            tempTask.setEndTime(t.getEndTime());
            // 设置流程配置
            tempTask.setFlowConfig(t.getFlowConfig());
            // 设置FlowInstanceID
            tempTask.setFlowInstanceID(infoForm.getFlowInstanceId());
            // 设置流程key
            tempTask.setFlowKey(t.getFlowKey());
            // 设置流程名称
            tempTask.setFlowName(t.getFlowName());
            // 设置formId
            tempTask.setFormID(infoForm.getId());
            // 设置节点名称
            tempTask.setNodeName(t.getNodeName());
            // 设置开始时间
            tempTask.setStartTime(t.getStartTime());
            // 设置任务状态
            tempTask.setTaskState(t.getTaskState());
            // 设置Transactor
            tempTask.setTransactor(t.getTransactor());
            // 设置ViewFlag
            tempTask.setViewFlag(t.getViewFlag());
            // 设置TaskInstanceID
            tempTask.setTaskInstanceID(t.getTaskInstanceID());
            // 设置TaskName
            tempTask.setTaskName(t.getTaskName());
            // 设置FlowDefineID
            tempTask.setFlowDefineID(t.getFlowDefineID());
            taskDAO.save(tempTask);
        }
        // 复制完后模拟再插入一条重新修订
        Task task = tasks.get(0);
        Task modifyTask = new Task();
        // 设置Comment
        modifyTask.setComment("重新修订");
        // 设置CreateTime
        modifyTask.setCreateTime(task.getCreateTime());
        // 设置Description
        modifyTask.setDescription(task.getDescription());
        // 设置EndTime
        modifyTask.setEndTime(new Date());
        // 设置流程配置
        modifyTask.setFlowConfig(task.getFlowConfig());
        // 设置FlowInstanceID
        modifyTask.setFlowInstanceID(infoForm.getFlowInstanceId());
        // 设置流程key
        modifyTask.setFlowKey(task.getFlowKey());
        // 设置流程名称
        modifyTask.setFlowName(task.getFlowName());
        // 设置formId
        modifyTask.setFormID(infoForm.getId());
        // 设置节点名称
        modifyTask.setNodeName(task.getNodeName());
        // 设置开始时间
        modifyTask.setStartTime(task.getStartTime());
        // 设置任务状态
        modifyTask.setTaskState(task.getTaskState());
        // 设置Transactor
        modifyTask.setTransactor(infoForm.getInformation().getDraftsMan());
        // 设置ViewFlag
        modifyTask.setViewFlag(task.getViewFlag());
        // 设置TaskInstanceID
        modifyTask.setTaskInstanceID(task.getTaskInstanceID());
        // 设置TaskName
        modifyTask.setTaskName(task.getTaskName());
        // 设置FlowDefineID
        modifyTask.setFlowDefineID(task.getFlowDefineID());
        taskDAO.save(modifyTask);
    }

    public void deleteOldInfoFormAndTask(String id) throws OaException {
        InfoForm infoForm = infoFormDAO.findById(id);
        if (infoForm == null) {
            return;
        }

        if (infoForm.getContentDoc() != null) {
        	// 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(infoForm.getContentDoc().getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (infoForm.getInformation().getAttachments() != null) {
            for (Attachment am : infoForm.getInformation().getAttachments()) {
            	// 删除文件
	        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
				if (f != null) {
					f.delete();
				}
            }
        }
        String contentUrl = infoForm.getInformation().getContentUrl();
        if (contentUrl != null) {
            // 删除HTML附件
            int index = contentUrl.lastIndexOf('/');
            if (index != -1) {
                String dir = contentUrl.substring(0, index + 1);
                // 删除文件
                List<File> delDirs = FileDispatcher.findDirs(FileDispatcher.getAbsPath(dir));
                if (delDirs != null && !delDirs.isEmpty()) {
                	//只会有一个目录
                	FileUtil.delDir(delDirs.get(0));
                }
            }

        }
        // 删除Task表
        String flowInstId = infoForm.getFlowInstanceId();
        if (flowInstId != null) {
            List<Task> tasks = taskDAO.findEndedTasks(flowInstId);
            if (tasks != null && !tasks.isEmpty()) {
                for (Task t : tasks) {
                    taskDAO.delete(t);
                }
            }
        }
        // 删除信息
        infoFormDAO.delete(infoForm);
    }

    @Override
    public InfoForm txCopyInfoForm(String infoFormId, String accountId) throws OaException, IOException {
        if (infoFormId == null) {
            throw new IllegalArgumentException();
        }
        InfoForm infoForm = infoFormDAO.findById(infoFormId);
        if (infoForm == null) {
            throw new OaException("记录不存在");
        } else if (!accountId.equals(infoForm.getInformation().getDraftsMan())) {
            throw new OaException("此消息不是您发布的不允许修改");
        } else if (!infoForm.getPassed()) {
            throw new OaException("此消息已作废不允许修改");
        }
        // 获得原先的信息
        Information information = infoForm.getInformation();

        // 复制的信息审批单
        InfoForm modifyInfoForm = new InfoForm();
        // 复制的信息
        Information modifyInformation = new Information();
        // 流程ID
        modifyInfoForm.setFlowInstanceId(infoForm.getFlowInstanceId());
        // 保存原先ID
        modifyInfoForm.setCopyInfoFormId(infoForm.getId());

        // 设置DisplayMode
        modifyInformation.setDisplayMode(information.getDisplayMode());
        modifyInformation.setContent(information.getContent());
        // 设置DraftDate
        modifyInformation.setDraftDate(information.getDraftDate());
        // DraftsMan
        modifyInformation.setDraftsMan(information.getDraftsMan());
        // 设置GroupName
        modifyInformation.setGroupName(information.getGroupName());
        // 设置InfoClass
        modifyInformation.setInfoClass(information.getInfoClass());
        // 设置InfoLayout
        modifyInformation.setInfoLayout(information.getInfoLayout());
        // 设置InfoProperty
        modifyInformation.setInfoProperty(information.getInfoProperty());
        // 设置InfoStatus
        modifyInformation.setInfoStatus(Information.STATUS_UNPUBLISH);
        // 设置Subject
        modifyInformation.setSubject(information.getSubject());
        // 设置SubjectColor
        modifyInformation.setSubjectColor(information.getSubjectColor());

        // 拷贝正文附件
        Attachment attachment = infoForm.getContentDoc();
        // 拷贝一份附件到指定目录，返回Attachment对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/", Locale.CHINA);
        String dir = SysConstants.INFO_ATTACHMENT_DIR + sdf.format(new Date());
        Attachment attachmentCopy = AttachmentHelper.copyAttachment(attachment, dir);
        if (attachmentCopy != null) {
            modifyInfoForm.setContentDoc(attachmentCopy);// 正文的附件
        }

        // 拷贝源附件
        Set<Attachment> attachments = information.getAttachments();
        Set<Attachment> copyAttachments = null;
        // 生成新的附件Set关联到COPY的信息对象中
        if (attachments != null && !attachments.isEmpty()) {
            copyAttachments = new HashSet<Attachment>(attachments.size());
            for (Attachment a : attachments) {
                copyAttachments.add(AttachmentHelper.copyAttachment(a, dir));
            }
            // 附件
            modifyInformation.setAttachments(copyAttachments);
        }
        // 信息
        modifyInfoForm.setInformation(modifyInformation);
        // 保存信息审批单
        infoFormDAO.save(modifyInfoForm);
        return modifyInfoForm;
    }

    @Override
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String infoFormId) {
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try {
            InfoForm infoForm = infoFormDAO.findById(infoFormId);
            if (infoForm == null) {
                throw new IllegalArgumentException();
            }

            TaskInstance ti = context.getTaskInstance(taskInstanceId);
            if (ti == null) {
                throw new IllegalArgumentException();
            }
            // 更改流程上下文变量
            ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME,
                    ContextVariable.DATATYPE_STRING, infoForm.getInformation().getGroupName());
            ti.getFlowInstance().addContextVariable(var);
            var = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                    infoForm.getInformation().getInfoLayout() + "《" + infoForm.getInformation().getSubject() + "》");
            ti.getFlowInstance().addContextVariable(var);

            ti.setComment(comment);
            if (transitionName == null) {
                ti.end();
            } else {
                ti.end(transitionName);
            }
            context.save(ti);
        } catch (RuntimeException e) {
            LOG.error("txDealApproveTask failed: ", e);
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
    }

//    private void txSendEmail(String infoFormId) throws OaException, IOException, RpcAuthorizationException {
//        if (infoFormId == null) {
//            throw new OaException("参数异常：信息审批单ID为空");
//        }
//        // 根据ID查找信息审批单
//        InfoForm infoForm = this.getInfoFormById(infoFormId);
//        if (infoForm == null) {
//            throw new OaException("参数异常：信息审批单对象为空");
//        }
//        Information information = infoForm.getInformation();
//        // 公告类别
//        String infoLayoutName = information.getInfoLayout();
//        // 根据分类名称取得信息流程配置
//        InfoLayout infoLayout = infoLayoutBiz.getLayoutByName(infoLayoutName);
//        // 不需要发送邮件通知则退出
//        if (!infoLayout.getIsEmail()) {
//            return;
//        }
//
//        // 先获取收件人地址
//        String emailAddrs = generateMailAddr(infoLayoutName);
//        // 如果收件人为空，则直接返回，不发送邮件
//        if (StringUtils.isEmpty(emailAddrs)) {
//            return;
//        }
//
//        // 标题
//        String title = SysRuntimeParams.loadSysRuntimeParams().getInfoLayoutMailTitle();
//        title = title.replaceAll("@infoLayoutName", infoLayoutName);
//        // 内容
//        String content = generateMailContent(infoForm);
//
//        // 发送邮件
//        MailMessage msg = new MailMessage(emailAddrs, title, content);
//        try {
//            JMailProxy.sendMail(msg);
//        } catch (MessagingException e) {
//            LOG.error("txSendEmail failed: ", e);
//        }
//    }

    /**
     * 获取公告邮件通知内容
     * 
     * @param infoForm 公告邮件通知内容
     * @return 信息审批单
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	          李海根	新建
     * </pre>
     * @throws RpcAuthorizationException 异常
     * @throws MalformedURLException 异常
     */
//    private String generateMailContent(InfoForm infoForm) throws MalformedURLException, RpcAuthorizationException {
//        // 获取邮件内容配置
//        String content = SysRuntimeParams.loadSysRuntimeParams().getInfoLayoutMailContent();
//        Information information = infoForm.getInformation();
//        // 发布人
//        String publisherName = information.getDraftsManName();
//        // 公告分类名称
//        String infoLayoutName = information.getInfoLayout();
//        // 公告类别名称
//        String infoClass = information.getInfoClass();
//        // 标题
//        String subject = information.getSubject();
//        // 发布时间
//        Date publicDate = information.getPublicDate();
//        // 链接配置
//        String subSystemId = SysConstants.SYSTEM_ID;
//        SubSystemService subSystemService = new SubSystemService();
//        // 根据系统ID取得子系统配置
//        SubSystemConfig subSystemConfig = subSystemService.getSubSystemConfig(subSystemId);
//        // 服务域
//        String domainName = subSystemConfig.getDomainName();
//        // 服务名
//        String serverName = subSystemConfig.getServerName();
//        // 拼接请求地址
//        StringBuffer viewUrlSB = new StringBuffer("http://");
//        viewUrlSB.append(domainName);
//        viewUrlSB.append("/");
//        viewUrlSB.append(serverName);
//        viewUrlSB.append("/page/portlet?act=viewinfo&id=");
//        viewUrlSB.append(infoForm.getInformation().getId());
//
//        // 格式化时间（到天）
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        // 替换需要替换的内容
//        content = content.replaceAll("@infoLayoutName", infoLayoutName).replaceAll("@publisher", publisherName)
//                .replaceAll("@subject", subject).replaceAll("@viewAddress", viewUrlSB.toString());
//        // 替换infoClass
//        if (StringUtils.isNotEmpty(infoClass)) {
//            content = content.replaceAll("@infoClass", infoClass);
//        } else {
//            content = content.replaceAll("-> @infoClass", "");
//        }
//        // 替换发布时间
//        if (publicDate == null) {
//            content = content.replaceAll("@publishTime", "");
//        } else {
//            content = content.replaceAll("@publishTime", sdf.format(publicDate));
//        }
//        return content;
//    }

    /**
     * 根据公告类型名称获取收件人邮件地址
     * 
     * @param infoLayoutName 公告类型名称
     * @return 收件人邮件地址，多个用”,“隔开
     * @throws OaException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	            李海根	新建
     * </pre>
     */
//    @SuppressWarnings("unchecked")
//    private String generateMailAddr(String infoLayoutName) throws OaException {
//        if (StringUtils.isEmpty(infoLayoutName)) {
//            throw new OaException("参数异常：公告分类名称为空");
//        }
//        StringBuffer emailAddrs = new StringBuffer();
//        // 根据分类名称取得信息流程配置
//        InfoLayout infoLayout = infoLayoutBiz.getLayoutByName(infoLayoutName);
//        if (infoLayout == null) {
//            throw new OaException("参数异常：信息流程配置对象为空");
//        }
//        // 取得收件人账号
//        String emailUserAccounts = infoLayout.getEmailAddr();
//        if (StringUtils.isNotEmpty(emailUserAccounts)) {
//            // 根据收件人账号取得收件人邮箱地址，多个用”,“分隔
//            List<String> emailUserAccountList = Arrays.asList(emailUserAccounts.split(","));
//            for (String userAccount : emailUserAccountList) {
//                AddressList addressList = addressListBiz.getByAccountId(userAccount);
//                String emailAddr = "";
//                // 先从通讯录里面取邮件，如果没有在通过账号拼接
//                if (addressList != null) {
//                    emailAddr = addressList.getUserEmail();
//                }
//                if (StringUtils.isEmpty(emailAddr)) {
//                    // 根据账号拼接邮箱地址
//                    emailAddr = TransformTool.getEmail(userAccount);
//                }
//                // 如果邮箱地址不为空才加到收件人list里面
//                if (StringUtils.isNotEmpty(emailAddr)) {
//                    emailAddrs.append(emailAddr).append(",");
//                }
//            }
//        }
//        return emailAddrs.toString();
//    }

}
