#!/bin/bash

# Source env.sh
source env.sh

# spin up screen with the start-java.sh script
/usr/bin/screen -S $NAME -d -m ./start-java.sh
