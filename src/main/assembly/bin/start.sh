#!/bin/bash


source $HOME/.bash_profile

export JAVA_OPTS='-Xms256M -Xmx1536M -XX:NewSize=128m -XX:MaxNewSize=512m -XX:+CMSPermGenSweepingEnabled -XX:PermSize=128m -XX:MaxPermSize=256m -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC'

#Doing this for Java to be able to pick up HOSTNAME as a System PROPERTY
export HOSTNAME=$HOSTNAME

echo "Starting Tomcat"
$TOMCAT_HOME/bin/catalina.sh start
