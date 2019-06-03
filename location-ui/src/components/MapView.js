import React from "react"

import GoogleGeoLocation from "../domain/GoogleGeoLocation"

const {
	compose,
	withProps
} = require("recompose");

const {
	withScriptjs,
    withGoogleMap,
    GoogleMap,
	Marker,
	Polyline
} = require("react-google-maps");

const EINDHOVEN_GEOLOCATION = new GoogleGeoLocation(51.4416, 5.4697);

class MapView extends React.Component {

	generateUrl = () => {
		var url = "https://maps.googleapis.com/maps/api/js?key=" + this.props.apiKey +
			"&v=3.exp&libraries=geometry,drawing,places";
		console.log("GoogleMapUrl = " + url);
		return url;
	}

	findCenter = () => {
		var center;
		if (this.props.userPaths.length === 0) {
			center = EINDHOVEN_GEOLOCATION;
		} else {
			var userPath = this.props.userPaths[0];
			if (userPath && userPath.timedGeoLocations.length > 0) {
				center = userPath.timedGeoLocations[0].toGoogleGeoLocation();
			} else {
				center = EINDHOVEN_GEOLOCATION;
			}
		}

		return center;
	}

    render() {
		const mapCenter = this.findCenter();

		const flightPlanCoordinates = [
          {lat: 37.772, lng: -122.214},
          {lat: 21.291, lng: -157.821},
          {lat: -18.142, lng: 178.431},
          {lat: -27.467, lng: 153.027}
        ];

		const MyMapComponent = compose(
			withProps({
				googleMapURL: this.generateUrl(),
				loadingElement: <div style={{ height: `100%` }} />,
				containerElement: <div style={{ height: `600px` }} />,
				mapElement: <div style={{ height: `100%` }} />,
			}),
			withScriptjs,
			withGoogleMap
		) ((props) => {
			return (
				<GoogleMap defaultZoom={3} defaultCenter={mapCenter}>
					{
						props.isMarkerShown && <Marker position={mapCenter} />
					}
					<Polyline path={flightPlanCoordinates} options={{
						strokeColor: '#FF0000',
						strokeWeight: 2,
						strokeOpacity: 1.0
					}} />
				</GoogleMap>
			);
	  	});

        return (
			<MyMapComponent isMarkerShown />
		);
    }
}

export default MapView;
