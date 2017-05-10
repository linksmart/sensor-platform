#! /bin/sh

# ZTE MF190
ATTR{idVendor}=="19d2", ATTR{idProduct}=="1224", RUN+="usb_modeswitch '%b/%k'"

 idVendor=19d2, idProduct=2000
sudo /opt/sakis3g/sakis3g  connect --console APN="CUSTOM_APN" CUSTOM_APN=$1 APN_USER="user" APN_PASS=$2
sleep 2
	

exit 0
