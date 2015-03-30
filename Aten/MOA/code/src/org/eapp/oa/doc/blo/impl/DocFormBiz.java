package org.eapp.oa.doc.blo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eapp.oa.doc.blo.IDocFormBiz;
import org.eapp.oa.doc.dao.IDocFormDAO;
import org.eapp.oa.doc.dto.DocFormQueryParameters;
import org.eapp.oa.doc.hbean.DocForm;
import org.eapp.oa.flow.dao.ITaskDAO;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.info.dao.IInformationDAO;
import org.eapp.oa.info.hbean.Information;
import org.eapp.oa.system.config.SysConstants;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.oa.system.util.AttachmentHelper;
import org.eapp.oa.system.util.SerialNoCreater;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;
import org.eapp.workflow.WfmConfiguration;
import org.eapp.workflow.WfmContext;
import org.eapp.workflow.exe.ContextVariable;
import org.eapp.workflow.exe.FlowInstance;
import org.eapp.workflow.exe.TaskInstance;
import org.hibernate.Hibernate;

/**
 * 
 * @author TimLin
 * @version 创建时间：May 4, 2009
 * 
 */
public class DocFormBiz implements IDocFormBiz {
    private IDocFormDAO docFormDAO;
    private ITaskDAO taskDAO;
    private IInformationDAO informationDAO;

    public IDocFormDAO getDocFormDAO() {
        return docFormDAO;
    }

    public void setDocFormDAO(IDocFormDAO docFormDAO) {
        this.docFormDAO = docFormDAO;
    }

    public ITaskDAO getTaskDAO() {
        return taskDAO;
    }

