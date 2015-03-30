/*==============================================================*/
/* DBMS name:      ORACLE Version 10gR2                         */
/* Created on:     2015/1/4 18:29:02                            */
/*==============================================================*/


ALTER TABLE WF_ACTION
   DROP CONSTRAINT FK_ACTION_EVENT;

ALTER TABLE WF_ACTION
   DROP CONSTRAINT FK_ACTION_FLOWDEF;

ALTER TABLE WF_ASYNJOB
   DROP CONSTRAINT FK_ASYNJOB_ACTION;

ALTER TABLE WF_ASYNJOB
   DROP CONSTRAINT FK_ASYNJOB_FLOWINST;

ALTER TABLE WF_ASYNJOB
   DROP CONSTRAINT FK_ASYNJOB_FLOWTOKE;

ALTER TABLE WF_ASYNJOB
   DROP CONSTRAINT FK_ASYNJOB_NODE;

ALTER TABLE WF_CONTEXTVARIABLE
   DROP CONSTRAINT FK_CTXVAR_FLOWINST;

ALTER TABLE WF_EVENT
   DROP CONSTRAINT FK_EVENT_FLOWDEF;

ALTER TABLE WF_EVENT
   DROP CONSTRAINT FK_EVENT_NODE;

ALTER TABLE WF_EVENT
   DROP CONSTRAINT FK_EVENT_TASKDEF;

ALTER TABLE WF_EVENT
   DROP CONSTRAINT FK_EVENT_TRANSITION;

ALTER TABLE WF_FLOWDEFINE
   DROP CONSTRAINT FK_FLOW_STARTNODE;

ALTER TABLE WF_FLOWINSTANCE
   DROP CONSTRAINT FK_FLOWINST_FLOWDEF;

ALTER TABLE WF_FLOWINSTANCE
   DROP CONSTRAINT FK_FLOWINST_SUPERTOKEN;

ALTER TABLE WF_FLOWINSTANCE
   DROP CONSTRAINT FK_FLOWINST_TOKEN;

ALTER TABLE WF_FLOWTOKEN
   DROP CONSTRAINT FK_TOKEN_FLOWINST;

ALTER TABLE WF_FLOWTOKEN
   DROP CONSTRAINT FK_TOKEN_NODE;

ALTER TABLE WF_FLOWTOKEN
   DROP CONSTRAINT FK_TOKEN_PARENT;

ALTER TABLE WF_FLOWTOKEN
   DROP CONSTRAINT FK_TOKEN_SUBFLOWINST;

ALTER TABLE WF_NODE
   DROP CONSTRAINT FK_NODE_ACTION;

ALTER TABLE WF_NODE
   DROP CONSTRAINT FK_NODE_DELEGATION;

ALTER TABLE WF_NODE
   DROP CONSTRAINT FK_NODE_FLOWDEFINE;

ALTER TABLE WF_NODE
   DROP CONSTRAINT FK_NODE_SUBFLOWDEFINE;

ALTER TABLE WF_NODECONDITIONS
   DROP CONSTRAINT FK_NODE_CONDITIONS;

ALTER TABLE WF_POOLEDACTOR
   DROP CONSTRAINT FK_POOLEDACTORS_TASKINST;

ALTER TABLE WF_POOLEDROLE
   DROP CONSTRAINT FK_POOLEDROLE_TASKINST;

ALTER TABLE WF_TASKDEFINE
   DROP CONSTRAINT FK_TASKDEF_ASSIGNMENT;

ALTER TABLE WF_TASKDEFINE
   DROP CONSTRAINT FK_TASKDEF_FLOWDEF;

ALTER TABLE WF_TASKDEFINE
   DROP CONSTRAINT FK_TASKDEF_NODE;

ALTER TABLE WF_TASKDEFINE
   DROP CONSTRAINT FK_TASKDEF_TASKVIEW;

ALTER TABLE WF_TASKINSTANCE
   DROP CONSTRAINT FK_TASKINST_FLOWTOKE;

ALTER TABLE WF_TASKINSTANCE
   DROP CONSTRAINT FK_TASKINST_TASKDEFI;

ALTER TABLE WF_TASKINSTANCE
   DROP CONSTRAINT FK_TASKINST_FLOWINST;

ALTER TABLE WF_TRANSITION
   DROP CONSTRAINT FK_TRANSITION_FLOWDEF;

ALTER TABLE WF_TRANSITION
   DROP CONSTRAINT FK_TRANSITION_FROMNODE;

