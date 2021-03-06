WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK;
SET ECHO ON;

--添加字段：提交时间
DECLARE
    VN_FLAG NUMBER;
BEGIN
    SELECT COUNT(1)
      INTO VN_FLAG
      FROM ALL_TAB_COLUMNS T
     WHERE T.TABLE_NAME = 'CRM_CUSTOMER_INFO'
       AND T.COLUMN_NAME = 'SUBMIT_TIME_';
    IF VN_FLAG = 0 THEN
        EXECUTE IMMEDIATE 'alter table CRM_CUSTOMER_INFO add SUBMIT_TIME_ TIMESTAMP(6)';
    END IF;
END;
/

--添加字段：银行名称
DECLARE
    VN_FLAG NUMBER;
BEGIN
    SELECT COUNT(1)
      INTO VN_FLAG
      FROM ALL_TAB_COLUMNS T
     WHERE T.TABLE_NAME = 'CRM_CUSTOMER_INFO'
       AND T.COLUMN_NAME = 'BANKNAME_';
    IF VN_FLAG = 0 THEN
        EXECUTE IMMEDIATE 'alter table CRM_CUSTOMER_INFO add BANKNAME_ VARCHAR2(128)';
    END IF;
END;
/

--添加字段：开户行
DECLARE
    VN_FLAG NUMBER;
BEGIN
    SELECT COUNT(1)
      INTO VN_FLAG
      FROM ALL_TAB_COLUMNS T
     WHERE T.TABLE_NAME = 'CRM_CUSTOMER_INFO'
       AND T.COLUMN_NAME = 'BANKBRANCH_';
    IF VN_FLAG = 0 THEN
        EXECUTE IMMEDIATE 'alter table CRM_CUSTOMER_INFO add BANKBRANCH_ VARCHAR2(128)';
    END IF;
END;
/

--添加字段：银行账户
DECLARE
    VN_FLAG NUMBER;
BEGIN
    SELECT COUNT(1)
      INTO VN_FLAG
      FROM ALL_TAB_COLUMNS T
     WHERE T.TABLE_NAME = 'CRM_CUSTOMER_INFO'
       AND T.COLUMN_NAME = 'BANKACCOUNT_';
    IF VN_FLAG = 0 THEN
        EXECUTE IMMEDIATE 'alter table CRM_CUSTOMER_INFO add BANKACCOUNT_ VARCHAR2(128)';
    END IF;
END;
/

--添加字段：身份证号
DECLARE
    VN_FLAG NUMBER;
BEGIN
    SELECT COUNT(1)
      INTO VN_FLAG
      FROM ALL_TAB_COLUMNS T
     WHERE T.TABLE_NAME = 'CRM_CUSTOMER_INFO'
       AND T.COLUMN_NAME = 'IDENTITY_NUM_';
    IF VN_FLAG = 0 THEN
        EXECUTE IMMEDIATE 'alter table CRM_CUSTOMER_INFO add IDENTITY_NUM_ VARCHAR2(36)';
    END IF;
END;
/

--身份证号注释
comment on column CRM_CUSTOMER_INFO.IDENTITY_NUM_ is '身份证号';
/