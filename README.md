# climate-watch
An Android app that displays weather information based on the user's location.
Readme

To run this project on Android Studio(https://developer.android.com/studio) do the following:
1. Clone this repository (https://github.com/michaeloki/climate-watch) on your machine
2. Extract the folder to your development directory
3. Open Android Studio and click on File>>Open
4. Choose the directory in step 2
5. Navigate to the Constants file in the utils package and enter you Openweather API key.
6. Ensure you have Internet connection so that the gradle files for the third party libraries can be downloaded
7. Connect your device to the PC and grant permission when prompted. (Ensure Developer option is enabled on your phone)
8. Click on Run and select your device from the popup window.

App Build
To create an apk file,do these:
1. Click on the Build from the menu at the top of Android Studio
2. Select Debug

Tech Decision
I followed the MVVM architecture by adding an Activity, ActivityModel, Repository and API client. An intent service was used to update the app when there’s a location change. The intent service doesn't run on the same thread as the activity, hence, the app won't hang and the system resource won't be exhausted.
Retrofit library was used to make the API calls while the POJO files in the models directory had the data structure for the openweather endpoint.
Glide library was used to display the weather logo.

UI test was done using Espresso and this can be found in the androidTest folder.
Robolectric library was also used for creating the unit tests.

