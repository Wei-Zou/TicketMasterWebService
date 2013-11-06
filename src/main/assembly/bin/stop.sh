#!/bin/bash

source $HOME/.bash_profile

PID_FILE=${TOMCAT_HOME}/logs/catalina.pid

echo "Stopping Tomcat"
$CATALINA_HOME/bin/catalina.sh stop 60 -force

# check if PID file still there:
if [ -e "${PID_FILE}" ]
then
    test=$(ps -e -o pid,command | grep ' org\.apache\.catalina\.startup\.Bootstrap start' | awk '{print $1}')
    PID=$(head -n 1 ${PID_FILE})

    if [[ ! -z "${test}" ]] && [[ "${test}" = "${PID}" ]]
    then
        echo "Tomcat still running.  Killing it by pid ${test}"
        kill ${test}
    fi

    echo "Removing ${PID_FILE}"
    rm ${PID_FILE}
fi
