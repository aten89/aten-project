package org.eapp.oa.system.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.oa.system.util.web.HttpRequestHelper;

/**
 * @author zsy
 * @version
 */
public class SysConstants {

	/**
	 * 以上常量从ERMP-ClIENT的配置文件中加载
	 */
    public static String SYSTEM_ID;// 当前系统ID
    public static String SESSION_USER_KEY;// 绑定在session中的用户信息
    
    
    public static final String REQUEST_ERROR_MSG = "REQUEST_ERROR_MSG";// 绑定到页面错误信息
    
    public static final String SESSION_KNOW_IMG = "sessionKnowImg";// 绑定在session中的上传知识库图片
 
    public static final String SESSION_MEET_NOTICE_IMG = "sessionMeetNoticeImg";// 绑定在session中的上传会议通知图片
   
    /**
     * 系统动作
     */
    public static final String ADD = "add"; // 新增
    public static final String VIEW = "view"; // 查看
    public static final String MODIFY = "modify"; // 修改
    public static final String QUERY = "query"; // 查询
    public static final String DELETE = "delete"; // 删除
    public static final String ENABLE = "enable"; // 启用
    public static final String DISABLE = "disable"; // 禁用
    public static final String BIND_FLOW = "bindflow"; // 绑定流程
    public static final String BIND_POST = "bindpost"; // 绑定职位
    public static final String BIND_USER = "binduser";// 绑定用户
    public static final String BIND_GROUP = "bindgroup"; // 绑定群组
    public static final String ORDER = "order"; // 排序
    public static final String COPY = "copy"; // 复制
    public static final String CONFIG = "config"; // 配置
    public static final String START_FLOW = "startflow"; // 启动流程
    public static final String DISPOSE = "dispose"; // 处理
    public static final String COMPARE = "compare"; // 对比
    public static final String EXPORT = "export"; // 导出
    public static final String IMPORT = "import"; // 导入
    public static final String ASSIGN = "assign"; // 授权
    public static final String REDEPLOY = "redeploy"; // 重部署
    public static final String SHIELD = "shield"; // 屏蔽
    public static final String TO_TOP = "totop"; // 置顶
   
    public static final String MAINTAIN = "maintain"; // 维修
    public static final String REGISTER = "register"; // 登记
    public static final String RECIPIENTS = "recipients"; // 领用
    public static final String ALLOCATION = "allocation"; // 调拨
    public static final String SCRAP = "scrap"; // 报废
    public static final String SCRAPDEAL = "scrapdeal"; // 报废处理
    
    /**
     *  时间格式
     */
    public static final String STANDARD_DATE_PATTERN = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_PATTERN = "MM-dd";
    public static final String STANDARD_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String FULL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    
    /**
     * 文件保存目录
     */
    public static final String DOC_ATTACHMENT_DIR = "doc/";// 公文附件保存目录
    public static final String DOC_HTMLPAGE_DIR = "docHtmlPage/"; // 公文发布HTML保存目录
    public static final String INFO_ATTACHMENT_DIR = "info/"; // 信息附件保存目录
    public static final String INFO_HTMLPAGE_DIR = "infoHtmlPage/";// 信息发布HTML保存目录
    public static final String ADDRLIST_HD_DIR = "addressList/";// 通讯录头像保存目录
    public static final String KNOWLEDGE_ATTACHMENT_DIR = "knowledge/";// 知识库附件
    
    public static final String TEMPKB_INDEX_DIR = "knowledgeTempIndex/";//正式知识库保存目录
    public static final String FINALKB_INDEX_DIR = "knowledgeFinalIndex/";//临时知识库保存目录
    
    public static final String MEET_ATTACHMENT_DIR = "meeting/"; // 会议附件保存目录
    
    /**
     * 流程引擎上下文变量
     */
    // 绑定在流程引擎上下文变量名称---表单ID
    public static final String FLOW_VARNAME_FORMID = "formId";
    // 绑定在流程引擎上下文变量的名称---发起人帐号
    public static final String FLOW_VARNAME_USERACCOUNTID = "userAccountId";
    // 绑定在流程引擎上下文变量的名称---指定审批人
    public static final String FLOW_VARNAME_APPOINTTO = "appointTo";
    // 绑定在流程引擎上下文变量的名称---任务说明
    public static final String FLOW_VARNAME_TASKDESCRIPTION = "taskDescription";
    // 绑定在流程引擎上下文变量的名称---所属部门
    public static final String FLOW_VARNAME_GROUPNAME = "groupName";
    // 绑定在流程引擎上下文变量的名称---会签部门，多个用“,”分开
    public static final String FLOW_VARNAME_SIGNGROUPNAMES = "signGroupNames";
    // 绑定在流程引擎上下文变量的名称---执行时间
    public static final String FLOW_VARNAME_EXECUTIONTIME = "executionTime";
    // 绑定在流程引擎上下文变量的名称---执行类
    public static final String FLOW_VARNAME_EXECUTIONCLASS = "executionClass";
    
