import request from "request"
import config from "./http-config.json"
import staticUsers from "../resources/static_users.json"
import staticPaths from "../resources/static_paths.json"
import TimedGeoLocation from "../domain/TimedGeoLocation"

class HttpService {

	constructor() {
		this.baseurl = config.baseurl;
	}

	fetchAllUsers = callback => {
		var url = this.baseurl + "user";
		request.get(url).on('data', data => {
			callback(data);
		}).on('error', error => {
			callback(staticUsers);
		});
	}

	fetchPathByUserId = (userId, callback) => {
		var url = this.baseurl + "path/" + userId;
		request.get(url).on('data', data => {
			var pathForUser = data.geolocations;
			callback(pathForUser);
		}).on('error', error => {
			var matchingPaths = staticPaths.filter(path => path.id === userId)
				.map(path => path.geolocations.map(geolocation =>
					new TimedGeoLocation(geolocation.latitude, geolocation.longitude,
						geolocation.timestamp)
				));

			var pathForUser = null;
			if (matchingPaths.length > 0) {
				pathForUser = matchingPaths[0];
				callback(pathForUser);
			}
		});
	};

}

export default new HttpService();
