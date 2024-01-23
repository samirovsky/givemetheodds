#!/bin/bash

# Check if two arguments are passed
if [ "$#" -ne 2 ]; then
    echo "Usage: ./give-me-the-odds.sh <path-to-empire.json> <path-to-config.json>"
    exit 1
fi

./gradlew build > /dev/null
# Run the Java CLI application and filter the output
java -jar ./cli/build/libs/cli.jar "$1" "$2" | awk -F "The odds are:" '/The odds are:/ {print $2}'
