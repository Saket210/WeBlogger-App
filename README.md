# WeBlogger

Welcome to the **WeBlogger** repository! This Android project uses Firebase as the database. This README file provides all the necessary information to get started with the project, including setup instructions, project structure, and key features.

## Project Overview

**WeBlogger** is an Android application for realtime blogging experience. It leverages Firebase for authentication, real-time database, and storage to provide a seamless user experience.

## Features

- **Authentication**: User registration and login with Firebase Authentication.
- **Real-time Database**: Store and sync user and blog data using Firebase Realtime Database.
- **Cloud Storage**: Store and retrieve user-generated content like profile photo.
- **User Access**: Users can create, edit, like, save and delete blog posts.

## Images
<p align="center">
  <img src="https://github.com/Saket210/WeBlogger-App/assets/85545013/6d617883-9e3a-4581-a4ae-9cabba492383" alt="Image 2" width="200"/>
  <img src="https://github.com/Saket210/WeBlogger-App/assets/85545013/46f8a355-7904-40cf-acbe-61dfdd0b2fb2" alt="Image 3" width="200"/>
  <img src="https://github.com/Saket210/WeBlogger-App/assets/85545013/0d9b5622-085b-41dd-ab1e-638fe71bba80" alt="Image 1" width="200"/>
</p>

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Android Studio installed on your development machine.
- A Google account to access Firebase services.
- Basic knowledge of Android development and Kotlin/Java programming.

## Installation

1. **Clone the repository**:

   ```sh
   git clone https://github.com/Saket210/WeBlogger-App.git
2. **Open the project in Android Studio**

- Launch Android Studio.
- Click on `File > Open`.
- Navigate to the cloned repository and select it.

3. **Sync the project with Gradle files**

- Click on `File > Sync Project with Gradle Files`.
   
## Firebase Configuration

1. **Create a Firebase project**

- Go to the [Firebase Console](https://console.firebase.google.com/).
- Click on `Add Project` and follow the on-screen instructions.

2. **Register your app with Firebase**

- In the Firebase console, open your project.
- Click on the Android icon to add an Android app to your Firebase project.
- Register the app with the package name of your Android app (e.g., `com.example.yourapp`).

3. **Download the `google-services.json` file**

- After registering the app, download the `google-services.json` file.
- Place the `google-services.json` file in the `app/` directory of your Android project.

4. **Add Firebase SDK to your project**

- Add firebase dependencies in your app/build.gradle file

5. **Sync your project to ensure all dependencies are downloaded.**

