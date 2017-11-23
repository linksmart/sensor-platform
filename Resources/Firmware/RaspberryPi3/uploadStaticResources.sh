#!/bin/sh

remoteloginname=administrator
remotehost=$1.fit.fraunhofer.de
targetdirectory=/home/administrator/SPF/export

cd ../../../SensorplatformApp
run="scp -r staticResources ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
