#!/bin/sh

remoteloginname='pi'
remotehost='129.26.160.34'
targetdirectory='/home/pi/Sensorplatform/bin'

#run="ssh ${remoteloginname}@${remotehost} mkdir -p ${targetdirectory}"
#echo "Executing: ${run}"
#$run

cd ../../../SensorplatformApp/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
