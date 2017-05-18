#!/bin/sh

remoteloginname=administrator
remotehost=localhost
port=$1
targetdirectory=/home/administrator/export/bin

cd ../../../SensorplatformApp/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory} -p ${port}"
echo "Executing: ${run}"
$run
