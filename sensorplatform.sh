#!/bin/sh

cmd=$1
platform=$2

case ${cmd} in
	"setup")
		case ${platform} in
			"raspberrypi3")
				cd Resources/Firmware/RaspberryPi3/Programs
				sh install_java.sh
				sh install_maven.sh
				sh install_bluez.sh
				sh install_iptables.sh
				;;
			"cubieboard3")
				;;
			"windows")
				echo "Nothing to do"
				;;
			*)
				echo "setup (raspberrypi3|cubieboard3)"
				;;
		esac
		;;
	"install")
		case ${platform} in
			"raspberrypi3")
				cd SensorplatformParent
				mvn clean install -P raspberrypi3,telipro
				;;
			"cubieboard3")
				;;
			"windows")
				;;
			*)
				echo "install (raspberrypi3|cubieboard3|windows)"
				;;
		esac
		;;
	"export")
		;;
	"run")
		;;
	*)
		echo "This is the Sensorplatform script for setting up the software and environment!"
		echo "Usage (setup|install|export|run) [raspberrypi3|cubieboard|windows]"
		;;
esac

exit 0
