/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2014/12/4 18:11:27                           */
/*==============================================================*/


ALTER TABLE EAPP_ACTOR_SERVICE
   DROP CONSTRAINT FK_ACTOR_SERVICE;

ALTER TABLE EAPP_ACTOR_SERVICE
   DROP CONSTRAINT FK_SERV_ACTOR;

ALTER TABLE EAPP_DATA_DICTIONART
   DROP CONSTRAINT FK_DATADICT;

ALTER TABLE EAPP_DATA_DICTIONART
   DROP CONSTRAINT FK_DATA_DICT_SUBSYSTEM;

ALTER TABLE EAPP_DEFAULT_PORTLET
   DROP CONSTRAINT FK_DEFAULT_PORTLET;

ALTER TABLE EAPP_GROUP
   DROP CONSTRAINT FK_GROUP;

ALTER TABLE EAPP_GROUP
   DROP CONSTRAINT FK_RBACGROU_POST;

ALTER TABLE EAPP_GROUP_ROLE
   DROP CONSTRAINT FK_GROUP_ROLE;

ALTER TABLE EAPP_GROUP_ROLE
   DROP CONSTRAINT FK_ROLE_GROUP;

ALTER TABLE EAPP_GROUP_USER
   DROP CONSTRAINT FK_GROUP_USER;

ALTER TABLE EAPP_GROUP_USER
   DROP CONSTRAINT FK_USER_GROUP;

ALTER TABLE EAPP_MODULE
   DROP CONSTRAINT FK_MODULE;

ALTER TABLE EAPP_MODULE
   DROP CONSTRAINT FK_MOD_SYSTEM;

ALTER TABLE EAPP_MODULE
   DROP CONSTRAINT FK_QUOTE_MODULE;

ALTER TABLE EAPP_MODULE_ACTION
   DROP CONSTRAINT FK_ACTION_MODULE;

ALTER TABLE EAPP_MODULE_ACTION
   DROP CONSTRAINT FK_MOD_ACTION;

ALTER TABLE EAPP_PORTLET
   DROP CONSTRAINT FK_PORT_SYSTEM;

ALTER TABLE EAPP_PORTLET_ROLE
   DROP CONSTRAINT FK_PORTLET_ROLE;

ALTER TABLE EAPP_PORTLET_ROLE
   DROP CONSTRAINT FK_ROLE_PORTLET;

ALTER TABLE EAPP_POST
   DROP CONSTRAINT FK_POST;

ALTER TABLE EAPP_POST
   DROP CONSTRAINT FK_POST_GROU;

ALTER TABLE EAPP_POST_USER
   DROP CONSTRAINT FK_POST_USER_POST;

ALTER TABLE EAPP_POST_USER
   DROP CONSTRAINT FK_POST_USER_USER;

ALTER TABLE EAPP_ROLE_RIGHT
   DROP CONSTRAINT FK_RIGHT_ROLE;

ALTER TABLE EAPP_ROLE_RIGHT
   DROP CONSTRAINT FK_ROLE_RIGHT;

ALTER TABLE EAPP_SERVICE_RIGHT
   DROP CONSTRAINT FK_RIGHT_SERV;

ALTER TABLE EAPP_SERVICE_RIGHT
   DROP CONSTRAINT FK_SERV_RIGHT;

ALTER TABLE EAPP_SHORT_CUT_MENU
   DROP CONSTRAINT FK_SHTCUT_USER;

ALTER TABLE EAPP_USER_PORTLET
   DROP CONSTRAINT FK_PORTLET_USER;

ALTER TABLE EAPP_USER_PORTLET
   DROP CONSTRAINT FK_USER_PORTLET;

ALTER TABLE EAPP_USER_ROLE
   DROP CONSTRAINT FK_ROLE_USER;

ALTER TABLE EAPP_USER_ROLE
   DROP CONSTRAINT FK_USER_ROLE;

ALTER TABLE FLOW_TASK_ASSIGN
   DROP CONSTRAINT FK_TASKPOOLEDS_REF_TASKS;

DROP TABLE EAPP_ACTION CASCADE CONSTRAINTS;

DROP TABLE EAPP_ACTION_LOG CASCADE CONSTRAINTS;

DROP TABLE EAPP_ACTOR_ACCOUNT CASCADE CONSTRAINTS;

DROP INDEX ACTOR_SERVICE2_FK;

DROP INDEX ACTOR_SERVICE_FK;

DROP TABLE EAPP_ACTOR_SERVICE CASCADE CONSTRAINTS;

DROP INDEX PARENTDD_CHILDRENDD_FK;

DROP INDEX SUBSYSTEM_DATADICT_FK;

DROP TABLE EAPP_DATA_DICTIONART CASCADE CONSTRAINTS;

DROP TABLE EAPP_DEFAULT_PORTLET CASCADE CONSTRAINTS;

DROP TABLE EAPP_FLOW_CONFIG CASCADE CONSTRAINTS;

DROP TABLE EAPP_FLOW_HANDLER CASCADE CONSTRAINTS;

DROP TABLE EAPP_FLOW_VAR CASCADE CONSTRAINTS;

DROP INDEX MANAGERPOST_FK;

DROP INDEX GROUP_CHILDGROUP_FK;

DROP TABLE EAPP_GROUP CASCADE CONSTRAINTS;

DROP INDEX GROUP_ROLE2_FK;

