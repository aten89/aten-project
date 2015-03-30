package org.eapp.oa.info.blo;

import java.io.IOException;
import java.util.List;
import org.eapp.oa.flow.hbean.Task;
import org.eapp.oa.info.dto.InfoFormQueryParameters;
import org.eapp.oa.info.hbean.InfoForm;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.hibernate.ListPage;

/**
 * 信息公告业务逻辑接口
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2013-3-22	李海根	注释修改
 * </pre>
 */
public interface IInfoFormBiz {

    /**
     * 根据ID取得信息公告审批单
     * 
     * @param id ID
     * @return 信息公告审批单
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public InfoForm getInfoFormById(String id);

    /**
     * 查询待处理的信息
     * 
     * @param userId 当前登入者账号
     * @return 待处理的信息
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public List<InfoForm> getDealingInfoFrom(String userId);

    /**
     * 跟踪信息
     * 
     * @param qp 查询参数
     * @param userAccountId 当前登入者账号
     * @return 信息
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根         注释修改
     * </pre>
     */
    public ListPage<InfoForm> getTrackInfoFrom(InfoFormQueryParameters qp, String userAccountId);

    /**
     * 归档信息
     * 
     * @param qp 查询参数
     * @param userAccountId 当前登入者账号
     * @return 信息
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public ListPage<InfoForm> getArchInfoFrom(InfoFormQueryParameters qp, String userAccountId);

    /**
     * 根据状态查询用记起草的信息表单
     * 
     * @param userAccountId 当前登入者账号
     * @param infoStatus 状态
     * @return 信息表单
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public List<InfoForm> getInfoForm(String userAccountId, int infoStatus);

    /**
     * 保存信息表单
     * 
     * @param infoFormId 表单ID
     * @param usreAccountId 用户账号
     * @param subject 标题
     * @param subjectColor 颜色
     * @param infoLayout 类别
     * @param infoClass 分类
     * @param groupName 部门
     * @return 信息表单
     * @throws OaException 异常
     */
    public InfoForm addOrModifyInfoForm(String infoFormId, String usreAccountId, String subject, String subjectColor,
            String infoLayout, String infoClass, String groupName, int displayMode, String content) throws OaException;

    /**
     * 保存HTML内容
     * @param infoFormId
     * @param content
     */
    public void txSaveContent(String infoFormId, String content);
    
    /**
     * 保存信息表单的内容DOC
     * 
     * @param infoId 表单ID
     * @param am 附件
     */
    public void addContentDoc(String infoId, Attachment am);

    /**
     * 取得信息表单的内容DOC
     * 
     * @param infoId 表单ID
     * @return 附件
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public Attachment getContentDoc(String infoId);

    /**
     * 删除未发布的信息表单，只能删除未发布的
     * 
     * @param id 表单ID
     * @param accountId 用户账号
     * @throws OaException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    void deleteInfoForm(String id, String accountId) throws OaException;

    /**
     * 启动信息审批流程
     * 
     * @param id 表单ID
     * @param flowkey 流程key
     * @return 信息表单
     * @throws OaException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    InfoForm txStartFlow(String id, String flowkey) throws OaException;

    /**
     * 根据表单视图取得已结束的任务
     * 
     * @param formId 表单ID
     * @return 已结束的任务
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public List<Task> getEndedTasks(String formId);

    /**
     * txCancellationInfoForm
     * 
     * @param infoFormId 表单ID
     * @throws OaException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public void txCancellationInfoForm(String infoFormId) throws OaException;

    /**
     * 根绝CopyId查找是否已经存在复制对象
     * 
     * @param infoFormId 表单ID
     * @return 信息表单
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public InfoForm getInfoFormByCopyId(String infoFormId);

    /**
     * 烤贝信息审批单
     * 
     * @param infoFormId 表单ID
     * @param accountId 用户账号
     * @return 信息表单
     * @throws OaException 异常
     * @throws IOException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public InfoForm txCopyInfoForm(String infoFormId, String accountId) throws OaException, IOException;

    /**
     * 审批任务并更新流程引擎中的环境变量
     * 
     * @param taskInstanceId 流程实例ID
     * @param comment 注释
     * @param transitionName 节点名称
     * @param infoFormId 表单ID
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public void txDealApproveTask(String taskInstanceId, String comment, String transitionName, String infoFormId);

    /**
     * 添加infoForm
     * 
     * @param infoForm 信息表单
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-22	李海根	注释修改
     * </pre>
     */
    public void addInfoForm(InfoForm infoForm);

}
