#!/bin/bash
PHONE_NUMBER=$1
TRACK_NAME=$2
TMP_FOLDER=/tmp/phone-server-player

BASE_DIR="$PWD"
STOP_SOUNDS_CMD="$PWD/tools/stopSounds"
CALL_CMD="$PWD/tools/call"
SAY_IT_CMD="$PWD/tools/sayIt"
SWITCH_EQ_CMD="$PWD/tools/switchEqualizer"
SET_VOLUME_CMD="$PWD/tools/setVolume"
SET_VOLUME_DELAY_CMD="$PWD/tools/setVolumeDelay"

function createTmpFolder {
  if [ -d $TMP_FOLDER ]
  then
    clearTmpFolder
  else 
    mkdir $TMP_FOLDER
  fi
}

function clearTmpFolder {
  rm $TMP_FOLDER/*.mp3
}

function downloadTrack {
  DOWNLOAD_SUCCESS=0

  echo "Downloading track $TRACK_NAME"
  echo $TRACK_NAME > $TMP_FOLDER/tracks

  cd $TMP_FOLDER

  spotify-to-mp3 tracks
  SPOTIFY_RES=$?  

  cd $BASE_DIR


  if [ "$SPOTIFY_RES" -eq "0" ]; then
    DOWNLOAD_SUCCESS=1
  fi
}

function dialClient {
  echo "Calling $PHONE_NUMBER"
  $CALL_CMD $PHONE_NUMBER &
  sleep 2
}

function playTrack {
  $STOP_SOUNDS_CMD
#  $SWITCH_EQ_CMD "bass" enable
#  $SET_VOLUME_CMD 65555
#  $SET_VOLUME_DELAY_CMD 90000 26 &
#  $SWITCH_EQ_CMD "flat" enable

  if [ "$DOWNLOAD_SUCCESS" -eq "1" ]; then
    mplayer $TMP_FOLDER/*.mp3 -softvol
  else
    echo "Failed to download track"
    $SAY_IT_CMD 3 10 "szygi fołn nie znalazł tej piosenki. Ale się starał!" "ewa"
  fi
}

createTmpFolder
downloadTrack
dialClient
playTrack
