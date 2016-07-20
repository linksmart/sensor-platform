#! /bin/sh

# controls the green onboard LED of the Raspberry Pi 3

case "$1" in
	timer)
		echo "$1" > /sys/class/leds/led0/trigger
		echo 1000 >/sys/class/leds/led0/delay_on
		echo 1000 >/sys/class/leds/led0/delay_off
		echo "Green onboard LED blink slow"
		;;
	heartbeat)
		echo "$1" > /sys/class/leds/led0/trigger
		echo "Green onboard LED heartbeat"
		;;
	none)
		echo "$1" > /sys/class/leds/led0/trigger
	echo "Green onboard LED off"
	;;
	*)
	echo "(timer|heartbeat|none)"
	;;
esac

exit 0
