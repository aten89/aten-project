@REM
@REM %1:升级用户
@REM %2:升级用户的密码
@REM %3:数据库实例名
@REM %4:子系统用户
@REM 调用举例:MAIN_INIT.BAT ES_DBA 123456 ORA47 ES_SYSTEM
@REM

   @REM ==========================================================
   @REM BEGIN OF 升级脚本

    CD DB
    echo 开始初始化%4用户数据
    CALL Create_Init_Pub.bat %1 %2 %3 %4
    if not %ERRORLEVEL% == 0 goto error_exit
    CD ..

   @REM END OF 升级脚本
   @REM ==========================================================

:error_exit