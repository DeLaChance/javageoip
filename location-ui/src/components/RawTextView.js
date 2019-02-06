import React from "react"

import RawTextViewItem from "./RawTextViewItem"

class RawTextView extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const a = (
            <RawTextViewItem key="a" value="a" />
        );

        return (
            <ul>{a}</ul>
        );
    }
}

export default RawTextView;