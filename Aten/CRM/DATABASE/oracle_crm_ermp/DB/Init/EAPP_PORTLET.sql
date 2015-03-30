--EAPP_PORTLET 门户首页配置

DECLARE
	V_COUNT int;
BEGIN
	SELECT count(1) INTO V_COUNT FROM EAPP_PORTLET where PORTLET_ID_ = '8a29823e45ffbebf0145ffcd38be0001';
IF V_COUNT  = 0  THEN 
    insert into EAPP_PORTLET (PORTLET_ID_, PORTLET_NAME_, URL_, HIDDENABLE_, MOVEDABLE_, STYLE_, MORE_URL_, SUB_SYSTEM_ID_)
	values ('8a29823e45ffbebf0145ffcd38be0001', '最新客户', 'page/portlet/formalCustPortlet.jsp', 1, 1, 'width:100%;height:220px', 'page/MyCustomer/fomalcustomer/query_fomal.jsp', (SELECT T.SUB_SYSTEM_ID_ FROM EAPP_SUB_SYSTEM T WHERE T.SERVER_NAME_ = 'crm'));
END IF;
END;
/