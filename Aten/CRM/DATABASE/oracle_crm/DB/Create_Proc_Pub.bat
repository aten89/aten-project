rem ���濪ʼ�����洢���̺ʹ�����

sqlplus %1/%2@%3 @Proc\Create_proc.sql %4 <nul
if not %ERRORLEVEL% == 0 goto error_exit

rem �����洢���̺ʹ���������
