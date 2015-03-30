/*==============================================================*/
/* Table: EAPP_USER_PORTLET                                     */
/*==============================================================*/
CREATE TABLE EAPP_USER_PORTLET1  (
   USER_ID_             VARCHAR2(36)                    NOT NULL,
   PORTLET_ID_          VARCHAR2(36)                    NOT NULL,
   PAGE_CONTAINER_ID_   VARCHAR2(20)                    NOT NULL,
   POSITION_INDEX_      INTEGER                         NOT NULL,
   CONSTRAINT PK_EAPP_USER_PORTLET1 PRIMARY KEY (USER_ID_, PORTLET_ID_)
);



ALTER TABLE EAPP_USER_PORTLET1
   ADD CONSTRAINT FK_PORTLET_USER1 FOREIGN KEY (USER_ID_)
      REFERENCES EAPP_USER_ACCOUNT (USER_ID_);

ALTER TABLE EAPP_USER_PORTLET1
   ADD CONSTRAINT FK_USER_PORTLET1 FOREIGN KEY (PORTLET_ID_)
      REFERENCES EAPP_PORTLET (PORTLET_ID_);
