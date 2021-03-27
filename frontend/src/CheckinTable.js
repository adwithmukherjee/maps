import axios from "axios"
import React, { useState, useEffect } from "react"

const CheckinTable = () => {

    const [users, setUsers] = useState([])
    const [activeUserData, setActiveUserData] = useState([])

    const addUsers = (data) => {

        data.forEach((user) => {

            const userObj = {
                timestamp: parseFloat(user[0]),
                id: parseInt(user[1]), 
                name: user[2], 
                latitude: parseFloat(user[3]), 
                longitude: parseFloat(user[4])
            }    
            // console.log(userObj)
            setUsers(users => {
                return users.concat(userObj)
            })        
        })
     
    } 

    const addActiveUserData = (data) => {

        data.forEach((user) => {

            const userObj = {
                id: parseInt(user[0]), 
                name: user[1], 
                timestamp: parseFloat(user[2]),
                latitude: parseFloat(user[3]), 
                longitude: parseFloat(user[4])
            }    
            console.log(userObj)
            setActiveUserData(users => {
                return users.concat(userObj)
            })        
        })
     
    } 


    const requestUsers = () => {
        axios.post(
            "http://localhost:4567/checkin",
            {},
            {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }
        ).then((res) => {
            
            addUsers(res.data.users)
            
        }).catch((err) => {

        })
    }

    const requestActiveUserData = (id) =>{
        setActiveUserData([])
        axios.post(
            "http://localhost:4567/userCheckins",
            {"id": id},
            {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }
        ).then((res) => {
            
            addActiveUserData(res.data.user)
            
        }).catch((err) => {

        })
    }

    const listItems = users.map((user) =>
        <li style ={{listStyle: "none"}}key = {user.timestamp} onMouseDown = {() => {requestActiveUserData(user.id)}}>
            <div style ={{padding: 5, fontSize: 20, marginBottom: 5, borderBottomStyle: "solid"}}>{user.name} checked in to {user.latitude}, {user.longitude} at {new Date(user.timestamp*1000).toLocaleTimeString()}</div>
        </li>
    );

    const listUserData = activeUserData.map((user) =>
        <li style ={{listStyle: "none"}}key = {user.timestamp} onMouseDown = {() => {console.log(user.id)}}>
            <div style ={{padding: 5, fontSize: 20, marginBottom: 5, borderBottomStyle: "solid"}}>{user.name} checked in to {user.latitude}, {user.longitude} at {new Date(user.timestamp*1000).toLocaleTimeString()}</div>
        </li>
    );

    

    useEffect(() => {
        const interval = setInterval(() => {
          requestUsers();
        }, 2000);
        return () => clearInterval(interval);
    }, []);


    return (
        <div style = {{marginLeft: 20,display: "flex",justifyContent: "center", flexDirection:"column", width: "30%"}}>
            <div style = {{  borderStyle: "solid", width: "100%",height: 400, overflow:"scroll"}}>
                <ul>{listItems}</ul>
            </div>
            <div style = {{ borderStyle: "solid", height: 200, overflow:"scroll"}}>
                <ul>{listUserData}</ul>
            </div>
        </div>
    )

}

export default CheckinTable