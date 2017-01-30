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
		if [ -d /home/${username}/export ]
		then
			echo "Stop any running sensorplatform application"
			/etc/init.d/sensorplatform stop > /dev/null 2>&1
			echo "delete old files and folders"
			rm -r /home/${username}/export
		else
			echo "no previous installation found"
		fi
	
		# create folders
		echo "Create folders"
		mkdir -p /home/${username}/export/bin
		mkdir /home/${username}/export/staticResources
		mkdir /home/${username}/export/db
		
		# copy files
		echo "Copy files"
		cp -r SensorplatformApp/staticResources ../../export
		cp -r SensorplatformApp/db ../../export
		cp SensorplatformApp/target/resources/* ../../export/bin
		# make scp able to overwrite them when copied from windows for testing
		echo "Setting permissions"
		chmod -R 777 ../../export/bin
		chmod -R 777 ../../export/db
		chmod -R 777 ../../export/staticResources
		# copy start script for remote debugger
		cp Resources/Firmware/RaspberryPi3/System/debug.sh ../../export
		
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
		echo "Assuming the current time is set correctly by NTP"
		echo "Using system time to set hardware clock"
		hwclock -w
		echo "System time set"
		;;
	"start")
		# run this to see the log output in real time
		echo "Stop any running sensorplatform application"
		/etc/init.d/sensorplatform stop > /dev/null 2>&1
		cd /home/${username}/export
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
		if [ -d /home/${username}/export ]
		then
			echo "Stop any running sensorplatform application"
			/etc/init.d/sensorplatform stop > /dev/null 2>&1
			echo "delete old binary files in export directory"
			rm -r /home/${username}/export
		else
			echo "no previous installation found"
		fi
	
		# create folders
		echo "Create folders"
		mkdir -p /home/${username}/export/bin
		mkdir /home/${username}/export/staticResources
		mkdir /home/${username}/export/db
		chmod -R 777 ../../export/bin
		chmod -R 777 ../../export/db
		chmod -R 777 ../../export/staticResources
		;;
	*)
		echo "This is the Sensorplatform script for working with the application. It is intended to be executed only on the sensorplatform linux system. Always run this script with sudo!"
		echo ""
		echo "Valid commands: (dependencies|export|time|start|startbackground|stop|reset)"
		echo ""
		;;
esac

exit 0