    public void setTaskDAO(ITaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public IInformationDAO getInformationDAO() {
        return informationDAO;
    }

    public void setInformationDAO(IInformationDAO informationDAO) {
        this.informationDAO = informationDAO;
    }

    /**
     * 获取公文列表
     */
    @Override
    public List<DocForm> getDocForm(String userAccountId, int docStatus, int fileClass) {

        if (userAccountId == null) {
            return null;
        }
        List<DocForm> list = docFormDAO.findDocForm(userAccountId, docStatus, fileClass);
        return list;
    }

    /**
     * 
     * @author TimLin
     * @return 跟进id获取docForm
     */
    public DocForm getDocFormById(String id) {
        return docFormDAO.findById(id);
    }

    /**
     * 删除公文草稿
     */
    public void deleteDocForm(String id, String accountId) throws OaException {
        if (id == null) {
            return;
        }
        DocForm docForm = docFormDAO.findById(id);
        if (docForm == null) {
            return;
        }
        if (DocForm.STATUS_UNPUBLISH != docForm.getDocStatus()) {
            throw new OaException("只能删除未发布的消息");
        } else if (!accountId.equals(docForm.getDraftsman())) {
            throw new OaException("只能删除自己发布的消息");
        }

        if (docForm.getBodyDoc() != null) {
            // 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(docForm.getBodyDoc().getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (docForm.getBodyDraftDoc() != null) {
            // 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(docForm.getBodyDraftDoc().getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (docForm.getAttachments() != null) {
            for (Attachment am : docForm.getAttachments()) {
                // 删除文件
            	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
    			if (f != null) {
    				f.delete();
    			}
            }
        }

        docFormDAO.delete(docForm);
    }

    public void addContentDoc(String docId, Attachment am) {
        if (docId == null) {
            throw new IllegalArgumentException();
        }
        DocForm docForm = docFormDAO.findById(docId);
        if (docForm == null) {
            throw new IllegalArgumentException("公文表单不存在");
        }

        Attachment oam = docForm.getBodyDraftDoc();

        if (oam != null) {
            // 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(oam.getFilePath()));
			if (f != null) {
				f.delete();
			}
            docFormDAO.delete(oam);
        }
        docForm.setBodyDraftDoc(am);
        docFormDAO.saveOrUpdate(docForm);

    }

    public void addFinalContentDoc(String docId, Attachment am, String redHeaderStr) throws OaException {
        if (docId == null) {
            throw new IllegalArgumentException();
        }
        DocForm docForm = docFormDAO.findById(docId);
        if (docForm == null) {
            throw new IllegalArgumentException("公文表单不存在");
        }
        Attachment oam = docForm.getBodyDoc();
        if (oam != null) {
            // 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
			if (f != null) {
				f.delete();
			}
            docFormDAO.delete(oam);
        }
        docForm.setBodyDoc(am);
        if (redHeaderStr != null) {
            docForm.setDocNumber(redHeaderStr);
        } else if (docForm.getDocNumber() == null) {
            throw new OaException("发文字号不能为空");
        }
        docFormDAO.saveOrUpdate(docForm);
    }

    public Attachment getContentDoc(String docId) {
        if (docId == null) {
            throw new IllegalArgumentException();
        }
        DocForm docForm = docFormDAO.findById(docId);
        if (docForm == null) {
            throw new IllegalArgumentException("公文表单不存在");
        }
        return docForm.getBodyDraftDoc();
    }

    public void txUpdateContentUrl(String docId, String contentDir, String indexFileName, String[] infoLayouts)
            throws IOException {
        if (docId == null) {
            throw new IllegalArgumentException();
        }
        DocForm docForm = docFormDAO.findById(docId);
        if (docForm == null) {
            throw new IllegalArgumentException("公文不存在");
        }

        // 删除原来的information
        if (docForm.getCopyDocFormId() != null) {
            DocForm oldForm = docFormDAO.findById(docForm.getCopyDocFormId());
            Set<Information> informations = oldForm.getInformations();
            oldForm.setInformations(null);
            docFormDAO.saveOrUpdate(oldForm);

            for (Information info : informations) {
                if (info.getContentUrl() != null) {
                    String oUrl = info.getContentUrl();
                    int index = oUrl.lastIndexOf("/");
                    if (index != -1) {
                        String dir = oUrl.substring(0, index + 1);
                        // 删除文件
                        List<File> delDirs = FileDispatcher.findDirs(FileDispatcher.getAbsPath(dir));
                        if (delDirs != null && !delDirs.isEmpty()) {
                        	//只会有一个目录
                        	FileUtil.delDir(delDirs.get(0));
                        }
                    }
                }
                if (info != null && info.getAttachments() != null && info.getAttachments().size() > 0) {
                    for (Attachment infoAtt : info.getAttachments()) {
                    	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(infoAtt.getFilePath()));
            			if (f != null) {
            				f.delete();
            			}
                    }
                }
                informationDAO.delete(info);
            }
        }

        // 插入新的
        Set<Information> infos = new HashSet<Information>();
        if (infoLayouts != null && infoLayouts.length > 0) {

            boolean isFirst = true;
            for (String layout : infoLayouts) {
                String newPath = null;
                if (isFirst) {
                    newPath = contentDir + indexFileName;
                    isFirst = false;
                } else {
                    newPath = copyDocHtmPath(contentDir) + indexFileName;
                }
                Information info = new Information();
                info.setInfoLayout(layout);
                info.setSubject(docForm.getSubject());
                info.setDraftDate(docForm.getDraftDate());
                info.setDraftsMan(docForm.getDraftsman());
                info.setGroupName(docForm.getGroupName());
                info.setInfoStatus(Information.STATUS_PUBLISH);
                info.setInfoProperty(Information.PROPERTY_COMMON);
                info.setContentUrl(newPath);
                info.setDisplayMode(Information.DISPLAYMODE_URL);
                info.setPublicDate(new Date());
                informationDAO.save(info);
                // 复制附件
                Set<Attachment> ams = new HashSet<Attachment>();
                for (Attachment oam : docForm.getAttachments()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
                    Attachment nam = AttachmentHelper.copyAttachment(oam,
                            SysConstants.DOC_ATTACHMENT_DIR + sdf.format(new Date()));
                    ams.add(nam);
                }
                info.setAttachments(ams);
                informationDAO.saveOrUpdate(info);
                infos.add(info);
            }
        }
        docForm.setInformations(infos);

        docFormDAO.saveOrUpdate(docForm);
    }

