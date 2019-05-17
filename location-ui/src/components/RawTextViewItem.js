import React from "react"

class RawTextViewItem extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <li>{this.props.value}</li>
        );
    }
}

export default RawTextViewItem;