DROP INDEX GROUP_ROLE_FK;

DROP TABLE EAPP_GROUP_ROLE CASCADE CONSTRAINTS;

DROP INDEX GROUP_USER2_FK;

DROP INDEX GROUP_USER_FK;

DROP TABLE EAPP_GROUP_USER CASCADE CONSTRAINTS;

DROP TABLE EAPP_LOGIN_LOG CASCADE CONSTRAINTS;

DROP INDEX MOD_CHILDRENMOD_FK;

DROP INDEX SYSTEM_MODULE_FK;

DROP TABLE EAPP_MODULE CASCADE CONSTRAINTS;

DROP INDEX ACTION_MODULEACTION_FK;

DROP INDEX MODULE_MODULEACTION_FK;

DROP TABLE EAPP_MODULE_ACTION CASCADE CONSTRAINTS;

DROP TABLE EAPP_PORTLET CASCADE CONSTRAINTS;

DROP TABLE EAPP_PORTLET_ROLE CASCADE CONSTRAINTS;

DROP INDEX PARENTPOST_FK;

DROP TABLE EAPP_POST CASCADE CONSTRAINTS;

DROP INDEX POST_USER2_FK;

DROP INDEX POST_USER_FK;

DROP TABLE EAPP_POST_USER CASCADE CONSTRAINTS;

DROP TABLE EAPP_ROLE CASCADE CONSTRAINTS;

DROP INDEX RBACRIGHT2_FK;

DROP INDEX RBACRIGHT_FK;

DROP TABLE EAPP_ROLE_RIGHT CASCADE CONSTRAINTS;

DROP TABLE EAPP_SERVICE CASCADE CONSTRAINTS;

DROP INDEX SERVICE_RIGHT2_FK;

DROP INDEX SERVICE_RIGHT_FK;

DROP TABLE EAPP_SERVICE_RIGHT CASCADE CONSTRAINTS;

DROP INDEX USER_SHORTCUTMENU_FK;

DROP TABLE EAPP_SHORT_CUT_MENU CASCADE CONSTRAINTS;

DROP TABLE EAPP_SUB_SYSTEM CASCADE CONSTRAINTS;

DROP TABLE EAPP_SYS_MSG CASCADE CONSTRAINTS;

DROP TABLE EAPP_USER_ACCOUNT CASCADE CONSTRAINTS;

DROP INDEX USER_PORLET2_FK;

DROP INDEX USER_PORLET_FK;

DROP TABLE EAPP_USER_PORTLET CASCADE CONSTRAINTS;

DROP INDEX USER_ROLE2_FK;

DROP INDEX USER_ROLE_FK;

DROP TABLE EAPP_USER_ROLE CASCADE CONSTRAINTS;

DROP TABLE FLOW_TASK CASCADE CONSTRAINTS;

DROP TABLE FLOW_TASK_ASSIGN CASCADE CONSTRAINTS;

/*==============================================================*/
/* Table: EAPP_ACTION                                           */
/*==============================================================*/
CREATE TABLE EAPP_ACTION  (
   ACTION_ID_           VARCHAR2(36)                    NOT NULL,
   ACTION_KEY_          VARCHAR2(36),
   NAME_                VARCHAR2(100)                   NOT NULL,
   LOGO_URL_            VARCHAR2(1024),
   TIPS_                VARCHAR2(100),
   DESCRIPTION_         VARCHAR2(1024),
   CONSTRAINT PK_EAPP_ACTION PRIMARY KEY (ACTION_ID_)
);

/*==============================================================*/
/* Table: EAPP_ACTION_LOG                                       */
/*==============================================================*/
CREATE TABLE EAPP_ACTION_LOG  (
   LOG_ID_              VARCHAR2(36)                    NOT NULL,
   SYSTEM_ID_           VARCHAR2(36)                    NOT NULL,
   SYSTEM_NAME_         VARCHAR2(100),
   MODULE_KEY_          VARCHAR2(36),
   MODULE_NAME_         VARCHAR2(100),
   ACTION_KEY_          VARCHAR2(36),
   ACTION_NAME_         VARCHAR2(100),
   ACCOUNT_ID_          VARCHAR2(100),
   ACCOUNT_NAME_        VARCHAR2(100),
   OBJECT_ID_           VARCHAR2(36),
   OBJECT_              VARCHAR2(4000),
   IP_ADDRESS_          VARCHAR2(40),
   RESULT_STATUS_       VARCHAR2(100),
   OPERATE_TIME_        TIMESTAMP                       NOT NULL,
   IS_SERVICE_LOG_      SMALLINT,
   IS_BACKUP_           SMALLINT,
   CONSTRAINT PK_EAPP_ACTION_LOG PRIMARY KEY (LOG_ID_)
);

/*==============================================================*/
/* Table: EAPP_ACTOR_ACCOUNT                                    */
/*==============================================================*/
CREATE TABLE EAPP_ACTOR_ACCOUNT  (
   ACTOR_ID_            VARCHAR2(36)                    NOT NULL,
   ACCOUNT_ID_          VARCHAR2(100)                   NOT NULL,
   DISPLAY_NAME_        VARCHAR2(100)                   NOT NULL,
   CREDENCE_            VARCHAR2(2000),
   IS_LOCK_             SMALLINT                        NOT NULL,
   CHANGE_PASSWORD_FLAG_ CHAR(1)                         NOT NULL,
   CREATE_DATE_         DATE                           DEFAULT SYSDATE NOT NULL,
   INVALID_DATE_        DATE,
   IS_LOGIC_DELETE_     SMALLINT                        NOT NULL,
   DESCRIPTEION_        VARCHAR2(1024),
   CONSTRAINT PK_EAPP_ACTOR_ACCOUNT PRIMARY KEY (ACTOR_ID_)
);