ALTER TABLE WF_TRANSITION
   DROP CONSTRAINT FK_TRANSITION_TONODE;

ALTER TABLE WF_VARIABLEDECLARE
   DROP CONSTRAINT FK_VARIABLE_FLOWDEF;

DROP INDEX IDX_ACTION_FLOWDEF;

DROP INDEX IDX_ACTION_EVENT;

DROP TABLE WF_ACTION CASCADE CONSTRAINTS;

DROP INDEX IDX_ASYNJOB_ACTION;

DROP INDEX IDX_ASYNJOB_NODE;

DROP INDEX IDX_ASYNJOB_FLOWTOKEN;

DROP INDEX IDX_ASYNJOB_FLOWINST;

DROP TABLE WF_ASYNJOB CASCADE CONSTRAINTS;

DROP INDEX IDX_CTXVAR_FLOWINSTANCE;

DROP TABLE WF_CONTEXTVARIABLE CASCADE CONSTRAINTS;

DROP TABLE WF_DELEGATION CASCADE CONSTRAINTS;

DROP INDEX IDX_EVENT_TASKDEFINE;

DROP INDEX IDX_EVENT_TRANSITION;

DROP INDEX IDX_EVENT_NODE;

DROP INDEX IDX_EVENT_FLOWDEFINE;

DROP TABLE WF_EVENT CASCADE CONSTRAINTS;

DROP INDEX IDX_FLOWDEFINE_STARTNODE;

DROP INDEX IDX_FLOWDEFINE_VERSION;

DROP INDEX IDX_FLOWDEFINE_FLOWKEY;

DROP TABLE WF_FLOWDEFINE CASCADE CONSTRAINTS;

DROP TABLE WF_FLOWDRAFT CASCADE CONSTRAINTS;

DROP INDEX IDX_FLOWINST_SUPERTOKEN;

DROP INDEX IDX_FLOWINST_FLOWDEF;

DROP INDEX IDX_FLOWINST_TOKEN;

DROP TABLE WF_FLOWINSTANCE CASCADE CONSTRAINTS;

DROP INDEX IDX_TOKEN_SUBFLOWINSTANCE;

DROP INDEX IDX_TOKEN_NODE;

DROP INDEX IDX_TOKEN_FLOWINSTANCE;

DROP INDEX IDX_TOKEN_PARENT;

DROP TABLE WF_FLOWTOKEN CASCADE CONSTRAINTS;

DROP INDEX IDX_NODE_SUBFLOWDEFINE;

DROP INDEX IDX_NODE_ACTION;

DROP INDEX IDX_NODE_FLOWDEFINE;

DROP TABLE WF_NODE CASCADE CONSTRAINTS;

DROP TABLE WF_NODECONDITIONS CASCADE CONSTRAINTS;

DROP INDEX IDX_POOLEDACTOR_TASKINST;

DROP TABLE WF_POOLEDACTOR CASCADE CONSTRAINTS;

DROP INDEX IDX_POOLEDROLE_TASKINST;

DROP TABLE WF_POOLEDROLE CASCADE CONSTRAINTS;

DROP INDEX IDX_TASKDEF_TASKVIEW;

DROP INDEX IDX_TASKDEF_TASKASSN;

DROP INDEX IDX_TASKDEF_FLOWDEF;

DROP INDEX IDX_TASKDEF_NODE;

DROP TABLE WF_TASKDEFINE CASCADE CONSTRAINTS;

DROP INDEX IDX_TASKINST_FLOWINST;

DROP INDEX IDX_TASKINST_TASKDEF;

DROP INDEX IDX_TASKINST_TOKEN;

DROP TABLE WF_TASKINSTANCE CASCADE CONSTRAINTS;

DROP TABLE WF_TRACEPOINT CASCADE CONSTRAINTS;

DROP INDEX IDX_TRANSITION_FLOWDEF;

DROP INDEX IDX_TRANSITION_TO;

DROP INDEX IDX_TRANSITION_FROM;

DROP TABLE WF_TRANSITION CASCADE CONSTRAINTS;

DROP TABLE WF_VARIABLEDECLARE CASCADE CONSTRAINTS;

