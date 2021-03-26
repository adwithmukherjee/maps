import React, { useState, useRef, useEffect } from "react"
import TextBox from "./TextBox"
import { AwesomeButton } from "react-awesome-button"
import nearest from "./axios/nearest"
import intersection from "./axios/intersection"

//import AwesomeButtonStyles from "react-awesome-button/src/styles/styles.scss"

const Route = ({ title, pointOne, pointTwo, setPointOne, setPointTwo, setActiveRoute }) => {

    const [route, setRoute] = useState([])

    const startLat = useRef(null)
    const startLong = useRef(null)
    const endLat = useRef(null)
    const endLong = useRef(null)

    const startSt = useRef(null)
    const startCr = useRef(null)

    const handleSubmit = () => {


        if (startLat && endLat) {
            const lat1 = parseFloat(startLat.current.value)
            const long1 = parseFloat(startLong.current.value)
            const lat2 = parseFloat(endLat.current.value)
            const long2 = parseFloat(endLong.current.value)

            const onPointOneEntered = (res) => {
                // const { coords, id } = res.data
                // setPointOne({ coords: { latitude: coords[0], longitude: coords[1] }, id })
            }
            const onPointTwoEntered = (res) => {
                const { coords, id } = res.data
                setPointTwo({ coords: { latitude: coords[0], longitude: coords[1] }, id })
            }
            if (lat1 && long1 && lat2 && long2) {

                nearest(lat1, long1, onPointOneEntered)
                nearest(lat2, long2, onPointTwoEntered)
            }
        }

        if (startSt && startCr) {
            const street = startSt.current.value
            const cross = startCr.current.value

            const onPointOneIntEntered = (res) => {
                const { coords, id } = res.data
                setPointOne({ coords: { latitude: coords[0], longitude: coords[1] }, id })
            }

            if (startSt && startCr) {
                intersection(street, cross, onPointOneIntEntered)
            }
        }

    }

    const handleClearRoute = () => {
        setActiveRoute([])
        setPointOne(null)
        setPointTwo(null)
    }

    useEffect(() => {
        if (startLat.current && startLong.current && pointOne) {
            startLat.current.value = pointOne.coords.latitude
            startLong.current.value = pointOne.coords.longitude
        }

    }, [pointOne])
    useEffect(() => {
        if (endLat.current && endLong.current && pointTwo) {
            endLat.current.value = pointTwo.coords.latitude
            endLong.current.value = pointTwo.coords.longitude
        }

    }, [pointTwo])


    return (
        <div>
            <div>
                <h1> {title} </h1>

                <TextBox inputRef={startLat} label="Start Latitude" value={pointOne ? pointOne.coords.latitude : ""} />
                <TextBox label="Start Longitude" value={pointOne ? pointOne.coords.longitude : ""} inputRef={startLong} />
                <TextBox label="End Latitude" value={pointTwo ? pointTwo.coords.latitude : ""} inputRef={endLat} />
                <TextBox label="End Longitude" value={pointTwo ? pointTwo.coords.longitude : ""} inputRef={endLong} />

                <TextBox inputRef={startSt} label="Start Street" value={pointOne ? pointOne.startStreet : ""} />
                <TextBox inputRef={startCr} label="Start Cross" value={pointOne ? pointOne.startCross : ""} />

                {/*"Coordinates: (" + coords.srclat +","+ coords.srclong +") -> ("+ coords.destlat +","+ coords.destlong+")"*/}

            </div>
            <div>
                <AwesomeButton
                    //cssModule={AwesomeButtonStyles}
                    type="primary"
                    onPress={handleSubmit}
                >
                    Submit
                </AwesomeButton>
            </div>

            <div>
                <AwesomeButton
                    //cssModule={AwesomeButtonStyles}
                    type="primary"
                    onPress={handleClearRoute}
                >
                    Clear Route
                </AwesomeButton>
            </div>


        </div>
    )
}

export default Route