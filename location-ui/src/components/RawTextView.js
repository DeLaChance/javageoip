import React from "react"
import RawTextViewItem from "./RawTextViewItem"

class RawTextView extends React.Component {

    constructor(props) {
		super(props);
    }

    render() {
        const users = this.props.users.map((user, index) => {
				return (
					<>
					   	<RawTextViewItem key={user.id} value={JSON.stringify(user)} />
					</>
				);
        });

        return (
            <>
                <ul>{users}</ul>
            </>
        );
    }
}

export default RawTextView;
