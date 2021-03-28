import React, { useState, useRef, useEffect } from "react"
import TextBox from "./TextBox"
import { AwesomeButton } from "react-awesome-button"
import nearest from "./axios/nearest"
import intersection from "./axios/intersection"

//import AwesomeButtonStyles from "react-awesome-button/src/styles/styles.scss"

const Route = ({ title, pointOne, pointTwo, setPointOne, setPointTwo, setActiveRoute, setError }) => {

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

        const error = () => {
            setError("Error: Could not retrieve intersection from server")
        }

        if (startSt && startCr) {
            const street1 = startSt.current.value
            const cross1 = startCr.current.value
            if (street1 && cross1) {
                const onPointOneIntEntered = (res) => {
                    const {coords, id} = res.data
                    setPointOne({coords: {latitude: coords[0], longitude: coords[1]}, id})
                }

             
                intersection(street1, cross1, onPointOneIntEntered, error)
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

                intersection(street2, cross2, onPointTwoIntEntered, error)
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

                <TextBox inputRef={startSt} label="Start Street" value={pointOne ? pointOne.startStreet : ""} />
                <TextBox inputRef={startCr} label="Start Cross-Sreet" value={pointOne ? pointOne.startCross : ""} />
                <TextBox inputRef={endSt} label="End Street" value={pointTwo ? pointTwo.endStreet : ""} />
                <TextBox inputRef={endCr} label="End Cross-Street" value={pointTwo ? pointTwo.endCross : ""} />

                {}

            </div>
            <div>
                <AwesomeButton
                    type="primary"
                    onPress={handleSubmit}
                >
                    Submit
                </AwesomeButton>
            </div>

            <div>
                <AwesomeButton
                    type="primary"
                    onPress={handleClearRoute}
                >
                    Clear Route
                </AwesomeButton>
            </div>

            <div>
                <AwesomeButton
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