/*==============================================================*/
/* Table: WF_ACTION                                             */
/*==============================================================*/
CREATE TABLE WF_ACTION  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   NAME_                VARCHAR2(400)                   NOT NULL,
   ACTIONHANDLER_       VARCHAR2(400),
   ACTIONEXPRESSION_    VARCHAR2(4000),
   EVENT_               VARCHAR2(36),
   EVENTINDEX_          NUMBER,
   ISASYNC_             NUMBER,
   ACCEPTPROPAGATIONEVENT_ NUMBER,
   FLOWDEFINE_          VARCHAR2(36),
   CONSTRAINT PK_WF_ACTION PRIMARY KEY (ID_),
   CONSTRAINT AK_NAME_FLOWDEFINE_WF_ACTIO UNIQUE (NAME_, FLOWDEFINE_)
);

/*==============================================================*/
/* Index: IDX_ACTION_EVENT                                      */
/*==============================================================*/
CREATE INDEX IDX_ACTION_EVENT ON WF_ACTION (
   EVENT_ ASC
);

/*==============================================================*/
/* Index: IDX_ACTION_FLOWDEF                                    */
/*==============================================================*/
CREATE INDEX IDX_ACTION_FLOWDEF ON WF_ACTION (
   FLOWDEFINE_ ASC
);

