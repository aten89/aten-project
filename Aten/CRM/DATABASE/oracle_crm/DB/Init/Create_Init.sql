WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK;
SET ECHO ON;

insert into crm_rpt_norm_conf (ID_, RPT_ID_, NORM_CODE_, NORM_NAME_, NORM_VALUE_)
values ('1', 'yxrltj', 'AVG_AMOUNT', '��Ч������׼����ƽ��ҵ������λ����', '100');

insert into crm_rpt_norm_conf (ID_, RPT_ID_, NORM_CODE_, NORM_NAME_, NORM_VALUE_)
values ('2', 'tdyxrl', 'AVG_AMOUNT', '��Ч������׼����ƽ��ҵ������λ����', '100');