@REM
@REM  %1:�����û���
@REM  %2:�����û�������
@REM  %3:���ݿ�ʵ����
@REM  ���þ���:update_main_proc.bat es_system oracle orcl243
@REM	

@ECHO OFF

sqlplus %1/%2@%3 @DB\Update\Proc\Update_Proc_crm.sql %1 <nul
if not %ERRORLEVEL% == 0 goto error_exit


:error_exit

