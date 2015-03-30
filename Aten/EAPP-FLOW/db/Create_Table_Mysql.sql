/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2015/1/4 18:28:25                            */
/*==============================================================*/


DROP INDEX IDX_ACTION_FLOWDEF ON WF_ACTION;

DROP INDEX IDX_ACTION_EVENT ON WF_ACTION;

DROP TABLE IF EXISTS WF_ACTION;

DROP INDEX IDX_ASYNJOB_ACTION ON WF_ASYNJOB;

DROP INDEX IDX_ASYNJOB_NODE ON WF_ASYNJOB;

DROP INDEX IDX_ASYNJOB_FLOWTOKEN ON WF_ASYNJOB;

DROP INDEX IDX_ASYNJOB_FLOWINST ON WF_ASYNJOB;

DROP TABLE IF EXISTS WF_ASYNJOB;

DROP INDEX IDX_CTXVAR_FLOWINSTANCE ON WF_CONTEXTVARIABLE;

DROP TABLE IF EXISTS WF_CONTEXTVARIABLE;

DROP TABLE IF EXISTS WF_DELEGATION;

DROP INDEX IDX_EVENT_TASKDEFINE ON WF_EVENT;

DROP INDEX IDX_EVENT_TRANSITION ON WF_EVENT;

DROP INDEX IDX_EVENT_NODE ON WF_EVENT;

DROP INDEX IDX_EVENT_FLOWDEFINE ON WF_EVENT;

DROP TABLE IF EXISTS WF_EVENT;

DROP INDEX IDX_FLOWDEFINE_STARTNODE ON WF_FLOWDEFINE;

DROP INDEX IDX_FLOWDEFINE_VERSION ON WF_FLOWDEFINE;

DROP INDEX IDX_FLOWDEFINE_FLOWKEY ON WF_FLOWDEFINE;

DROP TABLE IF EXISTS WF_FLOWDEFINE;

DROP TABLE IF EXISTS WF_FLOWDRAFT;

DROP INDEX IDX_FLOWINST_SUPERTOKEN ON WF_FLOWINSTANCE;

DROP INDEX IDX_FLOWINST_FLOWDEF ON WF_FLOWINSTANCE;

DROP INDEX IDX_FLOWINST_TOKEN ON WF_FLOWINSTANCE;

DROP TABLE IF EXISTS WF_FLOWINSTANCE;

DROP INDEX IDX_TOKEN_SUBFLOWINSTANCE ON WF_FLOWTOKEN;

DROP INDEX IDX_TOKEN_NODE ON WF_FLOWTOKEN;

DROP INDEX IDX_TOKEN_FLOWINSTANCE ON WF_FLOWTOKEN;

DROP INDEX IDX_TOKEN_PARENT ON WF_FLOWTOKEN;

DROP TABLE IF EXISTS WF_FLOWTOKEN;

DROP INDEX IDX_NODE_SUBFLOWDEFINE ON WF_NODE;

DROP INDEX IDX_NODE_ACTION ON WF_NODE;

DROP INDEX IDX_NODE_FLOWDEFINE ON WF_NODE;

DROP TABLE IF EXISTS WF_NODE;

DROP TABLE IF EXISTS WF_NODECONDITIONS;

DROP INDEX IDX_POOLEDACTOR_TASKINST ON WF_POOLEDACTOR;

DROP TABLE IF EXISTS WF_POOLEDACTOR;

DROP INDEX IDX_POOLEDROLE_TASKINST ON WF_POOLEDROLE;

DROP TABLE IF EXISTS WF_POOLEDROLE;

DROP INDEX IDX_TASKDEF_TASKVIEW ON WF_TASKDEFINE;

DROP INDEX IDX_TASKDEF_TASKASSN ON WF_TASKDEFINE;

DROP INDEX IDX_TASKDEF_FLOWDEF ON WF_TASKDEFINE;

DROP INDEX IDX_TASKDEF_NODE ON WF_TASKDEFINE;

DROP TABLE IF EXISTS WF_TASKDEFINE;

