/**
* A UserPath is a collection of TimedGeoLocation's traversed by a User
**/
class UserPath {

    constructor(user, timedGeoLocations) {
        this.timedGeoLocations = timedGeoLocations ? timedGeoLocations : [];
		this.user = user;
    }
}

export default UserPath;
