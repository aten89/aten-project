--EAPP_ACTION ��������
-- �鿴��ɱ���
DECLARE
	V_COUNT int;
BEGIN
	SELECT count(1) INTO V_COUNT FROM EAPP_ACTION where ACTION_ID_ = '8a29822646cc49480146cc4e9b050002';
IF V_COUNT  = 0  THEN 
    insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
  ('8a29822646cc49480146cc4e9b050002','viewtackscale','�鿴��ɱ���',null,null,null);
END IF;
END;
/


insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('40288a86472024dd01472450b9440026','confirm','ȷ��',null,null,null);
	
insert into EAPP_ACTION (ACTION_ID_,ACTION_KEY_,NAME_,LOGO_URL_,TIPS_,DESCRIPTION_) values 
	('40288a8646091452014609439fab0069','check','���',null,null,null);