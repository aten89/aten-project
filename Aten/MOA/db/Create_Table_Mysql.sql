/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/8/22 19:04:33                           */
/*==============================================================*/


DROP TABLE IF EXISTS OA_ADDRESSLIST;

DROP TABLE IF EXISTS OA_AREADEVICECFG;

DROP TABLE IF EXISTS OA_ATTACHMENT;

DROP TABLE IF EXISTS OA_BUSTRIPAPPLY;

DROP TABLE IF EXISTS OA_BUSTRIPAPPLYDETAIL;

DROP TABLE IF EXISTS OA_BUSTRIPFLOWCONF;

DROP TABLE IF EXISTS OA_DEVDISCARDDISPOSEFORM;

DROP TABLE IF EXISTS OA_DEVALLOCATEFORM;

DROP TABLE IF EXISTS OA_DEVALLOCATELIST;

DROP TABLE IF EXISTS OA_DEVDISCARDFORM;

DROP TABLE IF EXISTS OA_DEVPURCHASEFORM;

DROP TABLE IF EXISTS OA_DEVPURCHASELIST;

DROP TABLE IF EXISTS OA_DEVREPAIRFORM;

DROP TABLE IF EXISTS OA_DEVVALIDATEFORM;

DROP TABLE IF EXISTS OA_DEVICE;

DROP TABLE IF EXISTS OA_DEVICECFGITEM;

DROP TABLE IF EXISTS OA_DEVICECHECKITEM;

DROP TABLE IF EXISTS OA_DEVICECLAASSIGNAREA;

DROP TABLE IF EXISTS OA_DEVICECLASS;

DROP TABLE IF EXISTS OA_DEVICECLASSASSIGN;

DROP TABLE IF EXISTS OA_DEVICECLASSASSIGNDETAIL;

DROP TABLE IF EXISTS OA_DEVICECURSTATUSINFO;

DROP TABLE IF EXISTS OA_DEVICEPARAMDETAIL;

DROP TABLE IF EXISTS OA_DEVICEPROPERTY;

DROP TABLE IF EXISTS OA_DEVICEUPDATELOG;

DROP TABLE IF EXISTS OA_DEVICEVALIDETAIL;

DROP TABLE IF EXISTS OA_DISCARDDEALDEVLIST;

DROP TABLE IF EXISTS OA_DISCARDDEVLIST;

DROP TABLE IF EXISTS OA_DOCCLASS;

DROP TABLE IF EXISTS OA_DOCCLASSASSIGN;

DROP TABLE IF EXISTS OA_DOCFORM;

DROP TABLE IF EXISTS OA_DOCFORM_ATTACH;

DROP TABLE IF EXISTS OA_DOCFORM_INFO;

DROP TABLE IF EXISTS OA_DOCNUMBER;

DROP TABLE IF EXISTS OA_FLOWCONFIG;

DROP TABLE IF EXISTS OA_FLOWMETA;

DROP TABLE IF EXISTS OA_FLOWNOTIFY;

DROP TABLE IF EXISTS OA_HRFLOWCONF;

DROP TABLE IF EXISTS OA_HOLIDAYAPPLY;

DROP TABLE IF EXISTS OA_HOLIDAYDETAIL;

DROP TABLE IF EXISTS OA_HOLIDAYTYPE;

DROP TABLE IF EXISTS OA_INDEXTASK;

DROP TABLE IF EXISTS OA_INFOFORM;

DROP TABLE IF EXISTS OA_INFOLAYOUT;

DROP TABLE IF EXISTS OA_INFOLAYOUTASSIGN;

DROP TABLE IF EXISTS OA_INFO_ATTACH;

DROP TABLE IF EXISTS OA_INFORMATION;

DROP TABLE IF EXISTS OA_KNOWLEDGE;

DROP TABLE IF EXISTS OA_KNOWLEDGECLASS;

DROP TABLE IF EXISTS OA_KNOWLEDGECLASSASSIGN;

DROP TABLE IF EXISTS OA_KNOWLEDGEREPLY;

DROP TABLE IF EXISTS OA_KNOWLEDGE_ATTACH;

DROP TABLE IF EXISTS OA_KNOWLEDGE_LOG;

DROP TABLE IF EXISTS OA_LABELLIB;

DROP TABLE IF EXISTS OA_MEETINGINFO;

DROP TABLE IF EXISTS OA_MEETINGMINUTE;

DROP TABLE IF EXISTS OA_MEETINGPARTICIPANT;

DROP TABLE IF EXISTS OA_MEETINGROOM;

DROP TABLE IF EXISTS OA_OUTLAYLIST;

DROP TABLE IF EXISTS OA_POSITIVEAPPLY;

DROP TABLE IF EXISTS OA_PURCHASEDEVPURPOSE;

DROP TABLE IF EXISTS OA_PURCHASEDEVICE;

DROP TABLE IF EXISTS OA_REIMFLOWCONF;

DROP TABLE IF EXISTS OA_REIMITEM;

DROP TABLE IF EXISTS OA_REIMBURSEMENT;

DROP TABLE IF EXISTS OA_SALARYBILL;

DROP TABLE IF EXISTS OA_STAFFFLOWAPPLY;

DROP TABLE IF EXISTS OA_STAFFFLOWQUERYASSIGN;

DROP TABLE IF EXISTS OA_TASK;

DROP TABLE IF EXISTS OA_TASKASSIGN;

DROP TABLE IF EXISTS OA_TRANSFERAPPLY;

DROP TABLE IF EXISTS OA_WORKEXPERIENCE;

