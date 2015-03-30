/*
ALTER TABLE CRM_CUSTOMER_APPOINTMENT
   DROP CONSTRAINT FK_CRM_APPO_REFERENCE_CRM_CUST;

ALTER TABLE CRM_CUSTOMER_CONSULT
   DROP CONSTRAINT FK_CRM_CUST_REFERENCE_CRM_CUST;

ALTER TABLE CRM_RETURN_VIST
   DROP CONSTRAINT FK_CRM_RETU_REFERENCE_CRM_CUST;

DROP TABLE CRM_CUSTOMER_INFO CASCADE CONSTRAINTS;

DROP TABLE CRM_CUSTOMER_APPOINTMENT CASCADE CONSTRAINTS;

DROP TABLE CRM_CUSTOMER_CONSULT CASCADE CONSTRAINTS;

DROP TABLE CRM_GROUP_EXT CASCADE CONSTRAINTS;

DROP TABLE CRM_IMPORT_CUSTOMER CASCADE CONSTRAINTS;

DROP TABLE CRM_RETURN_VIST CASCADE CONSTRAINTS;

DROP TABLE CRM_USER_ACCOUNT_EXT CASCADE CONSTRAINTS;
*/

/*==============================================================*/
/* Table: CRM_CUSTOMER_INFO                                     */
/*==============================================================*/
create table CRM_CUSTOMER_INFO
(
  id_                       VARCHAR2(36) not null,
  cust_name_                VARCHAR2(128),
  tel_                      VARCHAR2(128),
  sex_                      CHAR(1),
  age_                      INTEGER,
  birthday_                 DATE,
  email_                    VARCHAR2(128),
  address_                  VARCHAR2(128),
  cust_property_            INTEGER,
  financing_amount_         NUMBER(16,2),
  invest_experience_        VARCHAR2(512),
  expected_product_         VARCHAR2(128),
  recommend_product_        VARCHAR2(128),
  expected_invest_agelimit_ INTEGER,
  communication_type_       VARCHAR2(128),
  communication_result_     VARCHAR2(2000),
  return_vist_rejct_times_  INTEGER,
  sale_man_                 VARCHAR2(128),
  data_source_              VARCHAR2(128),
  status_                   VARCHAR2(128),
  recent_contact_time_      TIMESTAMP(6),
  submit_time_		    TIMESTAMP(6),
  create_time_              TIMESTAMP(6),
  bankName_                   VARCHAR2(128),
  bankBranch_                   VARCHAR2(128),
  bankAccount_                   VARCHAR2(128),
  Memo_Mark_                   SMALLINT,
  pass_time_              TIMESTAMP(6)
);
comment on table CRM_CUSTOMER_INFO
  is '�ͻ���Ϣ';
comment on column CRM_CUSTOMER_INFO.id_
  is '����';
comment on column CRM_CUSTOMER_INFO.cust_name_
  is '�ͻ�����';
comment on column CRM_CUSTOMER_INFO.tel_
  is '�ͻ��绰';
comment on column CRM_CUSTOMER_INFO.sex_
  is 'M:��
F:Ů';
comment on column CRM_CUSTOMER_INFO.age_
  is '����';
comment on column CRM_CUSTOMER_INFO.birthday_
  is '��������';
comment on column CRM_CUSTOMER_INFO.email_
  is '��������';
comment on column CRM_CUSTOMER_INFO.address_
  is 'ͨѶ��ַ';
comment on column CRM_CUSTOMER_INFO.cust_property_
  is '0:����
1:����';
comment on column CRM_CUSTOMER_INFO.financing_amount_
  is '��ƽ��';
comment on column CRM_CUSTOMER_INFO.invest_experience_
  is 'Ͷ�ʾ���';
comment on column CRM_CUSTOMER_INFO.expected_product_
  is '������Ʒ';
comment on column CRM_CUSTOMER_INFO.recommend_product_
  is '�Ƽ���Ʒ';
comment on column CRM_CUSTOMER_INFO.expected_invest_agelimit_
  is '����Ͷ������';
comment on column CRM_CUSTOMER_INFO.communication_type_
  is '�绰/��̸/ת����';
comment on column CRM_CUSTOMER_INFO.communication_result_
  is '��ͨ���';
comment on column CRM_CUSTOMER_INFO.return_vist_rejct_times_
  is '�طò��ش���';
