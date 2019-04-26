ps -ef | grep 'java'| grep 'test'|grep 'web'| cut -c 9-15 | xargs kill -9 &&
nohup java -Dspring.profiles.active=test -jar /data/app/web-1.0.jar &