    private String copyDocHtmPath(String dir) throws IOException {
        File root = new File(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(dir)));
        File[] dirOrFile = root.listFiles();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
        String nDir = SysConstants.DOC_HTMLPAGE_DIR + sdf.format(new Date());
        String newPath = nDir + SerialNoCreater.createUUID() + "/";
        for (int i = 0; i < dirOrFile.length; i++) {
            if (dirOrFile[i].isFile()) {
                String newFile = newPath + dirOrFile[i].getName();
                FileInputStream in = new FileInputStream(dirOrFile[i]);
                FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(newFile)), in);
            }
        }
        return newPath;
    }

    public Set<Attachment> getInfoAttachments(String docId) {
        if (docId == null) {
            throw new IllegalArgumentException();
        }
        DocForm docFrom = docFormDAO.findById(docId);
        if (docFrom == null) {
            throw new IllegalArgumentException("公文不存在");
        }
        Hibernate.initialize(docFrom.getAttachments());
        return docFrom.getAttachments();
    }

    public void txUpdateAttachment(String docId, String[] deletedFiles, List<Attachment> files) {
        if (docId == null) {
            throw new IllegalArgumentException();
        }
        DocForm docForm = docFormDAO.findById(docId);
        if (docForm == null) {
            throw new IllegalArgumentException("公文不存在");
        }

        // 删除旧附件
        if (deletedFiles != null && deletedFiles.length > 0 && docForm.getAttachments() != null
                && docForm.getAttachments().size() > 0) {
            List<String> delFileList = Arrays.asList(deletedFiles);
            // 通过名称删除附件，前台控件只支持名称删除
            for (Attachment am : new ArrayList<Attachment>(docForm.getAttachments())) {
                if (delFileList.contains(am.getDisplayName() + am.getFileExt())) {
                    docForm.getAttachments().remove(am);

                    // 删除文件
                    File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
        			if (f != null) {
        				f.delete();
        			}
                }
            }
        }
        // 添加新附件
        docForm.getAttachments().addAll(files);
        docFormDAO.saveOrUpdate(docForm);
    }

    public DocForm addOrModifyDocForm(String docFormId, String usreAccountId, String subject, String groupName,
            String toDept, String ccDept, String docClassName, String docSecurity, String docUrgency,
            String signGroupNames, int fileClass) throws OaException {
        DocForm docForm = null;
        // Information info = null;
        if (docFormId != null) {
            docForm = docFormDAO.findById(docFormId);
            if (docForm == null) {
                throw new OaException("该公文已不存在");
            }
        }
        if (docForm == null) {
            docForm = new DocForm();
            docForm.setDraftsman(usreAccountId);
            docForm.setDocClassName(docClassName);
            docForm.setDocStatus(DocForm.STATUS_UNPUBLISH);
            docForm.setDraftDate(new Timestamp(System.currentTimeMillis()));
        }
        docForm.setSignGroupNames(signGroupNames);
        docForm.setSubject(subject);
        docForm.setGroupName(groupName);
        docForm.setSubmitTo(toDept);
        docForm.setCopyTo(ccDept);
        docForm.setSecurityClass(docSecurity);
        docForm.setUrgency(docUrgency);
        docForm.setFileClass(fileClass);
        docFormDAO.saveOrUpdate(docForm);
        return docForm;
    }

    @Override
    public DocForm txStartFlow(String id, String flowKey, String signGroupNames, String users) throws OaException {

        if (id == null) {
            return null;
        }
        DocForm docForm = docFormDAO.findById(id);
        if (docForm == null) {
            return null;
        }
        if (docForm == null || docForm.getBodyDraftDoc() == null || docForm.getBodyDraftDoc().getFilePath() == null) {
            throw new OaException("公文内容不存在");
        }
        if (flowKey == null) {// 不启动流程，直接发布
            Date now = new Date();
            docForm.setPassed(true);
            docForm.setArchiveDate(now);
            docForm.setDocStatus(DocForm.STATUS_PUBLISH);
//            for (Information info : new ArrayList<Information>(docForm.getInformations())) {
//                info.setPublicDate(now);
//                info.setInfoStatus(Information.STATUS_PUBLISH);
//            }
            // 删除原先的信息和TASK
            if (docForm.getCopyDocFormId() != null) {
            	deleteOldDocFormAndTask(docForm.getCopyDocFormId());
            }
        } else {
            docForm.setDocStatus(DocForm.STATUS_APPROVAL);
            WfmContext context = WfmConfiguration.getInstance().getWfmContext();
            try {
                // 设置流程上下文变量中，并启动流程
                Map<String, ContextVariable> contextVariables = new HashMap<String, ContextVariable>();
                // 把表单ID
                ContextVariable cv = new ContextVariable(SysConstants.FLOW_VARNAME_FORMID,
                        ContextVariable.DATATYPE_STRING, docForm.getId());
                contextVariables.put(cv.getName(), cv);
                // 发起人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_USERACCOUNTID, ContextVariable.DATATYPE_STRING,
                        docForm.getDraftsman());
                contextVariables.put(cv.getName(), cv);

                // 指定审批人
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO, ContextVariable.DATATYPE_STRING, users);
                contextVariables.put(cv.getName(), cv);

                // 部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_GROUPNAME, ContextVariable.DATATYPE_STRING,
                        docForm.getGroupName());
                contextVariables.put(cv.getName(), cv);

                // 会签部门
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_SIGNGROUPNAMES, ContextVariable.DATATYPE_STRING,
                        signGroupNames);
                contextVariables.put(cv.getName(), cv);

