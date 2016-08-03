#!/bin/bash

# always run as sudo

# newest version (19.07.2016)
version="5.41"
url="http://www.kernel.org/pub/linux/bluetooth/bluez-${version}.tar.xz"

echo "Installing Bluez Bluetooth stack"
echo "version to be installed: ${version}"
echo "from: ${url}"

# install dependencies
echo "Update package manager"
apt-get update
echo "Installing dependencies"
#  D-Bus-1.10.8 GLib-2.48.1, and libical-2.0.0 
# apt-get install -y dbus glib libical
apt-get install -y libusb-dev libdbus-1-dev libglib2.0-dev libudev-dev libical-dev libreadline-dev

# download bluez
echo "Downloading Bluez"
mkdir /home/${USER}/bluez
cd /home/${USER}/bluez
wget ${url}
tar xvf bluez-${version}.tar.xz
cd bluez-${version}

# install
echo "Installing Bluez"
./configure
make
make install

# system deamon update to new version
echo "Restarting daemon"
systemctl daemon-reload
systemctl restart bluetooth

# run at boot automatically
systemctl enable bluetooth

# print result at the end, should be new version now
systemctl status bluetooth

# delete down
echo "Cleanup"
cd /home/${USER}
rm -r /home/${USER}/bluez

echo "Finished"
exit 0