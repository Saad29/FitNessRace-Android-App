# FitNessRace-Android-App
A fitness tracker and social media based android app, with Augmented Reality gamification.I developed the Android app for a project in my Masters course.
A demo is present here : https://www.youtube.com/watch?v=65g1W1mnlOg

# Architectural Overview

On the Front end we have used Google Activity Recognition API because it is very robust and we could
report the activity based on the accuracy results. Same with Android Fused location API which reports the
location very accurately. We used this API to trigger changes on the Google map as the user moves. Additionally, we sampled out results from Google Places API which we also used as the best Place Information
service out. For showing the listed results of timeline,friend list or rewards, we used Recycler View along
with Data Adapter to show it. All the network calls were done by using Retrofit2 with okhttp3 , which are
one of many ways to make network calls. But these libraries were used because asynchrnous calls could be
made and they have a well defined interface.

On the backend we have implemented the server using Node.js and NoSQL database MongoDB.
For resolving the app-calls several REST-APIs have been created. Sent data are stored on the collections of
the schema created on MongoDB.

# Requirements and Instructions:

 Android phone with at least API Level 16 ( Android 4.1).
 For phones which have Marshmallow or higher the Permissions of Location and Phone
given by the user to avoid app crashes.
 Active Internet connection.
 Register on the App ( be sure to update the credectials according to your server)
 Keep in the pocket or wherever with you, it will start automatic detection of your activity

# Used Technologies on App side
 Google Activity Recognition API for detecting user activity
 Picasso Image Library for displaying Images
 Retrofit2 and okhttp3 for Network calls
 Android Sensor manager for detecting step count.
 Google maps and Places API
 Manual conversion of GPS coordinate system (Fused location API) to camera coordinate system for
placing AR object in the camera view.

# TODO
1. Work on sharing and viweing the friends activities.
2. Improvement to GUI.
3. Improvement to overall design and workflow.
4. Harcoded location for AR which must be made dynamic.

