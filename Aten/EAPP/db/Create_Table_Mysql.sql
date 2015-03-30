/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/12/4 18:11:20                           */
/*==============================================================*/


DROP TABLE IF EXISTS EAPP_ACTION;

DROP TABLE IF EXISTS EAPP_ACTION_LOG;

DROP TABLE IF EXISTS EAPP_ACTOR_ACCOUNT;

DROP TABLE IF EXISTS EAPP_ACTOR_SERVICE;

DROP TABLE IF EXISTS EAPP_DATA_DICTIONART;

DROP TABLE IF EXISTS EAPP_DEFAULT_PORTLET;

DROP TABLE IF EXISTS EAPP_FLOW_CONFIG;

DROP TABLE IF EXISTS EAPP_FLOW_HANDLER;

DROP TABLE IF EXISTS EAPP_FLOW_VAR;

DROP INDEX MANAGERPOST_FK ON EAPP_GROUP;

DROP TABLE IF EXISTS EAPP_GROUP;

DROP TABLE IF EXISTS EAPP_GROUP_ROLE;

DROP TABLE IF EXISTS EAPP_GROUP_USER;

DROP TABLE IF EXISTS EAPP_LOGIN_LOG;

DROP TABLE IF EXISTS EAPP_MODULE;

DROP TABLE IF EXISTS EAPP_MODULE_ACTION;

DROP TABLE IF EXISTS EAPP_PORTLET;

DROP TABLE IF EXISTS EAPP_PORTLET_ROLE;

DROP INDEX PARENTPOST_FK ON EAPP_POST;

DROP TABLE IF EXISTS EAPP_POST;

DROP INDEX POST_USER2_FK ON EAPP_POST_USER;

DROP INDEX POST_USER_FK ON EAPP_POST_USER;

DROP TABLE IF EXISTS EAPP_POST_USER;

DROP TABLE IF EXISTS EAPP_ROLE;

DROP TABLE IF EXISTS EAPP_ROLE_RIGHT;

DROP TABLE IF EXISTS EAPP_SERVICE;

DROP TABLE IF EXISTS EAPP_SERVICE_RIGHT;

DROP TABLE IF EXISTS EAPP_SHORT_CUT_MENU;

DROP TABLE IF EXISTS EAPP_SUB_SYSTEM;

DROP TABLE IF EXISTS EAPP_SYS_MSG;

DROP TABLE IF EXISTS EAPP_USER_ACCOUNT;

DROP INDEX USER_PORLET2_FK ON EAPP_USER_PORTLET;

DROP INDEX USER_PORLET_FK ON EAPP_USER_PORTLET;

DROP TABLE IF EXISTS EAPP_USER_PORTLET;

DROP TABLE IF EXISTS EAPP_USER_ROLE;

DROP TABLE IF EXISTS FLOW_TASK;

DROP TABLE IF EXISTS FLOW_TASK_ASSIGN;

/*==============================================================*/
/* Table: EAPP_ACTION                                           */
/*==============================================================*/
CREATE TABLE EAPP_ACTION
(
   ACTION_ID_           VARCHAR(36) NOT NULL,
   ACTION_KEY_          VARCHAR(36),
   NAME_                VARCHAR(100) NOT NULL,
   LOGO_URL_            VARCHAR(1024),
   TIPS_                VARCHAR(100),
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (ACTION_ID_)
);

/*==============================================================*/
/* Table: EAPP_ACTION_LOG                                       */
/*==============================================================*/
CREATE TABLE EAPP_ACTION_LOG
(
   LOG_ID_              VARCHAR(36) NOT NULL,
   SYSTEM_ID_           VARCHAR(36) NOT NULL,
   SYSTEM_NAME_         VARCHAR(100),
   MODULE_KEY_          VARCHAR(36),
   MODULE_NAME_         VARCHAR(100),
   ACTION_KEY_          VARCHAR(36),
   ACTION_NAME_         VARCHAR(100),
   ACCOUNT_ID_          VARCHAR(100),
   ACCOUNT_NAME_        VARCHAR(100),
   OBJECT_ID_           VARCHAR(36),
   OBJECT_              VARCHAR(4000),
   IP_ADDRESS_          VARCHAR(40),
   RESULT_STATUS_       VARCHAR(100),
   OPERATE_TIME_        DATETIME NOT NULL,
   IS_SERVICE_LOG_      SMALLINT,
   IS_BACKUP_           SMALLINT,
   PRIMARY KEY (LOG_ID_)
);

