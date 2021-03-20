import React, { useRef, useEffect, useState } from "react"
import axios from "axios"
import { act } from "react-dom/test-utils"

const Map = ({ corners, canvasRef }) => {
    const { se, nw } = corners
    const [CANVAS_WIDTH, CANVAS_HEIGHT] = [500, 500]

    const TILE_ORIGIN = { latitude: 41.82686216366997, longitude: -71.40645750837325 }
    const TILE_WIDTH = -1 * (71.4000750453612 - 71.40645750837325) / 2
    const TILE_HEIGHT = (41.82686216366997 - 41.82355259953458) / 2

    const [activeWays, setActiveWays] = useState([])

    const getTileFromCoords = (latitude, longitude) => {

        const tilesHeight = (latitude - TILE_ORIGIN.latitude) / TILE_HEIGHT
        const tilesWidth = (longitude - TILE_ORIGIN.longitude) / TILE_WIDTH

        let tileLong
        let tileLat
        if (tilesHeight == 0) {
            tileLong = TILE_ORIGIN.longitude
        } else {
            tileLong = TILE_ORIGIN.longitude + TILE_WIDTH * tilesWidth / Math.abs(tilesWidth) * Math.floor(Math.abs(tilesWidth))
        }

        if (tilesWidth == 0) {
            tileLat = TILE_ORIGIN.latitude
        } else {
            tileLat = TILE_ORIGIN.latitude + TILE_HEIGHT * tilesHeight / Math.abs(tilesHeight) * Math.floor(Math.abs(tilesHeight))
        }

        return { latitude: tileLat, longitude: tileLong }
    }

    const getActiveTiles = () => {

        const tileNW = getTileFromCoords(nw.latitude, nw.longitude)
        const tileSE = getTileFromCoords(se.latitude, se.longitude)

        // console.log(tileNW)
        // console.log(tileSE)
        //  console.log("start")

        let currentTile = { latitude: tileNW.latitude + TILE_HEIGHT, longitude: tileNW.longitude - TILE_WIDTH }

        const activeTiles = []

        while (currentTile.latitude > tileSE.latitude - TILE_HEIGHT) {
            while (currentTile.longitude < tileSE.longitude + TILE_WIDTH) {
                activeTiles.push(currentTile)
                const newTile = { latitude: currentTile.latitude, longitude: currentTile.longitude + TILE_WIDTH }
                currentTile = newTile
            }
            currentTile.longitude = tileNW.longitude - TILE_WIDTH
            currentTile.latitude = currentTile.latitude - TILE_HEIGHT
        }

        // console.log("active tiles set")
        console.log(activeTiles)

        return activeTiles
    }

    const getPointsFromWay = (start, end) => {

        const latRange = nw.latitude - se.latitude
        const longRange = se.longitude - nw.longitude

        const startX = CANVAS_WIDTH * (start.longitude - nw.longitude) / longRange
        const startY = CANVAS_HEIGHT * -1 * (start.latitude - nw.latitude) / latRange

        const endX = CANVAS_WIDTH * (end.longitude - nw.longitude) / longRange
        const endY = CANVAS_HEIGHT * -1 * (end.latitude - nw.latitude) / latRange

        return { start: { x: startX, y: startY }, end: { x: endX, y: endY } }
    }

    const draw = context => {

        context.fillStyle = '#000000'
        context.canvas.width = CANVAS_WIDTH
        context.canvas.height = CANVAS_HEIGHT
        context.fillRect(0, 0, context.canvas.width, context.canvas.height)
    }

    const drawWay = (context, way) => {
        const { start, end } = getPointsFromWay(way.start, way.end);
        context.moveTo(start.x, start.y)
        context.lineTo(end.x, end.y)
    }

    const requestWays = (tile) => {

        axios.post(
            "http://localhost:4567/ways",
            {
                "nwLat": tile.latitude,
                "nwLong": tile.longitude,
                "seLat": tile.latitude - TILE_HEIGHT,
                "seLong": tile.longitude + TILE_WIDTH
            },
            {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }
        ).then((res) => {
            console.log("Requesting tiles: " + tile)
            const ways = res.data.ways
            const tileId = JSON.stringify(tile)
            localStorage.setItem(tileId, JSON.stringify(ways))
            setActiveWays(actWays => {
                return actWays.concat(ways)
            })

            // context.strokeStyle = "#FF0000";

            // context.clearRect(0, 0, context.canvas.width, context.canvas.height);
            // context.beginPath()

            // ways.map((way) => {
            //     const wayObj = {
            //         start: {latitude: way[0], longitude: way[1]}, 
            //         end: {latitude: way[2], longitude: way[3]}
            //     }
            //     drawWay(context,wayObj)
            // })

            // context.stroke();

        }).catch((err) => {

        })
    }

    useEffect(() => {

        setActiveWays([])
        const getWays = () => {

            const tiles = getActiveTiles();
            tiles.forEach((tile) => {
                const tileId = JSON.stringify(tile)
                const tileWays = JSON.parse(localStorage.getItem(tileId))
                if (tileWays) {
                    setActiveWays(actWays => {
                        return actWays.concat(tileWays)
                    })
                } else {
                    requestWays(tile);
                }
            })
        }
        getWays()
    }, [corners])

    //DRAW CANVAS
    useEffect(() => {
        const canvas = canvasRef.current
        const context = canvas.getContext('2d')
        draw(context)

        context.strokeStyle = "#FF0000";

        //context.clearRect(0, 0, context.canvas.width, context.canvas.height);
        context.beginPath()

        activeWays.map((way) => {
            const wayObj = {
                start: { latitude: way[0], longitude: way[1] },
                end: { latitude: way[2], longitude: way[3] }
            }
            drawWay(context, wayObj)
        })
        context.stroke();

    }, [activeWays])


    return (
        <canvas ref={canvasRef} />
    )
}

export default Map