comment on column CRM_CUSTOMER_INFO.sale_man_
  is '������Ա';
comment on column CRM_CUSTOMER_INFO.data_source_
  is '������䣻�˹�¼�룻�Ǳ�ԤԼ���¿ͻ�����';
comment on column CRM_CUSTOMER_INFO.status_
  is 'δ�ύ���ط��У����أ�Ǳ�ڣ������ƣ���ʽ';
comment on column CRM_CUSTOMER_INFO.recent_contact_time_
  is '�����ϵʱ��';
comment on column CRM_CUSTOMER_INFO.create_time_
  is '����ʱ��';
comment on column CRM_CUSTOMER_INFO.pass_time_
  is 'ͨ��ʱ��';
alter table CRM_CUSTOMER_INFO
  add constraint PK_CRM_CUSTOMER_INFO primary key (ID_);

/*==============================================================*/
/* Table: CRM_CUSTOMER_APPOINTMENT                              */
/*==============================================================*/
create table CRM_CUSTOMER_APPOINTMENT
(
  id_               VARCHAR2(36) not null,
  cust_id_          VARCHAR2(36),
  appointment_time_ TIMESTAMP(6),
  appointment_type_ VARCHAR2(36),
  warn_opportunity_ INTEGER,
  remark_           VARCHAR2(512),
  createor_         VARCHAR2(36)
);
comment on table CRM_CUSTOMER_APPOINTMENT
  is 'ԤԼ��¼';
comment on column CRM_CUSTOMER_APPOINTMENT.id_
  is '����';
comment on column CRM_CUSTOMER_APPOINTMENT.cust_id_
  is '�ͻ�ID';
comment on column CRM_CUSTOMER_APPOINTMENT.appointment_time_
  is 'ԤԼʱ��';
comment on column CRM_CUSTOMER_APPOINTMENT.appointment_type_
  is '���š��ʼ������������ɶ�ѡ';
comment on column CRM_CUSTOMER_APPOINTMENT.warn_opportunity_
  is '����ʱ��';
comment on column CRM_CUSTOMER_APPOINTMENT.remark_
  is '��ע';
comment on column CRM_CUSTOMER_APPOINTMENT.createor_
  is '�Ǽ���';
alter table CRM_CUSTOMER_APPOINTMENT
  add constraint PK_CRM_CUSTOMER_APPOINTMENT primary key (ID_);
alter table CRM_CUSTOMER_APPOINTMENT
  add constraint FK_CRM_APPO_REFERENCE_CRM_CUST foreign key (CUST_ID_)
  references CRM_CUSTOMER_INFO (ID_);

/*==============================================================*/
/* Table: CRM_CUSTOMER_CONSULT                                  */
/*==============================================================*/
create table CRM_CUSTOMER_CONSULT
(
  id_           VARCHAR2(36) not null,
  cust_id_      VARCHAR2(36),
  content_      VARCHAR2(512),
  consult_time_ TIMESTAMP(6),
  createor_     VARCHAR2(36),
  inpnut_type_  VARCHAR2(36)
);
comment on table CRM_CUSTOMER_CONSULT
  is '��ѯ��¼';
comment on column CRM_CUSTOMER_CONSULT.id_
  is '����';
comment on column CRM_CUSTOMER_CONSULT.cust_id_
  is '�ͻ�ID';
comment on column CRM_CUSTOMER_CONSULT.content_
  is '����';
comment on column CRM_CUSTOMER_CONSULT.consult_time_
  is '��ѯʱ��';
comment on column CRM_CUSTOMER_CONSULT.createor_
  is '�Ǽ���';
comment on column CRM_CUSTOMER_CONSULT.inpnut_type_
  is '�ͷ�������';
alter table CRM_CUSTOMER_CONSULT
  add constraint PK_CRM_CUSTOMER_CONSULT primary key (ID_);
alter table CRM_CUSTOMER_CONSULT
  add constraint FK_CRM_CUST_REFERENCE_CRM_CUST foreign key (CUST_ID_)
  references CRM_CUSTOMER_INFO (ID_);

/*==============================================================*/
/* Table: CRM_GROUP_EXT                                         */
/*==============================================================*/
create table CRM_GROUP_EXT
(
  group_id_      VARCHAR2(36) not null,
  business_type_ VARCHAR2(100),
  group_name_     VARCHAR2(255),
  remark_        VARCHAR2(1024)
);