/*==============================================================*/
/* Table: WF_ASYNJOB                                            */
/*==============================================================*/
CREATE TABLE WF_ASYNJOB  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   JOBTYPE_             VARCHAR2(20),
   VERSION_             NUMBER,
   DUEDATE_             DATE,
   FLOWINSTANCE_        VARCHAR2(36),
   FLOWTOKEN_           VARCHAR2(36),
   ISSUSPENDED_         NUMBER,
   LOCKOWNER_           VARCHAR2(20),
   LOCKTIME_            DATE,
   EXCEPTION_           CLOB,
   RETRIES_             NUMBER,
   NODE_                VARCHAR2(36),
   ACTION_              VARCHAR2(36),
   CONSTRAINT PK_WF_ASYNJOB PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_FLOWINST                                  */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_FLOWINST ON WF_ASYNJOB (
   FLOWINSTANCE_ ASC
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_FLOWTOKEN                                 */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_FLOWTOKEN ON WF_ASYNJOB (
   FLOWTOKEN_ ASC
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_NODE                                      */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_NODE ON WF_ASYNJOB (
   NODE_ ASC
);

/*==============================================================*/
/* Index: IDX_ASYNJOB_ACTION                                    */
/*==============================================================*/
CREATE INDEX IDX_ASYNJOB_ACTION ON WF_ASYNJOB (
   ACTION_ ASC
);

/*==============================================================*/
/* Table: WF_CONTEXTVARIABLE                                    */
/*==============================================================*/
CREATE TABLE WF_CONTEXTVARIABLE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   FLOWINSTANCE_        VARCHAR2(36),
   NAME_                VARCHAR2(400),
   DISPLAYNAME_         VARCHAR2(400),
   TYPE_                VARCHAR2(20),
   VALUE_               VARCHAR2(4000),
   DISPLAYORDER_        NUMBER,
   CONSTRAINT PK_WF_CONTEXTVARIABLE PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_CTXVAR_FLOWINSTANCE                               */
/*==============================================================*/
CREATE INDEX IDX_CTXVAR_FLOWINSTANCE ON WF_CONTEXTVARIABLE (
   FLOWINSTANCE_ ASC
);

/*==============================================================*/
/* Table: WF_DELEGATION                                         */
/*==============================================================*/
CREATE TABLE WF_DELEGATION  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   EXPRESSION_          VARCHAR2(4000),
   CLASSNAME_           VARCHAR2(400),
   CONSTRAINT PK_WF_DELEGATION PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: WF_EVENT                                              */
/*==============================================================*/
CREATE TABLE WF_EVENT  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   EVENTTYPE_           VARCHAR2(20),
   FLOWELEMENT_         VARCHAR2(36),
   ELEMENTTYPE_         VARCHAR2(20),
   NODE_                VARCHAR2(36),
   TASKDEFINE_          VARCHAR2(36),
   TRANSITION_          VARCHAR2(36),
   FLOWDEFINE_          VARCHAR2(36),
   CONSTRAINT PK_WF_EVENT PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_EVENT_FLOWDEFINE                                  */
/*==============================================================*/
CREATE INDEX IDX_EVENT_FLOWDEFINE ON WF_EVENT (
   FLOWDEFINE_ ASC
);

/*==============================================================*/
/* Index: IDX_EVENT_NODE                                        */
/*==============================================================*/
CREATE INDEX IDX_EVENT_NODE ON WF_EVENT (
   NODE_ ASC
);

/*==============================================================*/
/* Index: IDX_EVENT_TRANSITION                                  */
/*==============================================================*/
CREATE INDEX IDX_EVENT_TRANSITION ON WF_EVENT (
   TRANSITION_ ASC
);

/*==============================================================*/
/* Index: IDX_EVENT_TASKDEFINE                                  */
/*==============================================================*/
CREATE INDEX IDX_EVENT_TASKDEFINE ON WF_EVENT (
   TASKDEFINE_ ASC
);

/*==============================================================*/
/* Table: WF_FLOWDEFINE                                         */
/*==============================================================*/
CREATE TABLE WF_FLOWDEFINE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   FLOWKEY_             VARCHAR2(36)                    NOT NULL,
   NAME_                VARCHAR2(400)                   NOT NULL,
   VERSION_             NUMBER(14)                      NOT NULL,
   STATE_               VARCHAR2(20)                    NOT NULL,
   CATEGORY_            VARCHAR2(20),
   DESCRIPTION_         VARCHAR2(1000),
   STARTNODE_           VARCHAR2(36),
   XMLDEFINE_           CLOB,
   CONSTRAINT PK_WF_FLOWDEFINE PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_FLOWDEFINE_FLOWKEY                                */
/*==============================================================*/
CREATE INDEX IDX_FLOWDEFINE_FLOWKEY ON WF_FLOWDEFINE (
   FLOWKEY_ ASC
);

/*==============================================================*/
/* Index: IDX_FLOWDEFINE_VERSION                                */
/*==============================================================*/
CREATE INDEX IDX_FLOWDEFINE_VERSION ON WF_FLOWDEFINE (
   VERSION_ ASC
);

/*==============================================================*/
/* Index: IDX_FLOWDEFINE_STARTNODE                              */
/*==============================================================*/
CREATE INDEX IDX_FLOWDEFINE_STARTNODE ON WF_FLOWDEFINE (
   STARTNODE_ ASC
);

/*==============================================================*/
/* Table: WF_FLOWDRAFT                                          */
/*==============================================================*/
CREATE TABLE WF_FLOWDRAFT  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   FLOWKEY_             VARCHAR2(36)                    NOT NULL,
   VERSION_             NUMBER(14)                      NOT NULL,
   NEWVERSION_          NUMBER(14)                      NOT NULL,
   XMLDEFINE_           CLOB,
   NAME_                VARCHAR2(400),
   CONSTRAINT PK_WF_FLOWDRAFT PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: WF_FLOWINSTANCE                                       */
/*==============================================================*/
CREATE TABLE WF_FLOWINSTANCE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   FLOWDEFINE_          VARCHAR2(36),
   FLOWTOKEN_           VARCHAR2(36),
   FLOWKEY_             VARCHAR2(36),
   VERSION_             NUMBER(14),
   FLOWNAME_            VARCHAR2(400)                   NOT NULL,
   CREATETIME_          DATE                            NOT NULL,
   ENDTIME_             DATE,
   LOCK_                NUMBER,
   SUPERFLOWTOKEN_      VARCHAR2(36),
   ISSUSPENDED_         NUMBER,
   CONSTRAINT PK_WF_FLOWINSTANCE PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_FLOWINST_TOKEN                                    */
/*==============================================================*/
CREATE INDEX IDX_FLOWINST_TOKEN ON WF_FLOWINSTANCE (
   FLOWTOKEN_ ASC
);

/*==============================================================*/
/* Index: IDX_FLOWINST_FLOWDEF                                  */
/*==============================================================*/
CREATE INDEX IDX_FLOWINST_FLOWDEF ON WF_FLOWINSTANCE (
   FLOWDEFINE_ ASC
);

/*==============================================================*/
/* Index: IDX_FLOWINST_SUPERTOKEN                               */
/*==============================================================*/
CREATE INDEX IDX_FLOWINST_SUPERTOKEN ON WF_FLOWINSTANCE (
   SUPERFLOWTOKEN_ ASC
);

/*==============================================================*/
/* Table: WF_FLOWTOKEN                                          */
/*==============================================================*/
CREATE TABLE WF_FLOWTOKEN  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   VERSION_             NUMBER,
   NAME_                VARCHAR2(400),
   PARENT_              VARCHAR2(36),
   NODE_                VARCHAR2(36),
   FLOWINSTANCE_        VARCHAR2(36),
   START_               DATE,
   END_                 DATE,
   SUBFLOWINSTANCE_     VARCHAR2(36),
   LOCK_                VARCHAR2(255),
   NEXTLOGINDEX_        NUMBER,
   ISABLETOREACTIVATEPARENT_ NUMBER,
   ISSUSPENDED_         NUMBER,
   ISREJECTED_          NUMBER,
   CONSTRAINT PK_WF_FLOWTOKEN PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TOKEN_PARENT                                      */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_PARENT ON WF_FLOWTOKEN (
   PARENT_ ASC
);

/*==============================================================*/
/* Index: IDX_TOKEN_FLOWINSTANCE                                */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_FLOWINSTANCE ON WF_FLOWTOKEN (
   FLOWINSTANCE_ ASC
);

/*==============================================================*/
/* Index: IDX_TOKEN_NODE                                        */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_NODE ON WF_FLOWTOKEN (
   NODE_ ASC
);

/*==============================================================*/
/* Index: IDX_TOKEN_SUBFLOWINSTANCE                             */
/*==============================================================*/
CREATE INDEX IDX_TOKEN_SUBFLOWINSTANCE ON WF_FLOWTOKEN (
   SUBFLOWINSTANCE_ ASC
);

/*==============================================================*/
/* Table: WF_NODE                                               */
/*==============================================================*/
CREATE TABLE WF_NODE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   ACTION_              VARCHAR2(36),
   NAME_                VARCHAR2(400)                   NOT NULL,
   FUNCTYPE_            VARCHAR2(20),
   ISASYNC_             NUMBER,
   LISTORDER_           NUMBER,
   SIGNALTYPE_          NUMBER,
   FLOWDEFINE_          VARCHAR2(36),
   DESCRIPTION_         VARCHAR2(1000),
   DECISIONDELEGATION_  VARCHAR2(36),
   SUBFLOWNAME_         VARCHAR2(400),
   SUBFLOWDEFINE_       VARCHAR2(36),
   ENDFLOW_             NUMBER,
   AUTOCREATETASKS_     NUMBER,
   AUTOENDTASKS_        NUMBER,
   REFGRAPHKEY_         VARCHAR2(100),
   CONSTRAINT PK_WF_NODE PRIMARY KEY (ID_),
   CONSTRAINT AK_NATIVEID_WF_NODE UNIQUE (NAME_, FLOWDEFINE_)
);

/*==============================================================*/
/* Index: IDX_NODE_FLOWDEFINE                                   */
/*==============================================================*/
CREATE INDEX IDX_NODE_FLOWDEFINE ON WF_NODE (
   FLOWDEFINE_ ASC
);

/*==============================================================*/
/* Index: IDX_NODE_ACTION                                       */
/*==============================================================*/
CREATE INDEX IDX_NODE_ACTION ON WF_NODE (
   ACTION_ ASC
);

/*==============================================================*/
/* Index: IDX_NODE_SUBFLOWDEFINE                                */
/*==============================================================*/
CREATE INDEX IDX_NODE_SUBFLOWDEFINE ON WF_NODE (
   SUBFLOWDEFINE_ ASC
);

/*==============================================================*/
/* Table: WF_NODECONDITIONS                                     */
/*==============================================================*/
CREATE TABLE WF_NODECONDITIONS  (
   NODE_                VARCHAR2(36)                    NOT NULL,
   INDEX_               NUMBER                          NOT NULL,
   EXPRESSION_          VARCHAR2(4000),
   TRANSITIONNAME_      VARCHAR2(400),
   CONSTRAINT PK_WF_NODECONDITIONS PRIMARY KEY (NODE_, INDEX_)
);

COMMENT ON TABLE WF_NODECONDITIONS IS
'判断节点与分支节点的条件';

/*==============================================================*/
/* Table: WF_POOLEDACTOR                                        */
/*==============================================================*/
CREATE TABLE WF_POOLEDACTOR  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   TASKINSTANCE_        VARCHAR2(36),
   ACTOR_               VARCHAR2(255)                   NOT NULL,
   CONSTRAINT PK_WF_POOLEDACTOR PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_POOLEDACTOR_TASKINST                              */
/*==============================================================*/
CREATE INDEX IDX_POOLEDACTOR_TASKINST ON WF_POOLEDACTOR (
   TASKINSTANCE_ ASC
);

/*==============================================================*/
/* Table: WF_POOLEDROLE                                         */
/*==============================================================*/
CREATE TABLE WF_POOLEDROLE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   TASKINSTANCE_        VARCHAR2(36),
   ROLE_                VARCHAR2(255)                   NOT NULL,
   CONSTRAINT PK_WF_POOLEDROLE PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_POOLEDROLE_TASKINST                               */
/*==============================================================*/
CREATE INDEX IDX_POOLEDROLE_TASKINST ON WF_POOLEDROLE (
   TASKINSTANCE_ ASC
);

/*==============================================================*/
/* Table: WF_TASKDEFINE                                         */
/*==============================================================*/
CREATE TABLE WF_TASKDEFINE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   TASKNODE_            VARCHAR2(36),
   TASKASSIGNMENT_      VARCHAR2(36),
   TASKNAME_            VARCHAR2(400)                   NOT NULL,
   EXECUTEORDER_        NUMBER,
   PRIORITY_            NUMBER,
   DUEDATE_             DATE,
   TASKVIEW_            VARCHAR2(36),
   FLOWDEFINE_          VARCHAR2(36),
   ISBLOCKING_          NUMBER,
   ISSIGNALLING_        NUMBER,
   DESCRIPTION_         VARCHAR2(1000),
   FUNCTYPE_            VARCHAR2(20),
   CONSTRAINT PK_WF_TASKDEFINE PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TASKDEF_NODE                                      */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_NODE ON WF_TASKDEFINE (
   TASKNODE_ ASC
);

/*==============================================================*/
/* Index: IDX_TASKDEF_FLOWDEF                                   */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_FLOWDEF ON WF_TASKDEFINE (
   FLOWDEFINE_ ASC
);

/*==============================================================*/
/* Index: IDX_TASKDEF_TASKASSN                                  */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_TASKASSN ON WF_TASKDEFINE (
   TASKASSIGNMENT_ ASC
);

/*==============================================================*/
/* Index: IDX_TASKDEF_TASKVIEW                                  */
/*==============================================================*/
CREATE INDEX IDX_TASKDEF_TASKVIEW ON WF_TASKDEFINE (
   TASKVIEW_ ASC
);

/*==============================================================*/
/* Table: WF_TASKINSTANCE                                       */
/*==============================================================*/
CREATE TABLE WF_TASKINSTANCE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   FLOWTOKEN_           VARCHAR2(36),
   TASKDEFINE_          VARCHAR2(36),
   FLOWINSTANCE_        VARCHAR2(36),
   TASKNAME_            VARCHAR2(400)                   NOT NULL,
   PROCESSSTATE_        VARCHAR2(20),
   ACTORID_             VARCHAR2(255),
   EXECUTEORDER_        NUMBER,
   PRIORITY_            NUMBER,
   DESCRIPTION_         VARCHAR2(1000),
   CREATETIME_          DATE,
   STARTTIME_           DATE,
   ENDTIME_             DATE,
   DUETIME_             DATE,
   COMMENT_             VARCHAR2(4000),
   ISSIGNALLING_        NUMBER                          NOT NULL,
   ISBLOCKING_          NUMBER                          NOT NULL,
   FUNCTYPE_            VARCHAR2(20),
   VERSION_             NUMBER,
   ISCANCELLED_         NUMBER,
   ISSUSPENDED_         NUMBER,
   ISOPEN_              NUMBER,
   CONSTRAINT PK_WF_TASKINSTANCE PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TASKINST_TOKEN                                    */
/*==============================================================*/
CREATE INDEX IDX_TASKINST_TOKEN ON WF_TASKINSTANCE (
   FLOWTOKEN_ ASC
);

/*==============================================================*/
/* Index: IDX_TASKINST_TASKDEF                                  */
/*==============================================================*/
CREATE INDEX IDX_TASKINST_TASKDEF ON WF_TASKINSTANCE (
   TASKDEFINE_ ASC
);

/*==============================================================*/
/* Index: IDX_TASKINST_FLOWINST                                 */
/*==============================================================*/
CREATE INDEX IDX_TASKINST_FLOWINST ON WF_TASKINSTANCE (
   FLOWINSTANCE_ ASC
);

/*==============================================================*/
/* Table: WF_TRACEPOINT                                         */
/*==============================================================*/
CREATE TABLE WF_TRACEPOINT  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   FLOWDEFINE_          VARCHAR2(36),
   FLOWINSTANCE_        VARCHAR2(36),
   ELEMENT_             VARCHAR2(36),
   ELEMENTTYPE_         VARCHAR2(50),
   ACTIONTYPE_          VARCHAR2(50),
   REFGRAPHKEY_         VARCHAR2(100),
   SNAPTIME_            DATE                            NOT NULL,
   CONSTRAINT PK_WF_TRACEPOINT PRIMARY KEY (ID_)
);

