#!/bin/bash
INBOX_DIR=/tmp/phone-inbox
SMS_RECEIVER_SCRIPT="$PWD/tools/smsReceiver.js"

echo "Creating $INBOX_DIR dir"
mkdir -p "$INBOX_DIR"
echo "Killing previous instances of SMS Receiver"
killall -s 9 node
echo "Starting SMS receiver server in background on port 9000"
node "$SMS_RECEIVER_SCRIPT" &
