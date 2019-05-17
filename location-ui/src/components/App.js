import React from "react"

import RawTextView from "./RawTextView"
import TitleBar from "./TitleBar";
import SearchAndSelectDrawer from "./SearchAndSelectDrawer"

import SelectableUser from '../domain/SelectableUser'
import SearchItem from "./SearchItem";

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            drawerOpen: true,
            users: [],
        };
    }

    toggleDrawer = () => {
        this.setState({
            drawerOpen: !this.state.drawerOpen
        });
    };

    toggleSelectedUser = userName => {
        this.setState({
            users: this.state.users.map(user => {
                if (user.userName === userName) {
                    return new SelectableUser(userName, !user.isSelected)
                } else {
                    return user;
                }
            })
        });
    };

    render() {
        return (
            <>
                <TitleBar openMenu={this.toggleDrawer} />
                <SearchAndSelectDrawer drawerOpen={this.state.drawerOpen} items={this.state.users.map(user => new SearchItem(user.userName, user.isSelected))}
                   drawerToggle={this.toggleDrawer} toggleSelectedUser={name => this.toggleSelectedUser(name)} />
                <RawTextView users={this.state.users} />
            </>
        );
    }
}

export default App;
