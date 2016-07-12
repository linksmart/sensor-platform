#!/bin/bash

# install dependencies
echo "Installing dependencies"
sudo apt-get update
sudo apt-get install -y libusb-dev libdbus-1-dev libglib2.0-dev libudev-dev libical-dev libreadline-dev

# download bluez
echo "Downloading Bluez"
cd /home/pi
wget wget http://www.kernel.org/pub/linux/bluetooth/bluez-5.39.tar.xz
tar xvf bluez-5.39.tar.xz
rm bluez-5.39.tar.xz
cd bluez-5.37

# install
echo "Installing Bluez"
./configure
make
make install

echo "additional manual configuration required!"
echo "sudo nano /lib/systemd/system/bluetooth.service"
echo "change line"
echo "ExecStart=/usr/local/libexec/bluetooth/bluetoothd"
echo "to"
echo "ExecStart=/usr/local/libexec/bluetooth/bluetoothd --experimental"
echo ""
echo "sudo systemctl daemon-reload"
echo "systemctl restart bluetooth"
echo "reboot recommended"
echo "systemctl status bluetooth (for checking)"