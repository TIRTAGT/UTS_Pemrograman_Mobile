#!/bin/bash

echo "Starting Gradle build tools for Linux"
./gradlew installDebug

retVal=$?
if [ $retVal -ne 0 ]; then
	echo "Gradle build tools for Linux failed !"
	exit 0
fi
echo "Using Android Debug Bridge to start apk..."

adb shell am start xyz.tirtagt.matthew_kampus.utsmobileapps/.MainActivity