COMMENT ON TABLE WF_TRACEPOINT IS
'记录流程轨迹';

COMMENT ON COLUMN WF_TRACEPOINT.ELEMENTTYPE_ IS
'NODE/TRANSITION';

COMMENT ON COLUMN WF_TRACEPOINT.ACTIONTYPE_ IS
'ENTER/LEAVE';

/*==============================================================*/
/* Table: WF_TRANSITION                                         */
/*==============================================================*/
CREATE TABLE WF_TRANSITION  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   NAME_                VARCHAR2(400),
   FROMNODE_            VARCHAR2(36),
   TONODE_              VARCHAR2(36),
   FROMINDEX_           NUMBER,
   CONDITIONS_          VARCHAR2(4000),
   DESCRIPTION_         VARCHAR2(1000),
   FLOWDEFINE_          VARCHAR2(36),
   ISREJECTED_          NUMBER,
   REFGRAPHKEY_         VARCHAR2(100),
   CONSTRAINT PK_WF_TRANSITION PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Index: IDX_TRANSITION_FROM                                   */
/*==============================================================*/
CREATE INDEX IDX_TRANSITION_FROM ON WF_TRANSITION (
   FROMNODE_ ASC
);

/*==============================================================*/
/* Index: IDX_TRANSITION_TO                                     */
/*==============================================================*/
CREATE INDEX IDX_TRANSITION_TO ON WF_TRANSITION (
   TONODE_ ASC
);

