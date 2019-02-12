import React from "react"

import RawTextView from "./RawTextView"
import TitleBar from "./TitleBar";
import SearchAndSelectDrawer from "./SearchAndSelectDrawer"

class App extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <>
                <TitleBar />
                <SearchAndSelectDrawer items={['John Snow', 'Daenerys Targaryen', 'Tyrion Lannister']} />
                <RawTextView />
            </>
        );
    }
}

export default App;
