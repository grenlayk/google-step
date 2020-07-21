#!/bin/bash

(mvn package)

if [[ $? -ne 0 ]]; then
  echo "Error in compiling"
  exit 1
fi
echo "Compiled"

name=$(cat SERVLET_NAME)
scriptFolder=$(cat TOMCAT_FOLDER)
from="target/$name.war"
to="$scriptFolder/webapps/$name.war"
toDel="$scriptFolder/webapps/$name"

rm -rf $toDel

if [[ $? -ne 0 ]]; then
  echo "Error in deleting"
  exit 1
fi
echo "Deleted"

cp $from $to

if [[ $? -ne 0 ]]; then
  echo "Error in copying"
  exit 1
fi
echo "Copied"

($scriptFolder/bin/shutdown.sh) >/dev/null 2>&1
if [[ $? -ne 0 ]]; then
  echo "Error in shutdown"
  exit 1
fi
echo "Shutdown"

($scriptFolder/bin/startup.sh) >/dev/null 2>&1
if [[ $? -ne 0 ]]; then
  echo "Error in starting"
  exit 1
fi
echo "Started"
