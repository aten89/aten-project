@REM
@REM  %1:�����û���
@REM  %2:�����û�������
@REM  %3:���ݿ�ʵ����
@REM  ���þ���:update_main_init.bat es_system oracle orcl243
@REM

@ECHO OFF
sqlplus %1/%2@%3 @DB\Update\Init\Update_Init_crm.sql<nul
if not %ERRORLEVEL% == 0 goto error_exit



:error_exit
