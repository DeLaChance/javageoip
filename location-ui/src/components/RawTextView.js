import React from "react"
import RawTextViewItem from "./RawTextViewItem"
import paths from "../resources/static_paths.json"

class RawTextView extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            paths: paths
        };
    }

    render() {
        const points = this.state.paths[0].map((point, pointIndex) => {
            return (
                <RawTextViewItem key={point.timestamp}
                    value={point.longitude + " : " + point.latitude + " @ " + point.timestamp}/>
            );
        });

        return (
            <ul>{points}</ul>
        );
    }
}

export default RawTextView;