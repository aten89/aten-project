WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK;
SET ECHO ON;

insert into crm_rpt_norm_conf (ID_, RPT_ID_, NORM_CODE_, NORM_NAME_, NORM_VALUE_)
values ('1', 'yxrltj', 'AVG_AMOUNT', '人效人力标准（月平均业绩，单位：万）', '100');

insert into crm_rpt_norm_conf (ID_, RPT_ID_, NORM_CODE_, NORM_NAME_, NORM_VALUE_)
values ('2', 'tdyxrl', 'AVG_AMOUNT', '人效人力标准（月平均业绩，单位：万）', '100');