@echo off
color a
mode con lines=20 cols=38
Title 泛泛之辈

set aa=E:\Working\Workspace_NetBeans\GitHub_MyObj\myobj\GitHub_My\dist\GitHub_My.jar;

SETLOCAL ENABLEDELAYEDEXPANSION
for /f "delims=" %%a in ('dir /b ".\lib\*.jar"') do (
set "aa=!aa!%%a;"
)
::echo !aa!

 
java -cp !aa! main.run_getwh

echo ______________________
echo THE END :按任意键退出... 
pause>nul

