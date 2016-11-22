#!/bin/sh

remoteloginname='administrator'
remotehost='SPF2.fit.fraunhofer.de'
targetdirectory='/home/administrator/Sensorplatform'

cd ../../../../SensorplatformApp
run="scp -r staticResources ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
