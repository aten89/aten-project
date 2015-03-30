@REM
@REM  %1:ERMP安装用户名
@REM  %2:ERMP用户的密码
@REM  %3:ERMP数据库实例名
@REM  %4:OA安装用户名
@REM  %5:OA用户的密码
@REM  %6:OA数据库实例名
@REM  %7:POSS安装用户名
@REM  %8:POSS用户的密码
@REM  %9:POSS数据库实例名
@REM  调用举例:main.bat ermp_system ermp_system orcl1 eoa_system eoa_system orcl1 ecit_system ecit_system orcl1
@REM
@REM 独立安装调用脚本

CD oracle_poss_ermp
CALL poss_ermp_main.bat %1 %2 %3 >.\..\CREATE_POSS_ERMP.LOG
if not %ERRORLEVEL% == 0 goto error_exit

CD..

CD oracle_poss_oa
CALL poss_oa_main.bat %4 %5 %6 >.\..\CREATE_POSS_OA.LOG
if not %ERRORLEVEL% == 0 goto error_exit

CD..

CD oracle_poss
CALL poss_main.bat %7 %8 %9 >.\..\CREATE_POSS.LOG
if not %ERRORLEVEL% == 0 goto error_exit

CD..

:error_exit

@ECHO 本次升级已执行完成，请查看升级日志&PAUSE