/*==============================================================*/
/* Table: EAPP_ACTOR_ACCOUNT                                    */
/*==============================================================*/
CREATE TABLE EAPP_ACTOR_ACCOUNT
(
   ACTOR_ID_            VARCHAR(36) NOT NULL,
   ACCOUNT_ID_          VARCHAR(100) NOT NULL,
   DISPLAY_NAME_        VARCHAR(100) NOT NULL,
   CREDENCE_            VARCHAR(2000),
   IS_LOCK_             SMALLINT NOT NULL,
   CHANGE_PASSWORD_FLAG_ CHAR(1) NOT NULL,
   CREATE_DATE_         DATE NOT NULL DEFAULT '2013-01-01',
   INVALID_DATE_        DATE,
   IS_LOGIC_DELETE_     SMALLINT NOT NULL,
   DESCRIPTEION_        VARCHAR(1024),
   PRIMARY KEY (ACTOR_ID_)
);

/*==============================================================*/
/* Table: EAPP_ACTOR_SERVICE                                    */
/*==============================================================*/
CREATE TABLE EAPP_ACTOR_SERVICE
(
   ACTOR_ID_            VARCHAR(36) NOT NULL,
   SERVICE_ID_          VARCHAR(36) NOT NULL,
   PRIMARY KEY (ACTOR_ID_, SERVICE_ID_)
);

/*==============================================================*/
/* Table: EAPP_DATA_DICTIONART                                  */
/*==============================================================*/
CREATE TABLE EAPP_DATA_DICTIONART
(
   DICT_ID_             VARCHAR(36) NOT NULL,
   PARENT_DICT_ID_      VARCHAR(36),
   SUB_SYSTEM_ID_       VARCHAR(36),
   DICT_NAME_           VARCHAR(100) NOT NULL,
   DICT_CODE_           VARCHAR(100) NOT NULL,
   CEIL_VALUE_          VARCHAR(100),
   FLOOR_VALUE_         VARCHAR(100),
   DISPLAY_SORT_        INT NOT NULL,
   DICT_TYPE_           VARCHAR(20) NOT NULL,
   TREE_LEVEL_          INT NOT NULL,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (DICT_ID_)
);

/*==============================================================*/
/* Table: EAPP_DEFAULT_PORTLET                                  */
/*==============================================================*/
CREATE TABLE EAPP_DEFAULT_PORTLET
(
   DEFAULT_PORTLET_ID_  VARCHAR(36) NOT NULL,
   PORTLET_ID_          VARCHAR(36) NOT NULL,
   PAGE_CONTAINER_ID_   VARCHAR(20) NOT NULL,
   POSITION_INDEX_      INT NOT NULL,
   PRIMARY KEY (DEFAULT_PORTLET_ID_)
);

/*==============================================================*/
/* Table: EAPP_FLOW_CONFIG                                      */
/*==============================================================*/
CREATE TABLE EAPP_FLOW_CONFIG
(
   CONF_ID_             VARCHAR(36) NOT NULL,
   FLOW_CLASS_          VARCHAR(100),
   FLOW_KEY_            VARCHAR(36),
   FLOW_NAME_           VARCHAR(100),
   FLOW_VERSION_        NUMERIC(14,0),
   DRAFT_FLAG_          SMALLINT COMMENT '1：草稿；2：发布；3：禁用',
   PRIMARY KEY (CONF_ID_)
);

