import React from "react"

import RawTextView from "./RawTextView"
import TitleBar from "./TitleBar";
import SearchAndSelectDrawer from "./SearchAndSelectDrawer"

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            drawerOpen: true
        };
    }


    toggleDrawer = () => {
        this.setState({
            drawerOpen: !this.state.drawerOpen
        });
    };

    render() {
        return (
            <>
                <TitleBar openMenu={this.toggleDrawer} />
                <SearchAndSelectDrawer drawerOpen={this.state.drawerOpen} items={['John Snow', 'Daenerys Targaryen', 'Tyrion Lannister']} drawerToggle={this.toggleDrawer} />
                <RawTextView />
            </>
        );
    }
}

export default App;
