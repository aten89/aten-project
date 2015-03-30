--EAPP_PORTLET 门户首页配置
DECLARE
	V_COUNT int;
BEGIN
	SELECT count(1) INTO V_COUNT FROM EAPP_PORTLET where PORTLET_ID_ = 'c1918999e80641f8adb08d02d7425b74';
IF V_COUNT  = 0  THEN 
    insert into EAPP_PORTLET (PORTLET_ID_, PORTLET_NAME_, URL_, HIDDENABLE_, MOVEDABLE_, STYLE_, MORE_URL_, SUB_SYSTEM_ID_)
	values ('c1918999e80641f8adb08d02d7425b74', '最新产品', 'm/prod_type/initProdPortlet', 1, 1, 'width:100%;height:220px', 'page/ProdInfoManage/prodInfo/prodInfoList.jsp', (SELECT T.SUB_SYSTEM_ID_ FROM EAPP_SUB_SYSTEM T WHERE T.SERVER_NAME_ = 'poss'));
END IF;
END;
/