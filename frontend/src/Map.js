import React, { useEffect, useState } from "react"
import axios from "axios"

const Map = ({ corners, canvasRef, onZoom }) => {
    const { se, nw } = corners
    const [CANVAS_WIDTH, CANVAS_HEIGHT] = [800, 800]

    const TILE_ORIGIN = { latitude: 41.82686216366997, longitude: -71.40645750837325 }
    const TILE_WIDTH = -1 * (71.4000750453612 - 71.40645750837325) / 2
    const TILE_HEIGHT = (41.82686216366997 - 41.82355259953458) / 2

    const [activeWays, setActiveWays] = useState([])

    const [pointOne, setPointOne] = useState(null)
    const [pointTwo, setPointTwo] = useState(null)

    const [activeRoute, setActiveRoute] = useState([])

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
        //console.log(activeTiles)

        return activeTiles
    }

    const getPointFromCoords = (point) => {

        const latRange = nw.latitude - se.latitude
        const longRange = se.longitude - nw.longitude

        const startX = CANVAS_WIDTH * (point.longitude - nw.longitude) / longRange
        const startY = CANVAS_HEIGHT * -1 * (point.latitude - nw.latitude) / latRange

        return {x: startX, y: startY}
    }

    const getPointsFromWay = (n1, n2) => {

        const start = getPointFromCoords(n1)

        const end = getPointFromCoords(n2)

        return { start, end }
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
            //console.log("Requesting tiles: " + tile)
            const ways = res.data.ways
            const tileId = JSON.stringify(tile)
            localStorage.setItem(tileId, JSON.stringify(ways))
            setActiveWays(actWays => {
                return actWays.concat(ways)
            })

        }).catch((err) => {

        })
    }

    const onDoubleClick = (e) => {
        const canvas = canvasRef.current
        const rect = canvas.getBoundingClientRect()
        const x = e.pageX - rect.x
        const y = e.pageY - rect.y

        const latRange = nw.latitude - se.latitude
        const longRange = se.longitude - nw.longitude

        const lat = nw.latitude - latRange * (y) / CANVAS_HEIGHT
        const long = nw.longitude + longRange * x / CANVAS_WIDTH


        axios.post(
            "http://localhost:4567/nearest",
            {
                "nodeLat": lat,
                "nodeLong": long
            },
            {
                headers: {
                    "Content-Type": "application/json",
                    'Access-Control-Allow-Origin': '*',
                }
            }
        ).then((res) => {
            const node = res.data.nearest
            if(!pointTwo && !pointOne){
                setPointOne({coords: {latitude: node[0], longitude: node[1]}, id: })
            } else {
                if(pointOne && pointTwo){
                    setPointOne({latitude: node[0], longitude: node[1]})
                    setPointTwo(null)
    
                } else if(pointOne) {
                    //WHERE ROUTE CALL GOES 
                    setPointTwo({latitude: node[0], longitude: node[1]})
                 

                }
            }
            

        }).catch((err) => {

        })



        


    }

    useEffect(()=>{

        document.addEventListener("dblclick", onDoubleClick)
        return () => {
          document.removeEventListener("dblclick", onDoubleClick);
        };
    })

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

        canvas.onmousewheel = (e) => {
            onZoom(e.wheelDelta)
        }
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

        
        

        context.strokeStyle = "#00FF00";
        
        if(pointOne){
            const n1 = getPointFromCoords(pointOne)
            context.beginPath()
            context.arc(n1.x, n1.y, 10, 0, Math.PI * 2, true)
            context.stroke()
        }
        
        if(pointTwo){
            const n2 = getPointFromCoords(pointTwo)
            context.beginPath()
            context.arc(n2.x, n2.y, 10, 0, Math.PI * 2, true)
            context.stroke()
        }

        if(activeRoute.length != 0) {
            //DRAW ROUTE
            activeRoute.forEach((way) => {
                drawWay(way);
            })
        }
    
        

    }, [activeWays, pointOne, pointTwo, activeRoute])


    return (
        <canvas ref={canvasRef} />
    )
}

export default Map