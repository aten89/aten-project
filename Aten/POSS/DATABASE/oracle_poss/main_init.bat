@REM
@REM %1:�����û�
@REM %2:�����û�������
@REM %3:���ݿ�ʵ����
@REM %4:��ϵͳ�û�
@REM ���þ���:MAIN_INIT.BAT ES_DBA 123456 ORA47 ES_SYSTEM
@REM

   @REM ==========================================================
   @REM BEGIN OF �����ű�

    CD DB
    echo ��ʼ��ʼ��%4�û�����
    CALL Create_Init_Pub.bat %1 %2 %3 %4
    if not %ERRORLEVEL% == 0 goto error_exit
    CD ..

   @REM END OF �����ű�
   @REM ==========================================================

:error_exit