/*==============================================================*/
/* Index: IDX_TRANSITION_FLOWDEF                                */
/*==============================================================*/
CREATE INDEX IDX_TRANSITION_FLOWDEF ON WF_TRANSITION (
   FLOWDEFINE_ ASC
);

/*==============================================================*/
/* Table: WF_VARIABLEDECLARE                                    */
/*==============================================================*/
CREATE TABLE WF_VARIABLEDECLARE  (
   ID_                  VARCHAR2(36)                    NOT NULL,
   NAME_                VARCHAR2(400),
   DISPLAYNAME_         VARCHAR2(400),
   TYPE_                VARCHAR2(36)                    NOT NULL,
   NOTNULL_             NUMBER,
   FLOWDEFINE_          VARCHAR2(36),
   CONSTRAINT PK_WF_VARIABLEDECLARE PRIMARY KEY (ID_)
);

COMMENT ON TABLE WF_VARIABLEDECLARE IS
'表达式中的变量要事先申明';

ALTER TABLE WF_ACTION
   ADD CONSTRAINT FK_ACTION_EVENT FOREIGN KEY (EVENT_)
      REFERENCES WF_EVENT (ID_);

ALTER TABLE WF_ACTION
   ADD CONSTRAINT FK_ACTION_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_ASYNJOB
   ADD CONSTRAINT FK_ASYNJOB_ACTION FOREIGN KEY (ACTION_)
      REFERENCES WF_ACTION (ID_);

