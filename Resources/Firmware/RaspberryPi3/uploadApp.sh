#!/bin/sh

remoteloginname=administrator
remotehost=$1.fit.fraunhofer.de
targetdirectory=/home/administrator/export/bin

cd ../../../SensorplatformApp/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
