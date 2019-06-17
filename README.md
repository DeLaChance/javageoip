LocationApp
The app should be able to trace a users location over time and give notification based on this location.


# Technologies:
Google Cloud
React
Kotlin + Micronaut
Gradle
C#

# TODO:
1. Write Kotlin Vert.x app for backend replacing 'location-cloud' (Requirements 1., 2. and 5.)
2. Deploy location-cloud to Google App Engine (Requirements 3.)
3. Requirement 4.
4. Requirement 6.
5. Write an instance of location-tracker in C# + Integration between both


# Requirements:
1. You should be able to add a user to follow via REST endpoint.
2. User should send updates to a REST endpoint to give its location.
3. The system should (auto-)scale well with the number of users.
4. You should be able to receive notifications on SMS/email whenever a user leaves a certain defined area.
5. You should be able to query a users location over a specified period of time.
6. Lambda function indicating countries + locations that were visited over last month


# Data entities + REST API:
GET:
/api/user/ -> Fetches all users
/api/user/:id -> Fetches user info for user with id :id
/api/path/:userid?startTime=XXX&endTime=YYY&pageSize=A&pageNumber=B -> Fetches path of user between moments in time XXX and YYY

POST:
/api/user/ -> Creates user
/api/user/ -> Updates path of user


User:
{
	"id": "5febd415-ddee-4bef-a266-eae2612e8e4c" # uuid4
	"name": "Jon Snow",
	"keywords": [ "Azor Ahai", "Lord Commander"]
}

GeoLocationResponse: {
	"userId": "5febd415-ddee-4bef-a266-eae2612e8e4c", # uuid4 of the user
	"startTime": 0, # unix timestamp millis
	"endTime": 100000,  # unix timestamp millis
	"pageSize": 100 # pageSize
	"pageNumber": 0 # 0-X page number
	"maxNumberOfPages": 10 #
	"geolocations": [ ... ] # <see below>

}

Geolocation:
{
	"longitude": 51.43657684326172, # double
	"latitude": 5.460134983062744, # double
	"timestamp": 1549467150312 # unix timstamp millis
}
