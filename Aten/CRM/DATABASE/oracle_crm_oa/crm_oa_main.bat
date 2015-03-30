@REM
@REM  %1:安装用户名
@REM  %2:用户的密码
@REM  %3:数据库实例名
@REM  调用举例:ecsm_oa_main.bat es_system oracle orcl243
@REM
    
    CALL main_init.bat %1 %2 %3 %1
    if not %ERRORLEVEL% == 0 goto error_exit

:error_exit