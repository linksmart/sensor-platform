#!/bin/sh

remoteloginname='pi'
remotehost='129.26.160.34'
targetdirectory='/home/pi/Sensorplatform'

cd ../../../SensorplatformApp
run="scp -r staticResources ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
