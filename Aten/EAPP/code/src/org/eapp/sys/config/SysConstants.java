package org.eapp.sys.config;

/**
 * @author zsy
 * @version 
 */
public class SysConstants {

	//绑定在session中的用户信息
	public static final String SESSION_USER_KEY = "sessionUser";
	public static final String RPCSESSION_USER_KEY = "RPCSESSION_USER";
	//绑定在session中的RPCAuthorizationFilter
	public static final String RPCSESSION_AUTHORIZATIONFILTER_KEY = "RPCSESSION_AUTHORIZATIONFILTER_KEY";
	//当前系统ID
	public static final String EAPP_SUBSYSTEM_ID = "a3e67054-b593-4887-998e-d8955eaf4ebb";
	
	//用户基础角色
	public static final String ROLE_BASE = "userbaserole";
	//超级管理员的角色ID
	public static final String ROLE_ADMIN = "administrators";
	//拥有超级管理员角色的用户帐号，不允许删除
	public static final String USER_ACCOUNT_ADMIN = "super@admins";
	//拥有超级管理员角色的用户ID
	public static final String USER_ID_ADMIN = "e40c2495-efde-457d-a2d3-ecfa2ac3b3cf";
	
	
	//系统动作
	public static final String ADD = "add";//新增
	public static final String VIEW = "view";//查看
	public static final String MODIFY = "modify";//修改
	public static final String QUERY = "query";//查询
	public static final String DELETE = "delete";//删除
	public static final String ORDER = "order";//排序
	public static final String BIND_ACTION = "bindaction";//绑定动作
	public static final String BIND_ROLE = "bindrole";//绑定角色
	public static final String BIND_USER = "binduser";//绑定用户
	public static final String BIND_SERVICE = "bindservice";//绑定接口服务
	public static final String REDEPLOY = "redeploy";//重部署
	public static final String ENABLE = "enable";//启用
	public static final String DISABLE = "disable";//禁用
	public static final String EXPORT = "export";//导出
	public static final String BIND_GROUP = "bindgroup";//绑定群组
	public static final String BIND_RIGHT = "bindright";//绑定权限
	public static final String BIND_ACTOR = "bindactor";//绑定接口帐号
	public static final String SET_PASSWORD = "setpassword";//设置密码
	public static final String BIND_POST = "bindpost";//绑定职位
	public static final String SET_DEFAULT = "setdefault";//设置默认
	public static final String SEND = "send";//发送
	
	//时间格式
	public static final String STANDARD_DATE_PATTERN = "yyyy-MM-dd";
	public static final String SIMPLE_DATE_PATTERN = "MM-dd";
	public static final String STANDARD_TIME_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String FULL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	
	//字典KEY
	public static final String DICT_KEY_INDEX_PAGE = "INDEX_PAGE";//首页URL
	public static final String DICT_KEY_FLOW_CLASS = "FLOW_CLASS";//流程类别
	
	//系统常量
	public static final String DEFAULT_INDEX_PAGE = "/page/main.jsp";//默认主页
	
	public static final int PORT_MAX = 65535;// 端口的最大值
	public static final int PORT_MIN = 1;// 端口的最小值
}
