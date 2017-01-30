#!/bin/bash

# always run as sudo

# newest version (29. Oktober 2016)
version="5.43"
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

# delete downloaded file
echo "Cleanup"
cd ..
rm -r /home/${username}/Downloads/bluez-${version}

echo "Finished installing Bluez"
exit 0