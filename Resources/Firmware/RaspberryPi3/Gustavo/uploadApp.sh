#!/bin/sh

remoteloginname='administrator'
remotehost='SPF2.fit.fraunhofer.de'
targetdirectory='/home/administrator/Sensorplatform/bin'

#run="ssh ${remoteloginname}@${remotehost} mkdir -p ${targetdirectory}"
#echo "Executing: ${run}"
#$run

cd ../../../../SensorplatformApp/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
