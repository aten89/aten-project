@REM
@REM  %1:��װ�û���
@REM  %2:�û�������
@REM  %3:���ݿ�ʵ����
@REM  ���þ���:ecsm_oa_main.bat es_system oracle orcl243
@REM
    
    CALL main_init.bat %1 %2 %3 %1
    if not %ERRORLEVEL% == 0 goto error_exit

:error_exit