alter table CRM_GROUP_EXT
  add constraint PK_CRM_GROUP_EXT primary key (GROUP_ID_);

/*==============================================================*/
/* Table: CRM_IMPORT_CUSTOMER                                   */
/*==============================================================*/
create table CRM_IMPORT_CUSTOMER
(
  id_            VARCHAR2(36) not null,
  cust_name_     VARCHAR2(128),
  sex_           CHAR(1),
  tel_           VARCHAR2(256),
  email_         VARCHAR2(256),
  batch_number_  VARCHAR2(36),
  allocate_flag_ INTEGER,
  import_user_   VARCHAR2(36),
  import_time_   TIMESTAMP(6),
  allocate_to_   VARCHAR2(36),
  allocate_time_ TIMESTAMP(6),
  relate_custid_ VARCHAR2(36)
);
comment on table CRM_IMPORT_CUSTOMER
  is '����ͻ�';
comment on column CRM_IMPORT_CUSTOMER.id_
  is '����';
comment on column CRM_IMPORT_CUSTOMER.cust_name_
  is '�ͻ�����';
comment on column CRM_IMPORT_CUSTOMER.sex_
  is 'F:Ů��
M:����';
comment on column CRM_IMPORT_CUSTOMER.tel_
  is '�绰';
comment on column CRM_IMPORT_CUSTOMER.email_
  is '����';
comment on column CRM_IMPORT_CUSTOMER.batch_number_
  is '���κ�';
comment on column CRM_IMPORT_CUSTOMER.allocate_flag_
  is '0:��
1:��';
comment on column CRM_IMPORT_CUSTOMER.import_user_
  is '������';
comment on column CRM_IMPORT_CUSTOMER.import_time_
  is '����ʱ��';
comment on column CRM_IMPORT_CUSTOMER.allocate_to_
  is '�������';
comment on column CRM_IMPORT_CUSTOMER.allocate_time_
  is '����ʱ��';
comment on column CRM_IMPORT_CUSTOMER.relate_custid_
  is '���ͺ��Ӧ�Ŀͻ�ID';
alter table CRM_IMPORT_CUSTOMER
  add constraint PK_CRM_IMPORT_CUSTOMER primary key (ID_);

/*==============================================================*/
/* Table: CRM_RETURN_VIST                                       */
/*==============================================================*/
create table CRM_RETURN_VIST
(
  id_               VARCHAR2(36) not null,
  cust_id_          VARCHAR2(36),
  content_          VARCHAR2(512),
  return_vist_time_ TIMESTAMP(6),
  return_vist_user_ VARCHAR2(36)
);
comment on table CRM_RETURN_VIST
  is '�طü�¼';
comment on column CRM_RETURN_VIST.id_
  is '����';
comment on column CRM_RETURN_VIST.cust_id_
  is '�ͻ�ID';
comment on column CRM_RETURN_VIST.content_
  is '����';
comment on column CRM_RETURN_VIST.return_vist_time_
  is '�ط�ʱ��';
comment on column CRM_RETURN_VIST.return_vist_user_
  is '�ط���Ա';
alter table CRM_RETURN_VIST
  add constraint PK_CRM_RETURN_VIST primary key (ID_);
alter table CRM_RETURN_VIST
  add constraint FK_CRM_RETU_REFERENCE_CRM_CUST foreign key (CUST_ID_)
  references CRM_CUSTOMER_INFO (ID_);

/*==============================================================*/
/* Table: CRM_USER_ACCOUNT_EXT                                  */
/*==============================================================*/
create table CRM_USER_ACCOUNT_EXT
(
  user_id_            VARCHAR2(36) not null,
  account_id_         VARCHAR2(100),
  service_account_id_ VARCHAR2(100),
  remark_             VARCHAR2(1024)
);
comment on table CRM_USER_ACCOUNT_EXT
  is '�û��˺ű���չ';
comment on column CRM_USER_ACCOUNT_EXT.user_id_
  is '����';
comment on column CRM_USER_ACCOUNT_EXT.account_id_
  is '�磺kezhijian';
comment on column CRM_USER_ACCOUNT_EXT.service_account_id_
  is '�磺kezhijian';
