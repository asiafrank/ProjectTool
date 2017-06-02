#!/bin/sh

MODULE=${projectName}-service
PID=0

getPID() {
    findPid=`pgrep -f $MODULE`
    if [ -n "$findPid" ]; then
        PID=`echo $findPid`
    else
        PID=0
    fi
}

shutdown() {
    getPID
    if [ $PID -ne 0 ]; then
        echo -n "Stopping $MODULE (PID=$PID) "
        kill -9 $PID
        if [ $? -eq 0 ]; then
            echo " [Success]"
        else
            echo " [Failed]"
        fi
    else
        echo "$MODULE is not running"
    fi
}

echo ""
shutdown
echo ""