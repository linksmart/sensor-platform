#! /bin/sh

# controls the green onboard LED of the Raspberry Pi 3

case "$1" in
	blink)
		echo "timer" > /sys/class/leds/led0/trigger
		echo 1000 >/sys/class/leds/led0/delay_on
		echo 1000 >/sys/class/leds/led0/delay_off
		echo "Green onboard LED blink"
		;;
	heartbeat)
		echo "heartbeat" > /sys/class/leds/led0/trigger
		echo "Green onboard LED heartbeat"
		;;
	error)
		echo "timer" > /sys/class/leds/led0/trigger
		echo 200 >/sys/class/leds/led0/delay_on
		echo 200 >/sys/class/leds/led0/delay_off
		echo "Green onboard LED error blink"
		;;
	off)
		echo "none" > /sys/class/leds/led0/trigger
	echo "Green onboard LED off"
	;;
	*)
	echo "(blink|off|heartbeat)"
	;;
esac

exit 0
