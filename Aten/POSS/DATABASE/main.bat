@REM
@REM  %1:ERMP��װ�û���
@REM  %2:ERMP�û�������
@REM  %3:ERMP���ݿ�ʵ����
@REM  %4:OA��װ�û���
@REM  %5:OA�û�������
@REM  %6:OA���ݿ�ʵ����
@REM  %7:POSS��װ�û���
@REM  %8:POSS�û�������
@REM  %9:POSS���ݿ�ʵ����
@REM  ���þ���:main.bat ermp_system ermp_system orcl1 eoa_system eoa_system orcl1 ecit_system ecit_system orcl1
@REM
@REM ������װ���ýű�

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

@ECHO ����������ִ����ɣ���鿴������־&PAUSE