//                // 程序执行类
//                cv = new ContextVariable(SysConstants.FLOW_VARNAME_EXECUTIONCLASS, ContextVariable.DATATYPE_STRING,
//                        executionClass);
//                contextVariables.put(cv.getName(), cv);
//
//                // 程序执行时间
//                cv = new ContextVariable(SysConstants.FLOW_VARNAME_EXECUTIONTIME, ContextVariable.DATATYPE_STRING,
//                        endTime);
//                contextVariables.put(cv.getName(), cv);

                // 任务描述
                cv = new ContextVariable(SysConstants.FLOW_VARNAME_TASKDESCRIPTION, ContextVariable.DATATYPE_STRING,
                        docForm.getDocClassName() + "《" + docForm.getSubject() + "》");
                contextVariables.put(cv.getName(), cv);
                FlowInstance flowInstance = context.newFlowInstance(flowKey, contextVariables);
                flowInstance.signal();
                context.save(flowInstance);
                // 设置表单视图的ID
                String odlFlowInsId = docForm.getFlowInstanceId();
                docForm.setFlowInstanceId(flowInstance.getId());
                // 判断该信息是否是进行修改的信息,如果是进行修改的则把源信息的TSAK内容重新COPY一份设置它的FlowInstanceID和FormID.
                if (docForm.getCopyDocFormId() != null) {
                    txCopyTasks(docForm, odlFlowInsId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                context.rollback();
                throw new OaException(e.getMessage());
            } finally {
                context.close();
            }
        }

        docFormDAO.saveOrUpdate(docForm);
        return docForm;
    }

    private void deleteOldDocFormAndTask(String id) throws OaException {

        DocForm docForm = docFormDAO.findById(id);
        if (docForm == null) {
            return;
        }
        String flowInstId = docForm.getFlowInstanceId();
        if (docForm.getBodyDraftDoc() != null) {
            // 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(docForm.getBodyDraftDoc().getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (docForm.getBodyDoc() != null) {
            // 删除文件
        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(docForm.getBodyDoc().getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (docForm.getAttachments() != null) {
            for (Attachment am : docForm.getAttachments()) {
                // 删除文件
            	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(am.getFilePath()));
    			if (f != null) {
    				f.delete();
    			}
            }
        }
//        if (docForm.getInformations() != null) {
//            // 删除HTML附件
//            for (Information info : docForm.getInformations()) {
//                String delUrl = info.getContentUrl();
//                int index = delUrl.lastIndexOf("/");
//                if (index != -1) {
//                    String dir = delUrl.substring(0, index + 1);
//                    // 删除文件
//                    FileUtil.delDir(FileDispatcher.getAbsPath(dir));
//                }
//            }
//        }
        // 删除Task表
        if (flowInstId != null) {
            List<Task> tasks = taskDAO.findEndedTasks(flowInstId);
            if (tasks != null && tasks.size() > 0) {
                for (Task t : tasks) {
                    taskDAO.delete(t);
                }
            }
        }
        // 删除信息
        docFormDAO.delete(docForm);
    }

    public void txCopyTasks(DocForm docForm, String oldFlowInstId) {
        List<Task> tasks = taskDAO.findEndedTasks(oldFlowInstId);
        if (tasks == null) {
            return;
        }
        List<Task> newTasks = new ArrayList<Task>();
        for (Task t : tasks) {
            Task tempTask = new Task();
            tempTask.setComment(t.getComment());
            tempTask.setCreateTime(t.getCreateTime());
            tempTask.setDescription(t.getDescription());
            tempTask.setEndTime(t.getEndTime());
            tempTask.setFlowConfig(t.getFlowConfig());
            tempTask.setFlowInstanceID(docForm.getFlowInstanceId());
            tempTask.setFlowKey(t.getFlowKey());
            tempTask.setFlowName(t.getFlowName());
            tempTask.setFormID(docForm.getId());
            tempTask.setNodeName(t.getNodeName());
            tempTask.setStartTime(t.getStartTime());
            tempTask.setTaskState(t.getTaskState());
            tempTask.setTransactor(t.getTransactor());
            tempTask.setViewFlag(t.getViewFlag());
            tempTask.setTaskInstanceID(t.getTaskInstanceID());
            tempTask.setTaskName(t.getTaskName());
            tempTask.setFlowDefineID(t.getFlowDefineID());
            newTasks.add(tempTask);
        }
        // 复制完后模拟再插入一条重新修订
        Task task = newTasks.get(0);
        Task modifyTask = new Task();
        modifyTask.setComment("重新修订");
        modifyTask.setCreateTime(task.getCreateTime());
        modifyTask.setDescription(task.getDescription());
        modifyTask.setEndTime(new Date());
        modifyTask.setFlowConfig(task.getFlowConfig());
        modifyTask.setFlowInstanceID(docForm.getFlowInstanceId());
        modifyTask.setFlowKey(task.getFlowKey());
        modifyTask.setFlowName(task.getFlowName());
        modifyTask.setFormID(docForm.getId());
        modifyTask.setNodeName(task.getNodeName());
        modifyTask.setStartTime(task.getStartTime());
        modifyTask.setTaskState(task.getTaskState());
        modifyTask.setTransactor(docForm.getDraftsman());
        modifyTask.setViewFlag(task.getViewFlag());
        modifyTask.setTaskInstanceID(task.getTaskInstanceID());
        modifyTask.setTaskName(task.getTaskName());
        modifyTask.setFlowDefineID(task.getFlowDefineID());
        newTasks.add(modifyTask);
        for (Task t : newTasks) {
            taskDAO.save(t);
        }
    }

    public List<DocForm> queryDealingDocFrom(String userId, int fileClass) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        return docFormDAO.queryDealingDocForm(userId, fileClass);

    }

    public List<Task> getEndedTasks(String formId) {
        DocForm docForm = docFormDAO.findById(formId);
        if (docForm == null) {
            throw new IllegalArgumentException("非法参数:formId");
        }
        return taskDAO.findEndedTasks(docForm.getFlowInstanceId());
    }

