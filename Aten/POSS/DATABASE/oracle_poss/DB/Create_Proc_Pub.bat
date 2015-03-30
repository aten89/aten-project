rem 下面开始创建存储过程和触发器

sqlplus %1/%2@%3 @Proc\Create_proc.sql %4 <nul
if not %ERRORLEVEL% == 0 goto error_exit

rem 创建存储过程和触发器结束