ALTER TABLE WF_ASYNJOB
   ADD CONSTRAINT FK_ASYNJOB_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_);

ALTER TABLE WF_ASYNJOB
   ADD CONSTRAINT FK_ASYNJOB_FLOWTOKE FOREIGN KEY (FLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_);

ALTER TABLE WF_ASYNJOB
   ADD CONSTRAINT FK_ASYNJOB_NODE FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_CONTEXTVARIABLE
   ADD CONSTRAINT FK_CTXVAR_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_);

ALTER TABLE WF_EVENT
   ADD CONSTRAINT FK_EVENT_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_EVENT
   ADD CONSTRAINT FK_EVENT_NODE FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_EVENT
   ADD CONSTRAINT FK_EVENT_TASKDEF FOREIGN KEY (TASKDEFINE_)
      REFERENCES WF_TASKDEFINE (ID_);

ALTER TABLE WF_EVENT
   ADD CONSTRAINT FK_EVENT_TRANSITION FOREIGN KEY (TRANSITION_)
      REFERENCES WF_TRANSITION (ID_);

ALTER TABLE WF_FLOWDEFINE
   ADD CONSTRAINT FK_FLOW_STARTNODE FOREIGN KEY (STARTNODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_FLOWINSTANCE
   ADD CONSTRAINT FK_FLOWINST_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_FLOWINSTANCE
   ADD CONSTRAINT FK_FLOWINST_SUPERTOKEN FOREIGN KEY (SUPERFLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_);

ALTER TABLE WF_FLOWINSTANCE
   ADD CONSTRAINT FK_FLOWINST_TOKEN FOREIGN KEY (FLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_);

ALTER TABLE WF_FLOWTOKEN
   ADD CONSTRAINT FK_TOKEN_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_);

