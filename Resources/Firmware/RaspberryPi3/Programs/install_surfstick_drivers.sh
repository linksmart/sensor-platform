#!/bin/bash

apt install -y wvdial comgt

rm /etc/wvdial.conf

cp /home/administrator/Repositories/Sensorplatform/Resources/Firmware/RaspberryPi3/System/wvdial.conf /etc
