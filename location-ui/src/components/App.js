import React from "react"

import RawTextView from "./RawTextView"
import TitleBar from "./TitleBar";

class App extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <>
                <TitleBar />
                <RawTextView />
            </>
        );
    }
}

export default App;