    // 绑定在流程引擎上下文变量的名称---隶属
    public static final String FLOW_VARNAME_AREA = "area";
    // 绑定在流程引擎上下文变量的名称---受款人
    public static final String FLOW_VARNAME_PAYEE = "payee";
    // 绑定在流程引擎上下文变量的名称---是否特批
    public static final String FLOW_VARNAME_ISSPECIAL = "special";
    // 绑定在流程引擎上下文变量的名称---天数
    public static final String FLOW_VARNAME_APPLY_DAYS = "days";
    // 绑定在流程引擎上下文变量的名称---区分领用单和申购单
    public static final String FLOW_VARNAME_FORMTYPE = "formType";
    // 绑定在流程引擎上下文变量的名称---设备调出人
    public static final String FLOW_VARNAME_OUTACCOUNTID = "outAccountId";
    // 绑定在流程引擎上下文变量的名称---设备调出部门
    public static final String FLOW_VARNAME_OUTGROUPNAME = "outGroupName";
    // 绑定在流程引擎上下文变量的名称---设备调入人
    public static final String FLOW_VARNAME_INACCOUNTID = "inAccountId";
    // 绑定在流程引擎上下文变量的名称---设备调入部门
    public static final String FLOW_VARNAME_INGROUPNAME = "inGroupName";
    
    // 绑定在流程引擎上下文变量的名称---入/离职人
    public static final String FLOW_VARNAME_STAFFUSERACCOUNTID = "staffUserAccountId";
    
    // 绑定在流程引擎上下文变量的名称---异动人
    public static final String FLOW_VARNAME_TRANUSERACCOUNTID = "tranAccountId";
    // 绑定在流程引擎上下文变量的名称---转正人
    public static final String FLOW_VARNAME_POSITIVEACCOUNTID = "posiAccountId";
    
    /**
     * 流程类别
     */
    // 报销流程类别
    public static final String FLOWCLASS_BX = "BXLC";
    // 请假流程类别
    public static final String FLOWCLASS_QJ = "QJLC";
    // 入职离职流程类别
    public static final String FLOWCLASS_HR = "HRLC";
    // 出差审批流程类别
    public static final String FLOWCLASS_TRIP = "CCLC";
    // 异动审批流程类别
    public static final String FLOWCLASS_TRAN = "YDLC";
    // 转正审批流程类别
    public static final String FLOWCLASS_POSI = "ZZLC";
    
    /**
     * 字典KEY
     */
    public static final String FLOW_CLASS_KEY;//流程分类
    public static final String DOC_SECURITY_KEY;//公文密级
    public static final String DOC_URGENCY_KEY;//公文缓急
    public static final String COMPANY_AREA_KEY;//所属地区
    public static final String COST_CLASS_KEY;//费用类别
    public static final String DEVICE_TYPE_KEY;//设备分类
    public static final String DEVICE_VALIDATE_KEY;//设备分类
//    public static final String DEV_USETYPE_KEY;//设备用途
    public static final String BUY_TYPE_KEY;//购买方式 
    public static final String ALLOT_TYPE_KEY;//调拨类型 
    public static final String SCRAP_TYPE_KEY;//报废类型 
    public static final String SCRAP_DISPOSE_TYPE_KEY;//报废处理方式 
    public static final String LEAVE_DEAL_TYPE_KEY;//离职处理方式 
    
    public static final String KB_FIRST_TYPE_KEY;//知识库一级分类FIRST-TYPE
    public static final String KB_SECOND_TYPE_KEY;//知识库二级分类SECOND-TYPE
    
    private static Log log = LogFactory.getLog(SysConstants.class);
    static {
		Properties prop = new Properties();
		InputStream pin = null;
		try {
			pin = HttpRequestHelper.class.getResourceAsStream("/config.properties");
			prop.load(pin);
		} catch (Exception e) {
			log.error("加载属性文件失败", e);
		} finally {
			if (pin != null) {
				try {
					pin.close();
				} catch (IOException e) {
					log.error("关闭属性文件失败", e);
				}
			}
		}
		FLOW_CLASS_KEY = prop.getProperty("FLOW_CLASS_KEY");
		DOC_SECURITY_KEY = prop.getProperty("DOC_SECURITY_KEY");
		DOC_URGENCY_KEY = prop.getProperty("DOC_URGENCY_KEY");
		COMPANY_AREA_KEY = prop.getProperty("COMPANY_AREA_KEY");
		COST_CLASS_KEY = prop.getProperty("COST_CLASS_KEY");
		DEVICE_TYPE_KEY = prop.getProperty("DEVICE_TYPE_KEY");
		DEVICE_VALIDATE_KEY = prop.getProperty("DEVICE_VALIDATE_KEY");
//		DEV_USETYPE_KEY = prop.getProperty("DEV_USETYPE_KEY");
		BUY_TYPE_KEY = prop.getProperty("BUY_TYPE_KEY");
		ALLOT_TYPE_KEY = prop.getProperty("ALLOT_TYPE_KEY");
		SCRAP_TYPE_KEY = prop.getProperty("SCRAP_TYPE_KEY");
		SCRAP_DISPOSE_TYPE_KEY = prop.getProperty("SCRAP_DISPOSE_TYPE_KEY");
		LEAVE_DEAL_TYPE_KEY = prop.getProperty("LEAVE_DEAL_TYPE_KEY");
		KB_FIRST_TYPE_KEY = prop.getProperty("KB_FIRST_TYPE_KEY");
		KB_SECOND_TYPE_KEY = prop.getProperty("KB_SECOND_TYPE_KEY");
	}
}
