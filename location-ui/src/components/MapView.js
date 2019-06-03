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

const DEFAULT_MAPCENTER = new GoogleGeoLocation(51.4416, 5.4697);

class MapView extends React.Component {

	generateUrl = () => {
		var url = "https://maps.googleapis.com/maps/api/js?key=" + this.props.apiKey +
			"&v=3.exp&libraries=geometry,drawing,places";
		console.log("GoogleMapUrl = " + url);
		return url;
	}

	determinePathCoordinates = () => {
		var path;
		if (this.props.userPaths.length > 0 && this.props.userPaths[0].timedGeoLocations
			&& this.props.userPaths[0].timedGeoLocations.length > 0) {

			path = this.props.userPaths[0]
				.timedGeoLocations.map(timedGeoLocation => timedGeoLocation.toGoogleGeoLocation());
		} else {
			path = [];
		}

		return path;
	}

    render() {
		const userPathCoordinates = this.determinePathCoordinates();
		const mapCenter = userPathCoordinates.length > 0 ? userPathCoordinates[0] : DEFAULT_MAPCENTER;

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
					{userPathCoordinates.length > 0 && <Marker position={mapCenter} />}
					<Polyline path={userPathCoordinates} options={{
							strokeColor: '#FF0000',
							strokeWeight: 2,
							strokeOpacity: 1.0
						}}
					/>
				</GoogleMap>
			);
	  	});

        return (
			<MyMapComponent isMarkerShown />
		);
    }
}

export default MapView;
