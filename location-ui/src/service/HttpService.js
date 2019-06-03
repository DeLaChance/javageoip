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
		request.get(url).on('data', pathData => {
			var parsedPath = this.parsePathData(pathData);
			callback(parsedPath);
		}).on('error', error => {
			var matchingPaths = staticPaths.filter(path => path.id === userId);

			if (matchingPaths.length > 0) {
				var parsedPath = this.parsePathData(matchingPaths[0]);
				callback(parsedPath);
			}
		});
	};

	parsePathData(pathData) {
		return pathData.geolocations.map(geolocation =>
			new TimedGeoLocation(geolocation.timestamp, geolocation.latitude,
				geolocation.longitude)
		);
	}
}

export default new HttpService();
