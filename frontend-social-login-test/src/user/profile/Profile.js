import React, { Component } from 'react';
import './Profile.css';

class Profile extends Component {
    constructor(props) {
        super(props);
        console.log(props);
    }
    render() {
        return (
            <div className="profile-container">
                <div className="container">
                    <div className="profile-info">
                        <div className="profile-avatar">
                            { 
                                this.props.currentUser.profileImg ? (
                                    <img src={this.props.currentUser.profileImg} alt={this.props.currentUser.name}/>
                                ) : (
                                    <div className="text-avatar">
                                        <span>{this.props.currentUser.nickname && this.props.currentUser.nickname[0]}</span>
                                    </div>
                                )
                            }
                        </div>
                        <div className="profile-name">
                           <h2>{this.props.currentUser.nickname}</h2>
                           <p className="profile-email">{this.props.currentUser.userId}</p>
                        </div>
                    </div>
                </div>    
            </div>
        );
    }
}

export default Profile