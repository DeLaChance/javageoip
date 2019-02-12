import React from "react"
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import CheckBox from '@material-ui/icons/CheckBox';

import SearchInput from './SearchInput/SearchInput'

class SearchAndSelectDrawer extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            open: true,
            text: "",
            items: props.items
        }
    }

    toggleDrawer = () => {
        this.setState({
            open: !this.state.open
        })
    };

    handleInputChange = event => {
        event.preventDefault();
        this.changeInputText(event.target.value);
    };

    changeInputText = text => {
        this.setState({
           text: text,
           items: this.props.items.filter(value => {
                if (text === null || text.length === 0) {
                    return true;
                } else {
                    return value.indexOf(text) !== -1;
                }
           })
        });
    };

    render() {
        return (
            <Drawer anchor="left" open={this.state.open}>
                <div tabIndex={0} role="button">
                    <List>
                        <SearchInput onChange={this.handleInputChange} />
                        {this.state.items.map(text => (
                            <ListItem button key={text} onClick={this.toggleDrawer}>
                                <ListItemIcon>
                                    <CheckBox />
                                </ListItemIcon>
                                <ListItemText primary={text} />
                            </ListItem>
                        ))}
                    </List>
                </div>
            </Drawer>
        );
    }

}

export default SearchAndSelectDrawer;