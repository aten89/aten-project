rem 下面将开始创建表

sqlplus %1/%2@%3 @Table\Create_Table.sql %4 <nul
if not %ERRORLEVEL% == 0 goto error_exit

rem 创建表结束
