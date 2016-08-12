#!/bin/sh

sudo timeout -s SIGINT "$1"s hcitool lescan

exit 0
