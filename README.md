# Galactic Odds Calculator Application

Hello there! 🚀 Welcome to the Galactic Odds Calculator application. This guide will walk you through getting the application up and running in no time! 🌌

## Overview

Our application comprises three main components:
1. **Backend** - The core logic for calculating odds.
2. **Frontend** - A user-friendly interface for easy interaction.
3. **CLI** - A command-line interface for direct access through the terminal.

## Prerequisites

Before starting, ensure you have the following installed:
- Java (JDK 11 or newer) ☕
- Node.js and npm (for the frontend) 📦
- Your favorite IDE or text editor 📝

## Running the Backend

To start the backend server:

1. Navigate to the backend directory:
   ```bash
   cd backend
   ./gradlew bootRun
   ```
On Windows, use 
    ```gradlew.bat bootRun
    ```
## Running the frontend

To start the backend server:

1. Navigate to the backend directory:
   ```bash
   cd frontend
   ```
2. Install the dependencies:
   ```bash
   npm install
   ```   
3. Start the application:
   ```bash
   npm start
   ```
## Running the cli

To start the backend server:

1. Navigate to the root directory:
2. Run give-me-the-odds script
   On Unix-like Systems (Linux, macOS):
   Ensure the script is executable:
```bash
   ./give-me-the-odds.sh /absolute-path/to/empire.json /absolute-path/to/config.json
```
   And run the script :
```bash
   ./give-me-the-odds.sh /absolute-path/to/empire.json /absolute-path/to/config.json
```

On Windows:
```bash
   give-me-the-odds.bat \absolute-path\to\empire.json \absolute-path\to\config.json
```

P.S: 
Always build on root project since cli has backend as a dependency.
To fix spotless style violations, run "./gradlew spotlessApply"


