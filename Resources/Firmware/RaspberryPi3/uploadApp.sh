#!/bin/sh

remoteloginname=administrator
remotehost=$1.fit.fraunhofer.de
targetdirectory=/home/administrator/Sensorplatform/bin

cd ../../../SensorplatformApp/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