/*==============================================================*/
/* Table: EAPP_FLOW_HANDLER                                     */
/*==============================================================*/
CREATE TABLE EAPP_FLOW_HANDLER
(
   HAND_ID_             VARCHAR(36) NOT NULL,
   FLOW_CLASS_          VARCHAR(100),
   NAME_                VARCHAR(100),
   HANDLER_CLASS_       VARCHAR(100),
   TYPE_                VARCHAR(20) COMMENT 'ACTION/ASSIGN/MUTIASSIGN/DECISION/VIEW',
   GLOBAL_FLAG_         SMALLINT,
   PRIMARY KEY (HAND_ID_)
);

/*==============================================================*/
/* Table: EAPP_FLOW_VAR                                         */
/*==============================================================*/
CREATE TABLE EAPP_FLOW_VAR
(
   VAR_ID_              VARCHAR(36) NOT NULL,
   FLOW_CLASS_          VARCHAR(100),
   NAME_                VARCHAR(100),
   DISPLAY_NAME_        VARCHAR(100),
   TYPE_                VARCHAR(20) COMMENT 'STRING/BOOLEAN/INT/LONG/FLOAT/DOUBLE/DATE',
   NOT_NUL_             SMALLINT,
   GLOBAL_FLAG_         SMALLINT,
   PRIMARY KEY (VAR_ID_)
);

/*==============================================================*/
/* Table: EAPP_GROUP                                            */
/*==============================================================*/
CREATE TABLE EAPP_GROUP
(
   GROUP_ID             VARCHAR(36) NOT NULL,
   PARENT_GROUP_ID_     VARCHAR(36),
   MANAGER_POST_ID_     VARCHAR(36),
   GROUP_NAME_          VARCHAR(100) NOT NULL,
   DISPLAY_ORDER_       INT NOT NULL,
   TYPE_                VARCHAR(20),
   TREE_LEVEL_          INT,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (GROUP_ID)
);

/*==============================================================*/
/* Index: MANAGERPOST_FK                                        */
/*==============================================================*/
CREATE INDEX MANAGERPOST_FK ON EAPP_GROUP
(
   MANAGER_POST_ID_
);

/*==============================================================*/
/* Table: EAPP_GROUP_ROLE                                       */
/*==============================================================*/
CREATE TABLE EAPP_GROUP_ROLE
(
   GROUP_ID_            VARCHAR(36) NOT NULL,
   ROLE_ID_             VARCHAR(36) NOT NULL,
   PRIMARY KEY (GROUP_ID_, ROLE_ID_)
);

/*==============================================================*/
/* Table: EAPP_GROUP_USER                                       */
/*==============================================================*/
CREATE TABLE EAPP_GROUP_USER
(
   GROUP_ID_            VARCHAR(36) NOT NULL,
   USER_ID_             VARCHAR(36) NOT NULL,
   PRIMARY KEY (GROUP_ID_, USER_ID_)
);

/*==============================================================*/
/* Table: EAPP_LOGIN_LOG                                        */
/*==============================================================*/
CREATE TABLE EAPP_LOGIN_LOG
(
   LOG_ID_              VARCHAR(36) NOT NULL,
   SESSION_ID_          VARCHAR(36),
   ACCOUNT_ID_          VARCHAR(100),
   ACCOUNT_NAME_        VARCHAR(100),
   IP_ADDRESS_          VARCHAR(40),
   IS_SUCCESS_          SMALLINT,
   LOGIN_INFO_          VARCHAR(200),
   LOGIN_TIME_          DATETIME NOT NULL,
   LOGOUT_TIME_         DATETIME,
   IS_BACKUP_           SMALLINT,
   PRIMARY KEY (LOG_ID_)
);

/*==============================================================*/
/* Table: EAPP_MODULE                                           */
/*==============================================================*/
CREATE TABLE EAPP_MODULE
(
   MODULE_ID_           VARCHAR(36) NOT NULL,
   PARENT_MODULE_ID_    VARCHAR(36),
   MODULE_KEY_          VARCHAR(36),
   NAME_                VARCHAR(100) NOT NULL,
   DISPLAY_ORDER_       INT,
   URL_                 VARCHAR(1024),
   TREE_LEVEL_          INT NOT NULL,
   DESCRIPTION_         VARCHAR(1024),
   QUOTE_MODULE_ID_     VARCHAR(36),
   SUB_SYSTEM_ID_       VARCHAR(36),
   PRIMARY KEY (MODULE_ID_)
);