/*==============================================================*/
/* Table: OA_ADDRESSLIST                                        */
/*==============================================================*/
CREATE TABLE OA_ADDRESSLIST
(
   ID_                  VARCHAR(36) NOT NULL,
   USERACCOUNTID_       VARCHAR(30),
   EMPLOYEENUMBER_      VARCHAR(30),
   SEATNUMBER_          VARCHAR(10),
   ENTERCORPDATE_       DATETIME,
   MOBILE_              VARCHAR(30),
   OFFICETEL_           VARCHAR(30),
   EMAIL_               VARCHAR(50),
   QQ_                  VARCHAR(20),
   MSN_                 VARCHAR(50),
   NICKNAME_            VARCHAR(30),
   SEX_                 CHAR(1),
   BIRTHDATE_           DATETIME,
   NATIVEPLACE_         VARCHAR(30),
   NATION_              VARCHAR(30),
   COMMADDR_            VARCHAR(100),
   ZIPCODE_             VARCHAR(10),
   HOMEADDR_            VARCHAR(100),
   HOMETEL_             VARCHAR(30),
   REMARK_              VARCHAR(4000),
   PHOTOURL_            VARCHAR(500),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_AREADEVICECFG                                      */
/*==============================================================*/
CREATE TABLE OA_AREADEVICECFG
(
   ID_                  VARCHAR(36) NOT NULL,
   DEVICETYPE_          VARCHAR(36),
   AREACODE_            VARCHAR(128),
   ORDERPREFIX_         VARCHAR(32),
   ORDERPOSTFIX_        VARCHAR(32),
   SEPARATOR_           VARCHAR(16),
   SEQNUM_              INT,
   PURCHASEFLOWKEY_     VARCHAR(36),
   USEAPPLYFLOWKEY_     VARCHAR(36),
   ALLOCATEFLOWKEY_     VARCHAR(36),
   DISCARDFLOWKEY_      VARCHAR(36),
   DIMISSIONFLOWKEY_    VARCHAR(36),
   MAINDEVFLAG_         INT,
   REMARK_              VARCHAR(512),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_ATTACHMENT                                         */
/*==============================================================*/
CREATE TABLE OA_ATTACHMENT
(
   ID_                  VARCHAR(36) NOT NULL,
   FILEEXT_             VARCHAR(36),
   FILEPATH_            VARCHAR(512),
   DISPLAYNAME_         VARCHAR(128),
   SIZE_                INT,
   UPLOADDATE_          DATETIME,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_BUSTRIPAPPLY                                       */
/*==============================================================*/
CREATE TABLE OA_BUSTRIPAPPLY
(
   ID_                  VARCHAR(36) NOT NULL,
   APPLICANT_           VARCHAR(36),
   APPLYDATE_           DATETIME,
   REGIONAL_            VARCHAR(36),
   APPLYDEPT_           VARCHAR(128),
   APPOINTTO_           VARCHAR(36),
   BORROWSUM_           NUMERIC(10,2),
   TERMTYPE_            VARCHAR(128),
   TOTALDAYS_           NUMERIC(5,1),
   TRIPAPPID_           VARCHAR(36),
   CHANGEREASON_        VARCHAR(512),
   ARCHIVEDATE_         DATETIME,
   FLOWINSTANCEID_      VARCHAR(36),
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   APPLYSTATUS_         SMALLINT COMMENT '0:取消；1：正常；2：变更',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_BUSTRIPAPPLYDETAIL                                 */
/*==============================================================*/
CREATE TABLE OA_BUSTRIPAPPLYDETAIL
(
   ID_                  VARCHAR(36) NOT NULL,
   FROMPLACE_           VARCHAR(128),
   TOPLACE_             VARCHAR(128),
   STARTDATE_           DATETIME,
   ENDDATE_             DATETIME,
   DAYS_                NUMERIC(5,1),
   CAUSA_               VARCHAR(512),
   TRIPAPPLYID_         VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_BUSTRIPFLOWCONF                                    */
/*==============================================================*/
CREATE TABLE OA_BUSTRIPFLOWCONF
(
   ID_                  VARCHAR(36) NOT NULL,
   GROUPNAME_           VARCHAR(128) COMMENT '来源于ERMP字典表，非空非重复约束',
   FLOWKEY_             VARCHAR(36),
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVDISCARDDISPOSEFORM                              */
/*==============================================================*/
CREATE TABLE OA_DEVDISCARDDISPOSEFORM
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICETYPE_          VARCHAR(128),
   REGACCOUNTID_        VARCHAR(128),
   REGTIME_             DATETIME,
   SALEPRICE_           NUMERIC(16,2) COMMENT '流程实例 FlowInstanceID_',
   SALEDATE_            DATETIME COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询是否生效 Passed_',
   FORMNO_              INT,
   SEQUENCEYEAR_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVALLOCATEFORM                                    */
/*==============================================================*/
CREATE TABLE OA_DEVALLOCATEFORM
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   REGACCOUNTID_        VARCHAR(128),
   REGTIME_             DATETIME,
   APPLICANT_           VARCHAR(128),
   APPLYGROUPNAME_      VARCHAR(128),
   APPLYDATE_           DATETIME,
   MOVETYPE_            VARCHAR(128) COMMENT '调拨类型 MoveType_
            0：调拨给其他人
            1：调拨回库存',
   INGROUPNAME_         VARCHAR(128) COMMENT '调入部门 InGroupName_',
   INACCOUNTID_         VARCHAR(128) COMMENT '调入部门经办人 InAccountID_',
   MOVEDATE_            DATETIME COMMENT '调拨日期 MoveDate_',
   REASON_              VARCHAR(2000) COMMENT '调拨原因 reason_',
   FLOWINSTANCEID_      VARCHAR(36) COMMENT '流程实例 FlowInstanceID_',
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询是否生效 Passed_',
   ARCHIVEDATE_         DATETIME COMMENT '归档时间 ArchiveDate_',
   FORMSTATUS_          SMALLINT COMMENT '0:草稿
            1:审批中
            2:已发布
            3:作废',
   DEVICEVALIDATEID_    VARCHAR(36) COMMENT '验收单ID DeviceValidateID_',
   APPLYFORMNO_         INT,
   DEVICETYPE_          VARCHAR(128),
   SEQUENCEYEAR_        SMALLINT,
   REFDEVUSEFORMID_     VARCHAR(36),
   AREACODE_            VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVALLOCATELIST                                    */
/*==============================================================*/
CREATE TABLE OA_DEVALLOCATELIST
(
   ID_                  VARCHAR(36) NOT NULL,
   DEVALLOCATEFORMID_   VARCHAR(36) COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36) COMMENT '主键 ID_',
   DEVICECFGDESC_       VARCHAR(2000),
   PURPOSEBEF_          VARCHAR(128),
   PURPOSE_             VARCHAR(128),
   AREACODEBEF_         VARCHAR(128),
   AREACODE_            VARCHAR(128),
   DEVSTATUSBEF_        SMALLINT,
   DISPLAYORDER_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVDISCARDFORM                                     */
/*==============================================================*/
CREATE TABLE OA_DEVDISCARDFORM
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICETYPE_          VARCHAR(128),
   REGACCOUNTID_        VARCHAR(128),
   REGTIME_             DATETIME,
   APPLICANT_           VARCHAR(128) COMMENT '申请人 AccountID_',
   APPLYGROUPNAME_      VARCHAR(128) COMMENT '申请部门 GroupName_',
   APPLYDATE_           DATETIME COMMENT '申请日期 ApplyDate_',
   FLOWINSTANCEID_      VARCHAR(36) COMMENT '流程实例 FlowInstanceID_',
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询是否生效 Passed_',
   ARCHIVEDATE_         DATETIME COMMENT '归档时间 ArchiveDate_',
   FORMSTATUS_          SMALLINT COMMENT '0:草稿
            1:审批中
            2:已发布
            3:作废',
   DEVICEVALIDATEID_    VARCHAR(36) COMMENT '验收单ID DeviceValidateID_',
   APPLYFORMNO_         INT,
   SEQUENCEYEAR_        SMALLINT,
   ENTERCOMPANYDATE_    DATETIME,
   WORKYEAR_            NUMERIC(16,2),
   FORMTYPE_            SMALLINT COMMENT '0：设备报废
            1：离职报废',
   AREACODE_            VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVPURCHASEFORM                                    */
/*==============================================================*/
CREATE TABLE OA_DEVPURCHASEFORM
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   APPLYTYPE_           SMALLINT COMMENT '0:领用;
            1:申购;',
   REGACCOUNTID_        VARCHAR(128),
   REGTIME_             DATETIME,
   APPLICANT_           VARCHAR(128) COMMENT '申购人 AccountID_',
   APPLYGROUPNAME_      VARCHAR(128) COMMENT '申请部门 GroupName_',
   APPLYDATE_           DATETIME COMMENT '申购日期 BuyDate_',
   BUYTYPE_             VARCHAR(128) COMMENT '申购方式 BuyType_',
   BUDGETMONEY_         NUMERIC(16,2) COMMENT '预算金额 BudgetMoney_',
   DEVCFGDESC_          VARCHAR(4000),
   REMARK_              VARCHAR(4000) COMMENT '领用(申购)说明 Remark_',
   DEVICEVALIDATEID_    VARCHAR(36) COMMENT '验收单ID DeviceValidateID_',
   FORMSTATUS_          SMALLINT COMMENT '0:草稿
            1:审批中
            2:已发布
            3:作废',
   DEVICETYPE_          VARCHAR(128) COMMENT '设备类型 DeviceClass_',
   FLOWINSTANCEID_      VARCHAR(36) COMMENT '流程实例 FlowInstanceID_',
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询是否生效 Passed_',
   ARCHIVEDATE_         DATETIME COMMENT '归档时间 ArchiveDate_',
   APPLYFORMNO_         INT,
   SEQUENCEYEAR_        SMALLINT,
   AREACODE_            VARCHAR(128),
   DEVICECLASS_         VARCHAR(128),
   WORKAREACODE_        VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVPURCHASELIST                                    */
/*==============================================================*/
CREATE TABLE OA_DEVPURCHASELIST
(
   ID_                  VARCHAR(36) NOT NULL,
   PURCHASEFORMID_      VARCHAR(36) COMMENT '设备领用、申购、调拨单ID',
   DEVICEID_            VARCHAR(36) COMMENT '主键 ID_',
   PURPOSE_             VARCHAR(128),
   AREACODE_            VARCHAR(128),
   PLANUSEDATE_         DATETIME,
   RETURNBACKDATE_      DATETIME,
   DISPLAYORDER_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVREPAIRFORM                                      */
/*==============================================================*/
CREATE TABLE OA_DEVREPAIRFORM
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36) COMMENT '设备ID DeviceID_',
   DEVICENAME_          VARCHAR(128),
   REPAIRDEVICENO_      INT COMMENT '维修单编号 RepairDeviceNo_',
   REGACCOUNTID_        VARCHAR(128),
   CREATETIME_          TIMESTAMP COMMENT '登记时间 CreateTime_',
   ACCOUNTID_           VARCHAR(128) COMMENT '申请人 AccountID_',
   GROUPNAME_           VARCHAR(128) COMMENT '申请部门 GroupName_',
   APPLYTIME_           DATETIME,
   STATUS_              SMALLINT COMMENT '维修单状态 Status_
            0：维修中
            1：已维修',
   BUDGETMONEY_         NUMERIC(16,2) COMMENT '预算费用 BudgetMoney_',
   REASON_              VARCHAR(1000) COMMENT '损坏原因 Reason_',
   REMARK_              VARCHAR(1000) COMMENT '维修说明 Remark_',
   REPAIRMAN_           VARCHAR(128),
   SEQUENCEYEAR_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVVALIDATEFORM                                    */
/*==============================================================*/
CREATE TABLE OA_DEVVALIDATEFORM
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36) COMMENT '设备ID DeviceID_',
   PURCHASEDEVID_       VARCHAR(36) COMMENT '主键 ID_',
   VALITYPE_            SMALLINT COMMENT '0.调拨验收1.采购验收2.直接入库验收
            验收单类型 ValiType_',
   ACCOUNTID_           VARCHAR(128) COMMENT '检验人 AccountID_',
   VALIDATE_            DATETIME COMMENT '检验日期 ValiDate_',
   REMARK_              VARCHAR(1000) COMMENT '备注 Remark_',
   VALIDFORMNO_         INT,
   SEQUENCEYEAR_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICE                                             */
/*==============================================================*/
CREATE TABLE OA_DEVICE
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICENO_            VARCHAR(36) COMMENT '设备编号 DeviceNO_',
   DEVICETYPE_          VARCHAR(128) COMMENT '设备类型 Type_',
   DEVICECLASS_         VARCHAR(128) COMMENT '设备类别 DeviceSort_',
   DEVICENAME_          VARCHAR(128) COMMENT '设备名称 DeviceName_',
   DEVICEMODEL_         VARCHAR(128) COMMENT '设备型号 DeviceModel_',
   DESCRIPTION_         VARCHAR(2000) COMMENT '设备描述 Description_',
   REGACCOUNTID_        VARCHAR(128),
   REGTIME_             TIMESTAMP COMMENT '登记时间 RegTime_',
   BUYTIME_             TIMESTAMP COMMENT '购买时间 BuyTime_',
   BUYTYPE_             VARCHAR(128),
   DEDUCTFLAG_          SMALLINT,
   DEDUCTMONEY_         NUMERIC(16,2),
   INDATE_              DATETIME,
   AREACODE_            VARCHAR(128),
   SEQUENCE_            INT,
   PRICE_               NUMERIC(16,2),
   FINANCEORIGINALVAL_  NUMERIC(16,2),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECFGITEM                                      */
/*==============================================================*/
CREATE TABLE OA_DEVICECFGITEM
(
   ID_                  VARCHAR(36) NOT NULL,
   DEVICETYPE_          VARCHAR(36),
   ITEMID_              VARCHAR(36) COMMENT '主键 ID_',
   DISPLAYORDER_        INT,
   REMARK_              VARCHAR(512),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECHECKITEM                                    */
/*==============================================================*/
CREATE TABLE OA_DEVICECHECKITEM
(
   ID_                  VARCHAR(36) NOT NULL,
   ITEMNAME_            VARCHAR(128),
   DEVICECLASSID_       VARCHAR(36),
   DISPLAYORDER_        INT,
   REMARK_              VARCHAR(512),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECLAASSIGNAREA                                */
/*==============================================================*/
CREATE TABLE OA_DEVICECLAASSIGNAREA
(
   ID_                  VARCHAR(36) NOT NULL,
   ASSIGNID_            VARCHAR(36),
   DEVICECLASS_         VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECLASS                                        */
/*==============================================================*/
CREATE TABLE OA_DEVICECLASS
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(128),
   REMARK_              VARCHAR(512),
   DEVICETYPE_          VARCHAR(128),
   STATUS_              INT DEFAULT 1 COMMENT '0：删除
            1：正常',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECLASSASSIGN                                  */
/*==============================================================*/
CREATE TABLE OA_DEVICECLASSASSIGN
(
   ID_                  VARCHAR(36) NOT NULL,
   AREACODE_            VARCHAR(128),
   CONFIGTIME_          DATETIME,
   REMARK_              VARCHAR(512),
   ASSIGNEDVALUE_       VARCHAR(128) COMMENT '该字段是用逗号分隔的授权类别串。如：0，1，2说明已授予三种权限。',
   DEVICETYPE_          VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECLASSASSIGNDETAIL                            */
/*==============================================================*/
CREATE TABLE OA_DEVICECLASSASSIGNDETAIL
(
   ID_                  VARCHAR(36) NOT NULL,
   ASSIGNCLASS_         INT,
   ASSIGNID_            VARCHAR(36),
   TYPE_                CHAR(1) COMMENT '0：用户
            1：职位
            2：机构',
   ASSIGNKEY_           VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICECURSTATUSINFO                                */
/*==============================================================*/
CREATE TABLE OA_DEVICECURSTATUSINFO
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36),
   OWNER_               VARCHAR(128),
   GROUPNAME_           VARCHAR(128),
   DEVICECURSTATUS_     INT COMMENT '0：已删除
            1：正常
            4:离职回购
            5:离职赠送
            6:拿走
            7:报废未处理
            8:报废已处理
            ',
   STATUSUPTDATE_       DATETIME,
   AREACODE_            VARCHAR(128),
   PURPOSE_             VARCHAR(128),
   APPROVETYPE_         SMALLINT COMMENT '1:领用审批中
            2:调拨审批中
            3:报废审批中
            4:离职审批中',
   FORMID_              VARCHAR(36) COMMENT '用于跟踪',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICEPARAMDETAIL                                  */
/*==============================================================*/
CREATE TABLE OA_DEVICEPARAMDETAIL
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36) COMMENT '设备ID DeviceID_',
   PURCHASEDEVID_       VARCHAR(36) COMMENT '主键 ID_',
   PROPERTYNAME_        VARCHAR(128) COMMENT '属性名 PropertyName_',
   PROPERTYVALUE_       VARCHAR(128) COMMENT '属性值 PropertyValue_',
   REMARK_              VARCHAR(512),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICEPROPERTY                                     */
/*==============================================================*/
CREATE TABLE OA_DEVICEPROPERTY
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   PROPERTYNAME_        VARCHAR(128) COMMENT '属性名 PropertyName_',
   REMARK_              VARCHAR(512),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICEUPDATELOG                                    */
/*==============================================================*/
CREATE TABLE OA_DEVICEUPDATELOG
(
   ID_                  VARCHAR(36) NOT NULL,
   DEVICEID_            VARCHAR(36) COMMENT '主键 ID_',
   OPERATOR_            VARCHAR(128),
   OPERATEDATE_         DATETIME,
   OPERATETYPE_         SMALLINT,
   UPDATECONTENT_       VARCHAR(2000),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DEVICEVALIDETAIL                                   */
/*==============================================================*/
CREATE TABLE OA_DEVICEVALIDETAIL
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   DEVICEVALIDATEID_    VARCHAR(36) COMMENT '验收单ID',
   ITEM_                VARCHAR(256) COMMENT '检查项目 Item_',
   ISELIGIBILITY_       SMALLINT COMMENT '是否合格 IsEligibility_
            0：���合格
            1：合格',
   REMARK_              VARCHAR(1000) COMMENT '备注 Remark_',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DISCARDDEALDEVLIST                                 */
/*==============================================================*/
CREATE TABLE OA_DISCARDDEALDEVLIST
(
   ID_                  VARCHAR(36) NOT NULL,
   DISCARDDEALFORMID_   VARCHAR(36) COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36) COMMENT '主键 ID_',
   DISPLAYORDER_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DISCARDDEVLIST                                     */
/*==============================================================*/
CREATE TABLE OA_DISCARDDEVLIST
(
   ID_                  VARCHAR(36) NOT NULL,
   DISCARDFORMID_       VARCHAR(36) COMMENT '主键 ID_',
   DEVICEID_            VARCHAR(36) COMMENT '主键 ID_',
   OWNERTYPE_           SMALLINT,
   REASON_              VARCHAR(2000),
   DEALTYPE_            VARCHAR(128),
   DEALDATE_            DATETIME,
   DISCARDTYPE_         VARCHAR(128),
   DISCARDDATE_         DATETIME,
   REMAINING_           NUMERIC(16,2),
   DEPRECIATION_        NUMERIC(16,2),
   BUYFLAG_             SMALLINT,
   BUYPRICE_            NUMERIC(16,2),
   NOBUYPRICE_          NUMERIC(16,2),
   INDATE_              DATETIME,
   DISPLAYORDER_        SMALLINT,
   PLANPAYDATE_         DATETIME,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DOCCLASS                                           */
/*==============================================================*/
CREATE TABLE OA_DOCCLASS
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(128) COMMENT '来源于ERMP字典表，非空非重复约束',
   FLOWCLASS_           VARCHAR(36),
   DISPLAYORDER_        INT,
   DESCRIPTION_         VARCHAR(1024),
   BODYTEMPLATE_        VARCHAR(36),
   FILECLASS_           INT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DOCCLASSASSIGN                                     */
/*==============================================================*/
CREATE TABLE OA_DOCCLASSASSIGN
(
   ID_                  VARCHAR(36) NOT NULL,
   DOCCLASSID_          VARCHAR(36),
   TYPE_                CHAR(1) COMMENT '0：用户
            1：角色
            2：机构',
   ASSIGNKEY_           VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DOCFORM                                            */
/*==============================================================*/
CREATE TABLE OA_DOCFORM
(
   ID_                  VARCHAR(36) NOT NULL,
   DRAFTSMAN_           VARCHAR(128),
   DOCCLASS_            VARCHAR(128),
   GROUPNAME_           VARCHAR(128),
   DRAFTDATE_           DATETIME,
   DOCNUMBER_           VARCHAR(256),
   SUBMITTO_            VARCHAR(512),
   COPYTO_              VARCHAR(512),
   SUBJECT_             VARCHAR(256),
   BODYDRAFTDOC_        VARCHAR(36),
   BODYDOC_             VARCHAR(36),
   FLOWINSTANCEID_      VARCHAR(36),
   FORMSTATUS_          SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   ARCHIVEDATE_         DATETIME,
   URGENCY_             VARCHAR(128),
   SECURITYCLASS_       VARCHAR(128),
   COPYDOCFORMID_       VARCHAR(36),
   SIGNGROUPNAMES_      VARCHAR(512),
   FILECLASS_           INT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_DOCFORM_ATTACH                                     */
/*==============================================================*/
CREATE TABLE OA_DOCFORM_ATTACH
(
   DOCFORMID_           VARCHAR(36) NOT NULL,
   ATTACHMENTID_        VARCHAR(36) NOT NULL,
   PRIMARY KEY (DOCFORMID_, ATTACHMENTID_)
);

/*==============================================================*/
/* Table: OA_DOCFORM_INFO                                       */
/*==============================================================*/
CREATE TABLE OA_DOCFORM_INFO
(
   DOCFORMID_           VARCHAR(36) NOT NULL,
   INFORMATIONID_       VARCHAR(36) NOT NULL,
   PRIMARY KEY (DOCFORMID_, INFORMATIONID_)
);

/*==============================================================*/
/* Table: OA_DOCNUMBER                                          */
/*==============================================================*/
CREATE TABLE OA_DOCNUMBER
(
   ID_                  VARCHAR(36) NOT NULL,
   DOCWORD_             VARCHAR(128),
   YEARPREFIX_          VARCHAR(32),
   CURRENTYEAR_         INT,
   YEARPOSTFIX_         VARCHAR(32),
   ORDERPREFIX_         VARCHAR(32),
   ORDERNUMBER_         INT,
   ORDERPOSTFIX_        VARCHAR(32),
   HEADTEMPLATE_        VARCHAR(32),
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_FLOWCONFIG                                         */
/*==============================================================*/
CREATE TABLE OA_FLOWCONFIG
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWCLASS_           VARCHAR(128),
   FLOWKEY_             VARCHAR(36),
   FLOWNAME_            VARCHAR(400),
   FLOWVERSION_         NUMERIC(14,0),
   DRAFTFLAG_           CHAR(1),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_FLOWMETA                                           */
/*==============================================================*/
CREATE TABLE OA_FLOWMETA
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWCLASS_           VARCHAR(128),
   NAME_                VARCHAR(200),
   DISPLAYNAME_         VARCHAR(200),
   TYPE_                VARCHAR(20),
   NOTNULL_             SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_FLOWNOTIFY                                         */
/*==============================================================*/
CREATE TABLE OA_FLOWNOTIFY
(
   ID_                  VARCHAR(36) NOT NULL,
   SUBJECT_             VARCHAR(200),
   NOTIFYUSER_          VARCHAR(100),
   NOTIFYTYPE_          SMALLINT COMMENT '1立即知会、3流程结束后知会',
   CREATOR_             VARCHAR(100),
   GROUPFULLNAME_       VARCHAR(500),
   CREATETIME_          DATETIME,
   STATUS_              SMALLINT COMMENT '0登记、1知会、2已读',
   FLOWCLASS_           VARCHAR(20),
   NOTIFYTIME_          DATETIME,
   REFFORMID_           VARCHAR(36),
   VIEWDETAILURL_       VARCHAR(200),
   FLOW_STATUS_         SMALLINT DEFAULT -1 COMMENT '0作废、1通过、-1未知',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_HRFLOWCONF                                         */
/*==============================================================*/
CREATE TABLE OA_HRFLOWCONF
(
   ID_                  VARCHAR(36) NOT NULL,
   GROUPNAME_           VARCHAR(128) COMMENT '来源于ERMP字典表，非空非重复约束',
   HOLIDAYFLOWKEY_      VARCHAR(36),
   CANHOLIDAYFLOWKEY_   VARCHAR(36),
   ENTRYFLOWKEY_        VARCHAR(36),
   RESIGNFLOWKEY_       VARCHAR(36),
   DESCRIPTION_         VARCHAR(256),
   TRANSFERFLOWKEY_     VARCHAR(36),
   POSITIVEFLOWKEY_     VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_HOLIDAYAPPLY                                       */
/*==============================================================*/
CREATE TABLE OA_HOLIDAYAPPLY
(
   ID_                  VARCHAR(36) NOT NULL,
   APPLICANT_           VARCHAR(36),
   APPLYDATE_           DATETIME,
   APPLYDEPT_           VARCHAR(128),
   APPOINTTO_           VARCHAR(36) COMMENT '0：普通
            1：不可回复
            2：只读',
   ISSPECIAL_           SMALLINT,
   SPECIALREASON_       VARCHAR(256),
   TOTALDAYS_           DOUBLE(5,1),
   REGIONAL_            VARCHAR(36),
   CANCELFLAG_          SMALLINT DEFAULT 0,
   REMARK_              VARCHAR(36),
   FLOWINSTANCEID_      VARCHAR(36),
   ARCHIVEDATE_         DATETIME,
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   APPLYSTATUS_         SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_HOLIDAYDETAIL                                      */
/*==============================================================*/
CREATE TABLE OA_HOLIDAYDETAIL
(
   ID_                  VARCHAR(36) NOT NULL,
   HOLIDAYAPPLYID_      VARCHAR(36),
   HOLIDAYNAME_         VARCHAR(36),
   STARTDATE_           DATETIME,
   STARTTIME_           VARCHAR(8),
   ENDDATE_             DATETIME,
   ENDTIME_             VARCHAR(8),
   DAYS_                DOUBLE(5,1),
   REMARK_              VARCHAR(200),
   CANCELDAYS_          DOUBLE(5,1),
   CANCELREMARK_        VARCHAR(200),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_HOLIDAYTYPE                                        */
/*==============================================================*/
CREATE TABLE OA_HOLIDAYTYPE
(
   ID_                  VARCHAR(36) NOT NULL,
   HOLIDAYNAME_         VARCHAR(256),
   MAXDAYS_             DOUBLE(5,1),
   EXPRESSION_          VARCHAR(256),
   DESCRIPTION_         VARCHAR(5),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_INDEXTASK                                          */
/*==============================================================*/
CREATE TABLE OA_INDEXTASK
(
   ID_                  VARCHAR(36) NOT NULL,
   ACTION_              VARCHAR(36),
   DOCUMENTID_          VARCHAR(36),
   DOCUMENTTYPE_        VARCHAR(36),
   CREATEDATE_          DATETIME,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_INFOFORM                                           */
/*==============================================================*/
CREATE TABLE OA_INFOFORM
(
   ID_                  VARCHAR(36) NOT NULL,
   CONTENTDOC_          VARCHAR(36),
   FLOWINSTANCEID_      VARCHAR(36),
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   ARCHIVEDATE_         DATETIME,
   INFORMATIONID_       VARCHAR(36),
   COPYINFOFORMID_      VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_INFOLAYOUT                                         */
/*==============================================================*/
CREATE TABLE OA_INFOLAYOUT
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(128) COMMENT '来源于ERMP字典表，非空非重复约束',
   FLOWCLASS_           VARCHAR(36),
   DISPLAYORDER_        INT,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_INFOLAYOUTASSIGN                                   */
/*==============================================================*/
CREATE TABLE OA_INFOLAYOUTASSIGN
(
   ID_                  VARCHAR(36) NOT NULL,
   INFOLAYOUTID_        VARCHAR(36),
   TYPE_                CHAR(1) COMMENT '0：用户
            1：角色
            2：机构',
   ASSIGNKEY_           VARCHAR(128),
   FLAG_                CHAR(1),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_INFO_ATTACH                                        */
/*==============================================================*/
CREATE TABLE OA_INFO_ATTACH
(
   INFOID_              VARCHAR(36) NOT NULL,
   ATTACHMENTID_        VARCHAR(36) NOT NULL,
   PRIMARY KEY (INFOID_, ATTACHMENTID_)
);

/*==============================================================*/
/* Table: OA_INFORMATION                                        */
/*==============================================================*/
CREATE TABLE OA_INFORMATION
(
   ID_                  VARCHAR(36) NOT NULL,
   SUBJECT_             VARCHAR(256),
   INFOLAYOUT_          VARCHAR(128),
   INFOCLASS_           VARCHAR(128),
   CONTENT_             TEXT,
   CONTENTURL_          VARCHAR(512),
   DISPLAYMODE_         SMALLINT DEFAULT 0 COMMENT '0：内容地址，
            1：直接显示内容，
            默认0',
   INFOSTATUS_          SMALLINT DEFAULT 0 COMMENT '0：未发布
            1：已发布',
   INFOPROPERTY_        SMALLINT DEFAULT 0 COMMENT '0：普通
            1：置顶
            2：屏蔽',
   GROUPNAME_           VARCHAR(128),
   DRAFTSMAN_           VARCHAR(128),
   DRAFTDATE_           DATETIME,
   PUBLICDATE_          DATETIME,
   INVALIDDATE_         DATETIME,
   SUBJECTCOLOR_        VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_KNOWLEDGE                                          */
/*==============================================================*/
CREATE TABLE OA_KNOWLEDGE
(
   ID_                  VARCHAR(36) NOT NULL,
   KNOWLEDGECLASSID_    VARCHAR(36),
   SUBJECT_             VARCHAR(256),
   LABELS_              VARCHAR(1000),
   REMARK_              VARCHAR(4000),
   CONTENT_             TEXT,
   CONTENTURL_          VARCHAR(512),
   DISPLAYMODE_         SMALLINT DEFAULT 0 COMMENT '0：内容地址，
            1：直接显示内容，
            默认0',
   GROUPNAME_           VARCHAR(128),
   PUBLISHER_           VARCHAR(128),
   PUBLISHDATE_         DATETIME,
   MODIFYDATE_          DATETIME,
   PROPERTY_            SMALLINT COMMENT '0：普通
            1：不可回复
            2：只读',
   STATUS_              SMALLINT COMMENT '0：收集中
            1：正式
            ',
   FIRSTTYPE_           VARCHAR(128),
   SECONDTYPE_          VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_KNOWLEDGECLASS                                     */
/*==============================================================*/
CREATE TABLE OA_KNOWLEDGECLASS
(
   ID_                  VARCHAR(36) NOT NULL,
   PARENTID_            VARCHAR(36),
   NAME_                VARCHAR(128),
   DISPLAYORDER_        INT,
   TREELEVEL_           INT,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_KNOWLEDGECLASSASSIGN                               */
/*==============================================================*/
CREATE TABLE OA_KNOWLEDGECLASSASSIGN
(
   ID_                  VARCHAR(36) NOT NULL,
   KNOWLEDGECLASSID_    VARCHAR(36),
   TYPE_                CHAR(1) COMMENT '0：用户
            1：角色
            2：机构',
   ASSIGNKEY_           VARCHAR(128),
   FLAG_                CHAR(1),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_KNOWLEDGEREPLY                                     */
/*==============================================================*/
CREATE TABLE OA_KNOWLEDGEREPLY
(
   ID_                  VARCHAR(36) NOT NULL,
   KNOWLEDGEID_         VARCHAR(36),
   CONTENT_             TEXT,
   GROUPNAME_           VARCHAR(128),
   REPLYMAN_            VARCHAR(128),
   REPLYDATE_           DATETIME,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_KNOWLEDGE_ATTACH                                   */
/*==============================================================*/
CREATE TABLE OA_KNOWLEDGE_ATTACH
(
   KNOWLEDGEID_         VARCHAR(36) NOT NULL,
   ATTACHMENTID_        VARCHAR(36) NOT NULL,
   ID_                  VARCHAR(36),
   TYPE_                INT COMMENT '0：附件
            1：内容图片
            2：内容视频',
   PRIMARY KEY (KNOWLEDGEID_, ATTACHMENTID_)
);

/*==============================================================*/
/* Table: OA_KNOWLEDGE_LOG                                      */
/*==============================================================*/
CREATE TABLE OA_KNOWLEDGE_LOG
(
   ID_                  VARCHAR(36) NOT NULL,
   USERID_              VARCHAR(128),
   TYPE_                VARCHAR(36),
   KNOWLEDGEID_         VARCHAR(36),
   KNOWLEDGETITLE_      VARCHAR(256),
   OPERATETIME_         DATETIME
);

/*==============================================================*/
/* Table: OA_LABELLIB                                           */
/*==============================================================*/
CREATE TABLE OA_LABELLIB
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(256),
   COUNT_               NUMERIC(10,0),
   PROPERTY_            SMALLINT DEFAULT 0 COMMENT '0：普通',
   CREATEDATE_          DATETIME,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_MEETINGINFO                                        */
/*==============================================================*/
CREATE TABLE OA_MEETINGINFO
(
   ID_                  VARCHAR(36) NOT NULL,
   APPLYMAN_            VARCHAR(128),
   GROUPNAME_           VARCHAR(128),
   RESERVETIME_         DATETIME,
   ROOMID_              VARCHAR(36),
   THEME_               VARCHAR(128),
   CONTENT_             TEXT,
   BEGINTIME_           DATETIME,
   ENDTIME_             DATETIME,
   REMARK_              VARCHAR(2048),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_MEETINGMINUTE                                      */
/*==============================================================*/
CREATE TABLE OA_MEETINGMINUTE
(
   MEETINGID_           VARCHAR(36) NOT NULL,
   ATTACHMENTID_        VARCHAR(36) NOT NULL,
   ID_                  VARCHAR(36),
   TYPE_                INT COMMENT '0：会议附件
            1：会议纪要
            2：邮件内容图片',
   PRIMARY KEY (MEETINGID_, ATTACHMENTID_)
);

/*==============================================================*/
/* Table: OA_MEETINGPARTICIPANT                                 */
/*==============================================================*/
CREATE TABLE OA_MEETINGPARTICIPANT
(
   ID_                  VARCHAR(36) NOT NULL,
   MEETINGID_           VARCHAR(36),
   PARTICIPANT_         VARCHAR(128) COMMENT '以后有通讯录时，改为通讯录表的外键',
   EMAIL_               VARCHAR(64),
   NAME_                VARCHAR(128),
   TYPE_                SMALLINT COMMENT '0：系统用户
            1：手动添加的用户',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_MEETINGROOM                                        */
/*==============================================================*/
CREATE TABLE OA_MEETINGROOM
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(128),
   SEATNUM_             INT,
   ENVIRONMENT_         VARCHAR(128),
   POWERJACKNUM_        INT,
   CABLENUM_            INT,
   LINENUM_             INT,
   PHONENUMBER_         VARCHAR(128),
   STATUS_              SMALLINT COMMENT '0：可预订
            1：不可预订
            ',
   DISPLAYORDER_        INT,
   REMARK_              VARCHAR(2048),
   AREACODE_            VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_OUTLAYLIST                                         */
/*==============================================================*/
CREATE TABLE OA_OUTLAYLIST
(
   ID_                  VARCHAR(36) NOT NULL,
   REIMBURSEMENTID_     VARCHAR(36),
   OUTLAYCATEGORY_      VARCHAR(128),
   OUTLAYNAME_          VARCHAR(128),
   DOCUMETNUM_          INT,
   OUTLAYSUM_           NUMERIC(10,2),
   DESCRIPTION_         VARCHAR(512),
   REIMITEMID_          VARCHAR(36),
   DISPLAYORDER_        INT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_POSITIVEAPPLY                                      */
/*==============================================================*/
CREATE TABLE OA_POSITIVEAPPLY
(
   ID_                  VARCHAR(36) NOT NULL,
   APPLICANT_           VARCHAR(36),
   APPLYDATE_           DATETIME,
   POSITIVEUSER_        VARCHAR(36),
   DEPT_                VARCHAR(128),
   ENTRYDATE_           DATETIME,
   SEX_                 SMALLINT,
   POST_                VARCHAR(100),
   PROBATION_           SMALLINT,
   FORMALDATE_          DATETIME,
   FORMALTYPE_          VARCHAR(100),
   WORKRESULTS_         VARCHAR(1000),
   CULTUREUNDERSTAND_   VARCHAR(1000),
   RULESCOMPLIANCE_     VARCHAR(1000),
   WORKEXPERIENCE_      VARCHAR(1000),
   WORKSUMMARY_         VARCHAR(1000),
   WORKIMPROVE_         VARCHAR(1000),
   FLOWINSTANCEID_      VARCHAR(36),
   ARCHIVEDATE_         DATETIME,
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   APPLYSTATUS_         SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_PURCHASEDEVPURPOSE                                 */
/*==============================================================*/
CREATE TABLE OA_PURCHASEDEVPURPOSE
(
   ID_                  VARCHAR(36) NOT NULL,
   PURCHASEFORMID_      VARCHAR(36) COMMENT '主键 ID_',
   PURPOSE_             VARCHAR(128),
   SELECTEDFLAG_        SMALLINT,
   MANYTIMEFLAG_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_PURCHASEDEVICE                                     */
/*==============================================================*/
CREATE TABLE OA_PURCHASEDEVICE
(
   ID_                  VARCHAR(36) NOT NULL COMMENT '主键 ID_',
   PURCHASEFORMID_      VARCHAR(36) COMMENT '主键 ID_',
   DEVICECLASS_         VARCHAR(128) COMMENT '设备类别 DeviceSort_',
   DEVICENAME_          VARCHAR(128) COMMENT '设备名称 DeviceName_',
   DEVICEMODEL_         VARCHAR(128) COMMENT '设备型号 DeviceModel_',
   DESCRIPTION_         VARCHAR(2000) COMMENT '设备描述 Description_',
   BUYTIME_             DATETIME COMMENT '购买时间 BuyTime_',
   PRICE_               NUMERIC(16,2),
   DEDUCTFLAG_          SMALLINT,
   DEDUCTMONEY_         NUMERIC(16,2),
   INDATE_              DATETIME,
   DEVICEID_            VARCHAR(36),
   DEVICECFGDESC_       VARCHAR(2000),
   PURPOSE_             VARCHAR(128),
   PLANUSEDATE_         DATETIME,
   BELONGTOAREACODE_    VARCHAR(128),
   AREACODE_            VARCHAR(128),
   RETURNBACKDATE_      DATETIME,
   DISPLAYORDER_        SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_REIMFLOWCONF                                       */
/*==============================================================*/
CREATE TABLE OA_REIMFLOWCONF
(
   ID_                  VARCHAR(36) NOT NULL,
   GROUPNAME_           VARCHAR(128) COMMENT '来源于ERMP字典表，非空非重复约束',
   FLOWKEY_             VARCHAR(36),
   DESCRIPTION_         VARCHAR(256),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_REIMITEM                                           */
/*==============================================================*/
CREATE TABLE OA_REIMITEM
(
   ID_                  VARCHAR(36) NOT NULL,
   REIMBURSEMENTID_     VARCHAR(36),
   REGIONNAME_          VARCHAR(128),
   CUSTOMNAME_          VARCHAR(128),
   TRAVELPLACE_         VARCHAR(512),
   TRAVELBEGINDATE_     DATETIME,
   TRAVELENDDATE_       DATETIME,
   COTERIELLIST_        VARCHAR(1024),
   DISPLAYORDER_        INT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_REIMBURSEMENT                                      */
/*==============================================================*/
CREATE TABLE OA_REIMBURSEMENT
(
   ID_                  VARCHAR(36) NOT NULL,
   FINANCE_             VARCHAR(10) COMMENT '0：福州
            1：上海',
   APPLICANT_           VARCHAR(128),
   APPLYDATE_           DATETIME,
   APPLYDEPT_           VARCHAR(128),
   PAYEE_               VARCHAR(128),
   CAUSA_               VARCHAR(4000),
   LOANSUM_             NUMERIC(10,2),
   REIMBURSEMENTSUM_    NUMERIC(16,2),
   APPOINTTO_           VARCHAR(128),
   ARCHIVEDATE_         DATETIME COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   FLOWINSTANCEID_      VARCHAR(36),
   FORMSTATUS_          SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_SALARYBILL                                         */
/*==============================================================*/
CREATE TABLE OA_SALARYBILL
(
   ID_                  VARCHAR(36) NOT NULL,
   USERACCOUNTID_       VARCHAR(100),
   MONTH_               NUMERIC(6,0),
   IMPORTDATE_          DATETIME,
   DEPT_                VARCHAR(100),
   POST_                VARCHAR(36),
   EMPLOYEENUMBER_      VARCHAR(30),
   USERNAME_            VARCHAR(100),
   ENTRYDATE_           DATETIME,
   PEOPLENUMS_          NUMERIC(4,0),
   STATUS_              VARCHAR(50),
   WAGETOTAL_           NUMERIC(10,2),
   WAGEBASIC_           NUMERIC(10,2),
   WAGEPERFORMANCE_     NUMERIC(10,2),
   SCOREPERFORMANCE_    NUMERIC(10,2),
   WAGEPERFORMANCEREAL_ NUMERIC(10,2),
   ALLOWANCE_           NUMERIC(10,2),
   COMMISSION_          NUMERIC(10,2),
   LESSMONTHDAYS_       NUMERIC(2,0),
   DEDUCTLESSMONTH_     NUMERIC(10,2),
   LEAVECOMPASSIONATE_  NUMERIC(2,0),
   DEDUCTCOMPASSIONATE_ NUMERIC(10,2),
   LEAVESICK_           NUMERIC(2,0),
   DEDUCTSICK_          NUMERIC(10,2),
   DEDUCTLATE_          NUMERIC(10,2),
   DEDUCTELSE_          NUMERIC(10,2),
   WAGEPAYABLE_         NUMERIC(10,2),
   PENSION_             NUMERIC(10,2),
   LOSTJOB_             NUMERIC(10,2),
   MEDICAL_             NUMERIC(10,2),
   INSURANCEPAYMENT_    NUMERIC(10,2),
   COSTSOCIALSECURITY_  NUMERIC(10,2),
   COSTACCUMULATIONFUND_ NUMERIC(10,2),
   COSTFIVEINSURANCE_   NUMERIC(10,2),
   TAXWAGE_             NUMERIC(10,2),
   WAGEPRETAX_          NUMERIC(10,2),
   TAXPERSONAL_         NUMERIC(10,2),
   WAGEREAL_            NUMERIC(10,2),
   WAGEALLOWANCE_       NUMERIC(10,2),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_STAFFFLOWAPPLY                                     */
/*==============================================================*/
CREATE TABLE OA_STAFFFLOWAPPLY
(
   ID_                  VARCHAR(36) NOT NULL,
   COMPANYAREA_         VARCHAR(128),
   GROUPNAME_           VARCHAR(128),
   USERACCOUNTID_       VARCHAR(36),
   POST_                VARCHAR(36),
   EMPLOYEENUMBER_      VARCHAR(30),
   USERNAME_            VARCHAR(36),
   SEX_                 SMALLINT,
   IDCARD_              VARCHAR(20),
   BIRTHDATE_           DATETIME,
   DESCRIPTION_         VARCHAR(2000),
   APPLYTYPE_           SMALLINT COMMENT '1：离职；2：入职',
   ENTRYDATE_           DATETIME,
   RESIGNDATE_          DATETIME,
   APPLICANT_           VARCHAR(36),
   APPLYDATE_           DATETIME,
   FLOWINSTANCEID_      VARCHAR(36),
   ARCHIVEDATE_         DATETIME,
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   APPLYSTATUS_         SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   STAFFSTATUS_         VARCHAR(100),
   GROUPFULLNAME_       VARCHAR(300),
   LEVEL_               VARCHAR(100),
   FORMALDATE_          DATETIME,
   AGE_                 NUMERIC(3,0),
   EDUCATION_           VARCHAR(20),
   FINISHSCHOOL_        VARCHAR(100),
   PROFESSIONAL_        VARCHAR(100),
   DEGREE_              VARCHAR(20),
   CONTRACTTYPE_        VARCHAR(50),
   CONTRACTSTARTDATE_   DATETIME,
   CONTRACTENDDATE_     DATETIME,
   MOBILE_              VARCHAR(20),
   OFFICETEL_           VARCHAR(20),
   EMAIL_               VARCHAR(100),
   POLITICALSTATUS_     VARCHAR(50),
   NATION_              VARCHAR(50),
   NATIVEPLACE_         VARCHAR(300),
   HOMEADDR_            VARCHAR(300),
   ZIPCODE_             VARCHAR(20),
   DOMICILEPLACE_       VARCHAR(300),
   DOMICILETYPE_        VARCHAR(50),
   MARITALSTATUS_       VARCHAR(100),
   HADKIDS_             SMALLINT,
   SALESEXPERIENCE_     SMALLINT,
   FINANCIALEXPERIENCE_ SMALLINT,
   FINANCIALQUALIFICATION_ SMALLINT,
   WORKSTARTDATE_       DATETIME,
   SUPERVISOR_          VARCHAR(36),
   RECRUITMENTTYPE_     VARCHAR(100),
   RECOMMENDED_         VARCHAR(50),
   SENIORITY_           NUMERIC(3,0),
   EMERGENCYCONTACT_    VARCHAR(50),
   EMERGENCYCONTACTTEL_ VARCHAR(20),
   BANKCARDNO_          VARCHAR(50),
   BANKTYPE_            VARCHAR(50),
   TRAININFO_           VARCHAR(2000),
   SKILLSINFO_          VARCHAR(2000),
   RESIGNTYPE_          VARCHAR(100),
   RESIGNREASON_        VARCHAR(50),
   RESIGNDESC_          VARCHAR(2000),
   ACHIEVEMENT_         VARCHAR(50),
   PROJECT_             VARCHAR(50),
   TRANSTARTDATE_       DATETIME,
   TRANENDDATE_         DATETIME,
   TRANCOST_            VARCHAR(50),
   PENALTY_             VARCHAR(50),
   STAFFCLASS_          VARCHAR(100),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_STAFFFLOWQUERYASSIGN                               */
/*==============================================================*/
CREATE TABLE OA_STAFFFLOWQUERYASSIGN
(
   ID_                  VARCHAR(36) NOT NULL,
   USERACCOUNTID_       VARCHAR(100),
   GROUPNAME_           VARCHAR(100),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_TASK                                               */
/*==============================================================*/
CREATE TABLE OA_TASK
(
   ID_                  VARCHAR(36) NOT NULL,
   TASKINSTANCEID_      VARCHAR(36),
   FLOWINSTANCEID_      VARCHAR(36),
   FLOWKEY_             VARCHAR(36),
   FLOWDEFINEID_        VARCHAR(36),
   TASKSTATE_           VARCHAR(20),
   FORMID_              VARCHAR(36) COMMENT '跟据流程分类关联不同类型的流程表单',
   TRANSACTOR_          VARCHAR(128),
   CREATETIME_          DATETIME,
   STARTTIME_           DATETIME,
   ENDTIME_             DATETIME,
   COMMENT_             VARCHAR(4000),
   NODENAME_            VARCHAR(400),
   TASKNAME_            VARCHAR(400),
   FLOWNAME_            VARCHAR(400),
   VIEWFLAG_            SMALLINT,
   DESCRIPTION_         VARCHAR(400),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_TASKASSIGN                                         */
/*==============================================================*/
CREATE TABLE OA_TASKASSIGN
(
   ID_                  VARCHAR(36) NOT NULL,
   TASKID_              VARCHAR(36),
   TYPE_                CHAR(1) COMMENT '0：用户
            1：角色
            2：机构',
   ASSIGNKEY_           VARCHAR(128),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_TRANSFERAPPLY                                      */
/*==============================================================*/
CREATE TABLE OA_TRANSFERAPPLY
(
   ID_                  VARCHAR(36) NOT NULL,
   APPLICANT_           VARCHAR(36),
   APPLYDATE_           DATETIME,
   TRANSFERUSER_        VARCHAR(36),
   CONTRACTBODY_        VARCHAR(100),
   CONTRACTSTARTDATE_   DATETIME,
   CONTRACTENDDATE_     DATETIME,
   TRANSFERDATE_        DATETIME,
   ENTRYDATE_           DATETIME,
   TRANSFEROUTDEPT_     VARCHAR(100),
   TRANSFEROUTPOST_     VARCHAR(100),
   TRANSFERINDEPT_      VARCHAR(100),
   TRANSFERINPOST_      VARCHAR(100),
   REPORTDATE_          DATETIME,
   TRANSFERREASON_      VARCHAR(1000),
   TRANSITIONCONTENT_   VARCHAR(1000),
   FINACESUPPORTUSER_   VARCHAR(100),
   FINACESUPPORTTEL_    VARCHAR(100),
   FINACESUPPORTEMAIL_  VARCHAR(100),
   ITSUPPORTUSER_       VARCHAR(100),
   ITSUPPORTTEL_        VARCHAR(100),
   ITSUPPORTEMAIL_      VARCHAR(100),
   HRSUPPORTUSER_       VARCHAR(100),
   HRSUPPORTTEL_        VARCHAR(100),
   HRSUPPORTEMAIL_      VARCHAR(100),
   CHANGEOFFICE_        VARCHAR(200),
   WEEKLYREPORTTO_      VARCHAR(100),
   MONTHLYREPORTTO_     VARCHAR(100),
   FLOWINSTANCEID_      VARCHAR(36),
   ARCHIVEDATE_         DATETIME,
   PASSED_              SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   APPLYSTATUS_         SMALLINT COMMENT '流程结束时为已归档状态
            用于区分流程跟踪与归档查询',
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: OA_WORKEXPERIENCE                                     */
/*==============================================================*/
CREATE TABLE OA_WORKEXPERIENCE
(
   ID_                  VARCHAR(36) NOT NULL,
   STAFFFLOWAPPLYID_    VARCHAR(36),
   STARTDATE_           DATETIME,
   ENDDATE_             DATETIME,
   COMPANYNAME_         VARCHAR(100),
   POSTNAME_            VARCHAR(100),
   POSTDUTY_            VARCHAR(100),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* View: VIEW_DEVICEFLOW                                        */
/*==============================================================*/
CREATE VIEW  VIEW_DEVICEFLOW 
AS 
SELECT
   0 AS FORMTYPE_,
   T.ID_,
   T.APPLICANT_,
   T.PASSED_,
   T.APPLYGROUPNAME_,
   T.APPLYDATE_,
   T.FORMSTATUS_,
   T.DEVICEVALIDATEID_,
   T.ARCHIVEDATE_,
   T.REGTIME_,
   T.DEVICETYPE_,
   T.APPLYFORMNO_,
   T.SEQUENCEYEAR_,
   T.REGACCOUNTID_,
   T.FLOWINSTANCEID_,
   '1' AS APPLYCODE_
FROM
   OA_DEVPURCHASEFORM T
WHERE
   T.APPLYTYPE_ = 0

UNION
SELECT
   1 AS FORMTYPE_,
   T.ID_,
   T.APPLICANT_,
   T.PASSED_,
   T.APPLYGROUPNAME_,
   T.APPLYDATE_,
   T.FORMSTATUS_,
   T.DEVICEVALIDATEID_,
   T.ARCHIVEDATE_,
   T.REGTIME_,
   T.DEVICETYPE_,
   T.APPLYFORMNO_,
   T.SEQUENCEYEAR_,
   T.REGACCOUNTID_,
   T.FLOWINSTANCEID_,
   '2' AS APPLYCODE_
FROM
   OA_DEVPURCHASEFORM T
WHERE
   T.APPLYTYPE_ = 1

UNION
SELECT
   2 AS FORMTYPE_,
   T.ID_,
   T.APPLICANT_,
   T.PASSED_,
   T.APPLYGROUPNAME_,
   T.APPLYDATE_,
   T.FORMSTATUS_,
   T.DEVICEVALIDATEID_,
   T.ARCHIVEDATE_,
   T.REGTIME_,
   T.DEVICETYPE_,
   T.APPLYFORMNO_,
   T.SEQUENCEYEAR_,
   T.REGACCOUNTID_,
   T.FLOWINSTANCEID_,
   '3' AS APPLYCODE_
FROM
   OA_DEVALLOCATEFORM T

UNION
SELECT
   3 AS FORMTYPE_,
   T.ID_,
   T.APPLICANT_,
   T.PASSED_,
   T.APPLYGROUPNAME_,
   T.APPLYDATE_,
   T.FORMSTATUS_,
   T.DEVICEVALIDATEID_,
   T.ARCHIVEDATE_,
   T.REGTIME_,
   T.DEVICETYPE_,
   T.APPLYFORMNO_,
   T.SEQUENCEYEAR_,
   T.REGACCOUNTID_,
   T.FLOWINSTANCEID_,
   '4' AS APPLYCODE_
FROM
   OA_DEVDISCARDFORM T
WHERE
   T.FORMTYPE_ = 0

UNION
SELECT
   4 AS FORMTYPE_,
   T.ID_,
   T.APPLICANT_,
   T.PASSED_,
   T.APPLYGROUPNAME_,
   T.APPLYDATE_,
   T.FORMSTATUS_,
   T.DEVICEVALIDATEID_,
   T.ARCHIVEDATE_,
   T.REGTIME_,
   T.DEVICETYPE_,
   T.APPLYFORMNO_,
   T.SEQUENCEYEAR_,
   T.REGACCOUNTID_,
   T.FLOWINSTANCEID_,
   '5' AS APPLYCODE_
FROM
   OA_DEVDISCARDFORM T
WHERE
   T.FORMTYPE_ = 1;

ALTER TABLE OA_AREADEVICECFG ADD CONSTRAINT FK_AREADEV_REF_DEVCLASS FOREIGN KEY (DEVICETYPE_)
      REFERENCES OA_DEVICECLASS (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_BUSTRIPAPPLYDETAIL ADD CONSTRAINT FK__BUSTRIP_APPLY_REF FOREIGN KEY (TRIPAPPLYID_)
      REFERENCES OA_BUSTRIPAPPLY (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVALLOCATELIST ADD CONSTRAINT FK_DEVALLOT_REF_DEV FOREIGN KEY (DEVICEID_)
      REFERENCES OA_DEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVPURCHASELIST ADD CONSTRAINT FK_OA_DEVPURCHASE_REF_DEV FOREIGN KEY (DEVICEID_)
      REFERENCES OA_DEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVVALIDATEFORM ADD CONSTRAINT FK_DEVVALID_REF_PURCHDEV FOREIGN KEY (PURCHASEDEVID_)
      REFERENCES OA_PURCHASEDEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICECFGITEM ADD CONSTRAINT FK_DEVCFGITEM_REF_DEVPROP FOREIGN KEY (ITEMID_)
      REFERENCES OA_DEVICEPROPERTY (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICECLAASSIGNAREA ADD CONSTRAINT FK_ASSIGNAREA_REF_ASSIGN FOREIGN KEY (ASSIGNID_)
      REFERENCES OA_DEVICECLASSASSIGN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICECLAASSIGNAREA ADD CONSTRAINT FK_DEVCLASSIGNAREA_REF_DEVCLA FOREIGN KEY (DEVICECLASS_)
      REFERENCES OA_DEVICECLASS (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICECLASSASSIGNDETAIL ADD CONSTRAINT FK_DETAIL_REF_DEVCLASSIGN FOREIGN KEY (ASSIGNID_)
      REFERENCES OA_DEVICECLASSASSIGN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICECURSTATUSINFO ADD CONSTRAINT FK_DEVSTAUS_REF_DEV FOREIGN KEY (DEVICEID_)
      REFERENCES OA_DEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICEPARAMDETAIL ADD CONSTRAINT FK_DEVPARAM_REF_DEV FOREIGN KEY (DEVICEID_)
      REFERENCES OA_DEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DEVICEPARAMDETAIL ADD CONSTRAINT FK_DEVPARAM_REF_PURCHDEV FOREIGN KEY (PURCHASEDEVID_)
      REFERENCES OA_PURCHASEDEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DISCARDDEALDEVLIST ADD CONSTRAINT FK_LIST_REF_DISCARDDEAL FOREIGN KEY (DISCARDDEALFORMID_)
      REFERENCES OA_DEVDISCARDDISPOSEFORM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DISCARDDEVLIST ADD CONSTRAINT FK_DEVSCRAP_REF_DEV FOREIGN KEY (DEVICEID_)
      REFERENCES OA_DEVICE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCCLASS ADD CONSTRAINT FK_DOCCLASS_REF_ATTACHMENT FOREIGN KEY (BODYTEMPLATE_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCCLASSASSIGN ADD CONSTRAINT FK_DOCCLASS_REF_DCASSIGN FOREIGN KEY (DOCCLASSID_)
      REFERENCES OA_DOCCLASS (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCFORM ADD CONSTRAINT FK_DOCF_C_REF_ATTACHMENT FOREIGN KEY (BODYDOC_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCFORM ADD CONSTRAINT FK_DOCF_DC_REF_ATTACHMENT FOREIGN KEY (BODYDRAFTDOC_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCFORM_ATTACH ADD CONSTRAINT FK_DF_ATTACH_REF_ATTACH FOREIGN KEY (ATTACHMENTID_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCFORM_ATTACH ADD CONSTRAINT FK_DF_ATTACH_REF_DOCFORM FOREIGN KEY (DOCFORMID_)
      REFERENCES OA_DOCFORM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCFORM_INFO ADD CONSTRAINT FK_DFI_REF_DOCFORM FOREIGN KEY (DOCFORMID_)
      REFERENCES OA_DOCFORM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCFORM_INFO ADD CONSTRAINT FK_DFI_REF_INFORMATION FOREIGN KEY (INFORMATIONID_)
      REFERENCES OA_INFORMATION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_DOCNUMBER ADD CONSTRAINT FK_DOCNO_REF_ATTACHMENT FOREIGN KEY (HEADTEMPLATE_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_HOLIDAYDETAIL ADD CONSTRAINT FK_HOLID_DETAIL_REF_HOLID FOREIGN KEY (HOLIDAYAPPLYID_)
      REFERENCES OA_HOLIDAYAPPLY (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_INFOFORM ADD CONSTRAINT FK_INFOFORM_REF_INFO FOREIGN KEY (INFORMATIONID_)
      REFERENCES OA_INFORMATION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_INFOFORM ADD CONSTRAINT FK_INFOF_DOC_REF_ATTACH FOREIGN KEY (CONTENTDOC_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_INFOLAYOUTASSIGN ADD CONSTRAINT FK_IFLAYOUT_REF_IFLASSIGN FOREIGN KEY (INFOLAYOUTID_)
      REFERENCES OA_INFOLAYOUT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_INFO_ATTACH ADD CONSTRAINT FK_INFO_ATTACH_REF_ATTACH FOREIGN KEY (ATTACHMENTID_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_INFO_ATTACH ADD CONSTRAINT FK_INFO_ATTACH_REF_INFOR FOREIGN KEY (INFOID_)
      REFERENCES OA_INFORMATION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_KNOWLEDGE_ATTACH ADD CONSTRAINT FK_KNL_ATTACH_REF_ATTACH FOREIGN KEY (ID_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_MEETINGINFO ADD CONSTRAINT FK_MEETINFO_REF_MEETROOM FOREIGN KEY (ROOMID_)
      REFERENCES OA_MEETINGROOM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_MEETINGMINUTE ADD CONSTRAINT FK_MINUTE_REF_ATTACH FOREIGN KEY (ID_)
      REFERENCES OA_ATTACHMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_MEETINGMINUTE ADD CONSTRAINT FK_MINUTE_REF_MEET FOREIGN KEY (MEETINGID_)
      REFERENCES OA_MEETINGINFO (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_MEETINGPARTICIPANT ADD CONSTRAINT FK_MEET_REF_MEETPARTIPANT FOREIGN KEY (MEETINGID_)
      REFERENCES OA_MEETINGINFO (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_OUTLAYLIST ADD CONSTRAINT FK_OUTLAYLIST_REF_REIMB FOREIGN KEY (REIMBURSEMENTID_)
      REFERENCES OA_REIMBURSEMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_OUTLAYLIST ADD CONSTRAINT FK_OUTLAYLIST_REF_REIMITEM FOREIGN KEY (REIMITEMID_)
      REFERENCES OA_REIMITEM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_PURCHASEDEVPURPOSE ADD CONSTRAINT FK_PURPOSE_REF_PURCHFORM FOREIGN KEY (PURCHASEFORMID_)
      REFERENCES OA_DEVPURCHASEFORM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_PURCHASEDEVICE ADD CONSTRAINT FK_PURCHDEV_REF_DEVPUCH FOREIGN KEY (PURCHASEFORMID_)
      REFERENCES OA_DEVPURCHASEFORM (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_REIMITEM ADD CONSTRAINT FK_REIMITEM_REF_REIM FOREIGN KEY (REIMBURSEMENTID_)
      REFERENCES OA_REIMBURSEMENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_TASKASSIGN ADD CONSTRAINT FK_TASKPOOLEDS_REF_TASKS FOREIGN KEY (TASKID_)
      REFERENCES OA_TASK (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE OA_WORKEXPERIENCE ADD CONSTRAINT FK_OA_WORKEXP_REF_OA_STAFFFLOW FOREIGN KEY (STAFFFLOWAPPLYID_)
      REFERENCES OA_STAFFFLOWAPPLY (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

