rem ���濪ʼ��ʼ������

sqlplus %1/%2@%3 @Init\Create_Init.sql %4 <nul
if not %ERRORLEVEL% == 0 goto error_exit

rem ��ʼ�����ݽ���