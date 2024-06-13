#!/bin/bash

cd ~/Code/java/trackerV1

echo -n "> Compiling... "

javac -cp src:lib/mysql-connector-j-8.4.0.jar -d bin/ src/TrackerApp.java 

echo -e "Done.\n"

java -cp bin:lib/mysql-connector-j-8.4.0.jar TrackerApp

