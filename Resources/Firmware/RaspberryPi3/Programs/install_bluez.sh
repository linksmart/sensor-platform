#!/bin/bash

# always run as sudo

# newest version (19.07.2016)
version="5.41"
url="http://www.kernel.org/pub/linux/bluetooth/bluez-${version}.tar.xz"

username=administrator

echo "Installing Bluez Bluetooth Stack"
echo "version to be installed: ${version}"
echo "from: ${url}"

# install dependencies
echo "Update package manager"
apt-get update
echo "Installing dependencies"
apt install -y libglib2.0-dev libdbus-1-dev libudev-dev libical-dev libreadline-dev

# download bluez
echo "Downloading Bluez"
cd /home/${username}/Downloads
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
rm -r /home/${username}/bluez

echo "Finished installing Bluez"
exit 0