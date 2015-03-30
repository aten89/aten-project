package org.eapp.poss.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zsy
 * @version
 */
public class SysConstants {
    
    /**
     * 绑定在session中的用户信息
     */
    public static String SESSION_USER_KEY;

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
    /**
     * 退款须知附件路径
     */
    public static final String REFUNC_NOTICE_DIR = "refuncNotice/";
    /**
     * 退款申请附件路径
     */
    public static final String CUST_REFUND_DIR = "custRefund/";
    /**
     * 产品附件路径
     */
    public static final String PROD_INFO_ATTACH_DIR = "prodInfo/";
    /**
     * 划款附件路径
     */
    public static final String PAYMENT_INFO_ATTACH_DIR = "payMent/";

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
   
    /**
     * 绑定在流程引擎上下文变量的发名称---流程分类
     */
    public static final String FLOW_VARNAME_FLOWCLASS = "flowClass";
    /**
     * prod status
     */
    public static final String PROD_STATUS = "PROD_STATUS";
    /**
     * sell rank
     */
    public static final String SELL_RANK = "SELL_RANK";
    
    
    /**
     * 字典KEY
     */
//    public static final String FLOW_CLASS_KEY;//流程分类
    
    
    
    private static Log log = LogFactory.getLog(SysConstants.class);
    static {
		Properties prop = new Properties();
		InputStream pin = null;
		try {
			pin = SysConstants.class.getResourceAsStream("/config.properties");
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
//		FLOW_CLASS_KEY = prop.getProperty("FLOW_CLASS_KEY");
	}
}
