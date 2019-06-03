import GoogleGeoLocation from './GoogleGeoLocation'

/**
* Represents a location on earth at a certain time.
**/
class TimedGeoLocation {

    constructor(timestamp, latitude, longitude) {
        this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;

		this.toGoogleGeoLocation = this.toGoogleGeoLocation.bind(this);
    }

	toGoogleGeoLocation() {
		return new GoogleGeoLocation(this.latitude, this.longitude);
	}

}

export default TimedGeoLocation
