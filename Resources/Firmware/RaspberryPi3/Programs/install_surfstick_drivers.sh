#!/bin/bash

# install wvdial and comgt
# wvdial is used for the mobile internet connection (it also installs ppp package)
# comgt does some settings for the serial port to the surfstick /dev/ttyUSB0 and so on
apt install -y wvdial comgt

# remove default wvdial configuration
rm /etc/wvdial.conf

# copy wvdial configuration file (specific setup for telekom provider)
cp /home/administrator/Repositories/Sensorplatform/Resources/Firmware/RaspberryPi3/System/wvdial.conf /etc

# copy udev rule for surfstick (specific setup for the huawei E352S-5 surf stick)
cp /home/administrator/Repositories/Sensorplatform/Resources/Firmware/RaspberryPi3/System/70-huawei_e352.rules /etc/udev/rules.d

# use mobile internet connection for outgoing network traffic (uploading samples for example)
# remove both lines in this file to revert this change and restart the connection
sh -c "echo 'defaultroute\n' >> /etc/ppp/peers/wvdial"
sh -c "echo 'replacedefaultroute\n' >> /etc/ppp/peers/wvdial"

exit 0