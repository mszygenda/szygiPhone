#!/bin/bash
INBOX_DIR=/tmp/phone-inbox
FILES=$INBOX_DIR/msg.*
for f in $FILES; do 
  if [ -f "$f" ]; then
    cat "$f"
    rm "$f"
    echo
    echo "####"
    echo
  fi
done