/*==============================================================*/
/* Table: EAPP_MODULE_ACTION                                    */
/*==============================================================*/
CREATE TABLE EAPP_MODULE_ACTION
(
   MODULE_ACTION_ID_    VARCHAR(36) NOT NULL,
   MODULE_ID_           VARCHAR(36),
   ACTION_ID_           VARCHAR(36),
   MODULE_KEY_          VARCHAR(36),
   ACTION_KEY_          VARCHAR(36),
   IS_VALID_            SMALLINT NOT NULL,
   IS_RPC_              SMALLINT NOT NULL,
   IS_HTTP_             SMALLINT DEFAULT 1,
   PRIMARY KEY (MODULE_ACTION_ID_)
);

/*==============================================================*/
/* Table: EAPP_PORTLET                                          */
/*==============================================================*/
CREATE TABLE EAPP_PORTLET
(
   PORTLET_ID_          VARCHAR(36) NOT NULL,
   PORTLET_NAME_        VARCHAR(100) NOT NULL,
   URL_                 VARCHAR(1024) NOT NULL,
   HIDDENABLE_          SMALLINT,
   MOVEDABLE_           SMALLINT,
   STYLE_               VARCHAR(100),
   MORE_URL_            VARCHAR(1024),
   SUB_SYSTEM_ID_       VARCHAR(36),
   PRIMARY KEY (PORTLET_ID_)
);

/*==============================================================*/
/* Table: EAPP_PORTLET_ROLE                                     */
/*==============================================================*/
CREATE TABLE EAPP_PORTLET_ROLE
(
   PORTLET_ID_          VARCHAR(36) NOT NULL,
   ROLE_ID_             VARCHAR(36) NOT NULL,
   PRIMARY KEY (PORTLET_ID_, ROLE_ID_)
);

/*==============================================================*/
/* Table: EAPP_POST                                             */
/*==============================================================*/
CREATE TABLE EAPP_POST
(
   POST_ID_             VARCHAR(36) NOT NULL,
   GROUP_ID_            VARCHAR(36),
   PARENT_POST_ID_      VARCHAR(36),
   POST_NAME_           VARCHAR(100) NOT NULL,
   DISPLAY_ORDER_       INT NOT NULL,
   TREE_LEVEL_          INT,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (POST_ID_)
);

/*==============================================================*/
/* Index: PARENTPOST_FK                                         */
/*==============================================================*/
CREATE INDEX PARENTPOST_FK ON EAPP_POST
(
   PARENT_POST_ID_
);

/*==============================================================*/
/* Table: EAPP_POST_USER                                        */
/*==============================================================*/
CREATE TABLE EAPP_POST_USER
(
   POST_ID_             VARCHAR(36) NOT NULL,
   USER_ID_             VARCHAR(36) NOT NULL,
   PRIMARY KEY (POST_ID_, USER_ID_)
);

/*==============================================================*/
/* Index: POST_USER_FK                                          */
/*==============================================================*/
CREATE INDEX POST_USER_FK ON EAPP_POST_USER
(
   POST_ID_
);

/*==============================================================*/
/* Index: POST_USER2_FK                                         */
/*==============================================================*/
CREATE INDEX POST_USER2_FK ON EAPP_POST_USER
(
   USER_ID_
);

/*==============================================================*/
/* Table: EAPP_ROLE                                             */
/*==============================================================*/
CREATE TABLE EAPP_ROLE
(
   ROLE_ID_             VARCHAR(36) NOT NULL,
   ROLE_NAME_           VARCHAR(100) NOT NULL,
   IS_VALID_            SMALLINT NOT NULL,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (ROLE_ID_)
);

/*==============================================================*/
/* Table: EAPP_ROLE_RIGHT                                       */
/*==============================================================*/
CREATE TABLE EAPP_ROLE_RIGHT
(
   ROLE_ID_             VARCHAR(36) NOT NULL,
   MODULE_ACTION_ID_    VARCHAR(36) NOT NULL,
   PRIMARY KEY (ROLE_ID_, MODULE_ACTION_ID_)
);

