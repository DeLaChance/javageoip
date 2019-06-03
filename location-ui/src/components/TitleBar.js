import React from "react"
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';

class TitleBar extends React.Component {

    render() {
        return (
            <AppBar position="static">
                <Toolbar>
                    <IconButton color="inherit" aria-label="Menu">
                        <MenuIcon onClick={this.props.openMenu} />
                    </IconButton>
                    <Typography variant="h6" color="inherit">
                        Location Tracker
                    </Typography>
                </Toolbar>
            </AppBar>
        );
    }
}

export default TitleBar;
