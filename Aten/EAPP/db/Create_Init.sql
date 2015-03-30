
insert into EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_,NAME_,LOGO_URL_,IP_ADDRESS_,SERVER_NAME_,DOMAIN_NAME_,PORT_,DESCRIPTION_,IS_VALID_,DISPLAY_ORDER_) values 
	('a3e67054-b593-4887-998e-d8955eaf4ebb','企业应用公共平台',null,null,null,null,null,null,1,0);


insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('2c90816e1e1ac092011e1ae6edd40001', null, 'a3e67054-b593-4887-998e-d8955eaf4ebb', 'user_right', '用户权限', 1, null, 1, 'hessian专用');
	
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('9366d1b6-b970-4ffe-ae47-70827cc2dc43',null,'a3e67054-b593-4887-998e-d8955eaf4ebb','user_diy','用户中心',2,null,1,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('0c63605b-e078-4d69-9a69-2cc7d884bf17','9366d1b6-b970-4ffe-ae47-70827cc2dc43','a3e67054-b593-4887-998e-d8955eaf4ebb','my_portal','首页定制',1,'l/frame/initportal',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('afa773f7-59b7-4968-b3fe-2e7db0968306','9366d1b6-b970-4ffe-ae47-70827cc2dc43','a3e67054-b593-4887-998e-d8955eaf4ebb','my_scm','快捷菜单',2,'l/frame/initscm',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('442e92c8-81fb-4565-9586-24ef8cc11d21','9366d1b6-b970-4ffe-ae47-70827cc2dc43','a3e67054-b593-4887-998e-d8955eaf4ebb','my_skin','皮肤更换',3,'l/frame/initskin',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('4d106df9-eb28-433c-af74-56f0a626c7fa','9366d1b6-b970-4ffe-ae47-70827cc2dc43','a3e67054-b593-4887-998e-d8955eaf4ebb','my_psw','密码修改',4,'l/frame/initpsw',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('a272eb31-791a-427a-80c1-9920824881d3','9366d1b6-b970-4ffe-ae47-70827cc2dc43','a3e67054-b593-4887-998e-d8955eaf4ebb','my_msg','系统消息',5,'l/frame/initmsg',2,null);
	
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('cd613fb7-2e66-44f9-8805-48ec9c38e989',null,'a3e67054-b593-4887-998e-d8955eaf4ebb','sys_config','系统配置',3,null,1,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('8496b5c4-8502-4deb-b748-194625069345','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','subsystem','系统信息',1,'m/subsystem/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('2b300d94-5c41-42c4-89f6-cf180bd3b863','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','module','系统模块',2,'m/module/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('41e51a0f-19c6-4e4f-a8a3-6b6e05bb2594','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','action','模块动作',3,'m/action/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('8bbb06b5-542a-4012-a2d8-a7a0e3afdf3c','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','module_action','模块服务',4,'m/module_action/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('9a2e0bac-bb7a-4c55-9a71-cb9131d9039f','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','datadict','数据字典',5,'m/datadict/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','portlet','首页板块',6,'m/portlet/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('ca6b89a7-64e1-4c1c-a7ae-29533a35279c','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','shortcutmenu','系统快捷菜单',7,'m/shortcutmenu/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('787afd3b-adb5-4033-bbdc-cd5d25f25c52','cd613fb7-2e66-44f9-8805-48ec9c38e989','a3e67054-b593-4887-998e-d8955eaf4ebb','systemdata','系统缓存数据',8,'m/systemdata/initpage',2,null);

insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('755f10dc-f66d-4624-8c10-2b9fba6beae2',null,'a3e67054-b593-4887-998e-d8955eaf4ebb','sys_user','用户管理',4,null,1,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('f9cde7eb-f30f-4a7b-b961-26ea3b73a801','755f10dc-f66d-4624-8c10-2b9fba6beae2','a3e67054-b593-4887-998e-d8955eaf4ebb','user_account','用户帐号',1,'m/user_account/initquery',2,null);	
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('e92436f7-323e-4760-9ce9-8442e22e76ee','755f10dc-f66d-4624-8c10-2b9fba6beae2','a3e67054-b593-4887-998e-d8955eaf4ebb','rbac_role','用户角色',2,'m/rbac_role/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('b5517bf5-70d2-401b-a182-6fca60c5d7bd','755f10dc-f66d-4624-8c10-2b9fba6beae2','a3e67054-b593-4887-998e-d8955eaf4ebb','rbac_group','组织机构',3,'m/rbac_group/initquery',2,null);	
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('30d9817d-9ba0-46a4-8f00-de944c0c5b1a','755f10dc-f66d-4624-8c10-2b9fba6beae2','a3e67054-b593-4887-998e-d8955eaf4ebb','post','职位信息',4,'m/post/initquery',2,null);	

insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('d08ded7a-3be2-4a6d-afa6-221a29cffbab',null,'a3e67054-b593-4887-998e-d8955eaf4ebb','interface_service','接口管理',5,null,1,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('597cff46-9a5b-42c7-81fe-70b04940ab6a','d08ded7a-3be2-4a6d-afa6-221a29cffbab','a3e67054-b593-4887-998e-d8955eaf4ebb','actor_account','接口帐号',1,'m/actor_account/initquery ',2,null);	
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('37e796fe-a509-4298-aa6b-ca7fd810b838','d08ded7a-3be2-4a6d-afa6-221a29cffbab','a3e67054-b593-4887-998e-d8955eaf4ebb','service','接口服务',2,'m/service/initquery',2,null);

insert into EAPP_MODULE (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249d2b7be0149d2d41a1c0000', null, 'flow_manage', '流程管理', 6, null, 1, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');
insert into eapp_module (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249fbda620149fc8e4ac20000', '4028818249d2b7be0149d2d41a1c0000', 'flow_var', '流程变量', 1, 'm/flow_var/initquery', 2, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');
insert into eapp_module (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249fbda620149fc8f73750001', '4028818249d2b7be0149d2d41a1c0000', 'flow_handler', '处理程序', 2, 'm/flow_handler/initquery', 2, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');
insert into EAPP_MODULE (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249d2b7be0149d2d5d5340001', '4028818249d2b7be0149d2d41a1c0000', 'flow_draft', '流程草稿', 3, 'm/flow_draft/initquery', 2, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');
insert into EAPP_MODULE (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249d2b7be0149d2d63a850002', '4028818249d2b7be0149d2d41a1c0000', 'flow_pub', '正式流程', 4, 'm/flow_pub/initquery', 2, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');
insert into EAPP_MODULE (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249d2b7be0149d2d798350003', '4028818249d2b7be0149d2d41a1c0000', 'flow_inst', '任务实例', 5, 'm/flow_inst/initquery', 2, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');
insert into EAPP_MODULE (MODULE_ID_, PARENT_MODULE_ID_, MODULE_KEY_, NAME_, DISPLAY_ORDER_, URL_, TREE_LEVEL_, DESCRIPTION_, QUOTE_MODULE_ID_, SUB_SYSTEM_ID_)
	values ('4028818249d2b7be0149d2d7e18d0004', '4028818249d2b7be0149d2d41a1c0000', 'flow_data', '历史数据', 6, 'm/flow_data/initpage', 2, null, null, 'a3e67054-b593-4887-998e-d8955eaf4ebb');

	
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('45db871d-7562-423a-9621-ef522278ddd7',null,'a3e67054-b593-4887-998e-d8955eaf4ebb','log_manage','日志查询',7,null,1,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('ff673832-38ee-4852-81bd-16fa109ba18a','45db871d-7562-423a-9621-ef522278ddd7','a3e67054-b593-4887-998e-d8955eaf4ebb','action_log','系统日志',1,'m/action_log/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('531f7a23-65a0-459c-aadc-44ae044edf72','45db871d-7562-423a-9621-ef522278ddd7','a3e67054-b593-4887-998e-d8955eaf4ebb','interface_log','接口日志',2,'m/interface_log/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('297efe87-4552-1d86-0145-522ab21c0001','45db871d-7562-423a-9621-ef522278ddd7','a3e67054-b593-4887-998e-d8955eaf4ebb','login_log','登录日志',3,'m/login_log/initquery',2,null);
insert into EAPP_MODULE (MODULE_ID_,PARENT_MODULE_ID_,SUB_SYSTEM_ID_,MODULE_KEY_,NAME_,DISPLAY_ORDER_,URL_,TREE_LEVEL_,DESCRIPTION_) values 
	('1a09111d-11ea-4624-ac5f-62dd5b881d0c','45db871d-7562-423a-9621-ef522278ddd7','a3e67054-b593-4887-998e-d8955eaf4ebb','online_user','在线用户',4,'m/online_user/initquery',2,null);




insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('8a283d981aa43dcb011aa44ec8210001', 'a3e67054-b593-4887-998e-d8955eaf4ebb', null, '打开方式', 'windowTargetType', null, null, 1, 'windowTargetType', 1, null);
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('8a283d981aa43dcb011aa44fd54b0002', 'a3e67054-b593-4887-998e-d8955eaf4ebb', '8a283d981aa43dcb011aa44ec8210001', '新窗口打开' , '_blank', null, null, 1, 'windowTargetType', 2, null);
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('8a283d981aa43dcb011aa4504f7d0003', 'a3e67054-b593-4887-998e-d8955eaf4ebb', '8a283d981aa43dcb011aa44ec8210001', '本窗口打开', '_self', null, null, 2, 'windowTargetType', 2, null);

insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('ff8080811add0d01011add24f1fd0001', 'a3e67054-b593-4887-998e-d8955eaf4ebb', null, '机构类型', 'GROUP_TYPE', null, null, 2, 'GROUP_TYPE', 1, null);
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('ff8080811add0d01011add2571080002', 'a3e67054-b593-4887-998e-d8955eaf4ebb', 'ff8080811add0d01011add24f1fd0001', '部门', 'D', null, null, 1, 'GROUP_TYPE', 2, null);
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('ff8080811add0d01011add263fca0003', 'a3e67054-b593-4887-998e-d8955eaf4ebb', 'ff8080811add0d01011add24f1fd0001', '项目组', 'J', null, null, 2, 'GROUP_TYPE', 2, null);
	
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('9e2c788e-a6e8-4c78-baea-ecf75c546edb', 'a3e67054-b593-4887-998e-d8955eaf4ebb', null, '首页配置', 'INDEX_PAGE', null, null, 3, 'INDEX_PAGE', 1, null);
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('f4aabbd7-61d3-41d5-97a0-0c8f6639a9fd', 'a3e67054-b593-4887-998e-d8955eaf4ebb', '9e2c788e-a6e8-4c78-baea-ecf75c546edb', '首页URL', 'INDEX_URL', '/page/main.jsp', null, 1, 'INDEX_PAGE', 2, null);
	
insert into EAPP_DATA_DICTIONART (DICT_ID_, SUB_SYSTEM_ID_, PARENT_DICT_ID_, DICT_NAME_, DICT_CODE_, CEIL_VALUE_, FLOOR_VALUE_, DISPLAY_SORT_, DICT_TYPE_, TREE_LEVEL_, DESCRIPTION_)
	values ('c0f52f61cebf4c6aa7b5f6381fa01456', 'a3e67054-b593-4887-998e-d8955eaf4ebb', null, '流程分类', 'FLOW_CLASS', null, null, 4, 'FLOW_CLASS', 1, null);


insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('b75aa44c-0bcb-47a8-8896-339efc018497','add','新增',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('22708456-b358-4084-a1ba-3b17015f63f7','view','查看',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('83523f2e-a57c-4eda-8948-d196afee898b','modify','修改',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('2410e029-e674-4108-a524-e1555cdd3509','query','查询',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('7a173067-7ca6-4330-a0d6-d486bf3cdf28','delete','删除',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('d91ff2db-aba0-4835-896e-b03faf15a76e','order','排序',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('b013d548-d5e0-4603-9359-457eaab27a6b','bindaction','绑定动作',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('7048089d-cb63-4758-84f9-7ef4c46d4e7f','bindrole','绑定角色',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('dd5e9230-2912-4959-b019-7785655b2133','binduser','绑定用户',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('4619136b-dcaf-4846-8424-f1e90ec4fbd6','bindservice','绑定接口服务',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('f67d30fc-9329-41b4-b5d5-7685a17dff14','redeploy','重部署',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('c9588033-7122-4412-b5e8-ff10060cb254','enable','启用',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('5b8d7894-06f1-41af-bb97-2262ce7b52b9','disable','禁用',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('d1ad0437-717f-4421-8b64-66c797dabf04','export','导出',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('8fc524be-e4b3-4b9f-b81b-de76b901c9f0','bindgroup','绑定机构',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('62dea2ec-f019-47bb-beac-2a048cf1e60d','bindright','绑定权限',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('01b38795-a45f-4579-8ad8-6852400b1896','bindactor','绑定接口帐号',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('26da2b41-5653-40c9-8a06-453bbb1b661f','setpassword','设置密码',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('8e79a829-6f41-4fea-9a5b-f1c81626485f','bindpost','绑定职位',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('47babaca-0b53-4d96-add6-3fb68c010fa3','setdefault','默认设置',null,null,null);
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('8d79d940-49cb-4517-95d5-ce8d928baaba','send','发送',null,null,null);
	

/* 登录用户认证接口 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('c1c48862-5aa8-4caa-a8b5-31fb5070954b', '2410e029-e674-4108-a524-e1555cdd3509', '2c90816e1e1ac092011e1ae6edd40001', 'user_right', 'query', 1, 1, 0);

/* 用户自助 */	
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
values ('1766c6bf-5b93-4b99-9295-7db733e2f236', '22708456-b358-4084-a1ba-3b17015f63f7', '4d106df9-eb28-433c-af74-56f0a626c7fa', 'my_psw', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
values ('098ac8ba-7099-4a8e-993b-76428c7324c9', '22708456-b358-4084-a1ba-3b17015f63f7', '442e92c8-81fb-4565-9586-24ef8cc11d21', 'my_skin', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
values ('11de7f46-53ac-4034-9e4e-c98fa44bcc81', '22708456-b358-4084-a1ba-3b17015f63f7', '0c63605b-e078-4d69-9a69-2cc7d884bf17', 'my_portal', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
values ('18127d3f-99ad-4dee-9c79-8fc5083527fa', '22708456-b358-4084-a1ba-3b17015f63f7', 'afa773f7-59b7-4968-b3fe-2e7db0968306', 'my_scm', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
values ('e45a538f-2fed-4c24-8944-aea25034f6c7', '22708456-b358-4084-a1ba-3b17015f63f7', 'a272eb31-791a-427a-80c1-9920824881d3', 'my_msg', 'view', 1, 0, 1);

/* 子系统管理 */	
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4b5f18d8-8907-470c-9a3a-6beb6c0cbd40', '2410e029-e674-4108-a524-e1555cdd3509', '8496b5c4-8502-4deb-b748-194625069345', 'subsystem', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('5bfb5b1c-c775-4f19-9d75-7512f9c399ce', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '8496b5c4-8502-4deb-b748-194625069345', 'subsystem', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('e69737ce-ec0f-4a7c-8a9e-442ebbf3a5f7', 'b75aa44c-0bcb-47a8-8896-339efc018497', '8496b5c4-8502-4deb-b748-194625069345', 'subsystem', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('ce96afe1-dcb2-4567-bfcd-f4b2b475fb8b', '83523f2e-a57c-4eda-8948-d196afee898b', '8496b5c4-8502-4deb-b748-194625069345', 'subsystem', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('f6c77df6-a2fa-462e-bd8b-18d552d0bca0', '22708456-b358-4084-a1ba-3b17015f63f7', '8496b5c4-8502-4deb-b748-194625069345', 'subsystem', 'view', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('06d3bcc0-1168-4750-850f-24790d3de910', 'd91ff2db-aba0-4835-896e-b03faf15a76e', '8496b5c4-8502-4deb-b748-194625069345', 'subsystem', 'order', 1, 0, 1);
	
/* 系统模块 */	
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('bdd01413-493a-434c-ad38-679267edb76f', '2410e029-e674-4108-a524-e1555cdd3509', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('16052112-1e27-4951-ac74-2c53462146fb', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('81104501-cace-4c49-9479-c0bd44885153', 'b75aa44c-0bcb-47a8-8896-339efc018497', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('33738848-f1b9-4752-8f34-8b5e6e45c97a', '83523f2e-a57c-4eda-8948-d196afee898b', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('2a2257c7-20fb-445f-afc2-5b5e5ef0042e', '22708456-b358-4084-a1ba-3b17015f63f7', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('931839b9-5fce-4d89-a2e4-395a1e3ac60e', 'd91ff2db-aba0-4835-896e-b03faf15a76e', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'order', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4f1c29e4-1553-46bf-ac9d-af37044abf50', 'b013d548-d5e0-4603-9359-457eaab27a6b', '2b300d94-5c41-42c4-89f6-cf180bd3b863', 'module', 'bindaction', 1, 0, 1);

/* 动作管理 */	
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('decabf90-0ab2-4497-9b99-9a31e80ed22f', '2410e029-e674-4108-a524-e1555cdd3509', '41e51a0f-19c6-4e4f-a8a3-6b6e05bb2594', 'action', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('90f6f01c-407e-44b7-b6cf-eb1380b5ec83', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '41e51a0f-19c6-4e4f-a8a3-6b6e05bb2594', 'action', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('082391c9-52b0-4caf-b355-ccd6f1cccd17', 'b75aa44c-0bcb-47a8-8896-339efc018497', '41e51a0f-19c6-4e4f-a8a3-6b6e05bb2594', 'action', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9ad59641-ee84-4629-be67-026fe2c6c0c3', '83523f2e-a57c-4eda-8948-d196afee898b', '41e51a0f-19c6-4e4f-a8a3-6b6e05bb2594', 'action', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('bec826e2-f203-41a6-8aa0-8f5cc75e64fe', '22708456-b358-4084-a1ba-3b17015f63f7', '41e51a0f-19c6-4e4f-a8a3-6b6e05bb2594', 'action', 'view', 1, 0, 1);

/* 模块动作 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9a714164-8746-4e4a-bca3-f76fb8e66573', '83523f2e-a57c-4eda-8948-d196afee898b', '8bbb06b5-542a-4012-a2d8-a7a0e3afdf3c', 'module_action', 'modify', 1, 0, 1);
/* insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('215b70c4-88d9-420c-b86c-77effc8eb611', '2410e029-e674-4108-a524-e1555cdd3509', '8bbb06b5-542a-4012-a2d8-a7a0e3afdf3c', 'module_action', 'query', 1, 1, 0);
*/
/* 数据字典 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('48bb56d4-b2df-40e4-95fa-92719c5b7526', '2410e029-e674-4108-a524-e1555cdd3509', '9a2e0bac-bb7a-4c55-9a71-cb9131d9039f', 'dataDict', 'query', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('5ced0561-a18f-4b4b-8d9e-c278526aad0e', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '9a2e0bac-bb7a-4c55-9a71-cb9131d9039f', 'dataDict', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('a2881740-fcd6-45f4-b8ab-cb9028574c93', 'b75aa44c-0bcb-47a8-8896-339efc018497', '9a2e0bac-bb7a-4c55-9a71-cb9131d9039f', 'dataDict', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9fb3cdf7-bc5a-42f1-8f53-4bef8c0e3d17', '83523f2e-a57c-4eda-8948-d196afee898b', '9a2e0bac-bb7a-4c55-9a71-cb9131d9039f', 'dataDict', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('da88b4b7-6060-46e2-9fe7-3db7c8bd381f', '22708456-b358-4084-a1ba-3b17015f63f7', '9a2e0bac-bb7a-4c55-9a71-cb9131d9039f', 'dataDict', 'view', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('06f3bcc0-1168-4750-850f-24790da6e962', 'd91ff2db-aba0-4835-896e-b03faf15a76e', '9a2e0bac-bb7a-4c55-9a71-cb9131d9039f', 'dataDict', 'order', 1, 0, 1);

/* 门户配置 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('32c98f14-0f46-4618-afab-6539d5b6c9ae','2410e029-e674-4108-a524-e1555cdd3509','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','query',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('35fffcd1-8309-438d-8bdf-296423ba98bd','7a173067-7ca6-4330-a0d6-d486bf3cdf28','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','delete',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('aa5a157f-be98-4823-a048-617dab44cbed','b75aa44c-0bcb-47a8-8896-339efc018497','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','add',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('049e867b-1438-4367-ad4e-73aaa37f2015','83523f2e-a57c-4eda-8948-d196afee898b','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','modify',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('f4aabbd7-61d3-41d5-97a0-0c8f6639a9fd','22708456-b358-4084-a1ba-3b17015f63f7','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','view',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('bd2cd18e-41c0-49b1-aa26-4cf948b397b2','47babaca-0b53-4d96-add6-3fb68c010fa3','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','setdefault',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('c7547175-76fe-4fbe-91fd-1e56ac361384','7048089d-cb63-4758-84f9-7ef4c46d4e7f','bb3fc96d-e1c5-42c4-bfd1-cb7a17bf5887','portlet','bindrole',1,0,1);

	
/* 快捷方式 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('5481dce3-2d82-4f0f-aa21-34c26aaee60a', '2410e029-e674-4108-a524-e1555cdd3509', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('5fb06ceb-0148-43a4-8121-7ced5e50fc39', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('808d86ab-2e6e-442a-beab-0cc7a92e7df1', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9f51781e-dfd0-44bc-8785-abad73e11e4b', '83523f2e-a57c-4eda-8948-d196afee898b', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('69669ae8-d78e-4643-ad2e-8ef645ddcef0', '22708456-b358-4084-a1ba-3b17015f63f7', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('fcb57bea-f4bd-4ef1-b3ed-8339bf1c644d', 'c9588033-7122-4412-b5e8-ff10060cb254', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'enable', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('363b3975-2092-4e9d-81a2-3023bcce9ece', '5b8d7894-06f1-41af-bb97-2262ce7b52b9', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'disable', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('80c191b1-ec23-4dea-9b87-89bcbf9773c8', 'd91ff2db-aba0-4835-896e-b03faf15a76e', 'ca6b89a7-64e1-4c1c-a7ae-29533a35279c', 'shortcutmenu', 'order', 1, 0, 1);

/* 数据热加载 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('570f6989-6ac8-4667-96be-3abe6eb38806', 'f67d30fc-9329-41b4-b5d5-7685a17dff14', '787afd3b-adb5-4033-bbdc-cd5d25f25c52', 'systemdata', 'redeploy', 1, 0, 1);

/* 用户帐号 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('93453785-59f9-4c89-988e-e84a40859178', '2410e029-e674-4108-a524-e1555cdd3509', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'query', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('bc2de980-fe6d-4264-8a73-edf1c4e999ed', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'delete', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('7ec5b743-a171-4b10-99cf-832e0d64d343', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'add', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('f548a646-0708-4ed2-abff-0364a749d3ed', '83523f2e-a57c-4eda-8948-d196afee898b', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'modify', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('7a60e5c2-09cb-4787-bb4a-3730b865d2a0', '22708456-b358-4084-a1ba-3b17015f63f7', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'view', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('18a75e82-61de-4e9e-91a5-f3cdb9c80684', '7048089d-cb63-4758-84f9-7ef4c46d4e7f', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'bindrole', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('e88a202c-79a8-4ec2-b32e-7d22a2294de3', '8fc524be-e4b3-4b9f-b81b-de76b901c9f0', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'bindgroup', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('338b00ec-c5f5-467a-a859-a83ead891385', '26da2b41-5653-40c9-8a06-453bbb1b661f', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'setpassword', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('b6bac234-c7c0-413b-9f32-116a107cc653', '47babaca-0b53-4d96-add6-3fb68c010fa3', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'setdefault', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('3fd85e58-0dc0-4393-bd27-657f61925e1e', '8d79d940-49cb-4517-95d5-ce8d928baaba', 'f9cde7eb-f30f-4a7b-b961-26ea3b73a801', 'user_account', 'send', 1, 1, 0);


/* 机构管理 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('64cc5927-559f-4b59-8517-330bef62fdf8', '2410e029-e674-4108-a524-e1555cdd3509', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'query', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9def6b04-eb79-4e3a-ad64-4a3ff469d340', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'delete', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('7c2929c1-00a0-44e6-ae2c-91ddb1363426', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'add', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('b8771794-8e90-4368-9130-eb0945987f67', '83523f2e-a57c-4eda-8948-d196afee898b', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'modify', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('f6849ed8-82ac-44a7-8767-fda8329c9493', '22708456-b358-4084-a1ba-3b17015f63f7', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'view', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('a6906105-4cb7-4404-97c7-2f260c8c65d0', '7048089d-cb63-4758-84f9-7ef4c46d4e7f', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'bindrole', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('b22ecf24-132f-44e1-9e62-ef889eadbd6e', 'dd5e9230-2912-4959-b019-7785655b2133', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'binduser', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('75315c19-e9c9-4790-b7a3-753cbb312fa4', 'd91ff2db-aba0-4835-896e-b03faf15a76e', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'order', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('3d4700a4-16f8-4cef-9052-a042bfaae4d1', '8e79a829-6f41-4fea-9a5b-f1c81626485f', 'b5517bf5-70d2-401b-a182-6fca60c5d7bd', 'rbac_group', 'bindpost', 1, 0, 1);

/* 角色管理 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('dc053f0d-7a12-4bad-a5ef-d570f11f43cc', '2410e029-e674-4108-a524-e1555cdd3509', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('b0e9d9c0-f5d9-4137-b55c-5c7820367b6f', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4789448a-fad5-450c-b3f4-b6a4c0339a94', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('b7089227-fbcb-4113-ba5d-d8909cb2be2a', '83523f2e-a57c-4eda-8948-d196afee898b', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('cfb10450-6969-48f5-bca7-d70ef1ea1c56', '22708456-b358-4084-a1ba-3b17015f63f7', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('523a29e5-4b94-40d7-9195-6ac5d54b5213', '8fc524be-e4b3-4b9f-b81b-de76b901c9f0', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'bindgroup', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('f0547dd8-5447-40d8-a912-b3a27c37108b', 'dd5e9230-2912-4959-b019-7785655b2133', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'binduser', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('ae380400-6adb-470d-9582-66fe8c51f9b8', '62dea2ec-f019-47bb-beac-2a048cf1e60d', 'e92436f7-323e-4760-9ce9-8442e22e76ee', 'rbac_role', 'bindright', 1, 0, 1);

/* 职位管理 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('814e343e-478e-4466-bdcd-284645a97272', '2410e029-e674-4108-a524-e1555cdd3509', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'query', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('1f81cdbc-33a7-46a9-bf55-a5dcef171684', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'delete', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('66a055cf-3f51-45a1-aec8-546c840bc715', 'b75aa44c-0bcb-47a8-8896-339efc018497', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'add', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('79c2774a-7f49-44f7-a09c-69e74e9ab557', '83523f2e-a57c-4eda-8948-d196afee898b', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'modify', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('26475f42-5299-4777-8c05-451c96558cfb', '22708456-b358-4084-a1ba-3b17015f63f7', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'view', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('be2ef0ba-d677-4e72-9390-34e6b5bf518d', 'dd5e9230-2912-4959-b019-7785655b2133', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'binduser', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('77b28666-70c8-4108-8ae0-c8fbf7fe4a61', 'd91ff2db-aba0-4835-896e-b03faf15a76e', '30d9817d-9ba0-46a4-8f00-de944c0c5b1a', 'post', 'order', 1, 0, 1);

/* 接口帐号 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('066ff81d-bd2b-424c-8462-dcd50f85f7c1', '2410e029-e674-4108-a524-e1555cdd3509', '597cff46-9a5b-42c7-81fe-70b04940ab6a', 'actor_account', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('41be2085-fd04-49d5-ab94-75024f5d7004', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '597cff46-9a5b-42c7-81fe-70b04940ab6a', 'actor_account', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('7f2d8a14-3459-4a7c-8ff2-cdefe33b8abe', 'b75aa44c-0bcb-47a8-8896-339efc018497', '597cff46-9a5b-42c7-81fe-70b04940ab6a', 'actor_account', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('546dd1b0-914c-4169-88c4-66cdc98a7e6c', '83523f2e-a57c-4eda-8948-d196afee898b', '597cff46-9a5b-42c7-81fe-70b04940ab6a', 'actor_account', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('71afd409-ddba-4360-982d-a57efd9e876c', '22708456-b358-4084-a1ba-3b17015f63f7', '597cff46-9a5b-42c7-81fe-70b04940ab6a', 'actor_account', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('ad1240c0-0d44-4511-bbe5-761b3f4326e5', '4619136b-dcaf-4846-8424-f1e90ec4fbd6', '597cff46-9a5b-42c7-81fe-70b04940ab6a', 'actor_account', 'bindservice', 1, 0, 1);

/* 服务配置 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('53ab5be1-b7b5-4c71-b98b-3678039d0ede', '2410e029-e674-4108-a524-e1555cdd3509', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('756659a0-fac7-4e28-b6ae-db9d5cd1d4ea', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('ef1dc0cc-6729-4ec7-9c49-0ffe59e0fa4c', 'b75aa44c-0bcb-47a8-8896-339efc018497', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('563589ef-2ef5-42b2-b695-569a31f22fb7', '83523f2e-a57c-4eda-8948-d196afee898b', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('a19d99d8-32ad-48e6-b9ec-1f03327073d8', '22708456-b358-4084-a1ba-3b17015f63f7', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('27e55d38-316c-4bf6-a720-520ea1ab4178', '01b38795-a45f-4579-8ad8-6852400b1896', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'bindactor', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9f5e15c6-3550-4c0b-9232-0ba858e1ca08', '62dea2ec-f019-47bb-beac-2a048cf1e60d', '37e796fe-a509-4298-aa6b-ca7fd810b838', 'service', 'bindright', 1, 0, 1);

/* 系统动作日志 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('323a2efd-6897-4f8c-b29d-1dd436a5c349', '2410e029-e674-4108-a524-e1555cdd3509', 'ff673832-38ee-4852-81bd-16fa109ba18a', 'action_log', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('9e47f6c6-f919-43a8-9a54-3f00e92ef8e2', '22708456-b358-4084-a1ba-3b17015f63f7', 'ff673832-38ee-4852-81bd-16fa109ba18a', 'action_log', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('139970a7-e224-46a3-a73e-ad366dbc5749', 'd1ad0437-717f-4421-8b64-66c797dabf04', 'ff673832-38ee-4852-81bd-16fa109ba18a', 'action_log', 'export', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('0bb41b9e-9752-4110-8845-10f2e0a44d8b', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'ff673832-38ee-4852-81bd-16fa109ba18a', 'action_log', 'add', 1, 1, 0);

/* 接口服务日志 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('ff8a36a3-3caf-4be9-8f5a-a0e550b7be1c', '2410e029-e674-4108-a524-e1555cdd3509', '531f7a23-65a0-459c-aadc-44ae044edf72', 'interface_log', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('15304a08-69cb-48f0-8816-6e3ffbbd3e54', '22708456-b358-4084-a1ba-3b17015f63f7', '531f7a23-65a0-459c-aadc-44ae044edf72', 'interface_log', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('74331f34-29b4-4e09-8ea4-a75c970c8431', 'd1ad0437-717f-4421-8b64-66c797dabf04', '531f7a23-65a0-459c-aadc-44ae044edf72', 'interface_log', 'export', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('146df818-7776-42b9-9889-cc7fea212a53', 'b75aa44c-0bcb-47a8-8896-339efc018497', '531f7a23-65a0-459c-aadc-44ae044edf72', 'interface_log', 'add', 1, 1, 0);

/* 登录日志与在线用户 */
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('297efe87-4552-1d86-0145-522b1fda0002','2410e029-e674-4108-a524-e1555cdd3509','297efe87-4552-1d86-0145-522ab21c0001','login_log','query',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('297efe87-4552-1d86-0145-522d2ac60003','d1ad0437-717f-4421-8b64-66c797dabf04','297efe87-4552-1d86-0145-522ab21c0001','login_log','export',1,0,1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, ACTION_ID_, MODULE_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('2c231455-71de-492a-8154-9a22ac7812af','2410e029-e674-4108-a524-e1555cdd3509','1a09111d-11ea-4624-ac5f-62dd5b881d0c','online_user','query',1,0,1);

/* 流程管理 */
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc90d0140002', '4028818249fbda620149fc8e4ac20000', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'flow_var', 'delete', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc90d0140003', '4028818249fbda620149fc8e4ac20000', '2410e029-e674-4108-a524-e1555cdd3509', 'flow_var', 'query', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc90d0140004', '4028818249fbda620149fc8e4ac20000', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'flow_var', 'add', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc90d0140005', '4028818249fbda620149fc8e4ac20000', '22708456-b358-4084-a1ba-3b17015f63f7', 'flow_var', 'view', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc90d0140006', '4028818249fbda620149fc8e4ac20000', '83523f2e-a57c-4eda-8948-d196afee898b', 'flow_var', 'modify', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc912b0c0007', '4028818249fbda620149fc8f73750001', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'flow_handler', 'add', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc912b0c0008', '4028818249fbda620149fc8f73750001', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'flow_handler', 'delete', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc912b0c0009', '4028818249fbda620149fc8f73750001', '2410e029-e674-4108-a524-e1555cdd3509', 'flow_handler', 'query', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc912b0c000a', '4028818249fbda620149fc8f73750001', '22708456-b358-4084-a1ba-3b17015f63f7', 'flow_handler', 'view', 1, 0, 1);
insert into eapp_module_action (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249fbda620149fc912b0c000b', '4028818249fbda620149fc8f73750001', '83523f2e-a57c-4eda-8948-d196afee898b', 'flow_handler', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249d2b7be0149d2d821fc0005', '4028818249d2b7be0149d2d5d5340001', '2410e029-e674-4108-a524-e1555cdd3509', 'fow_draf', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f19f42f40002', '4028818249d2b7be0149d2d5d5340001', '22708456-b358-4084-a1ba-3b17015f63f7', 'flow_draft', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f19f42f40003', '4028818249d2b7be0149d2d5d5340001', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'flow_draft', 'delete', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f19f42f40004', '4028818249d2b7be0149d2d5d5340001', 'b75aa44c-0bcb-47a8-8896-339efc018497', 'flow_draft', 'add', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f19f42f50005', '4028818249d2b7be0149d2d5d5340001', '83523f2e-a57c-4eda-8948-d196afee898b', 'flow_draft', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f19f42f50006', '4028818249d2b7be0149d2d5d5340001', 'c9588033-7122-4412-b5e8-ff10060cb254', 'flow_draft', 'enable', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249d2b7be0149d2d833cd0006', '4028818249d2b7be0149d2d63a850002', '2410e029-e674-4108-a524-e1555cdd3509', 'flow_pub', 'query', 1, 1, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f1a110690007', '4028818249d2b7be0149d2d63a850002', '22708456-b358-4084-a1ba-3b17015f63f7', 'flow_pub', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f1a110690008', '4028818249d2b7be0149d2d63a850002', '83523f2e-a57c-4eda-8948-d196afee898b', 'flow_pub', 'modify', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249f194840149f1a110690009', '4028818249d2b7be0149d2d63a850002', '5b8d7894-06f1-41af-bb97-2262ce7b52b9', 'flow_pub', 'disable', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249d2b7be0149d2d84b7c0007', '4028818249d2b7be0149d2d798350003', '2410e029-e674-4108-a524-e1555cdd3509', 'flow_inst', 'query', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('b074c17e9cb64a99b6591cbda4097e93', '4028818249d2b7be0149d2d798350003', '22708456-b358-4084-a1ba-3b17015f63f7', 'flow_inst', 'view', 1, 0, 1);
insert into EAPP_MODULE_ACTION (MODULE_ACTION_ID_, MODULE_ID_, ACTION_ID_, MODULE_KEY_, ACTION_KEY_, IS_VALID_, IS_RPC_, IS_HTTP_)
	values ('4028818249d2b7be0149d2d8e2d40008', '4028818249d2b7be0149d2d7e18d0004', '7a173067-7ca6-4330-a0d6-d486bf3cdf28', 'flow_data', 'delete', 1, 0, 1);
	


insert into EAPP_USER_ACCOUNT (USER_ID_,ACCOUNT_ID_,DISPLAY_NAME_,PASSWORD_,IS_LOCK_,CHANGE_PASSWORD_FLAG_,IS_LOGIC_DELETE_) values 
	('e40c2495-efde-457d-a2d3-ecfa2ac3b3cf','sys','System','c4ca4238a0b923820dcc509a6f75849b',0,'Y',0);
insert into EAPP_ROLE (ROLE_ID_,ROLE_NAME_,IS_VALID_,DESCRIPTION_) values
	('administrators','管理员',1,null);
insert into EAPP_USER_ROLE (ROLE_ID_,USER_ID_) values 
	('administrators','e40c2495-efde-457d-a2d3-ecfa2ac3b3cf');

/* 用户自助 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','1766c6bf-5b93-4b99-9295-7db733e2f236');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','098ac8ba-7099-4a8e-993b-76428c7324c9');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','11de7f46-53ac-4034-9e4e-c98fa44bcc81');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','18127d3f-99ad-4dee-9c79-8fc5083527fa');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','e45a538f-2fed-4c24-8944-aea25034f6c7');

/* 子系统管理 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4b5f18d8-8907-470c-9a3a-6beb6c0cbd40');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','5bfb5b1c-c775-4f19-9d75-7512f9c399ce');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','e69737ce-ec0f-4a7c-8a9e-442ebbf3a5f7');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','ce96afe1-dcb2-4567-bfcd-f4b2b475fb8b');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','f6c77df6-a2fa-462e-bd8b-18d552d0bca0');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','06d3bcc0-1168-4750-850f-24790d3de910');

/* 系统模块 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','bdd01413-493a-434c-ad38-679267edb76f');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','16052112-1e27-4951-ac74-2c53462146fb');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','81104501-cace-4c49-9479-c0bd44885153');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','33738848-f1b9-4752-8f34-8b5e6e45c97a');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','2a2257c7-20fb-445f-afc2-5b5e5ef0042e');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','931839b9-5fce-4d89-a2e4-395a1e3ac60e');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4f1c29e4-1553-46bf-ac9d-af37044abf50');

/* 动作管理 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','decabf90-0ab2-4497-9b99-9a31e80ed22f');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','90f6f01c-407e-44b7-b6cf-eb1380b5ec83');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','082391c9-52b0-4caf-b355-ccd6f1cccd17');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9ad59641-ee84-4629-be67-026fe2c6c0c3');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','bec826e2-f203-41a6-8aa0-8f5cc75e64fe');	

/* 模块动作 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9a714164-8746-4e4a-bca3-f76fb8e66573');

/* 数据字典 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','48bb56d4-b2df-40e4-95fa-92719c5b7526');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','5ced0561-a18f-4b4b-8d9e-c278526aad0e');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','a2881740-fcd6-45f4-b8ab-cb9028574c93');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9fb3cdf7-bc5a-42f1-8f53-4bef8c0e3d17');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','da88b4b7-6060-46e2-9fe7-3db7c8bd381f');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','06f3bcc0-1168-4750-850f-24790da6e962');

/* 门户配置 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','32c98f14-0f46-4618-afab-6539d5b6c9ae');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','35fffcd1-8309-438d-8bdf-296423ba98bd');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','aa5a157f-be98-4823-a048-617dab44cbed');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','049e867b-1438-4367-ad4e-73aaa37f2015');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','f4aabbd7-61d3-41d5-97a0-0c8f6639a9fd');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','bd2cd18e-41c0-49b1-aa26-4cf948b397b2');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','c7547175-76fe-4fbe-91fd-1e56ac361384');

/* 快捷方式 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','5481dce3-2d82-4f0f-aa21-34c26aaee60a');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','5fb06ceb-0148-43a4-8121-7ced5e50fc39');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','808d86ab-2e6e-442a-beab-0cc7a92e7df1');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9f51781e-dfd0-44bc-8785-abad73e11e4b');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','69669ae8-d78e-4643-ad2e-8ef645ddcef0');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','fcb57bea-f4bd-4ef1-b3ed-8339bf1c644d');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','363b3975-2092-4e9d-81a2-3023bcce9ece');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','80c191b1-ec23-4dea-9b87-89bcbf9773c8');

/* 数据热加载 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','570f6989-6ac8-4667-96be-3abe6eb38806');

/* 用户帐号 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','93453785-59f9-4c89-988e-e84a40859178');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','bc2de980-fe6d-4264-8a73-edf1c4e999ed');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','7ec5b743-a171-4b10-99cf-832e0d64d343');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','f548a646-0708-4ed2-abff-0364a749d3ed');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','7a60e5c2-09cb-4787-bb4a-3730b865d2a0');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','18a75e82-61de-4e9e-91a5-f3cdb9c80684');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','e88a202c-79a8-4ec2-b32e-7d22a2294de3');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','338b00ec-c5f5-467a-a859-a83ead891385');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','b6bac234-c7c0-413b-9f32-116a107cc653');


/* 机构管理 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','64cc5927-559f-4b59-8517-330bef62fdf8');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9def6b04-eb79-4e3a-ad64-4a3ff469d340');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','7c2929c1-00a0-44e6-ae2c-91ddb1363426');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','b8771794-8e90-4368-9130-eb0945987f67');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','f6849ed8-82ac-44a7-8767-fda8329c9493');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','a6906105-4cb7-4404-97c7-2f260c8c65d0');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','b22ecf24-132f-44e1-9e62-ef889eadbd6e');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','75315c19-e9c9-4790-b7a3-753cbb312fa4');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','3d4700a4-16f8-4cef-9052-a042bfaae4d1');

/* 角色管理 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','dc053f0d-7a12-4bad-a5ef-d570f11f43cc');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','b0e9d9c0-f5d9-4137-b55c-5c7820367b6f');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4789448a-fad5-450c-b3f4-b6a4c0339a94');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','b7089227-fbcb-4113-ba5d-d8909cb2be2a');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','cfb10450-6969-48f5-bca7-d70ef1ea1c56');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','523a29e5-4b94-40d7-9195-6ac5d54b5213');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','f0547dd8-5447-40d8-a912-b3a27c37108b');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','ae380400-6adb-470d-9582-66fe8c51f9b8');

/* 职位管理 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','814e343e-478e-4466-bdcd-284645a97272');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','1f81cdbc-33a7-46a9-bf55-a5dcef171684');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','66a055cf-3f51-45a1-aec8-546c840bc715');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','79c2774a-7f49-44f7-a09c-69e74e9ab557');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','26475f42-5299-4777-8c05-451c96558cfb');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','be2ef0ba-d677-4e72-9390-34e6b5bf518d');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','77b28666-70c8-4108-8ae0-c8fbf7fe4a61');

/* 接口帐号 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','066ff81d-bd2b-424c-8462-dcd50f85f7c1');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','41be2085-fd04-49d5-ab94-75024f5d7004');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','7f2d8a14-3459-4a7c-8ff2-cdefe33b8abe');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','546dd1b0-914c-4169-88c4-66cdc98a7e6c');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','71afd409-ddba-4360-982d-a57efd9e876c');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','ad1240c0-0d44-4511-bbe5-761b3f4326e5');

/* 服务配置 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','53ab5be1-b7b5-4c71-b98b-3678039d0ede');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','756659a0-fac7-4e28-b6ae-db9d5cd1d4ea');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','ef1dc0cc-6729-4ec7-9c49-0ffe59e0fa4c');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','563589ef-2ef5-42b2-b695-569a31f22fb7');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','a19d99d8-32ad-48e6-b9ec-1f03327073d8');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','27e55d38-316c-4bf6-a720-520ea1ab4178');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9f5e15c6-3550-4c0b-9232-0ba858e1ca08');

/* 系统动作日志 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','323a2efd-6897-4f8c-b29d-1dd436a5c349');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','9e47f6c6-f919-43a8-9a54-3f00e92ef8e2');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','139970a7-e224-46a3-a73e-ad366dbc5749');

/* 接口服务日志 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','ff8a36a3-3caf-4be9-8f5a-a0e550b7be1c');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','15304a08-69cb-48f0-8816-6e3ffbbd3e54');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','74331f34-29b4-4e09-8ea4-a75c970c8431');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','297efe87-4552-1d86-0145-522b1fda0002');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','297efe87-4552-1d86-0145-522d2ac60003');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','2c231455-71de-492a-8154-9a22ac7812af');

/* 流程管理 */
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc90d0140002');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc90d0140003');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc90d0140004');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc90d0140005');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc90d0140006');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc912b0c0007');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc912b0c0008');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc912b0c0009');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc912b0c000a');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249fbda620149fc912b0c000b');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249d2b7be0149d2d821fc0005');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f19f42f40002');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f19f42f40003');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f19f42f40004');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f19f42f50005');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f19f42f50006');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249d2b7be0149d2d833cd0006');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f1a110690007');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f1a110690008');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249f194840149f1a110690009');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249d2b7be0149d2d84b7c0007');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','b074c17e9cb64a99b6591cbda4097e93');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('administrators','4028818249d2b7be0149d2d8e2d40008');

insert into EAPP_ROLE (ROLE_ID_,ROLE_NAME_,IS_VALID_,DESCRIPTION_) values
	('userbaserole','基础角色',1,null);

insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('userbaserole','1766c6bf-5b93-4b99-9295-7db733e2f236');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('userbaserole','098ac8ba-7099-4a8e-993b-76428c7324c9');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('userbaserole','11de7f46-53ac-4034-9e4e-c98fa44bcc81');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('userbaserole','18127d3f-99ad-4dee-9c79-8fc5083527fa');
insert into EAPP_ROLE_RIGHT (ROLE_ID_,MODULE_ACTION_ID_) values ('userbaserole','e45a538f-2fed-4c24-8944-aea25034f6c7');




/* 流程全局变量 */
insert into eapp_flow_var (VAR_ID_, FLOW_CLASS_, NAME_, DISPLAY_NAME_, TYPE_, NOT_NUL_, GLOBAL_FLAG_)
	values ('1f5d226e816b493c9d517580d6d2b975', '', 'formId', '表单ID', 'DATATYPE_STRING', 1, 1);
insert into eapp_flow_var (VAR_ID_, FLOW_CLASS_, NAME_, DISPLAY_NAME_, TYPE_, NOT_NUL_, GLOBAL_FLAG_)
	values ('78bcd1676bb7421ba262f11de23f4a8d', '', 'userAccountId', '发起人帐号', 'DATATYPE_STRING', 1, 1);
insert into eapp_flow_var (VAR_ID_, FLOW_CLASS_, NAME_, DISPLAY_NAME_, TYPE_, NOT_NUL_, GLOBAL_FLAG_)
	values ('32114f345ebf433496c7b76c0602c690', '', 'taskDescription', '任务说明', 'DATATYPE_STRING', 0, 1);
insert into eapp_flow_var (VAR_ID_, FLOW_CLASS_, NAME_, DISPLAY_NAME_, TYPE_, NOT_NUL_, GLOBAL_FLAG_)
	values ('01568d3738ed4b7b900e6e73f8c75554', '', 'groupName', '发起人部门', 'DATATYPE_STRING', 0, 1);
	
/* 流程全局Handler */
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409ab0f08f0017', '', '同步创建任务数据', 'org.eapp.flow.handler.task.TaskCreateEvent', 'ACTION_TASKEVENT', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a29822641a05a480141a05bdd5e0001', '', '同步分配任务数据', 'org.eapp.flow.handler.task.TaskNotifyEvent', 'ACTION_TASKEVENT', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409aab9ab50006', '', '同步认领任务数据', 'org.eapp.flow.handler.task.TaskAssignEvent', 'ACTION_TASKEVENT', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409aab9ab40005', '', '同步开始任务数据', 'org.eapp.flow.handler.task.TaskStartEvent', 'ACTION_TASKEVENT', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409aab9ab40004', '', '同步放弃任务数据', 'org.eapp.flow.handler.task.TaskGiveUpEvent', 'ACTION_TASKEVENT', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409aab9ab40003', '', '同步结束任务数据', 'org.eapp.flow.handler.task.TaskEndEvent', 'ACTION_TASKEVENT', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409aab9ab30002', '', '授权给部门的领导', 'org.eapp.flow.handler.common.DeptLeaderAssign', 'ASSIGN', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a298226409a988001409aaf94e10012', '', '授权给部门的分管领导', 'org.eapp.flow.handler.common.DeptChargerAssign', 'ASSIGN', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('8a2982264080c700014080cc11820009', '', '授权给用户的领导', 'org.eapp.flow.handler.common.UserLeaderAssign', 'ASSIGN', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('4028818249e6642e0149e737f7d8004e', '', '授权给用户的分管领导', 'org.eapp.flow.handler.common.UserChargerAssign', 'ASSIGN', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('4028818249e6642e0149e6ec05b7003c', '', '是否部门的领导', 'org.eapp.flow.handler.common.DeptLeaderDecision', 'DECISION', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('40288a86460914520146091bfb4d000c', '', '是否部门的分管领导', 'org.eapp.flow.handler.common.DeptChargerDecision', 'DECISION', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('40288a86460914520146091c4400000d', '', '是否用户的领导', 'org.eapp.flow.handler.common.UserLeaderDecision', 'DECISION', 1);
insert into eapp_flow_handler (HAND_ID_, FLOW_CLASS_, NAME_, HANDLER_CLASS_, TYPE_, GLOBAL_FLAG_)
	values ('40288a86460914520146091c6f44000e', '', '是否用户的分管领导', 'org.eapp.flow.handler.common.UserChargerDecision', 'DECISION', 1);

