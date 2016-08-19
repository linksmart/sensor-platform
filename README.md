# Sensorplatform

Software for an extendible platform used for aggregating and transfering sensor data.

Masterthesis, Computer Science, Daniel Pyka, 2016

## Raspberry Pi 3 Installation Guide

1. Download the Raspbian Lite(!) Image from here:
https://www.raspberrypi.org/downloads/raspbian/
This is a more lightweight, HEADLESS version of raspbian.

2. Extract the file and install the image file on a micro-SD card
You can use a program of your choice, e.g. https://sourceforge.net/projects/win32diskimager/

3. Put the micro-SD card into the Raspberry Pi 3 (RPI3)

4. Connect a monitor, keyboard, power supply and network cable to the RPI3

5. Log in using the default credentials
Username: pi
Password: raspberry
(Attention: The default keyboard layout is british, so y becomes z and vice versa)

6. Enter "sudo raspi-config" (always without quotes) for some general settings
6.1 Internationalisation Options >> Change Timezone >> Europe >> Berlin
6.2 Internationalisation Options >> Change Keyboard Layout >> Generic 105-key (Intl) PC >> Other >> German >> German >> The default for the keyoard layout >> No compose key
6.3 Internationalisation Options >> Change Wi-fi Country >> DE Germany
6.4 Change User Password >> <your new password>
6.5 Boot Options >> B1 Console
6.6 Wait for Network at Boot >> No
6.7 Advanced Options >> Hostname
6.8 Advanced Options >> Memory Split >> 16
6.9 Expand File System
6.10 Finish >> Reboot

7. User account setup
7.1 Type in "sudo -i" for the root shell
7.2 Add a new user administrator, add him to group sudo: "useradd -m administrator -G sudo" (do not choose another name)
##### other groups required? pi has some more:
##### adm dialout cdrom audio video plugdev games users input netdev spi i2c gpio
7.3 Set a password for administrator "passwd administrator"
7.4 Set a password for root "passd root"
7.5 Log out by typing "exit" and again "exit"
7.6 Log in as root
7.7 Delete the default user pi "deluser -remove-home pi"
7.8 Log out by typing "exit"
7.9 Log in as administrator
7.10 Move to administrator's home directory "cd /home/administrator" and create some folders
	"mkdir Downloads"
	"mkdir Repositories"
	"mkdir Keytool"
	"mkdir Sensorplatform"
7.11 Allow administrator to use sudo without password check (password will be required ones)
	"sudo sed -i -- 's/pi/administrator/g' /etc/sudoers"
7.12 Do not allow root login via SSH
	"sudo sed -i -- 's/PermitRootLogin without-password/PermitRootLogin no/g' /etc/ssh/sshd_config"

8. Update packages
TODO sudo apt-mark hold bluez ???
8.1 "sudo apt update"
8.2 "sudo apt upgrade"
8.3 Install git "sudo apt install git"
8.4 "sudo reboot" and log in as administrator again

9. (optional) setup for network (Sensorplatform Fraunhofer FIT example configuration)
9.1 Setting the hostname is sufficient for connecting to the internet in the internal network of Fraunhofer FIT
9.2 static ip:
	echo "interface eth0" >> /etc/dhcpcd.conf (keep quotes in this section, replace eth0 with wlan0 in case of wifi)
	echo "static ip_address=129.26.160.38/21" >> /etc/dhcpcd.conf (replace 129.26.160.38/21 with your settings, /21 is CIDR notation for net mask, equivalent to 255.255.248.0)
	echo "static routers=129.26.160.1" >> /etc/dhcpcd.conf (replace 129.26.160.1 with your data)
	echo "static domain_name_servers=129.26.165.177" >> /etc/dhcpcd.conf (replace 129.26.165.177 with your data)
9.3 wifi login credentials: wpa_passphrase "ssid" "pw" >> /etc/wpa_supplicant/wpa_supplicant.conf (replace ssid and pw with your actual data)

10. "sudo reboot" and log in as administrator again

11. "cd /home/administrator/Repositories"
11.1 "git clone http://scm.fit.fraunhofer.de:8080/scm/git/Sensorplatform Sensorplatform"
	Use some valid login credentials here
11.2 "cd Sensorplatform"

12. Install Java and Maven "sudo sh Resources/Firmware/RaspberryPi3/Programs/install_java_maven.sh"

13. Install Bluez "sudo sh Resources/Firmware/RaspberryPi3/Programs/install_bluez.sh"

14. Install Iptables "sudo sh Resources/Firmware/RaspberryPi3/Programs/install_iptables.sh"

15. "sudo reboot" >> log in as administrator >> "cd /home/administrator/Repositories/Sensorplatform"

16. Maven clean install with Profiles
16.1 cd SensorplatformParent
16.2 mvn clean install -Praspberrypi3,telipro

16. Export
16.1 "cd .."
16.2 "sudo sensorplatform.sh --export"

TODO
init.d shell script
Sensorplatform:
bin
db
static resources
what about keystore file?

16.3 "sudo reboot"
16.4 The sensorplatform application will now start automatically after boot

17. Open https://sensorplatform.fit.fraunhofer.de:8080 in Chrome/Firefox/Edge(or https://129.26.160.38:8080 in case of static IP)

## Local Windows Installation Guide

Since there are no Linux command line tools available on windows, test classes with mock-data are used.
