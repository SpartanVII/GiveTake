# Setup

Step 1: Install Android Studio and [create an  Android Virtual Device (AVD)](https://developer.android.com/studio/run/managing-avds#createavd)
  - We recommend to use the Pixel 4 with the api 27 (Oreo)
  - Sign in to the Play Store with a valid account in the new AVD 

Step 2: Create a Firebase project by following the instructions given [here](https://firebase.google.com/docs/android/setup#console). Register app with package name 'com.example.givetake'. Download google-services.json and put it in the app directory.

Step 3: In Firebase Console -
  - Go to Authentication > Sign-in method. Enable Mail/password in sign-in providers.
  - Go to Realtime Database. Create a database and start in test mode.
  - Go to Storage > Get started. Set up Cloud Storage.

Step 4: Create a  new project in the Google Cloud Platform by following the instructions given [here](https://developers.google.com/maps/documentation/embed/cloud-setup#create). Then obtain the [map API key](https://developers.google.com/maps/documentation/embed/get-api-key#creating-api-keys) and finally in the properties file put the key as follows MAPS_API_KEY=xxxxxxxxxx

Step 5: Build and run your app. That's it! You will need two or more accounts to see the products of other users. 
