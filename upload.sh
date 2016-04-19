#!/bin/sh

remoteloginname='linaro'
remotehost='cubiboard3.fit.fraunhofer.de'
targetdirectory='/home/linaro/Sensorplatform/bin'
# Password for linaro: linaro

run="ssh ${remoteloginname}@${remotehost} mkdir -p ${targetdirectory}"
echo "Executing: ${run}"
$run

cd Sensorplatform/target/resources
run="scp *.jar ${remoteloginname}@${remotehost}:${targetdirectory}"
echo "Executing: ${run}"
$run
