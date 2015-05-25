#!/bin/sh
pid=`ps -ef | grep -v grep |  grep $1| grep -v "monitor.sh" | sed -n  '1P' | awk '{print $2}'`  
if [ -z $pid ] ; then
   
  echo "Start:" $1   
  
   #####################################################################################################
   #    进程监控 使用方法
   #    (1)crontab  -e
   #    (2)添加作业计划（如下每5分钟执行一次）
   #       0,5,10,15,20,25,30,35,40,45,50,55 * * * * cd /home/vmuser/GitHub_Tide && ./monitor.sh  main.run_send_sms >/dev/null 2>&1
   #    (3)[./monitor.sh  main.run_send_sms]:    main.run_send_sms为程序进程名
   #    (4)[./monitor.sh]    会匹配 系统的当前进程名， 如果没有找到会  执行相应的 程序启动脚本     
   #####################################################################################################
   #----------------160:Github_Tide  begin-------------------------------#
   if [ $1 = "main.run_settime" ] ;
   then
    (./run.sh) &
   fi
   
   
  
else  
    echo $1 "'s pid is "$pid  
fi  
