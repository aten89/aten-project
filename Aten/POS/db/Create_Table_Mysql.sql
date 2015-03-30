/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/3/29 0:33:34                            */
/*==============================================================*/


DROP TABLE IF EXISTS POS_AGENT;

DROP TABLE IF EXISTS POS_BANK_ACC;

DROP TABLE IF EXISTS POS_CALENDAR;

DROP TABLE IF EXISTS POS_DEVICES;

DROP TABLE IF EXISTS POS_JOIN_ORG;

DROP TABLE IF EXISTS POS_PAY_ACC;

DROP TABLE IF EXISTS POS_POS_ACC;

DROP TABLE IF EXISTS POS_TRADE_REC;

DROP TABLE IF EXISTS POS_TRANSFER_REC;

DROP TABLE IF EXISTS POS_USER_ACC;

/*==============================================================*/
/* Table: POS_AGENT                                             */
/*==============================================================*/
CREATE TABLE POS_AGENT
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20) NOT NULL,
   AGENT_NAME_          VARCHAR(100),
   PAY_RATE_            FLOAT,
   LIMIT_LINES_         DOUBLE COMMENT '一天最高划款',
   CONTACT_             VARCHAR(100),
   TEL_                 VARCHAR(100),
   EMAIL_               VARCHAR(100),
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_BANK_ACC                                          */
/*==============================================================*/
CREATE TABLE POS_BANK_ACC
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20),
   BANK_ACC_NAME_       VARCHAR(100),
   BANK_CARD_NO_        VARCHAR(50),
   BANK_NAME_           VARCHAR(100),
   BANK_BRANCH_         VARCHAR(100),
   PROVINCE_            VARCHAR(50),
   CITY_                VARCHAR(50),
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_CALENDAR                                          */
/*==============================================================*/
CREATE TABLE POS_CALENDAR
(
   DATE_                INT NOT NULL,
   DATE_TYPE_           SMALLINT COMMENT '0：节假日；1：工作日',
   REMPARKS_            VARCHAR(500),
   PRIMARY KEY (DATE_)
);

/*==============================================================*/
/* Table: POS_DEVICES                                           */
/*==============================================================*/
CREATE TABLE POS_DEVICES
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20) NOT NULL,
   AGENT_ID_            VARCHAR(36),
   POS_TYPE_            VARCHAR(50) COMMENT '快钱、银联',
   DEV_NO_              VARCHAR(50),
   DEV_NAME_            VARCHAR(100),
   CONTACT_             VARCHAR(100),
   TEL_                 VARCHAR(100),
   EMAIL_               VARCHAR(100),
   PAY_ACC_ID_          VARCHAR(36),
   BANK_ACC_ID_         VARCHAR(36),
   PAY_RATE_            FLOAT,
   HOLI_ADD_RATE_       FLOAT,
   HOLI_OFFSET_DAYS_    INT,
   STATUS_              SMALLINT COMMENT '1：可用；0：禁用',
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_JOIN_ORG                                          */
/*==============================================================*/
CREATE TABLE POS_JOIN_ORG
(
   ORG_CODE_            VARCHAR(20) NOT NULL,
   ORG_NAME_            VARCHAR(100),
   PAY_TYPE_            SMALLINT COMMENT '0：线下结算；1：实时结算',
   PAY_RATE_            FLOAT,
   CONTACT_             VARCHAR(100),
   TEL_                 VARCHAR(100),
   EMAIL_               VARCHAR(100),
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ORG_CODE_)
);

/*==============================================================*/
/* Table: POS_PAY_ACC                                           */
/*==============================================================*/
CREATE TABLE POS_PAY_ACC
(
   ID_                  VARCHAR(36) NOT NULL,
   ACC_CLASS_           SMALLINT COMMENT '0：属于加盟机构；1：属于系统（用于加盟商收款）',
   ORG_CODE_            VARCHAR(20),
   PAY_TYPE_            VARCHAR(50) COMMENT '融宝、支付宝',
   PAY_NAME_            VARCHAR(100),
   PAY_ACC_             VARCHAR(100),
   PAY_PSW_             VARCHAR(100),
   SINGLE_FEE_          FLOAT,
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_POS_ACC                                           */
/*==============================================================*/
CREATE TABLE POS_POS_ACC
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20) NOT NULL,
   POS_TYPE_            VARCHAR(50) COMMENT '快钱、银联（唯一约束）',
   POS_NAME_            VARCHAR(100),
   LOGIN_ACC_           VARCHAR(100),
   LOGIN_PSW_           VARCHAR(100),
   KEYSTORT_FILE_       VARCHAR(300),
   KEYSTORT_PSW_        VARCHAR(100),
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_TRADE_REC                                         */
/*==============================================================*/
CREATE TABLE POS_TRADE_REC
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20) NOT NULL,
   DEV_ID_              VARCHAR(36),
   AGENT_ID_            VARCHAR(36),
   POS_TYPE_            VARCHAR(50),
   DEV_NO_              VARCHAR(50),
   DEV_NAME_            VARCHAR(100),
   TRADE_NO_            VARCHAR(50),
   TRADE_TYPE_          VARCHAR(50),
   TRADE_STATUS_        VARCHAR(50),
   CARD_NO_             VARCHAR(50),
   BANK_NAME_           VARCHAR(100),
   TRAN_AMOUNT_         DOUBLE,
   TRAN_FEE_            DOUBLE,
   TRAN_DATE_           BIGINT,
   CLEAR_AMOUNT_        DOUBLE,
   CLEAR_FEE_           DOUBLE,
   PAY_RATE_            FLOAT,
   HOLI_FLAG_           SMALLINT,
   HOLI_ADD_RATE_       FLOAT,
   AGENT_PAY_RATE_      FLOAT,
   FLAT_STATUS_         SMALLINT,
   CLEAR_STATUS_        SMALLINT COMMENT '1：已结算；0：未结算',
   CLEAR_DATE_          BIGINT,
   TRANSFER_ID_         VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_TRANSFER_REC                                      */
