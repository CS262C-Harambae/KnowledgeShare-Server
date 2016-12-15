#!/bin/bash

# source env.sh
source env.sh

# Write our PID file
/usr/bin/pkill -P `cat $DIR/$USER-$NAME.pid`

# sleep 1 second
sleep 1

# clean up screen
/usr/bin/screen -wipe 
