#!/bin/sh

username=administrator

case $1 in
	"start")
		# run this to see the log output in real time
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null 2>&1
		sudo killall java
		echo "Start the sensorplatform application"
		sudo /home/administrator/lslc-core/lslc-0.4.0-SNAPSHOT_linux_arm/device-gateway
		;;
	"stop")
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null 2>&1
		sudo killall java
		;;
	
	*)
		echo "This is the Linksmart script for working with the application. It is intended to be executed only on the sensorplatform linux system. Always run this script with sudo!"
		echo ""
		echo "Valid commands: (start|stop)"
		echo ""
		;;
esac

exit 0
