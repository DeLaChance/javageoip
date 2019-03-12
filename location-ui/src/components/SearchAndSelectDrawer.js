import React from "react"
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Checkbox from '@material-ui/core/Checkbox';

import SearchInput from './SearchInput/SearchInput'

class SearchAndSelectDrawer extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            text: ""
        }
    }

    render() {
        return (
            <Drawer anchor="left" open={this.props.drawerOpen} onClose={this.props.drawerToggle}>
                <div tabIndex={0} role="button">
                    <List>
                        <SearchInput onChange={this.handleInputChange} />
                        {this.filterItems(this.props.items).map(item => this.convertToDomListItem(item))}
                    </List>
                </div>
            </Drawer>
        );
    }


    handleInputChange = event => {
        event.preventDefault();
        this.changeInputText(event.target.value);
    }

    changeInputText = text => {
        this.setState({
            text: text
        });
    }

    filterItems = items => {
        return items.filter(item => {
            if (this.state.text === null || this.state.text.length === 0) {
                return true;
            } else {
                return item.value.indexOf(this.state.text) !== -1;
            }
        });
    }

    convertToDomListItem = item => {
        return (
            <ListItem button key={item.value}>
                <Checkbox checked={item.isSelected} color="primary" onClick={() => this.props.toggleSelectedUser(item.value)} />
                <ListItemText primary={item.value} />
            </ListItem>
        );
    }
}

export default SearchAndSelectDrawer;