//    public List<Task> getDocTasks(String formId) {
//        DocForm docForm = docFormDAO.findById(formId);
//        if (docForm == null) {
//            throw new IllegalArgumentException("非法参数:formId");
//        }
//        return taskDAO.findDocTasks(docForm.getFlowInstanceId());
//    }

    public ListPage<DocForm> getTrackDocForm(DocFormQueryParameters qp, String userAccountId, int fileClass) {
        if (userAccountId == null) {
            throw new IllegalArgumentException();
        }

        ListPage<DocForm> page = docFormDAO.queryTrackDocForm(qp, userAccountId, fileClass);
        if (page != null && page.getDataList() != null) {
            List<DocForm> list = page.getDataList();
            for (DocForm r : list) {// 取得最后结束的一个任务findDealingTaskList
                List<Task> tasks = taskDAO.findDealingTaskList(r.getFlowInstanceId());
                StringBuffer sb = new StringBuffer();
                if (tasks != null && tasks.size() > 0) {
                    for (Task t : tasks) {
                        if (sb.length() > 0) {
                        	sb.append("<BR>");
                        }
                        sb.append((t.getTaskState().equals(TaskInstance.PROCESS_STATE_CREATE)) ? ("<b>"
                                + t.getTransactorDisplayName() + "</b>") : t.getTransactorDisplayName());
                    }
                    Task task = tasks.get(0);
                    task.setTransactor(sb.toString());
                    r.setTask(task);
                }
            }
        }
        return page;
    }

    public ListPage<DocForm> getArchDocForm(DocFormQueryParameters qp, String userAccountId, int fileClass) {
        if (userAccountId == null) {
            throw new IllegalArgumentException();
        }
        return docFormDAO.queryArchDocForm(qp, userAccountId, fileClass);
    }

    public List<DocForm> getDocFormByCopyId(String docFormId) {
        List<DocForm> docForm = docFormDAO.findByProperty("copyDocFormId", docFormId);
        return docForm;
    }

    public DocForm txCopyDocForm(String docFormId, String accountId) throws OaException, IOException {

        if (docFormId == null) {
            throw new IllegalArgumentException();
        }
        // 获得原先的信息
        DocForm docForm = getDocFormById(docFormId);
        if (docForm == null) {
            throw new OaException("记录不存在");
        } else if (!accountId.equals(docForm.getDraftsman())) {
            throw new OaException("此消息不是您发布的不允许修改");
        } else if (!docForm.getPassed()) {
            throw new OaException("此消息已作废不允许修改");
        }
        DocForm modifyDocForm = new DocForm();// 复制
        modifyDocForm.setFlowInstanceId(docForm.getFlowInstanceId());// 流程ID
        // 保存原先ID
        modifyDocForm.setCopyDocFormId(docForm.getId());
        // 拷贝草稿附件
        Attachment attachment = docForm.getBodyDraftDoc();
        // 拷贝一份附件到指定目录，返回Attachment对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
        String dir = SysConstants.DOC_ATTACHMENT_DIR + sdf.format(new Date());
        Attachment attachmentCopy = AttachmentHelper.copyAttachment(attachment, dir);
        if (attachmentCopy != null) {
            modifyDocForm.setBodyDraftDoc(attachmentCopy);
        }
        modifyDocForm.setDocNumber(docForm.getDocNumber());
        modifyDocForm.setDraftsman(docForm.getDraftsman());
        modifyDocForm.setGroupName(docForm.getGroupName());
        modifyDocForm.setDraftDate(docForm.getDraftDate());
        modifyDocForm.setSubmitTo(docForm.getSubmitTo());
        modifyDocForm.setCopyTo(docForm.getCopyTo());
        modifyDocForm.setUrgency(docForm.getUrgency());
        modifyDocForm.setDocStatus(DocForm.STATUS_UNPUBLISH);
        modifyDocForm.setSecurityClass(docForm.getSecurityClass());
        modifyDocForm.setSubject(docForm.getSubject());
        modifyDocForm.setPassed(docForm.getPassed());
        modifyDocForm.setDocClassName(docForm.getDocClassName());
        modifyDocForm.setSignGroupNames(docForm.getSignGroupNames());
        modifyDocForm.setFileClass(docForm.getFileClass());
        // 拷贝源附件
        Set<Attachment> attachments = docForm.getAttachments();
        Set<Attachment> copyAttachments = null;
        // 生成新的附件Set关联到COPY的信息对象中
        if (attachments != null && attachments.size() > 0) {
            copyAttachments = new HashSet<Attachment>();
            for (Attachment a : attachments) {
                Attachment tempAttachment = AttachmentHelper.copyAttachment(a, dir);
                copyAttachments.add(tempAttachment);
            }
            modifyDocForm.setAttachments(copyAttachments);// 附件
        }
        docFormDAO.save(modifyDocForm);
        return modifyDocForm;
    }

    public void txCancellationDocForm(String docFormId) throws OaException {
        if (docFormId == null) {
            throw new IllegalArgumentException("非法参数:infoFormId");
        }
        DocForm docForm = docFormDAO.findById(docFormId);
        docForm.setPassed(false);
        docForm.setDocStatus(DocForm.STATUS_CANCELLATION);
        docForm.setArchiveDate(new Date());
        docFormDAO.saveOrUpdate(docForm);
    }

    public List<DocForm> getDocFormByDocNum(String numStr) {
        List<DocForm> list = docFormDAO.findByDocNumber(numStr);
        return list;
    }

