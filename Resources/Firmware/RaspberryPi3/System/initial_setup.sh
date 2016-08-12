#!/bin/bash

cmd=$1

username=administrator
old=pi

function _usersetup {
	run=useradd -m ${username} -G sudo
	# other groups required? pi has some more:
	# adm dialout cdrom audio video plugdev games users input netdev spi i2c gpio
	echo "executing: $run"
	$run
	run=passwd ${username}
	echo "executing: $run"
	echo "Choose a password for ${username} and write it down!"
	$run
	run=deluser -remove-home ${old}
	echo "executing: $run"
	$run
	mkdir /home/${username}/Downloads
	mkdir /home/${username}/Repositories
	mkdir /home/${username}/Keytool
	mkdir /home/${username}/Sensorplatform
	run=sed -i -- 's/${old}/${username}/g' /etc/sudoers
	echo "executing: $run"
	$run
	run=sed -i -- 's/PermitRootLogin without-password/PermitRootLogin no/g' /etc/ssh/sshd_config
	echo "executing: $run"
	$run
}

function _networksetup {
	echo "Choose your desired network connection for the Sensorplatform."
	echo "Currently the following configurations are supported:"
	echo "(default|ethernet_static|wifi_dhcp)"
	echo "default: Sensorplatform gets an IP address from DHCP server in your network. This expects a network with net mask 255.255.255.0"
	echo "ethernet_static: E.g. Fraunhofer FIT internal network"
	echo "wifi_dhcp: E.g. Wifi at home"
	read -p "Choose a setup: " net
	case ${net} in
		default)
			;;
		ethernet_static)
			echo "Enter your network information now:"
			read -p "Enter IP address + net mask CIDR notation (e.g. 129.26.160.38/21): " ip
			read -p " Enter gateway IP: " gate
			read -p "Enter domain name server IP: " dns
			echo "# static settings for eth0" >> /etc/dhcpcd.conf
			echo "interface eth0" >> /etc/dhcpcd.conf
			echo "static ip_address=${ip}" >> /etc/dhcpcd.conf
			echo "# e.g. net mask 255.255.248.0 == /21 CIDR notation" >> /etc/dhcpcd.conf
			echo "static routers=${gate}" >> /etc/dhcpcd.conf
			echo "static domain_name_servers=${dns}" >> /etc/dhcpcd.conf
			;;
		wifi_dhcp)
			echo "Enter your network information now:"
			read -p "Enter wifi SSID: " ssid
			read -p "Enter password: " pw
			wpa_passphrase ssid pw >> /etc/wpa_supplicant/wpa_supplicant.conf
			;;
		* ) 
			echo "Wrong input. User will configure network settings manually."
			;;
	esac
	echo "Restart your Raspberry Pi 3 now!"
}

function _repositorysetup {
	echo "Cloning repository to /home/${username}/Repositories/Sensorplatform"
	cd /home/${username}/Repositories
	git clone http://scm.fit.fraunhofer.de:8080/scm/git/Sensorplatform Sensorplatform
}

case ${cmd} in
	"user")
		_usersetup
		;;
	"network")
		_networksetup
		;;
	"repository")
		_repositorysetup
		;;
	*)
		echo "(user|network|repository)"
		;;
esac

exit 0