/*==============================================================*/
/* Table: EAPP_SERVICE                                          */
/*==============================================================*/
CREATE TABLE EAPP_SERVICE
(
   SERVICE_ID_          VARCHAR(36) NOT NULL,
   SERVICE_NAME_        VARCHAR(100) NOT NULL,
   IS_VALID_            SMALLINT NOT NULL,
   DESCRIPTION_         VARCHAR(1024),
   PRIMARY KEY (SERVICE_ID_)
);

/*==============================================================*/
/* Table: EAPP_SERVICE_RIGHT                                    */
/*==============================================================*/
CREATE TABLE EAPP_SERVICE_RIGHT
(
   SERVICE_ID_          VARCHAR(36) NOT NULL,
   MODULE_ACTION_ID_    VARCHAR(36) NOT NULL,
   PRIMARY KEY (SERVICE_ID_, MODULE_ACTION_ID_)
);

/*==============================================================*/
/* Table: EAPP_SHORT_CUT_MENU                                   */
/*==============================================================*/
CREATE TABLE EAPP_SHORT_CUT_MENU
(
   SHORT_CUT_MENU_ID_   VARCHAR(36) NOT NULL,
   USER_ID_             VARCHAR(36),
   MODULE_TITLE_        VARCHAR(512),
   MENU_TITLE_          VARCHAR(100),
   WINDOW_TARGET_       VARCHAR(20) NOT NULL,
   URL_                 VARCHAR(1024) NOT NULL,
   LOGO_URL_            VARCHAR(1024),
   DISPLAY_ORDER_       INT NOT NULL,
   TYPE_                VARCHAR(20) COMMENT ' 快捷菜单的分类标识 —— “系统默认” 、“用户自定义”',
   IS_VALID_            INT,
   PRIMARY KEY (SHORT_CUT_MENU_ID_)
);

/*==============================================================*/
/* Table: EAPP_SUB_SYSTEM                                       */
/*==============================================================*/
CREATE TABLE EAPP_SUB_SYSTEM
(
   SUB_SYSTEM_ID_       VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(100) NOT NULL,
   LOGO_URL_            VARCHAR(1024),
   IP_ADDRESS_          VARCHAR(40),
   SERVER_NAME_         VARCHAR(100),
   DOMAIN_NAME_         VARCHAR(100),
   PORT_                INT,
   DESCRIPTION_         VARCHAR(1024),
   IS_VALID_            SMALLINT,
   DISPLAY_ORDER_       INT,
   PRIMARY KEY (SUB_SYSTEM_ID_)
);

/*==============================================================*/
/* Table: EAPP_SYS_MSG                                          */
/*==============================================================*/
CREATE TABLE EAPP_SYS_MSG
(
   MSG_ID_              VARCHAR(36) NOT NULL,
   FROM_SYSTEM_ID_      VARCHAR(36),
   TO_ACCOUNT_ID_       VARCHAR(100),
   MSG_SENDER_          VARCHAR(100),
   MSG_CONTENT_         VARCHAR(1000),
   SEND_TIME_           DATETIME NOT NULL,
   VIEW_FLAG_           SMALLINT,
   PRIMARY KEY (MSG_ID_)
);

/*==============================================================*/
/* Table: EAPP_USER_ACCOUNT                                     */
/*==============================================================*/
CREATE TABLE EAPP_USER_ACCOUNT
(
   USER_ID_             VARCHAR(36) NOT NULL,
   ACCOUNT_ID_          VARCHAR(100) NOT NULL,
   DISPLAY_NAME_        VARCHAR(100) NOT NULL,
   PASSWORD_            VARCHAR(100),
   IS_LOCK_             SMALLINT NOT NULL,
   CHANGE_PASSWORD_FLAG_ CHAR(1) NOT NULL,
   CREATE_DATE_         DATE NOT NULL DEFAULT '2013-01-01',
   INVALID_DATE_        DATE,
   IS_LOGIC_DELETE_     SMALLINT NOT NULL,
   DESCRIPTION_         VARCHAR(1024),
   STYLE_THEMES_        VARCHAR(36),
   LAST_LOGIN_TIME_     DATETIME,
   LOGIN_COUNT_         INT,
   LOGIN_IP_LIMIT_      VARCHAR(2048),
   PRIMARY KEY (USER_ID_)
);

