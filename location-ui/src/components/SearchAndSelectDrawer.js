import React from "react"
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import CheckBox from '@material-ui/icons/CheckBox';

class SearchAndSelectDrawer extends React.Component {

    state = {
        open: true
    }

    constructor(props) {
        super(props);
    }

    toggleDrawer = () => {
        this.setState({
            open: !this.state.open
        })
    };

    render() {
        return (
            <Drawer anchor="left" open={this.state.open}>
                <div tabIndex={0} role="button" onClick={this.toggleDrawer} onKeyDown={this.toggleDrawer}>
                    <List>
                        {['John Snow', 'Daenerys Targaryen', 'Tyrion Lannister'].map(text => (
                            <ListItem button key={text}>
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