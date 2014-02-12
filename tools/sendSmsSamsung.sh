#!/bin/bash
NUMBER=$1
MESSAGE=$2

BASE_URL="http://192.168.0.16:9090/sendsms"
URL="$BASE_URL?phone=$NUMBER&password="
echo $URL
curl "$URL" --data-urlencode "text=$MESSAGE"