/*==============================================================*/
/* Table: EAPP_USER_PORTLET                                     */
/*==============================================================*/
CREATE TABLE EAPP_USER_PORTLET
(
   USER_ID_             VARCHAR(36) NOT NULL,
   PORTLET_ID_          VARCHAR(36) NOT NULL,
   PAGE_CONTAINER_ID_   VARCHAR(20) NOT NULL,
   POSITION_INDEX_      INT NOT NULL,
   PRIMARY KEY (USER_ID_, PORTLET_ID_)
);

/*==============================================================*/
/* Index: USER_PORLET_FK                                        */
/*==============================================================*/
CREATE INDEX USER_PORLET_FK ON EAPP_USER_PORTLET
(
   USER_ID_
);

/*==============================================================*/
/* Index: USER_PORLET2_FK                                       */
/*==============================================================*/
CREATE INDEX USER_PORLET2_FK ON EAPP_USER_PORTLET
(
   PORTLET_ID_
);

/*==============================================================*/
/* Table: EAPP_USER_ROLE                                        */
/*==============================================================*/
CREATE TABLE EAPP_USER_ROLE
(
   ROLE_ID_             VARCHAR(36) NOT NULL,
   USER_ID_             VARCHAR(36) NOT NULL,
   PRIMARY KEY (ROLE_ID_, USER_ID_)
);

/*==============================================================*/
/* Table: FLOW_TASK                                             */
/*==============================================================*/
CREATE TABLE FLOW_TASK
(
   TASK_ID_             VARCHAR(36) NOT NULL,
   FLOW_CLASS_          VARCHAR(100),
   FLOW_KEY_            VARCHAR(36),
   TASK_INSTANCE_ID_    VARCHAR(36),
   FLOW_INSTANCE_ID_    VARCHAR(36),
   FLOW_DEFINE_ID_      VARCHAR(36),
   TASK_STATE_          VARCHAR(20),
   FORM_ID_             VARCHAR(36) COMMENT '跟据流程分类关联不同类型的流程表单',
   TRANSACTOR_          VARCHAR(100) COMMENT '为空时从任务授权里查找',
   CREATE_TIME_         DATETIME,
   START_TIME_          DATETIME,
   END_TIME_            DATETIME,
   COMMENT_             VARCHAR(4000),
   TASK_NAME_           VARCHAR(400),
   NODE_NAME_           VARCHAR(400),
   FLOW_NAME_           VARCHAR(100),
   VIEW_FLAG_           SMALLINT,
   DESCRIPTION_         VARCHAR(400),
   PRIMARY KEY (TASK_ID_)
);

/*==============================================================*/
/* Table: FLOW_TASK_ASSIGN                                      */
/*==============================================================*/
CREATE TABLE FLOW_TASK_ASSIGN
(
   ASSIGN_ID_           VARCHAR(36) NOT NULL,
   TASK_ID_             VARCHAR(36),
   TYPE_                CHAR(1) COMMENT '0：用户
            1：角色
            2：机构',
   ASSIGN_KEY_          VARCHAR(128),
   PRIMARY KEY (ASSIGN_ID_)
);

