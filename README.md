LocationApp


The app should be able to trace a users location over time and give notification based on this location.

# Technologies:
Google Cloud
React


# TODO:
1. Finish React-based frontend
2. Rewrite Kotlin app into Java app
3. Deploy location-cloud and location-tracker to Google App Engine

# Requirements:
1. You should be able to add a user to follow via REST endpoint.
2. User should send updates to a REST endpoint to give its location.
3. The system should (auto-)scale well with the number of users.
4. You should be able to receive notifications on SMS whenever a user leaves a certain defined area.
5. You should be able to query a users location over a specified period of time.
6. Visualize 5+6 using a map in a browser.


# Data entities:

User:
- Id
- Keywords (e.g. 'my girlfriend')
- List of geolocations
- List of notifications

Geolocation:
- Longitude
- Latitude
- Timestamp (unix ms)

Notification:
- Type
- Value (e.g. 'email', 'phone number')
