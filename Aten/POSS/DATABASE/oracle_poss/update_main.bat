@REM
@REM  %1:�����û���
@REM  %2:�����û�������
@REM  %3:���ݿ�ʵ����
@REM  ���þ���:update_main.bat es_system oracle orcl243
@REM

@ECHO OFF

   @ECHO ==========================================================
   @ECHO BEGIN OF �����ű�
    
    CALL update_main_table.bat %1 %2 %3
    if not %ERRORLEVEL% == 0 goto error_exit
    
    CALL update_main_proc.bat %1 %2 %3 
    if not %ERRORLEVEL% == 0 goto error_exit

    CALL update_main_init.bat %1 %2 %3
    if not %ERRORLEVEL% == 0 goto error_exit

    @ECHO END OF �����ű�����
    @ECHO ==========================================================

:error_exit


