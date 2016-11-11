#!/bin/sh

cmd=$1

username=administrator

case ${cmd} in
	"install")
		sh Resources/Firmware/RaspberryPi3/Programs/install_java_maven.sh
		sh Resources/Firmware/RaspberryPi3/Programs/install_bluez.sh
		sh Resources/Firmware/RaspberryPi3/Programs/install_wvdial.sh
		
		cd ../SensorplatformApp
		cp -r db /home/${username}/Sensorplatform
		cp -r staticResources /home/${username}/Sensorplatform
		mkdir /home/${username}/Sensorplatform/bin
		cd target/resources
		cp *.jar /home/${username}/Sensorplatform/bin
		cd ../../..
		cp Resources/Firmware/RaspberyyPi3/System/run.sh /home/${username}/Sensorplatform
		cp Resources/Firmware/RaspberyyPi3/System/debug.sh /home/${username}/Sensorplatform
		
		cp Resources/Firmware/RaspberyyPi3/System/sensorplatform /etc/init.d
		update-rc.d /etc/init.d/sensorplatform defaults
		;;
	"run")
		/etc/init.d/sensorplatform stop
		cd /home/${username}/Sensorplatform
		sh run.sh
		;;
	*)
		echo "This is the Sensorplatform script for setting up the software and environment!"
		echo "Usage (install|run)"
		;;
esac

exit 0

#Export script for sensorplatform
#/home/pi/Sensorplatform/bin/*.jar
#/home/pi/Sensorplatform/db/*.*
#/home/pi/Sensorplatform/staticResources/*.*
#/home/pi/Sensorplatform/run.sh
