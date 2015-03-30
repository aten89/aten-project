rem 下面开始初始化数据

sqlplus %1/%2@%3 @Init\Create_Init.sql %4 <nul
if not %ERRORLEVEL% == 0 goto error_exit

rem 初始化数据结束