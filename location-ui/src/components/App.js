import React from "react"

import MapView from "./MapView"
import TitleBar from "./TitleBar";
import SearchAndSelectDrawer from "./SearchAndSelectDrawer"
import UserPath from "../domain/UserPath";
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
		console.log("App has mounted.");

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
		// Copy old set into new set
		const newSelectedUserNameSet = new Set();
		this.state.selectedUserNames.forEach(userName => {
			newSelectedUserNameSet.add(userName);
		});

		if (newSelectedUserNameSet.has(name)) {
			newSelectedUserNameSet.delete(name);
		} else {
			newSelectedUserNameSet.add(name);
		}

        this.setState({
			selectedUserNames: newSelectedUserNameSet
		});

		this.state.users.filter(user => user.name === name)
			.forEach(user => this.fetchPathForUser(user.id));
    };

	fetchPathForUser = userId => {
		var self = this;
		httpService.fetchPathByUserId(userId, function(path) {
			self.setState((prevState, props) => ({
				userIdToPathMap: prevState.userIdToPathMap.set(userId, path)
			}));
		});
	}

    render() {
		const selectedUsers = this.state.users.filter(user => this.state.selectedUserNames.has(user.name));
		const userPaths = selectedUsers.map(user => new UserPath(user, this.state.userIdToPathMap.get(user.id)));

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
                <MapView userPaths={userPaths} apiKey={apiKeyJson.apiKey} />
            </>
        );
    }
}

export default App;
