#!/bin/sh

remoteloginname='administrator'
remotehost='129.26.160.38'
targetdirectory='/home/administrator/Sensorplatform'

cd ../../../SensorplatformApp
run="scp -r staticResources ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
