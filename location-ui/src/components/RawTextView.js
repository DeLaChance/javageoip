import React from "react"
import RawTextViewItem from "./RawTextViewItem"

class RawTextView extends React.Component {

    constructor(props) {
		super(props);
    }

    render() {
        const users = this.props.users
            .filter(user => user.isSelected)
            .map((user, index) => {
               return (
                   <RawTextViewItem key={user.userName} value={JSON.stringify(user)} />
               );
            });

        const points = [].map((point, pointIndex) => {
            return (
                <RawTextViewItem key={point.timestamp}
                    value={point.longitude + " : " + point.latitude + " @ " + point.timestamp}/>
            );
        });

        return (
            <>
                <ul>{users}</ul>
                <ul>{points}</ul>
            </>
        );
    }
}

export default RawTextView;
