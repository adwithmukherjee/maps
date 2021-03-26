import axios from "axios"
import React, { useState, useEffect } from "react"

const CheckinTable = () => {

    const [users, setUsers] = useState([])

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

    const listItems = users.map((user) =>
        <li style ={{listStyle: "none"}}key = {user.timestamp}>
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
        <>
            <div style = {{marginLeft: 20, display: "flex",justifyContent: "center", height: 500, borderStyle: "solid", width: "30%", overflow:"scroll"}}>
                <ul>{listItems}</ul>
            </div>
        </>
    )

}

export default CheckinTable