DROP INDEX IDX_TASKINST_FLOWINST ON WF_TASKINSTANCE;

DROP INDEX IDX_TASKINST_TASKDEF ON WF_TASKINSTANCE;

DROP INDEX IDX_TASKINST_TOKEN ON WF_TASKINSTANCE;

DROP TABLE IF EXISTS WF_TASKINSTANCE;

DROP TABLE IF EXISTS WF_TRACEPOINT;

DROP INDEX IDX_TRANSITION_FLOWDEF ON WF_TRANSITION;

DROP INDEX IDX_TRANSITION_TO ON WF_TRANSITION;

DROP INDEX IDX_TRANSITION_FROM ON WF_TRANSITION;

DROP TABLE IF EXISTS WF_TRANSITION;

DROP TABLE IF EXISTS WF_VARIABLEDECLARE;

/*==============================================================*/
/* Table: WF_ACTION                                             */
/*==============================================================*/
CREATE TABLE WF_ACTION
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(255) NOT NULL,
   ACTIONHANDLER_       VARCHAR(255),
   ACTIONEXPRESSION_    TEXT,
   EVENT_               VARCHAR(36),
   EVENTINDEX_          INT,
   ISASYNC_             SMALLINT,
   ACCEPTPROPAGATIONEVENT_ SMALLINT,
   FLOWDEFINE_          VARCHAR(36),
   PRIMARY KEY (ID_),
   KEY AK_NAME_FLOWDEFINE (NAME_, FLOWDEFINE_)
);

/*==============================================================*/
/* Index: IDX_ACTION_EVENT                                      */
/*==============================================================*/
CREATE INDEX IDX_ACTION_EVENT ON WF_ACTION
(
   EVENT_
);

/*==============================================================*/
/* Index: IDX_ACTION_FLOWDEF                                    */
/*==============================================================*/
CREATE INDEX IDX_ACTION_FLOWDEF ON WF_ACTION
(
   FLOWDEFINE_
);

