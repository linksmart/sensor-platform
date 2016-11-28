#!/bin/sh

username=administrator

case $1 in
	"dependencies")
		# install dependencies
		mkdir /home/${username}/Downloads
		echo "Install dependencies"
		sh Resources/Firmware/RaspberryPi3/Programs/install_java_maven.sh
		sh Resources/Firmware/RaspberryPi3/Programs/install_bluez.sh
		sh Resources/Firmware/RaspberryPi3/Programs/install_surfstick_drivers.sh
		sh -c "echo 'dtoverlay=i2c-rtc,ds3231\n' >> /boot/config.txt"
		echo "Please reboot the system NOW before going on with the installation guide!"
		;;
	"export")
		# delete old files
		if [ -d /home/${username}/Sensorplatform ]
		then
			echo "Stop any running sensorplatform application"
			/etc/init.d/sensorplatform stop > /dev/null 2>&1
			echo "delete old files and folders"
			rm -r /home/${username}/Sensorplatform
		else
			echo "no previous installation found"
		fi
	
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
		# make scp able to overwrite them when copied from windows for testing
		echo "Setting permissions"
		chmod -R 777 ../../Sensorplatform/bin
		chmod -R 777 ../../Sensorplatform/db
		chmod -R 777 ../../Sensorplatform/staticResources
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
	"time")
		echo "Configuring time and date"
		echo "Remove fake-hwclock"
		apt purge fake-hwclock
		echo "Remove ntp from autostart"
		update-rc.d -f ntp remove
		hwclock
		echo "Assuming the current time is set correctly by ntp"
		echo "Using system time to set hardware clock"
		hwclock -w
		;;
	"start")
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null 2>&1
		cd /home/${username}/Sensorplatform
		echo "Start the sensorplatform application"
		sudo /lib/jvm/jdk1.8.0_101/bin/java -cp "bin/*" de.fhg.fit.biomos.sensorplatform.main.Main
		;;
	"startbackground")
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null 2>&1
		echo "Starting the sensorplattform application as background service"
		/etc/init.d/sensorplatform start
		;;
	"stop")
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null 2>&1
		;;
	"reset")
		# delete old files
		if [ -d /home/${username}/Sensorplatform ]
		then
			echo "Stop any running sensorplatform application"
			/etc/init.d/sensorplatform stop > /dev/null 2>&1
			echo "delete old files and folders"
			rm -r /home/${username}/Sensorplatform
		else
			echo "no previous installation found"
		fi
	
		# create folders
		echo "Create folders"
		mkdir -p /home/${username}/Sensorplatform/bin
		mkdir /home/${username}/Sensorplatform/staticResources
		mkdir /home/${username}/Sensorplatform/db
		chmod -R 777 ../../Sensorplatform/bin
		chmod -R 777 ../../Sensorplatform/db
		chmod -R 777 ../../Sensorplatform/staticResources
		;;
	*)
		echo "This is the Sensorplatform script for working with the application. It is intended to be executed only on the sensorplatform linux system. Always run this script with sudo!"
		echo ""
		echo "Valid commands: (dependencies|export|start|startbackground|stop|reset)"
		echo ""
		;;
esac

exit 0
