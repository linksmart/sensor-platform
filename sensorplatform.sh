#!/bin/sh

username=administrator

# TODO add rtc

case $1 in
	"dependencies")
		# install dependencies
		mkdir /home/${username}/Downloads
		echo "Install dependencies"
		sh Resources/Firmware/RaspberryPi3/Programs/install_java_maven.sh
		sh Resources/Firmware/RaspberryPi3/Programs/install_bluez.sh
		sh Resources/Firmware/RaspberryPi3/Programs/install_surfstick_drivers.sh
		echo "Please reboot the system NOW before going on with the installation guide!"
		;;
	"export")
		# create folders
		echo "Create folders"
		mkdir -p /home/${username}/Sensorplatform/bin
		mkdir /home/${username}/Sensorplatform/staticResources
		mkdir /home/${username}/Sensorplatform/db
		
		# copy files
		echo "Copy files"
		cp -r SensorplatformApp/staticResources ../../Sensorplatform
		cp -r SensorplatformApp/db ../../Sensorplatform
		cp SensorplatformApp/target/resources/* ../../Sensorplatform/bin
		# copy udev rule for surfstick
		cp Resources/Firmware/RaspberryPi3/System/70-huawei_e352.rules /etc/udev/rules.d
		# copy start script for remote debugger
		cp Resources/Firmware/RaspberryPi3/System/debug.sh ../../Sensorplatform
		
		echo "configure autostart"
		# setup systemd service for sensorplatform (autostart)
		cp Resources/Firmware/RaspberryPi3/System/sensorplatform /etc/init.d
		chmod +x /etc/init.d/sensorplatform
		update-rc.d sensorplatform defaults
		;;
	"start")
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null
		cd /home/${username}/Sensorplatform
		echo "Start the sensorplatform application"
		sudo /lib/jvm/jdk1.8.0_101/bin/java -cp "bin/*" de.fhg.fit.biomos.sensorplatform.main.Main
		;;
	"startbackground")
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null
		echo "Starting the sensorplattform application as background service"
		/etc/init.d/sensorplatform start
		;;
	*)
		echo "This is the Sensorplatform script for working with the application. Always run this script with sudo!"
		echo ""
		echo "Usage (dependencies|export|start|startbackground)"
		echo ""
		;;
esac

exit 0
