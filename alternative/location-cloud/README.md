# LocationApp

## Description:

The app should be able to trace a users location over time and give notification based on this
location.

## How to build and run the app
   ```
   $ gradle assemble
   $ java -jar build/libs/location-cloud-0.1-all.jar
   ```

Then go to [this link](http://localhost:8086/api/users/hello/World) and you should see

> Hello World

## TODO
- Make code that adds entities via REST, add stubs for DB
- Study AWS sdk
- Choose database technology
- Make data entities in DB


## Requirements
1. You should be able to add a user to follow via REST endpoint.
2. User should send updates to a REST endpoint to give its location.
3. The system should (auto-)scale well with the number of users.
4. You should be able to receive notifications on SMS whenever a user leaves a certain defined area.
5. You should be able to query a users location over a specified period of time.
6. You should be able to query using a voice command, e.g. "Where is Lucien right now?"
7. Visualize 5+6 using a map in a browser.

## Technology

- AWS (For Alexa, notifications, auto-scaling, lambda)
- Micronaut

## Data entities

- User:
    - Id
    - Keywords (e.g. 'my girlfriend')
    - List of geolocations
    - List of notifications

- Geolocation:
    - Longitude
    - Latitude
    - Timestamp (unix ms)

- Notification:
    - Type
    - Value (e.g. 'email', 'phone number')


