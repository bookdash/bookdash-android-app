#!/bin/bash
#
# source https://github.com/zielmicha/adb-wrapper
#
# argument: apk package
# Set permission android.permission.SET_ANIMATION_SCALE for each device.
# ex: sh set_animation_permissions.sh <package>
#

adb=$ANDROID_HOME/platform-tools/adb
package=$1

if [ "$#" = 0 ]; then
    echo "No parameters found, run with sh set_animation_permissions.sh <package>"
    exit 0
fi

# get all the devices
devices=$($adb devices | grep -v 'List of devices' | cut -f1 | grep '.')

for device in $devices; do
    echo "Setting permissions to device" $device "for package" $package
    $adb -s $device shell pm grant $package android.permission.SET_ANIMATION_SCALE
done