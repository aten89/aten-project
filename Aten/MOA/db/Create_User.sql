create tablespace TBS_EAPP
logging
datafile 'D:\app\Administrator\oradata\orcl\EAPP.ORA'
size 200m
autoextend on
next 200m maxsize UNLIMITED
extent management local;

create user EAPP identified by eapp
default tablespace TBS_EAPP
temporary tablespace TEMP;

grant connect,resource to EAPP;


create tablespace TBS_OA
logging
datafile 'D:\app\Administrator\oradata\orcl\OA.ORA'
size 200m
autoextend on
next 200m maxsize UNLIMITED
extent management local;

create user OA identified by oa
default tablespace TBS_OA
temporary tablespace TEMP;

grant connect,resource to OA;


create tablespace TBS_CRM
logging
datafile 'D:\app\Administrator\oradata\orcl\CRM.ORA'
size 200m
autoextend on
next 200m maxsize UNLIMITED
extent management local;

create user CRM identified by crm
default tablespace TBS_CRM
temporary tablespace TEMP;

grant connect,resource to CRM;



create tablespace TBS_POSS
logging
datafile 'D:\app\Administrator\oradata\orcl\POSS.ORA'
size 200m
autoextend on
next 200m maxsize UNLIMITED
extent management local;

create user POSS identified by poss
default tablespace TBS_POSS
temporary tablespace TEMP;

grant connect,resource to POSS;


create tablespace TBS_WF
logging
datafile 'D:\app\Administrator\oradata\orcl\WF.ORA'
size 200m
autoextend on
next 200m maxsize UNLIMITED
extent management local;

create user WF identified by wf
default tablespace TBS_WF
temporary tablespace TEMP;

grant connect,resource to WF;