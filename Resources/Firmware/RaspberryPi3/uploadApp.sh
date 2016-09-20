#!/bin/sh

remoteloginname='pi'
remotehost='sensorplatform.fit.fraunhofer.de'
targetdirectory='/home/pi/Sensorplatform/bin'

#run="ssh ${remoteloginname}@${remotehost} mkdir -p ${targetdirectory}"
#echo "Executing: ${run}"
#$run

cd ../../../SensorplatformApp/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
