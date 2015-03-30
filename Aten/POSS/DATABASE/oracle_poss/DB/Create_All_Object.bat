
rem µ÷ÓÃ¾ÙÀý
rem Create_All_object.bat es_dba change_on_install esim141 es_system d:\oracle\oradata\ESIM60\  >create_all_es_system.log

call Create_TableSpace_pub.bat %1 %2 %3 %4 %5

call Create_User_pub.bat %1 %2 %3 %4

call Create_Table_pub.bat %1 %2 %3 %4

call Create_Proc_pub.bat %1 %2 %3 %4

call Create_Init_pub.bat %1 %2 %3 %4

