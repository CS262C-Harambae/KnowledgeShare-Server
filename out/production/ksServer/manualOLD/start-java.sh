#!/bin/bash

# Environment variables
NAME=Lab09
DIR=/var/cs262/$NAME/src/
PID=$DIR/$USER-$NAME.pid

# Write our PID file
echo $$ > $DIR/$USER-$NAME.pid

# Change to our working directory
cd $DIR

# Run this script to compile/start the cs262 data service.
javac -cp "../lib/*" edu/calvin/cs262/Player.java edu/calvin/cs262/MonopolyResource.java
java -cp ".:../lib/*" edu.calvin.cs262.MonopolyResource
