@REM
@REM  %1:ERMP��װ�û���
@REM  %2:ERMP�û�������
@REM  %3:ERMP���ݿ�ʵ����
@REM  %4:OA��װ�û���
@REM  %5:OA�û�������
@REM  %6:OA���ݿ�ʵ����
@REM  %7:CRM��װ�û���
@REM  %8:CRM�û�������
@REM  %9:CRM���ݿ�ʵ����
@REM  ���þ���:main.bat ermp_system ermp_system orcl1 eoa_system eoa_system orcl1 ecit_system ecit_system orcl1
@REM
@REM ������װ���ýű�

CD oracle_crm_ermp
CALL crm_ermp_main.bat %1 %2 %3 >.\..\CREATE_CRM_ERMP.LOG
if not %ERRORLEVEL% == 0 goto error_exit

CD..

CD oracle_crm_oa
CALL crm_oa_main.bat %4 %5 %6 >.\..\CREATE_CRM_OA.LOG
if not %ERRORLEVEL% == 0 goto error_exit

CD..

CD oracle_crm
CALL crm_main.bat %7 %8 %9 >.\..\CREATE_CRM.LOG
if not %ERRORLEVEL% == 0 goto error_exit

CD..

:error_exit

@ECHO ����������ִ����ɣ���鿴������־&PAUSE