ALTER TABLE EAPP_ACTOR_SERVICE ADD CONSTRAINT FK_ACTOR_SERVICE FOREIGN KEY (SERVICE_ID_)
      REFERENCES EAPP_SERVICE (SERVICE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_ACTOR_SERVICE ADD CONSTRAINT FK_SERV_ACTOR FOREIGN KEY (ACTOR_ID_)
      REFERENCES EAPP_ACTOR_ACCOUNT (ACTOR_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_DATA_DICTIONART ADD CONSTRAINT FK_DATADICT FOREIGN KEY (PARENT_DICT_ID_)
      REFERENCES EAPP_DATA_DICTIONART (DICT_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_DATA_DICTIONART ADD CONSTRAINT FK_DATA_DICT_SUBSYSTEM FOREIGN KEY (SUB_SYSTEM_ID_)
      REFERENCES EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_DEFAULT_PORTLET ADD CONSTRAINT FK_DEFAULT_PORTLET FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_GROUP ADD CONSTRAINT FK_GROUP FOREIGN KEY (PARENT_GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_GROUP ADD CONSTRAINT FK_RBACGROU_POST FOREIGN KEY (MANAGER_POST_ID_)
      REFERENCES EAPP_POST (POST_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_GROUP_ROLE ADD CONSTRAINT FK_GROUP_ROLE FOREIGN KEY (GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_GROUP_ROLE ADD CONSTRAINT FK_ROLE_GROUP FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_GROUP_USER ADD CONSTRAINT FK_GROUP_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_GROUP_USER ADD CONSTRAINT FK_USER_GROUP FOREIGN KEY (GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_MODULE ADD CONSTRAINT FK_MODULE FOREIGN KEY (PARENT_MODULE_ID_)
      REFERENCES EAPP_MODULE (MODULE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_MODULE ADD CONSTRAINT FK_MOD_SYSTEM FOREIGN KEY (SUB_SYSTEM_ID_)
      REFERENCES EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_MODULE ADD CONSTRAINT FK_QUOTE_MODULE FOREIGN KEY (QUOTE_MODULE_ID_)
      REFERENCES EAPP_MODULE (MODULE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_MODULE_ACTION ADD CONSTRAINT FK_ACTION_MODULE FOREIGN KEY (ACTION_ID_)
      REFERENCES EAPP_ACTION (ACTION_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_MODULE_ACTION ADD CONSTRAINT FK_MOD_ACTION FOREIGN KEY (MODULE_ID_)
      REFERENCES EAPP_MODULE (MODULE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_PORTLET ADD CONSTRAINT FK_PORT_SYSTEM FOREIGN KEY (SUB_SYSTEM_ID_)
      REFERENCES EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_PORTLET_ROLE ADD CONSTRAINT FK_PORTLET_ROLE FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_PORTLET_ROLE ADD CONSTRAINT FK_ROLE_PORTLET FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_POST ADD CONSTRAINT FK_POST FOREIGN KEY (PARENT_POST_ID_)
      REFERENCES EAPP_POST (POST_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_POST ADD CONSTRAINT FK_POST_GROU FOREIGN KEY (GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_POST_USER ADD CONSTRAINT FK_POST_USER_POST FOREIGN KEY (POST_ID_)
      REFERENCES EAPP_POST (POST_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_POST_USER ADD CONSTRAINT FK_POST_USER_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_ROLE_RIGHT ADD CONSTRAINT FK_RIGHT_ROLE FOREIGN KEY (MODULE_ACTION_ID_)
      REFERENCES EAPP_MODULE_ACTION (MODULE_ACTION_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_ROLE_RIGHT ADD CONSTRAINT FK_ROLE_RIGHT FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_SERVICE_RIGHT ADD CONSTRAINT FK_RIGHT_SERV FOREIGN KEY (SERVICE_ID_)
      REFERENCES EAPP_SERVICE (SERVICE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_SERVICE_RIGHT ADD CONSTRAINT FK_SERV_RIGHT FOREIGN KEY (MODULE_ACTION_ID_)
      REFERENCES EAPP_MODULE_ACTION (MODULE_ACTION_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_SHORT_CUT_MENU ADD CONSTRAINT FK_SHTCUT_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_USER_PORTLET ADD CONSTRAINT FK_PORTLET_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_USER_PORTLET ADD CONSTRAINT FK_USER_PORTLET FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_USER_ROLE ADD CONSTRAINT FK_ROLE_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE EAPP_USER_ROLE ADD CONSTRAINT FK_USER_ROLE FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FLOW_TASK_ASSIGN ADD CONSTRAINT FK_TASKPOOLEDS_REF_TASKS FOREIGN KEY (TASK_ID_)
      REFERENCES FLOW_TASK (TASK_ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

