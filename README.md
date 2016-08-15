# Sensorplatform

Software for an extendible platform used for aggregating and transfering sensor data.

## INITIAL SETUP

login pi : raspberry
sudo -i

useradd -m administrator -G sudo
# other groups required? pi has some more:
# adm dialout cdrom audio video plugdev games users input netdev spi i2c gpio
passwd ${username}
passwd root

logout pi

login root : pw
deluser -remove-home pi
logout root

login administrator : pw

sudo apt update
sudo apt upgrade
sudo apt install git

cd /home/administrator
mkdir Downloads
mkdir Repositories
mkdir Keytool
mkdir Sensorplatform
sed -i -- 's/administrator/pi/g' /etc/sudoers
sed -i -- 's/PermitRootLogin without-password/PermitRootLogin no/g' /etc/ssh/sshd_config

echo "interface eth0" >> /etc/dhcpcd.conf
echo "static ip_address=129.26.160.38/21" >> /etc/dhcpcd.conf
echo "static routers=129.26.160.1" >> /etc/dhcpcd.conf
echo "static domain_name_servers=129.26.165.177" >> /etc/dhcpcd.conf

wpa_passphrase ssid pw >> /etc/wpa_supplicant/wpa_supplicant.conf

cd /home/administraotr/Repositories
git clone http://scm.fit.fraunhofer.de:8080/scm/git/Sensorplatform Sensorplatform
