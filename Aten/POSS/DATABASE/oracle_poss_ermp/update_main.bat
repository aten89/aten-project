@REM
@REM  %1:升级用户名
@REM  %2:升级用户的密码
@REM  %3:数据库实例名
@REM  调用举例:update_eframe_main.bat es_system oracle orcl243
@REM

@ECHO OFF

   @ECHO ==========================================================
   @ECHO BEGIN OF 升级脚本
    
    CALL update_main_init.bat %1 %2 %3
    if not %ERRORLEVEL% == 0 goto error_exit

    @ECHO END OF 升级脚本结束
    @ECHO ==========================================================

:error_exit


