@REM
@REM  %1:升级用户名
@REM  %2:升级用户的密码
@REM  %3:数据库实例名
@REM  调用举例:update_main_init.bat es_system oracle orcl243
@REM

@ECHO OFF
sqlplus %1/%2@%3 @DB\Update\Init\Update_Init_crm.sql<nul
if not %ERRORLEVEL% == 0 goto error_exit



:error_exit
