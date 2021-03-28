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
    const endSt = useRef(null)
    const endCr = useRef(null)

    const handleSubmit = () => {


        // if (startLat && endLat) {
        //     const lat1 = parseFloat(startLat.current.value)
        //     const long1 = parseFloat(startLong.current.value)
        //     const lat2 = parseFloat(endLat.current.value)
        //     const long2 = parseFloat(endLong.current.value)
        //
        //     const onPointOneEntered = (res) => {
        //         // const { coords, id } = res.data
        //         // setPointOne({ coords: { latitude: coords[0], longitude: coords[1] }, id })
        //     }
        //     const onPointTwoEntered = (res) => {
        //         // const { coords, id } = res.data
        //         // setPointTwo({ coords: { latitude: coords[0], longitude: coords[1] }, id })
        //     }
        //     if (lat1 && long1 && lat2 && long2) {
        //
        //         nearest(lat1, long1, onPointOneEntered)
        //         nearest(lat2, long2, onPointTwoEntered)
        //     }
        // }

        if (startSt && startCr) {
            const street1 = startSt.current.value
            const cross1 = startCr.current.value
            if (street1 && cross1) {
                const onPointOneIntEntered = (res) => {
                    const {coords, id} = res.data
                    setPointOne({coords: {latitude: coords[0], longitude: coords[1]}, id})
                }

                intersection(street1, cross1, onPointOneIntEntered)
                // ONLY Redraws route on the setting of point 2
            }
        }

        if (endSt && endCr) {
            const street2 = endSt.current.value
            const cross2 = endCr.current.value

            if (street2 && cross2) {
                const onPointTwoIntEntered = (res) => {
                    const {coords, id} = res.data
                    setPointTwo({coords: {latitude: coords[0], longitude: coords[1]}, id})
                }

                intersection(street2, cross2, onPointTwoIntEntered)
            }
        }
    }

    const handleClearRoute = () => {
        setActiveRoute([])
        setPointOne(null)
        setPointTwo(null)
        startSt.current.value = ""
        startCr.current.value = ""
        endSt.current.value = ""
        endCr.current.value = ""
    }

    const handleResetMap = () => {

        localStorage.clear()
        handleClearRoute()
        //  setNW({ latitude: 41.82686216366997, longitude: -71.40645750837325 })
        // setSE({ latitude: 41.82355259953458, longitude: -71.4000750453612 })
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

                {/*<TextBox inputRef={startLat} label="Start Latitude" value={pointOne ? pointOne.coords.latitude : ""} />*/}
                {/*<TextBox label="Start Longitude" value={pointOne ? pointOne.coords.longitude : ""} inputRef={startLong} />*/}
                {/*<TextBox label="End Latitude" value={pointTwo ? pointTwo.coords.latitude : ""} inputRef={endLat} />*/}
                {/*<TextBox label="End Longitude" value={pointTwo ? pointTwo.coords.longitude : ""} inputRef={endLong} />*/}

                <TextBox inputRef={startSt} label="Start Street" value={pointOne ? pointOne.startStreet : ""} />
                <TextBox inputRef={startCr} label="Start Cross-Sreet" value={pointOne ? pointOne.startCross : ""} />
                <TextBox inputRef={endSt} label="End Street" value={pointTwo ? pointTwo.endStreet : ""} />
                <TextBox inputRef={endCr} label="End Cross-Street" value={pointTwo ? pointTwo.endCross : ""} />

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

            <div>
                <AwesomeButton
                    //cssModule={AwesomeButtonStyles}
                    type="primary"
                    onPress={handleResetMap}
                >
                    Reset Map
                </AwesomeButton>
            </div>

        </div>
    )
}

export default Route