ALTER TABLE WF_FLOWTOKEN
   ADD CONSTRAINT FK_TOKEN_NODE FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_FLOWTOKEN
   ADD CONSTRAINT FK_TOKEN_PARENT FOREIGN KEY (PARENT_)
      REFERENCES WF_FLOWTOKEN (ID_);

ALTER TABLE WF_FLOWTOKEN
   ADD CONSTRAINT FK_TOKEN_SUBFLOWINST FOREIGN KEY (SUBFLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_);

ALTER TABLE WF_NODE
   ADD CONSTRAINT FK_NODE_ACTION FOREIGN KEY (ACTION_)
      REFERENCES WF_ACTION (ID_);

ALTER TABLE WF_NODE
   ADD CONSTRAINT FK_NODE_DELEGATION FOREIGN KEY (DECISIONDELEGATION_)
      REFERENCES WF_DELEGATION (ID_);

ALTER TABLE WF_NODE
   ADD CONSTRAINT FK_NODE_FLOWDEFINE FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_NODE
   ADD CONSTRAINT FK_NODE_SUBFLOWDEFINE FOREIGN KEY (SUBFLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_NODECONDITIONS
   ADD CONSTRAINT FK_NODE_CONDITIONS FOREIGN KEY (NODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_POOLEDACTOR
   ADD CONSTRAINT FK_POOLEDACTORS_TASKINST FOREIGN KEY (TASKINSTANCE_)
      REFERENCES WF_TASKINSTANCE (ID_);

ALTER TABLE WF_POOLEDROLE
   ADD CONSTRAINT FK_POOLEDROLE_TASKINST FOREIGN KEY (TASKINSTANCE_)
      REFERENCES WF_TASKINSTANCE (ID_);

ALTER TABLE WF_TASKDEFINE
   ADD CONSTRAINT FK_TASKDEF_ASSIGNMENT FOREIGN KEY (TASKASSIGNMENT_)
      REFERENCES WF_DELEGATION (ID_);

ALTER TABLE WF_TASKDEFINE
   ADD CONSTRAINT FK_TASKDEF_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_TASKDEFINE
   ADD CONSTRAINT FK_TASKDEF_NODE FOREIGN KEY (TASKNODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_TASKDEFINE
   ADD CONSTRAINT FK_TASKDEF_TASKVIEW FOREIGN KEY (TASKVIEW_)
      REFERENCES WF_DELEGATION (ID_);

ALTER TABLE WF_TASKINSTANCE
   ADD CONSTRAINT FK_TASKINST_FLOWTOKE FOREIGN KEY (FLOWTOKEN_)
      REFERENCES WF_FLOWTOKEN (ID_);

ALTER TABLE WF_TASKINSTANCE
   ADD CONSTRAINT FK_TASKINST_TASKDEFI FOREIGN KEY (TASKDEFINE_)
      REFERENCES WF_TASKDEFINE (ID_);

ALTER TABLE WF_TASKINSTANCE
   ADD CONSTRAINT FK_TASKINST_FLOWINST FOREIGN KEY (FLOWINSTANCE_)
      REFERENCES WF_FLOWINSTANCE (ID_);

ALTER TABLE WF_TRANSITION
   ADD CONSTRAINT FK_TRANSITION_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

ALTER TABLE WF_TRANSITION
   ADD CONSTRAINT FK_TRANSITION_FROMNODE FOREIGN KEY (FROMNODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_TRANSITION
   ADD CONSTRAINT FK_TRANSITION_TONODE FOREIGN KEY (TONODE_)
      REFERENCES WF_NODE (ID_);

ALTER TABLE WF_VARIABLEDECLARE
   ADD CONSTRAINT FK_VARIABLE_FLOWDEF FOREIGN KEY (FLOWDEFINE_)
      REFERENCES WF_FLOWDEFINE (ID_);

