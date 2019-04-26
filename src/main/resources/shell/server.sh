#! /bin/sh

ENV="$2"

APP_NAME=null
APP_PORT=null
APP_PATH=null
JAVA_HOME=null

PS_ID=0

readConf() {
  cd `dirname $0`

  APP_NAME=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $2}' piece.conf | head -n 1`
  APP_PORT=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $3}' piece.conf | head -n 1`
  APP_PATH=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $4}' piece.conf | head -n 1`
  JAVA_HOME=`awk -v CurrentPath=$PWD -v Env=$ENV '$1==Env && $4==CurrentPath {print $5}' piece.conf | head -n 1`

  echo "================================"
  echo "Read Env as Below Config"
  echo "APP_ENV : $ENV"
  echo "APP_NAME : $APP_NAME"
  echo "APP_PORT : $APP_PORT"
  echo "APP_PATH : $APP_PATH"
  echo "JAVA_HOME : $JAVA_HOME"
  echo "================================"
}

checkPsid() {
   javaps=`$JAVA_HOME/bin/jps -l | grep $APP_NAME`
   if [ -n "$javaps" ]; then
      PS_ID=`echo $javaps | awk '{print $1}'`
   else
      PS_ID=0
   fi
}


start(){
   readConf
   checkPsid

   if [ $PS_ID -ne 0 ]; then
      echo "================================"
      echo "Warnning: $APP_NAME already started! (pid=$PS_ID) on $APP_PORT! "
      echo "================================"
   else
      echo "================================"
      echo "$APP_NAME $ENV Starting..."
      echo "================================"

      cd ~
      nohup $JAVA_HOME/bin/java -Xms128m -Xmx512m -DEnv=$ENV  -jar $APP_PATH/$APP_NAME.jar --spring.profiles.active=$ENV >/dev/null 2>&1 &

      checkPsid
      if [ $PS_ID -ne 0 ]; then
         echo "================================"
         echo "$APP_NAME $ENV Started (pid=$PS_ID) on $APP_PORT !!"
         echo "================================"
      else
         echo "================================"
         echo "$APP_NAME $ENV Start Failed!!"
         echo "================================"
      fi
   fi
}


stop(){
  readConf
  checkPsid
  if [ $PS_ID -ne 0 ]; then

     kill -9 $PS_ID

     echo "================================"
     echo "$APP_NAME $ENV Stoped (pid=$PS_ID) from $APP_PORT !!"
     echo "================================"
  else
     echo "================================"
     echo "$APP_NAME $ENV already Stoped!!"
     echo "================================"
  fi
}


case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
        *)
    echo 'Usage: %s {start|stop|restart}\n' "$prog"
    exit 1
    ;;
esac

exit 0