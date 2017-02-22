Sensorplatform

Software for an extendible platform used for aggregating and transfering sensor data.

Masterthesis, Computer Science, Daniel Pyka, 2016-2017

Raspberry Pi 3 Installation Guide

Prerequisites:
Raspberry Pi 3
Huawei Telekom Surfstick E352S-5 (with active contract)
Power supply
Sensors
Windows or Linux
IDE: eclipse or intellij
either separate maven installation available on command line or inbuild from the IDE
Gitscm installation (from opsi Client Kiosk)
At least read access http://scm.fit.fraunhofer.de:8080/scm/git/Sensorplatform

Network access @Fraunhofer FIT
There is no DHCP server in the Fraunhofer FIT network. Instead, static IP addresses are used for specific hostname and MAC adress.
The following device + hostname is already registered and may be used for the sensorplatform:
MAC: b8:27:eb:27:b4:27
hostname: sensorplatform

By using this configuration, you do not need any other network setup.

1. Download the Raspbian Lite(!) Image from here:
https://www.raspberrypi.org/downloads/raspbian/
This is a more lightweight, HEADLESS version of raspbian.

2. Extract the file and install the image on a micro-SD card
You can use a program of your choice, e.g. https://sourceforge.net/projects/win32diskimager/

3. Put the micro-SD card into the Raspberry Pi 3 (RPI3)

4. Connect a monitor, keyboard, power supply and network cable to the RPI3

5. Log in using the default credentials
Username: pi
Password: raspberry
(Attention: The default keyboard layout is english, so y becomes z and vice versa)

6. Enter "sudo raspi-config" (always without quotes) for some general settings
6.1 Internationalisation Options >> Change Timezone >> Europe >> Berlin
6.2 Internationalisation Options >> Change Keyboard Layout >> Generic 105-key (Intl) PC >> Other >> German >> German >> The default for the keyoard layout >> No compose key
6.3 Internationalisation Options >> Change Wi-fi Country >> DE Germany
6.4 Change User Password >> <your new password>
6.5 Boot Options >> B1 Desktop / CLI >> B1 Console
6.6 Boot Options >> Wait for Network at Boot >> No
6.7 Advanced Options >> Hostname
6.8 Advanced Options >> Memory Split >> 16
6.9 Advanced Options >> SSH >> Yes
6.10 Finish >> Reboot >> Yes

7. User account setup
7.1 Login as pi, type in "sudo -i" for the root shell
7.2 Add a new user administrator, add him to group sudo: "useradd -m administrator -G sudo" (do not choose another name)
7.3 Set a password for administrator "passwd administrator" (default is "fit" without "")
7.4 Set a password for root "passwd root" (default is "fit" without "")
7.5 Log out by typing "exit" and again "exit"
7.6 Log in as root
7.7 Delete the default user pi "deluser -remove-home pi"
7.8 Allow administrator to use sudo without password check (password will be required ones)
	echo "administrator ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers.d/010_pi-nopasswd
7.9 Do not allow root login via SSH
	sed -i -- 's/PermitRootLogin without-password/PermitRootLogin no/g' /etc/ssh/sshd_config
	exit
7.10 Log in as administrator

--------------------------------optional-----------------------------------------	

8. network setup
8.1 static ip:
	echo "interface eth0" >> /etc/dhcpcd.conf (keep quotes in this section, replace eth0 with wlan0 in case of wifi)
	echo "static ip_address=129.26.160.38/21" >> /etc/dhcpcd.conf (replace 129.26.160.38/21 with your settings, /21 is CIDR notation for net mask, equivalent to 255.255.248.0)
	echo "static routers=129.26.160.1" >> /etc/dhcpcd.conf (replace 129.26.160.1 with your data)
	echo "static domain_name_servers=129.26.165.177" >> /etc/dhcpcd.conf (replace 129.26.165.177 with your data)
8.2 wifi login credentials: wpa_passphrase "ssid" "pw" >> /etc/wpa_supplicant/wpa_supplicant.conf (replace ssid and pw with your actual data)

---------------------------------------------------------------------------------

9. Update packages
9.1 "sudo apt update"
9.2 "sudo apt upgrade"
9.3 Install git "sudo apt install git"
9.4 "sudo reboot"

10. Log in as administrator again

11.	"mkdir /home/administrator/Repositories"
11.1 "cd /home/administrator/Repositories"
11.1 "git clone http://scm.fit.fraunhofer.de:8080/scm/git/Sensorplatform Sensorplatform"
	Use some valid login credentials here, if you do not have any, ask the IT helpdesk for it. You need at least reading access for the Sensorplatform repository!
11.2 "cd Sensorplatform"

12. Install tools and dependencies of the sensorplatform application
12.1 "sudo sh sensorplatform.sh dependencies"
12.2 "sudo reboot" (important!)

13. Log in as administrator again

14. Install the sensorplatform application. The installation can be slightly modified by using maven profiles:
	targetplatform: raspberrypi3 or cubieboard (cubieboard is not yet supported)
	uploader: webhrs or telipro or may not be specified at all
14.1 "cd /home/administrator/Repositories/Sensorplatform/SensorplatformParent"
14.2 Run maven for installation with profiles.
	Example1: "mvn clean install -P raspberrypi3,webhrs"
	Example2: "mvn clean install -P raspberrypi3"
	This may take a while especially if it is the first time.
14.3 Return to top-level directory of the repository: "cd .."

15. Export the binary files of the sensorplatform application
15.1 "sudo sh sensorplatform.sh export"
	Binary files are copied to /home/administrator/export .
	On the next reboot the sensorplatform application will start automatically as a background service (autostart with systemd).
15.2 Make sure the sensorplattform is connected to network (cable) to get the current time from NTP. Configure time related functionality afterwards:
	"sudo sh sensorplatform.sh time"
	This will set the hardware time from system time (which was once set correctly by NTP) and disable NTP afterwards.
	It also uninstalls the fake-hwclock package. On every boot the system time is set by the hardware time.
	
16. You may still want to use the sensorplatform.sh script to start the application for testing.
	"sudo sh sensorplatform.sh start" 
	"sudo sh sensorplatform.sh startbackground"
16.1 To stop the sensorplatform application use the command
	"sudo sh sensorplatform.sh stop"

The sensorplatform application will start automatically during boot. The webapplication is accessible from
https://sensorplatform.fit.fraunhofer.de:8080
https://129.26.160.38:8080 or another IP
hostname (sensorplatform) might be different, depending on your system settings
Attention: https not http and port 8080 not default port 80
ssh: administrator@sensorplatform.fit.fraunhofer.de

-------------------------------------------------------------------------------------------------------------
Working with the sensorplatform application:
Assuming you made some changes in the code on your computer and you want to test them on the sensorplatform.
1. Maven install the project on your PC
2. Run db.sh, uploadApp.sh and uploadStaticResources.sh in the directory <Repository>/Resources/Firmware/RaspberryPi3/ in the Git SCM shell
3. Choose parameters for command and hostname correctly, for example:
	sh uploadApp.sh sensorplatform
	sh uploadStaticResources.sh sensorplatform
	sh db.sh upload sensorplatform
	sh db.sh download sensorplatform

You may still commit the changes on your PC and pull them on the sensorplatform.
Afterwards you install the project with maven and export it with the sensorplatform.sh script.

Do not change version numbers from programs, user name or any other information unless you are fully aware of all implicit changes.
Some of these information are hardcoded in multiple installation files.
