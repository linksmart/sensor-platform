#!/bin/bash

apt install -y wvdial

rm /etc/wvdial.conf

cp /home/administrator/Repositories/Sensorplatform/Resources/Firmware/RaspberryPi3/System/wvdial.conf /etc
