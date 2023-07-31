#!/bin/bash

ROOT_PATH="/home/ubuntu/app"
JAR="$ROOT_PATH/application.jar"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_PID=$(pgrep -f $JAR) # 실행중인 Spring 서버의 PID

if [ -z "$SERVICE_PID" ]; then
  echo "실행중인 애플리케이션이 없습니다." >> $STOP_LOG
else
  echo "실행중인 애플리케이션 종료" >> $STOP_LOG
  kill -15 "$SERVICE_PID"
  # kill -9 $SERVICE_PID # 강제 종료를 하고 싶다면 이 명령어 사용
fi