#!/bin/bash

echo -n "" > /dev/ttyS1
sleep 2
brcm_patchram_plus -d  --patchram /lib/firmware/ap6210/bcm20710a1.hcd --enable_hci --bd_addr 11:22:33:44:55:66 --no2bytes --tosleep 1000 /dev/ttyS1
sleep 2
hciattach /dev/ttyS1 any
sleep 2
hcitool dev
hciconfig -a
