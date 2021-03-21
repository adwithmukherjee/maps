import React,  { useState } from "react"
import TextBox from "./TextBox"
import { AwesomeButton } from "react-awesome-button"
import axios from "axios"
//import AwesomeButtonStyles from "react-awesome-button/src/styles/styles.scss"

const Route = ({title}) => {

    const [route, setRoute] = useState([])
    const [coords, setCoords] = useState({
        srclat: 0, 
        srclong: 0,
        destlat: 0, 
        destlong: 0
    })

    const handleSubmit = async () => {
        const res = axios.post(
            "http://localhost:4567/route", 
            coords, 
            {
                headers: {
                  "Content-Type": "application/json",
                  'Access-Control-Allow-Origin': '*',
                  }
            }
        )

        setRoute((await res).data.route)
        console.log((await res).data.route)
    }

    const showRoute = () => {
        return (
            route.length == 0 
            ? <div></div>
            : <div>
            { 
                route.map((item) => {
                    return <div> {"" + item[0] + ", " + item[1]} </div>
                })
            }
            </div>
        )
    }
    return(
        <div>
            <div>
                <h1> {title} </h1>

                <TextBox label = "Street 1" onChange = {(text) => {setCoords({...coords, srclat: text})}}/>
                <TextBox label = "Street 2" onChange = {(text) => {setCoords({...coords, destlat: text})}}/>
                <TextBox label = "Start Longitude" onChange = {(text) => {setCoords({...coords, srclong: text})}}/>
                <TextBox label = "End Longitude" onChange = {(text) => {setCoords({...coords, destlong: text})}}/>

                {"Coordinates: (" + coords.srclat +","+ coords.srclong +"), ("+ coords.destlat +","+ coords.destlong+")" }

             </div>
             <div>
                <AwesomeButton
                   //cssModule={AwesomeButtonStyles}
                   type = "primary"
                   onPress = {handleSubmit}
                >
                    Submit
                </AwesomeButton>
             </div>

             <div>
                {
                    route.length === 0
                    ? <div></div>
                    : <div> {showRoute()}</div>
                }
                
             </div>
         </div>
    )
}

export default Route