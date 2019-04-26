#! /bin/sh

DATE=`date +%Y%m%d-%H%M%S`

ENV="$1"
APP_NAME=null
APP_PORT=null
APP_PATH=null
JAVA_HOME=null

readConf() {
  APP_NAME=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $2}' piece.conf | head -n 1`
  APP_PORT=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $3}' piece.conf | head -n 1`
  APP_PATH=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $4}' piece.conf | head -n 1`
  JAVA_HOME=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $5}' piece.conf | head -n 1`

  echo "Read Env as Below Config"
  echo "APP_NAME : $APP_NAME"
  echo "APP_PORT : $APP_PORT"
  echo "APP_PATH : $APP_PATH"
  echo "JAVA_HOME : $JAVA_HOME"
}


cd `dirname $0`

readConf

mv $APP_NAME.jar backup/$APP_NAME.$DATE.jar
echo "================================"
echo "backup to:  backup/$APP_NAME.$DATE.jar"
echo "================================"


cp  deployment/target/$APP_NAME.jar ./
echo "================================"
echo "Jar update to latest"
echo "================================"
