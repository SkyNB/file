@echo off
call mvn clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
pause