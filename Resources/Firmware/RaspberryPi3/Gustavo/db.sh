#! /bin/sh

remoteloginname='administrator'
remotehost='SPF2.fit.fraunhofer.de'
targetdirectory='/home/administrator/Sensorplatform'

case "$1" in
	upload)
		cd ../../../../SensorplatformApp
		run="scp -r db ${remoteloginname}@${remotehost}:${targetdirectory}"
		echo "Executing: ${run}"
		$run
		;;
	download)
		cd ../../../../SensorplatformApp
		run="scp -r ${remoteloginname}@${remotehost}:${targetdirectory}/db ."
		echo "Executing: ${run}"
		$run
		;;
	*)
	echo "(upload|download)"
	;;
esac

exit 0
