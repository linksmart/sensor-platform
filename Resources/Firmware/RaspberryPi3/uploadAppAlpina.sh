#!/bin/sh

remoteloginname=administrator
remotehost=alpina.fit.fraunhofer.de
port=$1
targetdirectory=/home/administrator/export/bin

cd ../../../SensorplatformApp/target/resources
run="scp -P ${port} *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
