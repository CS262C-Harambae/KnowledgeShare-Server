#!/bin/bash

# source the env.sh file
source env.sh

# Write our PID file
echo $$ > $DIR/$USER-$NAME.pid

# Change to our working directory
cd $DIR

# Run this script to compile/start the cs262 data service.
javac -cp "../lib/*" edu/calvin/cs262/Article.java edu/calvin/cs262/NewsResource.java
java -cp ".:../lib/*" edu.calvin.cs262.NewsResource