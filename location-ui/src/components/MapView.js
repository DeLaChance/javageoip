import React from "react"

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

const { DrawingManager } = require("react-google-maps/lib/components/drawing/DrawingManager");
class MapView extends React.Component {

    constructor(props) {
		super(props);
    }

	generateUrl = () => {
		var url = "https://maps.googleapis.com/maps/api/js?key=" + this.props.apiKey +
			"&v=3.exp&libraries=geometry,drawing,places";
		console.log("GoogleMapUrl = " + url);
		return url;
	}

    render() {
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
			const google = window.google;

			return (
				<GoogleMap defaultZoom={3} defaultCenter={{ lat: 37.772, lng: -122.214 }}>
					{
						props.isMarkerShown && <Marker position={{ lat: 37.772, lng: -122.214 }} />
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
