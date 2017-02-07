#! /bin/sh

remoteloginname=administrator
remotehost=$2.fit.fraunhofer.de
targetdirectory=/home/administrator/export

case "$1" in
	upload)
		cd ../../../SensorplatformApp
		run="scp -r db ${remoteloginname}@${remotehost}:${targetdirectory}"
		echo "Executing: ${run}"
		$run
		;;
	download)
		cd ../../../SensorplatformApp
		run="scp -r ${remoteloginname}@${remotehost}:${targetdirectory}/db ."
		echo "Executing: ${run}"
		$run
		;;
	*)
	echo "(upload|download) hostname"
	;;
esac

exit 0