//    public void txDealApproveAssign(String taskInstanceId, String comment, String transitionName, String users) {
//        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
//        try {
//            TaskInstance ti = context.getTaskInstance(taskInstanceId);
//            if (ti == null) {
//                throw new IllegalArgumentException();
//            }
//            // 更改流程上下文变量
//            ContextVariable var = new ContextVariable(SysConstants.FLOW_VARNAME_APPOINTTO,
//                    ContextVariable.DATATYPE_STRING, users);
//            ti.getFlowInstance().addContextVariable(var);
//            ti.setComment(comment);
//            if (transitionName != null) {
//                ti.end(transitionName);
//            } else {
//                ti.end();
//            }
//            context.save(ti);
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            context.rollback();
//            throw e;
//        } finally {
//            context.close();
//        }
//    }

    /**
     * 强制结束流程，进入归档状态
     * 
     * @param docFormId
     * @param comment
     */
    public void txForceEnd(String docFormId, String comment) {
        WfmContext context = WfmConfiguration.getInstance().getWfmContext();
        try {
            DocForm docForm = docFormDAO.findById(docFormId);
            if (docForm == null) {
                throw new IllegalArgumentException("目标文件不存在。");
            }

            String flowInstanceId = docForm.getFlowInstanceId();

            // 同时，还要结束OA_TASK中的任务
            taskDAO.endTasksByFlowInstanceID(flowInstanceId, comment);

            // 强制结束流程引擎的流程实例
            FlowInstance fi = context.getFlowInstance(flowInstanceId);
            fi.end();

            // 最后，将文件改为“作废”状态
            docForm.setPassed(false);
            docForm.setDocStatus(DocForm.STATUS_CANCELLATION);
            docForm.setArchiveDate(new Date());
            docFormDAO.saveOrUpdate(docForm);

        } catch (RuntimeException e) {
            e.printStackTrace();
            context.rollback();
            throw e;
        } finally {
            context.close();
        }
    }
}