/*==============================================================*/
/* Table: EAPP_ACTOR_SERVICE                                    */
/*==============================================================*/
CREATE TABLE EAPP_ACTOR_SERVICE  (
   ACTOR_ID_            VARCHAR2(36)                    NOT NULL,
   SERVICE_ID_          VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_ACTOR_SERVICE PRIMARY KEY (ACTOR_ID_, SERVICE_ID_)
);

/*==============================================================*/
/* Index: ACTOR_SERVICE_FK                                      */
/*==============================================================*/
CREATE INDEX ACTOR_SERVICE_FK ON EAPP_ACTOR_SERVICE (
   ACTOR_ID_ ASC
);

/*==============================================================*/
/* Index: ACTOR_SERVICE2_FK                                     */
/*==============================================================*/
CREATE INDEX ACTOR_SERVICE2_FK ON EAPP_ACTOR_SERVICE (
   SERVICE_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_DATA_DICTIONART                                  */
/*==============================================================*/
CREATE TABLE EAPP_DATA_DICTIONART  (
   DICT_ID_             VARCHAR2(36)                    NOT NULL,
   PARENT_DICT_ID_      VARCHAR2(36),
   SUB_SYSTEM_ID_       VARCHAR2(36),
   DICT_NAME_           VARCHAR2(100)                   NOT NULL,
   DICT_CODE_           VARCHAR2(100)                   NOT NULL,
   CEIL_VALUE_          VARCHAR2(100),
   FLOOR_VALUE_         VARCHAR2(100),
   DISPLAY_SORT_        INTEGER                         NOT NULL,
   DICT_TYPE_           VARCHAR2(20)                    NOT NULL,
   TREE_LEVEL_          INTEGER                         NOT NULL,
   DESCRIPTION_         VARCHAR2(1024),
   CONSTRAINT PK_EAPP_DATA_DICTIONART PRIMARY KEY (DICT_ID_)
);

/*==============================================================*/
/* Index: SUBSYSTEM_DATADICT_FK                                 */
/*==============================================================*/
CREATE INDEX SUBSYSTEM_DATADICT_FK ON EAPP_DATA_DICTIONART (
   SUB_SYSTEM_ID_ ASC
);

/*==============================================================*/
/* Index: PARENTDD_CHILDRENDD_FK                                */
/*==============================================================*/
CREATE INDEX PARENTDD_CHILDRENDD_FK ON EAPP_DATA_DICTIONART (
   PARENT_DICT_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_DEFAULT_PORTLET                                  */
/*==============================================================*/
CREATE TABLE EAPP_DEFAULT_PORTLET  (
   DEFAULT_PORTLET_ID_  VARCHAR2(36)                    NOT NULL,
   PORTLET_ID_          VARCHAR2(36)                    NOT NULL,
   PAGE_CONTAINER_ID_   VARCHAR2(20)                    NOT NULL,
   POSITION_INDEX_      INTEGER                         NOT NULL,
   CONSTRAINT PK_EAPP_DEFAULT_PORTLET PRIMARY KEY (DEFAULT_PORTLET_ID_)
);

/*==============================================================*/
/* Table: EAPP_FLOW_CONFIG                                      */
/*==============================================================*/
CREATE TABLE EAPP_FLOW_CONFIG  (
   CONF_ID_             VARCHAR2(36)                    NOT NULL,
   FLOW_CLASS_          VARCHAR2(100),
   FLOW_KEY_            VARCHAR2(36),
   FLOW_NAME_           VARCHAR2(100),
   FLOW_VERSION_        NUMBER(14),
   DRAFT_FLAG_          SMALLINT,
   CONSTRAINT PK_EAPP_FLOW_CONFIG PRIMARY KEY (CONF_ID_)
);

COMMENT ON COLUMN EAPP_FLOW_CONFIG.DRAFT_FLAG_ IS
'1：草稿；2：发布；3：禁用';

/*==============================================================*/
/* Table: EAPP_FLOW_HANDLER                                     */
/*==============================================================*/
CREATE TABLE EAPP_FLOW_HANDLER  (
   HAND_ID_             VARCHAR2(36)                    NOT NULL,
   FLOW_CLASS_          VARCHAR2(100),
   NAME_                VARCHAR2(100),
   HANDLER_CLASS_       VARCHAR2(100),
   TYPE_                VARCHAR2(20),
   GLOBAL_FLAG_         SMALLINT,
   CONSTRAINT PK_EAPP_FLOW_HANDLER PRIMARY KEY (HAND_ID_)
);

COMMENT ON COLUMN EAPP_FLOW_HANDLER.TYPE_ IS
'ACTION/ASSIGN/MUTIASSIGN/DECISION/VIEW';

/*==============================================================*/
/* Table: EAPP_FLOW_VAR                                         */
/*==============================================================*/
CREATE TABLE EAPP_FLOW_VAR  (
   VAR_ID_              VARCHAR2(36)                    NOT NULL,
   FLOW_CLASS_          VARCHAR2(100),
   NAME_                VARCHAR2(100),
   DISPLAY_NAME_        VARCHAR2(100),
   TYPE_                VARCHAR2(20),
   NOT_NUL_             SMALLINT,
   GLOBAL_FLAG_         SMALLINT,
   CONSTRAINT PK_EAPP_FLOW_VAR PRIMARY KEY (VAR_ID_)
);

COMMENT ON COLUMN EAPP_FLOW_VAR.TYPE_ IS
'STRING/BOOLEAN/INT/LONG/FLOAT/DOUBLE/DATE';

/*==============================================================*/
/* Table: EAPP_GROUP                                            */
/*==============================================================*/
CREATE TABLE EAPP_GROUP  (
   GROUP_ID             VARCHAR2(36)                    NOT NULL,
   PARENT_GROUP_ID_     VARCHAR2(36),
   MANAGER_POST_ID_     VARCHAR2(36),
   GROUP_NAME_          VARCHAR2(100)                   NOT NULL,
   DISPLAY_ORDER_       INTEGER                         NOT NULL,
   TYPE_                VARCHAR2(20),
   TREE_LEVEL_          INTEGER,
   DESCRIPTION_         VARCHAR2(1024),
   CONSTRAINT PK_EAPP_GROUP PRIMARY KEY (GROUP_ID)
);

/*==============================================================*/
/* Index: GROUP_CHILDGROUP_FK                                   */
/*==============================================================*/
CREATE INDEX GROUP_CHILDGROUP_FK ON EAPP_GROUP (
   PARENT_GROUP_ID_ ASC
);

/*==============================================================*/
/* Index: MANAGERPOST_FK                                        */
/*==============================================================*/
CREATE INDEX MANAGERPOST_FK ON EAPP_GROUP (
   MANAGER_POST_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_GROUP_ROLE                                       */
/*==============================================================*/
CREATE TABLE EAPP_GROUP_ROLE  (
   GROUP_ID_            VARCHAR2(36)                    NOT NULL,
   ROLE_ID_             VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_GROUP_ROLE PRIMARY KEY (GROUP_ID_, ROLE_ID_)
);

/*==============================================================*/
/* Index: GROUP_ROLE_FK                                         */
/*==============================================================*/
CREATE INDEX GROUP_ROLE_FK ON EAPP_GROUP_ROLE (
   GROUP_ID_ ASC
);

/*==============================================================*/
/* Index: GROUP_ROLE2_FK                                        */
/*==============================================================*/
CREATE INDEX GROUP_ROLE2_FK ON EAPP_GROUP_ROLE (
   ROLE_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_GROUP_USER                                       */
/*==============================================================*/
CREATE TABLE EAPP_GROUP_USER  (
   GROUP_ID_            VARCHAR2(36)                    NOT NULL,
   USER_ID_             VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_GROUP_USER PRIMARY KEY (GROUP_ID_, USER_ID_)
);

/*==============================================================*/
/* Index: GROUP_USER_FK                                         */
/*==============================================================*/
CREATE INDEX GROUP_USER_FK ON EAPP_GROUP_USER (
   GROUP_ID_ ASC
);

/*==============================================================*/
/* Index: GROUP_USER2_FK                                        */
/*==============================================================*/
CREATE INDEX GROUP_USER2_FK ON EAPP_GROUP_USER (
   USER_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_LOGIN_LOG                                        */
/*==============================================================*/
CREATE TABLE EAPP_LOGIN_LOG  (
   LOG_ID_              VARCHAR2(36)                    NOT NULL,
   SESSION_ID_          VARCHAR2(36),
   ACCOUNT_ID_          VARCHAR2(100),
   ACCOUNT_NAME_        VARCHAR2(100),
   IP_ADDRESS_          VARCHAR2(40),
   IS_SUCCESS_          SMALLINT,
   LOGIN_INFO_          VARCHAR2(200),
   LOGIN_TIME_          TIMESTAMP                       NOT NULL,
   LOGOUT_TIME_         TIMESTAMP,
   IS_BACKUP_           SMALLINT,
   CONSTRAINT PK_EAPP_LOGIN_LOG PRIMARY KEY (LOG_ID_)
);

/*==============================================================*/
/* Table: EAPP_MODULE                                           */
/*==============================================================*/
CREATE TABLE EAPP_MODULE  (
   MODULE_ID_           VARCHAR2(36)                    NOT NULL,
   PARENT_MODULE_ID_    VARCHAR2(36),
   MODULE_KEY_          VARCHAR2(36),
   NAME_                VARCHAR2(100)                   NOT NULL,
   DISPLAY_ORDER_       INTEGER,
   URL_                 VARCHAR2(1024),
   TREE_LEVEL_          INTEGER                         NOT NULL,
   DESCRIPTION_         VARCHAR2(1024),
   QUOTE_MODULE_ID_     VARCHAR2(36),
   SUB_SYSTEM_ID_       VARCHAR2(36),
   CONSTRAINT PK_EAPP_MODULE PRIMARY KEY (MODULE_ID_)
);

/*==============================================================*/
/* Index: SYSTEM_MODULE_FK                                      */
/*==============================================================*/
CREATE INDEX SYSTEM_MODULE_FK ON EAPP_MODULE (
   SUB_SYSTEM_ID_ ASC
);

/*==============================================================*/
/* Index: MOD_CHILDRENMOD_FK                                    */
/*==============================================================*/
CREATE INDEX MOD_CHILDRENMOD_FK ON EAPP_MODULE (
   PARENT_MODULE_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_MODULE_ACTION                                    */
/*==============================================================*/
CREATE TABLE EAPP_MODULE_ACTION  (
   MODULE_ACTION_ID_    VARCHAR2(36)                    NOT NULL,
   MODULE_ID_           VARCHAR2(36),
   ACTION_ID_           VARCHAR2(36),
   MODULE_KEY_          VARCHAR2(36),
   ACTION_KEY_          VARCHAR2(36),
   IS_VALID_            SMALLINT                        NOT NULL,
   IS_RPC_              SMALLINT                        NOT NULL,
   IS_HTTP_             SMALLINT                       DEFAULT 1,
   CONSTRAINT PK_EAPP_MODULE_ACTION PRIMARY KEY (MODULE_ACTION_ID_)
);

/*==============================================================*/
/* Index: MODULE_MODULEACTION_FK                                */
/*==============================================================*/
CREATE INDEX MODULE_MODULEACTION_FK ON EAPP_MODULE_ACTION (
   MODULE_ID_ ASC
);

/*==============================================================*/
/* Index: ACTION_MODULEACTION_FK                                */
/*==============================================================*/
CREATE INDEX ACTION_MODULEACTION_FK ON EAPP_MODULE_ACTION (
   ACTION_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_PORTLET                                          */
/*==============================================================*/
CREATE TABLE EAPP_PORTLET  (
   PORTLET_ID_          VARCHAR2(36)                    NOT NULL,
   PORTLET_NAME_        VARCHAR2(100)                   NOT NULL,
   URL_                 VARCHAR2(1024)                  NOT NULL,
   HIDDENABLE_          SMALLINT,
   MOVEDABLE_           SMALLINT,
   STYLE_               VARCHAR2(100),
   MORE_URL_            VARCHAR2(1024),
   SUB_SYSTEM_ID_       VARCHAR2(36),
   CONSTRAINT PK_EAPP_PORTLET PRIMARY KEY (PORTLET_ID_)
);

/*==============================================================*/
/* Table: EAPP_PORTLET_ROLE                                     */
/*==============================================================*/
CREATE TABLE EAPP_PORTLET_ROLE  (
   PORTLET_ID_          VARCHAR2(36)                    NOT NULL,
   ROLE_ID_             VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_PORTLET_ROLE PRIMARY KEY (PORTLET_ID_, ROLE_ID_)
);

/*==============================================================*/
/* Table: EAPP_POST                                             */
/*==============================================================*/
CREATE TABLE EAPP_POST  (
   POST_ID_             VARCHAR2(36)                    NOT NULL,
   GROUP_ID_            VARCHAR2(36),
   PARENT_POST_ID_      VARCHAR2(36),
   POST_NAME_           VARCHAR2(100)                   NOT NULL,
   DISPLAY_ORDER_       INTEGER                         NOT NULL,
   TREE_LEVEL_          INTEGER,
   DESCRIPTION_         VARCHAR2(1024),
   CONSTRAINT PK_EAPP_POST PRIMARY KEY (POST_ID_)
);

/*==============================================================*/
/* Index: PARENTPOST_FK                                         */
/*==============================================================*/
CREATE INDEX PARENTPOST_FK ON EAPP_POST (
   PARENT_POST_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_POST_USER                                        */
/*==============================================================*/
CREATE TABLE EAPP_POST_USER  (
   POST_ID_             VARCHAR2(36)                    NOT NULL,
   USER_ID_             VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_POST_USER PRIMARY KEY (POST_ID_, USER_ID_)
);

/*==============================================================*/
/* Index: POST_USER_FK                                          */
/*==============================================================*/
CREATE INDEX POST_USER_FK ON EAPP_POST_USER (
   POST_ID_ ASC
);

/*==============================================================*/
/* Index: POST_USER2_FK                                         */
/*==============================================================*/
CREATE INDEX POST_USER2_FK ON EAPP_POST_USER (
   USER_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_ROLE                                             */
/*==============================================================*/
CREATE TABLE EAPP_ROLE  (
   ROLE_ID_             VARCHAR2(36)                    NOT NULL,
   ROLE_NAME_           VARCHAR2(100)                   NOT NULL,
   IS_VALID_            SMALLINT                        NOT NULL,
   DESCRIPTION_         VARCHAR2(1024),
   CONSTRAINT PK_EAPP_ROLE PRIMARY KEY (ROLE_ID_)
);

/*==============================================================*/
/* Table: EAPP_ROLE_RIGHT                                       */
/*==============================================================*/
CREATE TABLE EAPP_ROLE_RIGHT  (
   ROLE_ID_             VARCHAR2(36)                    NOT NULL,
   MODULE_ACTION_ID_    VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_ROLE_RIGHT PRIMARY KEY (ROLE_ID_, MODULE_ACTION_ID_)
);

/*==============================================================*/
/* Index: RBACRIGHT_FK                                          */
/*==============================================================*/
CREATE INDEX RBACRIGHT_FK ON EAPP_ROLE_RIGHT (
   ROLE_ID_ ASC
);

/*==============================================================*/
/* Index: RBACRIGHT2_FK                                         */
/*==============================================================*/
CREATE INDEX RBACRIGHT2_FK ON EAPP_ROLE_RIGHT (
   MODULE_ACTION_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_SERVICE                                          */
/*==============================================================*/
CREATE TABLE EAPP_SERVICE  (
   SERVICE_ID_          VARCHAR2(36)                    NOT NULL,
   SERVICE_NAME_        VARCHAR2(100)                   NOT NULL,
   IS_VALID_            SMALLINT                        NOT NULL,
   DESCRIPTION_         VARCHAR2(1024),
   CONSTRAINT PK_EAPP_SERVICE PRIMARY KEY (SERVICE_ID_)
);

/*==============================================================*/
/* Table: EAPP_SERVICE_RIGHT                                    */
/*==============================================================*/
CREATE TABLE EAPP_SERVICE_RIGHT  (
   SERVICE_ID_          VARCHAR2(36)                    NOT NULL,
   MODULE_ACTION_ID_    VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_SERVICE_RIGHT PRIMARY KEY (SERVICE_ID_, MODULE_ACTION_ID_)
);

/*==============================================================*/
/* Index: SERVICE_RIGHT_FK                                      */
/*==============================================================*/
CREATE INDEX SERVICE_RIGHT_FK ON EAPP_SERVICE_RIGHT (
   SERVICE_ID_ ASC
);

/*==============================================================*/
/* Index: SERVICE_RIGHT2_FK                                     */
/*==============================================================*/
CREATE INDEX SERVICE_RIGHT2_FK ON EAPP_SERVICE_RIGHT (
   MODULE_ACTION_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_SHORT_CUT_MENU                                   */
/*==============================================================*/
CREATE TABLE EAPP_SHORT_CUT_MENU  (
   SHORT_CUT_MENU_ID_   VARCHAR2(36)                    NOT NULL,
   USER_ID_             VARCHAR2(36),
   MODULE_TITLE_        VARCHAR2(512),
   MENU_TITLE_          VARCHAR2(100),
   WINDOW_TARGET_       VARCHAR2(20)                    NOT NULL,
   URL_                 VARCHAR2(1024)                  NOT NULL,
   LOGO_URL_            VARCHAR2(1024),
   DISPLAY_ORDER_       INTEGER                         NOT NULL,
   TYPE_                VARCHAR2(20),
   IS_VALID_            INTEGER,
   CONSTRAINT PK_EAPP_SHORT_CUT_MENU PRIMARY KEY (SHORT_CUT_MENU_ID_)
);

COMMENT ON COLUMN EAPP_SHORT_CUT_MENU.TYPE_ IS
' 快捷菜单的分类标识 —— “系统默认” 、“用户自定义”';

/*==============================================================*/
/* Index: USER_SHORTCUTMENU_FK                                  */
/*==============================================================*/
CREATE INDEX USER_SHORTCUTMENU_FK ON EAPP_SHORT_CUT_MENU (
   USER_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_SUB_SYSTEM                                       */
/*==============================================================*/
CREATE TABLE EAPP_SUB_SYSTEM  (
   SUB_SYSTEM_ID_       VARCHAR2(36)                    NOT NULL,
   NAME_                VARCHAR2(100)                   NOT NULL,
   LOGO_URL_            VARCHAR2(1024),
   IP_ADDRESS_          VARCHAR2(40),
   SERVER_NAME_         VARCHAR2(100),
   DOMAIN_NAME_         VARCHAR2(100),
   PORT_                INTEGER,
   DESCRIPTION_         VARCHAR2(1024),
   IS_VALID_            SMALLINT,
   DISPLAY_ORDER_       INTEGER,
   CONSTRAINT PK_EAPP_SUB_SYSTEM PRIMARY KEY (SUB_SYSTEM_ID_)
);

/*==============================================================*/
/* Table: EAPP_SYS_MSG                                          */
/*==============================================================*/
CREATE TABLE EAPP_SYS_MSG  (
   MSG_ID_              VARCHAR2(36)                    NOT NULL,
   FROM_SYSTEM_ID_      VARCHAR2(36),
   TO_ACCOUNT_ID_       VARCHAR2(100),
   MSG_SENDER_          VARCHAR2(100),
   MSG_CONTENT_         VARCHAR2(1000),
   SEND_TIME_           TIMESTAMP                       NOT NULL,
   VIEW_FLAG_           SMALLINT,
   CONSTRAINT PK_EAPP_SYS_MSG PRIMARY KEY (MSG_ID_)
);

/*==============================================================*/
/* Table: EAPP_USER_ACCOUNT                                     */
/*==============================================================*/
CREATE TABLE EAPP_USER_ACCOUNT  (
   USER_ID_             VARCHAR2(36)                    NOT NULL,
   ACCOUNT_ID_          VARCHAR2(100)                   NOT NULL,
   DISPLAY_NAME_        VARCHAR2(100)                   NOT NULL,
   PASSWORD_            VARCHAR2(100),
   IS_LOCK_             SMALLINT                        NOT NULL,
   CHANGE_PASSWORD_FLAG_ CHAR(1)                         NOT NULL,
   CREATE_DATE_         DATE                           DEFAULT SYSDATE NOT NULL,
   INVALID_DATE_        DATE,
   IS_LOGIC_DELETE_     SMALLINT                        NOT NULL,
   DESCRIPTION_         VARCHAR2(1024),
   STYLE_THEMES_        VARCHAR2(36),
   LAST_LOGIN_TIME_     TIMESTAMP,
   LOGIN_COUNT_         INT,
   LOGIN_IP_LIMIT_      VARCHAR2(2048),
   CONSTRAINT PK_EAPP_USER_ACCOUNT PRIMARY KEY (USER_ID_)
);

/*==============================================================*/
/* Table: EAPP_USER_PORTLET                                     */
/*==============================================================*/
CREATE TABLE EAPP_USER_PORTLET  (
   USER_ID_             VARCHAR2(36)                    NOT NULL,
   PORTLET_ID_          VARCHAR2(36)                    NOT NULL,
   PAGE_CONTAINER_ID_   VARCHAR2(20)                    NOT NULL,
   POSITION_INDEX_      INTEGER                         NOT NULL,
   CONSTRAINT PK_EAPP_USER_PORTLET PRIMARY KEY (USER_ID_, PORTLET_ID_)
);

/*==============================================================*/
/* Index: USER_PORLET_FK                                        */
/*==============================================================*/
CREATE INDEX USER_PORLET_FK ON EAPP_USER_PORTLET (
   USER_ID_ ASC
);

/*==============================================================*/
/* Index: USER_PORLET2_FK                                       */
/*==============================================================*/
CREATE INDEX USER_PORLET2_FK ON EAPP_USER_PORTLET (
   PORTLET_ID_ ASC
);

/*==============================================================*/
/* Table: EAPP_USER_ROLE                                        */
/*==============================================================*/
CREATE TABLE EAPP_USER_ROLE  (
   ROLE_ID_             VARCHAR2(36)                    NOT NULL,
   USER_ID_             VARCHAR2(36)                    NOT NULL,
   CONSTRAINT PK_EAPP_USER_ROLE PRIMARY KEY (ROLE_ID_, USER_ID_)
);

/*==============================================================*/
/* Index: USER_ROLE_FK                                          */
/*==============================================================*/
CREATE INDEX USER_ROLE_FK ON EAPP_USER_ROLE (
   ROLE_ID_ ASC
);

/*==============================================================*/
/* Index: USER_ROLE2_FK                                         */
/*==============================================================*/
CREATE INDEX USER_ROLE2_FK ON EAPP_USER_ROLE (
   USER_ID_ ASC
);

/*==============================================================*/
/* Table: FLOW_TASK                                             */
/*==============================================================*/
CREATE TABLE FLOW_TASK  (
   TASK_ID_             VARCHAR2(36)                    NOT NULL,
   FLOW_CLASS_          VARCHAR2(100),
   FLOW_KEY_            VARCHAR2(36),
   TASK_INSTANCE_ID_    VARCHAR2(36),
   FLOW_INSTANCE_ID_    VARCHAR2(36),
   FLOW_DEFINE_ID_      VARCHAR2(36),
   TASK_STATE_          VARCHAR2(20),
   FORM_ID_             VARCHAR2(36),
   TRANSACTOR_          VARCHAR2(100),
   CREATE_TIME_         TIMESTAMP,
   START_TIME_          TIMESTAMP,
   END_TIME_            TIMESTAMP,
   COMMENT_             VARCHAR2(4000),
   TASK_NAME_           VARCHAR2(400),
   NODE_NAME_           VARCHAR2(400),
   FLOW_NAME_           VARCHAR2(100),
   VIEW_FLAG_           SMALLINT,
   DESCRIPTION_         VARCHAR2(400),
   CONSTRAINT PK_FLOW_TASK PRIMARY KEY (TASK_ID_)
);

COMMENT ON COLUMN FLOW_TASK.FORM_ID_ IS
'跟据流程分类关联不同类型的流程表单';

COMMENT ON COLUMN FLOW_TASK.TRANSACTOR_ IS
'为空时从任务授权里查找';

/*==============================================================*/
/* Table: FLOW_TASK_ASSIGN                                      */
/*==============================================================*/
CREATE TABLE FLOW_TASK_ASSIGN  (
   ASSIGN_ID_           VARCHAR2(36)                    NOT NULL,
   TASK_ID_             VARCHAR2(36),
   TYPE_                CHAR(1),
   ASSIGN_KEY_          VARCHAR2(128),
   CONSTRAINT PK_FLOW_TASK_ASSIGN PRIMARY KEY (ASSIGN_ID_)
);

COMMENT ON COLUMN FLOW_TASK_ASSIGN.TYPE_ IS
'0：用户
1：角色
2：机构';

ALTER TABLE EAPP_ACTOR_SERVICE
   ADD CONSTRAINT FK_ACTOR_SERVICE FOREIGN KEY (SERVICE_ID_)
      REFERENCES EAPP_SERVICE (SERVICE_ID_);

ALTER TABLE EAPP_ACTOR_SERVICE
   ADD CONSTRAINT FK_SERV_ACTOR FOREIGN KEY (ACTOR_ID_)
      REFERENCES EAPP_ACTOR_ACCOUNT (ACTOR_ID_);

ALTER TABLE EAPP_DATA_DICTIONART
   ADD CONSTRAINT FK_DATADICT FOREIGN KEY (PARENT_DICT_ID_)
      REFERENCES EAPP_DATA_DICTIONART (DICT_ID_);

ALTER TABLE EAPP_DATA_DICTIONART
   ADD CONSTRAINT FK_DATA_DICT_SUBSYSTEM FOREIGN KEY (SUB_SYSTEM_ID_)
      REFERENCES EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_);

ALTER TABLE EAPP_DEFAULT_PORTLET
   ADD CONSTRAINT FK_DEFAULT_PORTLET FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_);

ALTER TABLE EAPP_GROUP
   ADD CONSTRAINT FK_GROUP FOREIGN KEY (PARENT_GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID);

ALTER TABLE EAPP_GROUP
   ADD CONSTRAINT FK_RBACGROU_POST FOREIGN KEY (MANAGER_POST_ID_)
      REFERENCES EAPP_POST (POST_ID_);

ALTER TABLE EAPP_GROUP_ROLE
   ADD CONSTRAINT FK_GROUP_ROLE FOREIGN KEY (GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID);

ALTER TABLE EAPP_GROUP_ROLE
   ADD CONSTRAINT FK_ROLE_GROUP FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_);

ALTER TABLE EAPP_GROUP_USER
   ADD CONSTRAINT FK_GROUP_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_);

ALTER TABLE EAPP_GROUP_USER
   ADD CONSTRAINT FK_USER_GROUP FOREIGN KEY (GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID);

ALTER TABLE EAPP_MODULE
   ADD CONSTRAINT FK_MODULE FOREIGN KEY (PARENT_MODULE_ID_)
      REFERENCES EAPP_MODULE (MODULE_ID_);

ALTER TABLE EAPP_MODULE
   ADD CONSTRAINT FK_MOD_SYSTEM FOREIGN KEY (SUB_SYSTEM_ID_)
      REFERENCES EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_);

ALTER TABLE EAPP_MODULE
   ADD CONSTRAINT FK_QUOTE_MODULE FOREIGN KEY (QUOTE_MODULE_ID_)
      REFERENCES EAPP_MODULE (MODULE_ID_);

ALTER TABLE EAPP_MODULE_ACTION
   ADD CONSTRAINT FK_ACTION_MODULE FOREIGN KEY (ACTION_ID_)
      REFERENCES EAPP_ACTION (ACTION_ID_);

ALTER TABLE EAPP_MODULE_ACTION
   ADD CONSTRAINT FK_MOD_ACTION FOREIGN KEY (MODULE_ID_)
      REFERENCES EAPP_MODULE (MODULE_ID_);

ALTER TABLE EAPP_PORTLET
   ADD CONSTRAINT FK_PORT_SYSTEM FOREIGN KEY (SUB_SYSTEM_ID_)
      REFERENCES EAPP_SUB_SYSTEM (SUB_SYSTEM_ID_);

ALTER TABLE EAPP_PORTLET_ROLE
   ADD CONSTRAINT FK_PORTLET_ROLE FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_);

ALTER TABLE EAPP_PORTLET_ROLE
   ADD CONSTRAINT FK_ROLE_PORTLET FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_);

ALTER TABLE EAPP_POST
   ADD CONSTRAINT FK_POST FOREIGN KEY (PARENT_POST_ID_)
      REFERENCES EAPP_POST (POST_ID_);

ALTER TABLE EAPP_POST
   ADD CONSTRAINT FK_POST_GROU FOREIGN KEY (GROUP_ID_)
      REFERENCES EAPP_GROUP (GROUP_ID);

ALTER TABLE EAPP_POST_USER
   ADD CONSTRAINT FK_POST_USER_POST FOREIGN KEY (POST_ID_)
      REFERENCES EAPP_POST (POST_ID_);

ALTER TABLE EAPP_POST_USER
   ADD CONSTRAINT FK_POST_USER_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_);

ALTER TABLE EAPP_ROLE_RIGHT
   ADD CONSTRAINT FK_RIGHT_ROLE FOREIGN KEY (MODULE_ACTION_ID_)
      REFERENCES EAPP_MODULE_ACTION (MODULE_ACTION_ID_);

ALTER TABLE EAPP_ROLE_RIGHT
   ADD CONSTRAINT FK_ROLE_RIGHT FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_);

ALTER TABLE EAPP_SERVICE_RIGHT
   ADD CONSTRAINT FK_RIGHT_SERV FOREIGN KEY (SERVICE_ID_)
      REFERENCES EAPP_SERVICE (SERVICE_ID_);

ALTER TABLE EAPP_SERVICE_RIGHT
   ADD CONSTRAINT FK_SERV_RIGHT FOREIGN KEY (MODULE_ACTION_ID_)
      REFERENCES EAPP_MODULE_ACTION (MODULE_ACTION_ID_);

ALTER TABLE EAPP_SHORT_CUT_MENU
   ADD CONSTRAINT FK_SHTCUT_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_);

ALTER TABLE EAPP_USER_PORTLET
   ADD CONSTRAINT FK_PORTLET_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_);

ALTER TABLE EAPP_USER_PORTLET
   ADD CONSTRAINT FK_USER_PORTLET FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_);

ALTER TABLE EAPP_USER_ROLE
   ADD CONSTRAINT FK_ROLE_USER FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_);

ALTER TABLE EAPP_USER_ROLE
   ADD CONSTRAINT FK_USER_ROLE FOREIGN KEY (ROLE_ID_)
      REFERENCES EAPP_ROLE (ROLE_ID_);

ALTER TABLE FLOW_TASK_ASSIGN
   ADD CONSTRAINT FK_TASKPOOLEDS_REF_TASKS FOREIGN KEY (TASK_ID_)
      REFERENCES FLOW_TASK (TASK_ID_);

