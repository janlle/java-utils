#! /bin/sh

deployFolder=deployment
backupFolder=backup

cd `dirname $0`

if [ -d "$backupFolder" ]
then
    echo $backupFolder ready!
else
    mkdir $backupFolder
    chmod 757 -R "$backupFolder"
fi

if [ -d "$deployFolder" ]
then
    echo $deployFolder ready!
else
    mkdir $deployFolder
    chmod 757 -R "$deployFolder"
fi