/*==============================================================*/
/* Table: WF_ASYNJOB                                            */
/*==============================================================*/
CREATE TABLE WF_ASYNJOB
(
   ID_                  VARCHAR(36) NOT NULL,
   JOBTYPE_             VARCHAR(20),
   VERSION_             INT,
   DUEDATE_             DATE,
   FLOWINSTANCE_        VARCHAR(36),
   FLOWTOKEN_           VARCHAR(36),
   ISSUSPENDED_         SMALLINT,
   LOCKOWNER_           VARCHAR(20),
   LOCKTIME_            DATE,
   EXCEPTION_           TEXT,
   RETRIES_             INT,
   NODE_                VARCHAR(36),
   ACTION_              VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_FLOWINST                                  */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_FLOWINST ON WF_ASYNJOB
(
   FLOWINSTANCE_
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_FLOWTOKEN                                 */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_FLOWTOKEN ON WF_ASYNJOB
(
   FLOWTOKEN_
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_NODE                                      */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_NODE ON WF_ASYNJOB
(
   NODE_
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_ACTION                                    */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_ACTION ON WF_ASYNJOB
(
   ACTION_
);

/*==============================================================*/
/* Table: WF_CONTEXTVARIABLE                                    */
/*==============================================================*/
CREATE TABLE WF_CONTEXTVARIABLE
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWINSTANCE_        VARCHAR(36),
   NAME_                VARCHAR(255),
   DISPLAYNAME_         VARCHAR(255),
   TYPE_                VARCHAR(20),
   VALUE_               TEXT,
   DISPLAYORDER_        INT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_CTXVAR_FLOWINSTANCE                               */
/*==============================================================*/
CREATE INDEX IDX_CTXVAR_FLOWINSTANCE ON WF_CONTEXTVARIABLE
(
   FLOWINSTANCE_
);

/*==============================================================*/
/* Table: WF_DELEGATION                                         */
/*==============================================================*/
CREATE TABLE WF_DELEGATION
(
   ID_                  VARCHAR(36) NOT NULL,
   EXPRESSION_          TEXT,
   CLASSNAME_           VARCHAR(255),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: WF_EVENT                                              */
/*==============================================================*/
CREATE TABLE WF_EVENT
(
   ID_                  VARCHAR(36) NOT NULL,
   EVENTTYPE_           VARCHAR(20),
   FLOWELEMENT_         VARCHAR(36),
   ELEMENTTYPE_         VARCHAR(20),
   NODE_                VARCHAR(36),
   TASKDEFINE_          VARCHAR(36),
   TRANSITION_          VARCHAR(36),
   FLOWDEFINE_          VARCHAR(36),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_EVENT_FLOWDEFINE                                  */
/*==============================================================*/
CREATE INDEX IDX_EVENT_FLOWDEFINE ON WF_EVENT
(
   FLOWDEFINE_
);

/*==============================================================*/
/* Index: IDX_EVENT_NODE                                        */
/*==============================================================*/
CREATE INDEX IDX_EVENT_NODE ON WF_EVENT
(
   NODE_
);

/*==============================================================*/
/* Index: IDX_EVENT_TRANSITION                                  */
/*==============================================================*/
CREATE INDEX IDX_EVENT_TRANSITION ON WF_EVENT
(
   TRANSITION_
);

/*==============================================================*/
/* Index: IDX_EVENT_TASKDEFINE                                  */
/*==============================================================*/
CREATE INDEX IDX_EVENT_TASKDEFINE ON WF_EVENT
(
   TASKDEFINE_
);

/*==============================================================*/
/* Table: WF_FLOWDEFINE                                         */
/*==============================================================*/
CREATE TABLE WF_FLOWDEFINE
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWKEY_             VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(255) NOT NULL,
   VERSION_             NUMERIC(14,0) NOT NULL,
   STATE_               VARCHAR(20) NOT NULL,
   CATEGORY_            VARCHAR(20),
   DESCRIPTION_         TEXT,
   STARTNODE_           VARCHAR(36),
   XMLDEFINE_           LONGTEXT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_FLOWDEFINE_FLOWKEY                                */
/*==============================================================*/
CREATE INDEX IDX_FLOWDEFINE_FLOWKEY ON WF_FLOWDEFINE
(
   FLOWKEY_
);

/*==============================================================*/
/* Index: IDX_FLOWDEFINE_VERSION                                */
/*==============================================================*/
CREATE INDEX IDX_FLOWDEFINE_VERSION ON WF_FLOWDEFINE
(
   VERSION_
);

/*==============================================================*/
/* Index: IDX_FLOWDEFINE_STARTNODE                              */
/*==============================================================*/
CREATE INDEX IDX_FLOWDEFINE_STARTNODE ON WF_FLOWDEFINE
(
   STARTNODE_
);

/*==============================================================*/
/* Table: WF_FLOWDRAFT                                          */
/*==============================================================*/
CREATE TABLE WF_FLOWDRAFT
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWKEY_             VARCHAR(36) NOT NULL,
   VERSION_             NUMERIC(14,0) NOT NULL,
   NEWVERSION_          NUMERIC(14,0) NOT NULL,
   XMLDEFINE_           LONGTEXT,
   NAME_                VARCHAR(255),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: WF_FLOWINSTANCE                                       */
/*==============================================================*/
CREATE TABLE WF_FLOWINSTANCE
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWDEFINE_          VARCHAR(36),
   FLOWTOKEN_           VARCHAR(36),
   FLOWKEY_             VARCHAR(36),
   VERSION_             NUMERIC(14,0),
   FLOWNAME_            VARCHAR(255) NOT NULL,
   CREATETIME_          DATE NOT NULL,
   ENDTIME_             DATE,
   LOCK_                SMALLINT,
   SUPERFLOWTOKEN_      VARCHAR(36),
   ISSUSPENDED_         SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_FLOWINST_TOKEN                                    */
/*==============================================================*/
CREATE INDEX IDX_FLOWINST_TOKEN ON WF_FLOWINSTANCE
(
   FLOWTOKEN_
);

/*==============================================================*/
/* Index: IDX_FLOWINST_FLOWDEF                                  */
/*==============================================================*/
CREATE INDEX IDX_FLOWINST_FLOWDEF ON WF_FLOWINSTANCE
(
   FLOWDEFINE_
);

/*==============================================================*/
/* Index: IDX_FLOWINST_SUPERTOKEN                               */
/*==============================================================*/
CREATE INDEX IDX_FLOWINST_SUPERTOKEN ON WF_FLOWINSTANCE
(
   SUPERFLOWTOKEN_
);

/*==============================================================*/
/* Table: WF_FLOWTOKEN                                          */
/*==============================================================*/
CREATE TABLE WF_FLOWTOKEN
(
   ID_                  VARCHAR(36) NOT NULL,
   VERSION_             INT,
   NAME_                VARCHAR(255),
   PARENT_              VARCHAR(36),
   NODE_                VARCHAR(36),
   FLOWINSTANCE_        VARCHAR(36),
   START_               DATE,
   END_                 DATE,
   SUBFLOWINSTANCE_     VARCHAR(36),
   LOCK_                VARCHAR(255),
   NEXTLOGINDEX_        INT,
   ISABLETOREACTIVATEPARENT_ SMALLINT,
   ISSUSPENDED_         SMALLINT,
   ISREJECTED_          SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TOKEN_PARENT                                      */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_PARENT ON WF_FLOWTOKEN
(
   PARENT_
);

/*==============================================================*/
/* Index: IDX_TOKEN_FLOWINSTANCE                                */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_FLOWINSTANCE ON WF_FLOWTOKEN
(
   FLOWINSTANCE_
);

/*==============================================================*/
/* Index: IDX_TOKEN_NODE                                        */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_NODE ON WF_FLOWTOKEN
(
   NODE_
);

/*==============================================================*/
/* Index: IDX_TOKEN_SUBFLOWINSTANCE                             */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_SUBFLOWINSTANCE ON WF_FLOWTOKEN
(
   SUBFLOWINSTANCE_
);

/*==============================================================*/
/* Table: WF_NODE                                               */
/*==============================================================*/
CREATE TABLE WF_NODE
(
   ID_                  VARCHAR(36) NOT NULL,
   ACTION_              VARCHAR(36),
   NAME_                VARCHAR(255) NOT NULL,
   FUNCTYPE_            VARCHAR(20),
   ISASYNC_             SMALLINT,
   LISTORDER_           INT,
   SIGNALTYPE_          INT,
   FLOWDEFINE_          VARCHAR(36),
   DESCRIPTION_         TEXT,
   DECISIONDELEGATION_  VARCHAR(36),
   SUBFLOWNAME_         VARCHAR(255),
   SUBFLOWDEFINE_       VARCHAR(36),
   ENDFLOW_             SMALLINT,
   AUTOCREATETASKS_     SMALLINT,
   AUTOENDTASKS_        SMALLINT,
   REFGRAPHKEY_         VARCHAR(100),
   PRIMARY KEY (ID_),
   KEY AK_NATIVEID (NAME_, FLOWDEFINE_)
);

/*==============================================================*/
/* Index: IDX_NODE_FLOWDEFINE                                   */
/*==============================================================*/
CREATE INDEX IDX_NODE_FLOWDEFINE ON WF_NODE
(
   FLOWDEFINE_
);

/*==============================================================*/
/* Index: IDX_NODE_ACTION                                       */
/*==============================================================*/
CREATE INDEX IDX_NODE_ACTION ON WF_NODE
(
   ACTION_
);

/*==============================================================*/
/* Index: IDX_NODE_SUBFLOWDEFINE                                */
/*==============================================================*/
CREATE INDEX IDX_NODE_SUBFLOWDEFINE ON WF_NODE
(
   SUBFLOWDEFINE_
);

/*==============================================================*/
/* Table: WF_NODECONDITIONS                                     */
/*==============================================================*/
CREATE TABLE WF_NODECONDITIONS
(
   NODE_                VARCHAR(36) NOT NULL,
   INDEX_               INT NOT NULL,
   EXPRESSION_          TEXT,
   TRANSITIONNAME_      VARCHAR(255),
   PRIMARY KEY (NODE_, INDEX_)
);

ALTER TABLE WF_NODECONDITIONS COMMENT '判断节点与分支节点的条件';

/*==============================================================*/
/* Table: WF_POOLEDACTOR                                        */
/*==============================================================*/
CREATE TABLE WF_POOLEDACTOR
(
   ID_                  VARCHAR(36) NOT NULL,
   TASKINSTANCE_        VARCHAR(36),
   ACTOR_               VARCHAR(255) NOT NULL,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_POOLEDACTOR_TASKINST                              */
/*==============================================================*/
CREATE INDEX IDX_POOLEDACTOR_TASKINST ON WF_POOLEDACTOR
(
   TASKINSTANCE_
);

/*==============================================================*/
/* Table: WF_POOLEDROLE                                         */
/*==============================================================*/
CREATE TABLE WF_POOLEDROLE
(
   ID_                  VARCHAR(36) NOT NULL,
   TASKINSTANCE_        VARCHAR(36),
   ROLE_                VARCHAR(255) NOT NULL,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_POOLEDROLE_TASKINST                               */
/*==============================================================*/
CREATE INDEX IDX_POOLEDROLE_TASKINST ON WF_POOLEDROLE
(
   TASKINSTANCE_
);

/*==============================================================*/
/* Table: WF_TASKDEFINE                                         */
/*==============================================================*/
CREATE TABLE WF_TASKDEFINE
(
   ID_                  VARCHAR(36) NOT NULL,
   TASKNODE_            VARCHAR(36),
   TASKASSIGNMENT_      VARCHAR(36),
   TASKNAME_            VARCHAR(255) NOT NULL,
   EXECUTEORDER_        INT,
   PRIORITY_            INT,
   DUEDATE_             DATE,
   TASKVIEW_            VARCHAR(36),
   FLOWDEFINE_          VARCHAR(36),
   ISBLOCKING_          SMALLINT,
   ISSIGNALLING_        SMALLINT,
   DESCRIPTION_         TEXT,
   FUNCTYPE_            VARCHAR(20),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TASKDEF_NODE                                      */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_NODE ON WF_TASKDEFINE
(
   TASKNODE_
);

/*==============================================================*/
/* Index: IDX_TASKDEF_FLOWDEF                                   */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_FLOWDEF ON WF_TASKDEFINE
(
   FLOWDEFINE_
);

/*==============================================================*/
/* Index: IDX_TASKDEF_TASKASSN                                  */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_TASKASSN ON WF_TASKDEFINE
(
   TASKASSIGNMENT_
);

/*==============================================================*/
/* Index: IDX_TASKDEF_TASKVIEW                                  */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_TASKVIEW ON WF_TASKDEFINE
(
   TASKVIEW_
);

/*==============================================================*/
/* Table: WF_TASKINSTANCE                                       */
/*==============================================================*/
CREATE TABLE WF_TASKINSTANCE
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWTOKEN_           VARCHAR(36),
   TASKDEFINE_          VARCHAR(36),
   FLOWINSTANCE_        VARCHAR(36),
   TASKNAME_            VARCHAR(255) NOT NULL,
   PROCESSSTATE_        VARCHAR(20),
   ACTORID_             VARCHAR(255),
   EXECUTEORDER_        INT,
   PRIORITY_            INT,
   DESCRIPTION_         TEXT,
   CREATETIME_          DATE,
   STARTTIME_           DATE,
   ENDTIME_             DATE,
   DUETIME_             DATE,
   COMMENT_             TEXT,
   ISSIGNALLING_        SMALLINT NOT NULL,
   ISBLOCKING_          SMALLINT NOT NULL,
   FUNCTYPE_            VARCHAR(20),
   VERSION_             INT,
   ISCANCELLED_         SMALLINT,
   ISSUSPENDED_         SMALLINT,
   ISOPEN_              SMALLINT,
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TASKINST_TOKEN                                    */
/*==============================================================*/
CREATE INDEX IDX_TASKINST_TOKEN ON WF_TASKINSTANCE
(
   FLOWTOKEN_
);

/*==============================================================*/
/* Index: IDX_TASKINST_TASKDEF                                  */
/*==============================================================*/
CREATE INDEX IDX_TASKINST_TASKDEF ON WF_TASKINSTANCE
(
   TASKDEFINE_
);

/*==============================================================*/
/* Index: IDX_TASKINST_FLOWINST                                 */
/*==============================================================*/
CREATE INDEX IDX_TASKINST_FLOWINST ON WF_TASKINSTANCE
(
   FLOWINSTANCE_
);

/*==============================================================*/
/* Table: WF_TRACEPOINT                                         */
/*==============================================================*/
CREATE TABLE WF_TRACEPOINT
(
   ID_                  VARCHAR(36) NOT NULL,
   FLOWDEFINE_          VARCHAR(36),
   FLOWINSTANCE_        VARCHAR(36),
   ELEMENT_             VARCHAR(36),
   ELEMENTTYPE_         VARCHAR(50) COMMENT 'NODE/TRANSITION',
   ACTIONTYPE_          VARCHAR(50) COMMENT 'ENTER/LEAVE',
   REFGRAPHKEY_         VARCHAR(100),
   SNAPTIME_            DATE NOT NULL,
   PRIMARY KEY (ID_)
);

ALTER TABLE WF_TRACEPOINT COMMENT '记录流程轨迹';

/*==============================================================*/
/* Table: WF_TRANSITION                                         */
/*==============================================================*/
CREATE TABLE WF_TRANSITION
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(255),
   FROMNODE_            VARCHAR(36),
   TONODE_              VARCHAR(36),
   FROMINDEX_           INT,
   CONDITIONS_          TEXT,
   DESCRIPTION_         TEXT,
   FLOWDEFINE_          VARCHAR(36),
   ISREJECTED_          SMALLINT,
   REFGRAPHKEY_         VARCHAR(100),
   PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TRANSITION_FROM                                   */
/*==============================================================*/
CREATE INDEX IDX_TRANSITION_FROM ON WF_TRANSITION
(
   FROMNODE_
);

/*==============================================================*/
/* Index: IDX_TRANSITION_TO                                     */
/*==============================================================*/
CREATE INDEX IDX_TRANSITION_TO ON WF_TRANSITION
(
   TONODE_
);

/*==============================================================*/
/* Index: IDX_TRANSITION_FLOWDEF                                */
/*==============================================================*/
CREATE INDEX IDX_TRANSITION_FLOWDEF ON WF_TRANSITION
(
   FLOWDEFINE_
);

/*==============================================================*/
/* Table: WF_VARIABLEDECLARE                                    */
/*==============================================================*/
CREATE TABLE WF_VARIABLEDECLARE
(
   ID_                  VARCHAR(36) NOT NULL,
   NAME_                VARCHAR(255),
   DISPLAYNAME_         VARCHAR(255),
   TYPE_                VARCHAR(36) NOT NULL,
   NOTNULL_             SMALLINT,
   FLOWDEFINE_          VARCHAR(36),
   PRIMARY KEY (ID_)
);

ALTER TABLE WF_VARIABLEDECLARE COMMENT '表达式中的变量要事先申明';

ALTER TABLE WF_ACTION ADD CONSTRAINT FK_ACTION_EVENT FOREIGN KEY (EVENT_)
      REFERENCES WF_EVENT (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_ACTION ADD CONSTRAINT FK_ACTION_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_ASYNJOB ADD CONSTRAINT FK_ASYNJOB_ACTION FOREIGN KEY (ACTION_)
      REFERENCES WF_ACTION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_ASYNJOB ADD CONSTRAINT FK_ASYNJOB_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_ASYNJOB ADD CONSTRAINT FK_ASYNJOB_FLOWTOKE FOREIGN KEY (FLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_ASYNJOB ADD CONSTRAINT FK_ASYNJOB_NODE FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_CONTEXTVARIABLE ADD CONSTRAINT FK_CTXVAR_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_EVENT ADD CONSTRAINT FK_EVENT_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_EVENT ADD CONSTRAINT FK_EVENT_NODE FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_EVENT ADD CONSTRAINT FK_EVENT_TASKDEF FOREIGN KEY (TASKDEFINE_)
      REFERENCES WF_TASKDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_EVENT ADD CONSTRAINT FK_EVENT_TRANSITION FOREIGN KEY (TRANSITION_)
      REFERENCES WF_TRANSITION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWDEFINE ADD CONSTRAINT FK_FLOW_STARTNODE FOREIGN KEY (STARTNODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWINSTANCE ADD CONSTRAINT FK_FLOWINST_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWINSTANCE ADD CONSTRAINT FK_FLOWINST_SUPERTOKEN FOREIGN KEY (SUPERFLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWINSTANCE ADD CONSTRAINT FK_FLOWINST_TOKEN FOREIGN KEY (FLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWTOKEN ADD CONSTRAINT FK_TOKEN_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWTOKEN ADD CONSTRAINT FK_TOKEN_NODE FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWTOKEN ADD CONSTRAINT FK_TOKEN_PARENT FOREIGN KEY (PARENT_)
      REFERENCES WF_FLOWTOKEN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_FLOWTOKEN ADD CONSTRAINT FK_TOKEN_SUBFLOWINST FOREIGN KEY (SUBFLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_NODE ADD CONSTRAINT FK_NODE_ACTION FOREIGN KEY (ACTION_)
      REFERENCES WF_ACTION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_NODE ADD CONSTRAINT FK_NODE_DELEGATION FOREIGN KEY (DECISIONDELEGATION_)
      REFERENCES WF_DELEGATION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_NODE ADD CONSTRAINT FK_NODE_FLOWDEFINE FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_NODE ADD CONSTRAINT FK_NODE_SUBFLOWDEFINE FOREIGN KEY (SUBFLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_NODECONDITIONS ADD CONSTRAINT FK_NODE_CONDITIONS FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_POOLEDACTOR ADD CONSTRAINT FK_POOLEDACTORS_TASKINST FOREIGN KEY (TASKINSTANCE_)
      REFERENCES WF_TASKINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_POOLEDROLE ADD CONSTRAINT FK_POOLEDROLE_TASKINST FOREIGN KEY (TASKINSTANCE_)
      REFERENCES WF_TASKINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKDEFINE ADD CONSTRAINT FK_TASKDEF_ASSIGNMENT FOREIGN KEY (TASKASSIGNMENT_)
      REFERENCES WF_DELEGATION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKDEFINE ADD CONSTRAINT FK_TASKDEF_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKDEFINE ADD CONSTRAINT FK_TASKDEF_NODE FOREIGN KEY (TASKNODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKDEFINE ADD CONSTRAINT FK_TASKDEF_TASKVIEW FOREIGN KEY (TASKVIEW_)
      REFERENCES WF_DELEGATION (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKINSTANCE ADD CONSTRAINT FK_TASKINST_FLOWTOKE FOREIGN KEY (FLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKINSTANCE ADD CONSTRAINT FK_TASKINST_TASKDEFI FOREIGN KEY (TASKDEFINE_)
      REFERENCES WF_TASKDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TASKINSTANCE ADD CONSTRAINT FK_TASKINST_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TRANSITION ADD CONSTRAINT FK_TRANSITION_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TRANSITION ADD CONSTRAINT FK_TRANSITION_FROMNODE FOREIGN KEY (FROMNODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_TRANSITION ADD CONSTRAINT FK_TRANSITION_TONODE FOREIGN KEY (TONODE_)
      REFERENCES WF_NODE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE WF_VARIABLEDECLARE ADD CONSTRAINT FK_VARIABLE_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_) ON DELETE RESTRICT ON UPDATE RESTRICT;

