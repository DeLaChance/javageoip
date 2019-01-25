LocationApp

Description:

The app should be able to trace a users location over time and give notification based on this
location.

TODO:
- Finish location-tracker
- Make React-based frontend
- Deploy location-cloud and location-tracker to AWS image
- Integrate with SMS service
- Integrate with Alexa service
- Make location-cloud scalable with number of trackers



Requirements:
1. You should be able to add a user to follow via REST endpoint. [x]
2. User should send updates to a REST endpoint to give its location.
3. The system should (auto-)scale well with the number of users.
4. You should be able to receive notifications on SMS whenever a user leaves a certain defined area.
5. You should be able to query a users location over a specified period of time.
6. You should be able to query using a voice command, e.g. "Where is Lucien right now?"
7. Visualize 5+6 using a map in a browser.
8. Make mobile phone app that sends location periodically to backend

Technology:

AWS (Alexa, notifications, auto-scaling, lambda)
Time-series database (choose of your liking)
Akka / Micronaut
React


Data entities:

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
