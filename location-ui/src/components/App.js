import React from "react"

import MapView from "./MapView"
import TitleBar from "./TitleBar";
import SearchAndSelectDrawer from "./SearchAndSelectDrawer"
import User from "../domain/User"
import SearchItem from "./SearchItem";

import httpService from "../service/HttpService"
import apiKeyJson from "../resources/apiKey.json"

class App extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            drawerOpen: true,
            users: [],
			userIdToPathMap: new Map([]),
			selectedUserNames: new Set([])
        };
    }

	componentDidMount() {
		console.log("App has mounted.")

		httpService.fetchAllUsers(users => {
			this.setState({
				users: users
			});
		});
	}

    toggleDrawer = () => {
        this.setState({
            drawerOpen: !this.state.drawerOpen
        });
    };

    toggleSelectedUser = name => {
		const newValue = new Set(this.state.selectedUserNames.entries());
		if (newValue.has(name)) {
			newValue.delete(name);
		} else {
			newValue.add(name);
		}

        this.setState({
			selectedUserNames: newValue
		});

		this.state.users.filter(user => user.name === name)
			.forEach(user => this.fetchPathForUser(user.id));
    };

	fetchPathForUser = userId => {
		httpService.fetchPathByUserId((userId, path => {
			this.setState((prevState, props) => ({
				userIdToPathMap: prevState.userIdToPathMap.set(userId, path)
			}));
		}));
	}

    render() {
        return (
            <>
                <TitleBar openMenu={this.toggleDrawer} />
                <SearchAndSelectDrawer drawerOpen={this.state.drawerOpen}
					items={
						this.state.users.map(user =>
							new SearchItem(user.name, this.state.selectedUserNames.has(user.name)))
					}
                   drawerToggle={this.toggleDrawer}
				   toggleSelectedUser={name => this.toggleSelectedUser(name)}
			    />
                <MapView users={
						this.state.users.filter(user => this.state.selectedUserNames.has(user.name))
					}
					paths={this.state.userIdToPathMap}
					apiKey={apiKeyJson.apiKey}
				/>
            </>
        );
    }
}

export default App;