/*==============================================================*/
CREATE TABLE POS_TRANSFER_REC
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20) NOT NULL,
   DEV_ID_              VARCHAR(36),
   AGENT_ID_            VARCHAR(36),
   POS_TYPE_            VARCHAR(50) COMMENT '快钱、银联（唯一约束）',
   DEV_NO_              VARCHAR(50),
   DEV_NAME_            VARCHAR(100),
   PAY_ACC_ID_          VARCHAR(36),
   PAY_TYPE_            VARCHAR(50) COMMENT '融宝、支付宝',
   PAY_ACC_             VARCHAR(100),
   PAY_SINGLE_FEE_      FLOAT,
   TOTAL_TRAN_AMOUNT_   DOUBLE,
   TOTAL_CLEAR_FEE_     DOUBLE,
   TOTAL_TRANSFER_AMOUNT_ DOUBLE,
   BANK_ACC_ID_         VARCHAR(36),
   BANK_ACC_NAME_       VARCHAR(100),
   BANK_CARD_NO_        VARCHAR(50),
   BANK_NAME_           VARCHAR(100),
   BANK_BRANCH_         VARCHAR(100),
   TRANSFER_STATUS_     SMALLINT COMMENT '1：已划款；0：未划款；-1划款失败',
   TRANSFER_WAY_        SMALLINT COMMENT '自动、手动、线下',
   TRANSFER_DATE_       BIGINT,
   JOIN_PAY_RATE_       FLOAT,
   JOIN_TRANSFER_AMOUNT_ DOUBLE,
   JOIN_TRANSFER_STATUS_ SMALLINT COMMENT '1：已划款；0：未划款；-1划款失败',
   JOIN_TRANSFER_DATE_  BIGINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: POS_USER_ACC                                          */
/*==============================================================*/
CREATE TABLE POS_USER_ACC
(
   ID_                  VARCHAR(36) NOT NULL,
   ORG_CODE_            VARCHAR(20) NOT NULL,
   AGENT_ID_            VARCHAR(36),
   USER_ACCOUNT_        VARCHAR(50),
   REMARKS_             VARCHAR(500),
   PRIMARY KEY (ID_)
);

ALTER TABLE POS_AGENT ADD CONSTRAINT FK_REFERENCE_2 FOREIGN KEY (ORG_CODE_)
      REFERENCES POS_JOIN_ORG (ORG_CODE_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_BANK_ACC ADD CONSTRAINT FK_REFERENCE_7 FOREIGN KEY (ORG_CODE_)
      REFERENCES POS_JOIN_ORG (ORG_CODE_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_DEVICES ADD CONSTRAINT FK_REFERENCE_10 FOREIGN KEY (BANK_ACC_ID_)
      REFERENCES POS_BANK_ACC (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_DEVICES ADD CONSTRAINT FK_REFERENCE_6 FOREIGN KEY (ORG_CODE_)
      REFERENCES POS_JOIN_ORG (ORG_CODE_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_DEVICES ADD CONSTRAINT FK_REFERENCE_8 FOREIGN KEY (AGENT_ID_)
      REFERENCES POS_AGENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_DEVICES ADD CONSTRAINT FK_REFERENCE_9 FOREIGN KEY (PAY_ACC_ID_)
      REFERENCES POS_PAY_ACC (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_PAY_ACC ADD CONSTRAINT FK_REFERENCE_5 FOREIGN KEY (ORG_CODE_)
      REFERENCES POS_JOIN_ORG (ORG_CODE_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_POS_ACC ADD CONSTRAINT FK_REFERENCE_4 FOREIGN KEY (ORG_CODE_)
      REFERENCES POS_JOIN_ORG (ORG_CODE_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_TRADE_REC ADD CONSTRAINT FK_REFERENCE_11 FOREIGN KEY (TRANSFER_ID_)
      REFERENCES POS_TRANSFER_REC (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_USER_ACC ADD CONSTRAINT FK_REFERENCE_1 FOREIGN KEY (ORG_CODE_)
      REFERENCES POS_JOIN_ORG (ORG_CODE_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE POS_USER_ACC ADD CONSTRAINT FK_REFERENCE_3 FOREIGN KEY (AGENT_ID_)
      REFERENCES POS_AGENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

