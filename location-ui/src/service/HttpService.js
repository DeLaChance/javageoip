

import request from "request"
import config from "./http-config.json"
import staticUsers from "../resources/static_users.json"
import staticPaths from "../resources/static_paths.json"

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
			var matchingPaths = staticPaths.filter(path => path.userId == userId);
			var pathForUser = null;
			if (matchingPaths.length > 0) {
				pathForUser = matchingPaths[0];
			}

			callback(staticPaths);
		});
	};

}

export default new HttpService();