comment on column CRM_USER_ACCOUNT_EXT.remark_
  is '��ע';
alter table CRM_USER_ACCOUNT_EXT
  add constraint PK_CRM_USER_ACCOUNT_EXT primary key (USER_ID_);
  
/*==============================================================*/
/* Table: CRM_CC_STAFF_INFO                                  */
/*==============================================================*/
create table CRM_CC_STAFF_INFO
(
  STAFF_ID_ VARCHAR2(36),
  STAFF_NO_ VARCHAR2(36) not null
);

alter table CRM_CC_STAFF_INFO
  add constraint FK_CRM_CC_STAFF_INFO primary key (STAFF_NO_);
  

/*==============================================================*/
/* Table: CRM_TEL_PART_AREA                                     */
/*==============================================================*/
CREATE TABLE CRM_TEL_PART_AREA  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   TEL_PART_            VARCHAR2(11),
   AREA_CODE_           VARCHAR2(10),
   CITY_NAME_           VARCHAR2(50),
   TEL_TYPE_            VARCHAR2(50),
   CONSTRAINT PK_CRM_TEL_PART_AREA PRIMARY KEY (ID_)
);

COMMENT ON TABLE CRM_TEL_PART_AREA IS
'�绰�������';

COMMENT ON COLUMN CRM_TEL_PART_AREA.ID_ IS
'����';

COMMENT ON COLUMN CRM_TEL_PART_AREA.TEL_PART_ IS
'�Ŷ�';

COMMENT ON COLUMN CRM_TEL_PART_AREA.AREA_CODE_ IS
'����';

COMMENT ON COLUMN CRM_TEL_PART_AREA.CITY_NAME_ IS
'����';

COMMENT ON COLUMN CRM_TEL_PART_AREA.TEL_TYPE_ IS
'����';

/*==============================================================*/
/* Index: INDEX_TEL_PART_                                       */
/*==============================================================*/
CREATE INDEX INDEX_TEL_PART_ ON CRM_TEL_PART_AREA (
   TEL_PART_ ASC
);


/*==============================================================*/
/* Table: CRM_RPT_QUERY_ASSIGN                                  */
/*==============================================================*/
CREATE TABLE CRM_RPT_QUERY_ASSIGN  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   RPT_ID_              VARCHAR2(36),
   USERACCOUNTID_       VARCHAR2(100),
   GROUPNAME_           VARCHAR2(100),
   CONSTRAINT PK_CRM_RPT_QUERY_ASSIGN PRIMARY KEY (ID_)
);

COMMENT ON TABLE CRM_RPT_QUERY_ASSIGN IS
'�����ѯ��Ȩ';

COMMENT ON COLUMN CRM_RPT_QUERY_ASSIGN.ID_ IS
'����';

COMMENT ON COLUMN CRM_RPT_QUERY_ASSIGN.RPT_ID_ IS
'����ID';

COMMENT ON COLUMN CRM_RPT_QUERY_ASSIGN.USERACCOUNTID_ IS
'�û��˺�';

COMMENT ON COLUMN CRM_RPT_QUERY_ASSIGN.GROUPNAME_ IS
'�鿴����';


/*==============================================================*/
/* Table: CRM_RPT_NORM_CONF                                     */
/*==============================================================*/
CREATE TABLE CRM_RPT_NORM_CONF  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   RPT_ID_              VARCHAR2(36),
   NORM_CODE_           VARCHAR2(50),
   NORM_NAME_           VARCHAR2(100),
   NORM_VALUE_          VARCHAR2(200),
   CONSTRAINT PK_CRM_RPT_NORM_CONF PRIMARY KEY (ID_)
);

COMMENT ON TABLE CRM_RPT_NORM_CONF IS
'����ָ������';

COMMENT ON COLUMN CRM_RPT_NORM_CONF.ID_ IS
'����';

COMMENT ON COLUMN CRM_RPT_NORM_CONF.RPT_ID_ IS
'����ID';

COMMENT ON COLUMN CRM_RPT_NORM_CONF.NORM_CODE_ IS
'ָ�����';

COMMENT ON COLUMN CRM_RPT_NORM_CONF.NORM_NAME_ IS
'ָ������';

COMMENT ON COLUMN CRM_RPT_NORM_CONF.NORM_VALUE_ IS
'ָ��ֵ';
