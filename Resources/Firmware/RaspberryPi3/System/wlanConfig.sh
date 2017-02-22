#! /bin/sh

cp /etc/wpa_supplicant/wpa_supplicant.conf.backup /etc/wpa_supplicant/wpa_supplicant.conf
wpa_passphrase $1 $2 >> /etc/wpa_supplicant/wpa_supplicant.conf
ifdown wlan0 
sleep 2
ifup wlan0
	

exit 0
