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
const POLYLINE_COLORS = [ "#FF0000", "#00FF00", "#0000FF" ];

class MapView extends React.Component {

	generateUrl = () => {
		var url = "https://maps.googleapis.com/maps/api/js?key=" + this.props.apiKey +
			"&v=3.exp&libraries=geometry,drawing,places";
		console.log("GoogleMapUrl = " + url);
		return url;
	}

	toPolyLineWithMarker = (userPath, index) => {
		const userPathCoordinates = userPath.timedGeoLocations.map(timedGeoLocation => timedGeoLocation.toGoogleGeoLocation());
		var firstName = userPath.user.name.split(" ")[0];

		return (
			<>
				<Polyline path={userPathCoordinates} options={{
						strokeColor: POLYLINE_COLORS[index],
						strokeWeight: 2,
						strokeOpacity: 1.0
					}}
				/>
				{userPathCoordinates.length > 0 && <Marker
						position={userPathCoordinates[userPathCoordinates.length-1]}
						label={firstName}
					/>
				}
			</>
		);
	}

    render() {
		const mapCenter = DEFAULT_MAPCENTER;

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
					{this.props.userPaths.map((userPath, index) => this.toPolyLineWithMarker(userPath, index))}
				</GoogleMap>
			);
	  	});

        return (
			<MyMapComponent isMarkerShown />
		);
    }
}

export default MapView;
