#!/bin/bash
TMP_MSG_FILE=/tmp/phone-server-msg
PHONE_DEV=/dev/ttyUSB0

rm -f $TMP_MSG_FILE 
scmxx -d $PHONE_DEV --sms --get --remove --slot "unread" --out $TMP_MSG_FILE 2> /dev/null

scala -cp tools/scala ParseFile "$TMP_MSG_FILE"

exit 0
