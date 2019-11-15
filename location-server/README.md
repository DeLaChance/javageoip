# About
`location-server` is the backend code for LocationCloud.

# How to build
Copy the file `src/main/liquibase/liquibase.properties.example` to `src/main/liquibase/liquibase.properties`. Either set
the environment variables or set the values in the file.

Then run:

`mvn clean install` 

# How to run
`mvn spring-boot:run`

# Technologies:
- React
- Java + Spring
- Postgresql 
- [R2DBC](https://r2dbc.io/)
- [Reactor](https://projectreactor.io/docs/core/release/reference/)
- Maven

# Structure
`location-ui` contains the React-based frontend. `location-server` contains the backend code.

# Rest API

## Users
### GET:
- `/api/users/` -> Fetches all users
- `/api/users/:userId` -> Fetches user info for user with id `:userId`

### POST:
- `/api/users/` -> Creates user

Example:
`curl -v -H 'Content-Type: application/json' -X POST --data '{ "name": "Darth Sidious", "keywords": ["Emperor", "Palpatine"] }' localhost:9000/api/users`

### PUT:
- `/api/users/` -> Updates user

Example:
`{"id":"a9815fcf-bb65-4643-ba9f-c281e1cd6b72","name":"Darth Sidious","keyWords":["Emperor"," Palpatine"," Master"]}` 

### DELETE:
- `/api/users/:userId` -> Deletes user

## Paths

### GET:
- `/api/paths/:pathId` -> Fetches path with `:pathId`
- `/api/paths?userId=userId&startTime=XXX&endTime=YYY&pageSize=A&pageNumber=B` 
    -> Fetches paths belonging to `:userId` between moments in time XXX and YYY

### POST:
- `/api/paths/users/:userId` -> Create a new path for a user

Example:
`curl -i -X POST -H "Content-Type: application/json" localhost:9000/api/paths/users/88d0095d-5240-41ee-96e9-f6827963776c`

# Data entities

`User`:

```
{
	"id": "5febd415-ddee-4bef-a266-eae2612e8e4c",
	"name": "Jon Snow",
	"keywords": [ "Azor Ahai", "Lord Commander"]
}
```

`GeoLocationResponse`:
 
```
{
	"userId": "5febd415-ddee-4bef-a266-eae2612e8e4c", # uuid4 of the user
	"startTime": 0, # unix timestamp millis
	"endTime": 100000,  # unix timestamp millis
	"pageSize": 100 # pageSize
	"pageNumber": 0 # 0-X page number
	"maxNumberOfPages": 10 #
	"geolocations": [ ... ] # <see below>
}
```

`Geolocation`:

```
{
	"longitude": 51.43657684326172, # double
	"latitude": 5.460134983062744, # double
	"timestamp": 1549467150312 # unix timstamp millis
}
```