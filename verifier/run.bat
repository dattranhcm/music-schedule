#!/bin/bash
# go to target folder that content the jar file to run
cd ../target
# run jar file with one argument (the url of input file)
java -jar music-schedule-1.0-SNAPSHOT-jar-with-dependencies.jar %1
# go back
cd ../verifier