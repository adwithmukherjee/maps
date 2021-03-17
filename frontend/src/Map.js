import React, { useRef, useEffect, useState } from "react"
import axios from "axios"

const Map = ({ corners, canvasRef }) => {
    const { se, nw } = corners
    const [CANVAS_WIDTH, CANVAS_HEIGHT] = [500, 500]
    

    const [activeWays, setActiveWays] = useState([])

    const getPointsFromWay = (start, end) => {

        const latRange = nw.latitude - se.latitude
        const longRange = se.longitude - nw.longitude

        const startX = CANVAS_WIDTH * Math.abs(start.longitude - nw.longitude) / longRange
        const startY = CANVAS_HEIGHT * Math.abs(start.latitude - nw.latitude) / latRange

        const endX = CANVAS_WIDTH * Math.abs(end.longitude - nw.longitude) / longRange
        const endY = CANVAS_HEIGHT * Math.abs(end.latitude - nw.latitude) / latRange

        return {start: {x: startX, y: startY}, end: {x: endX, y:endY}}
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

    useEffect(() => {
        
        const canvas = canvasRef.current
        const context = canvas.getContext('2d')
        draw(context)

        const getWays = () => {

            axios.post(
                "http://localhost:4567/ways", 
                {  
                    "nwLat" : nw.latitude,
                    "nwLong" : nw.longitude,
                    "seLat": se.latitude,
                    "seLong": se.longitude
                },
                {
                    headers: {
                      "Content-Type": "application/json",
                      'Access-Control-Allow-Origin': '*',
                    }
                }
            ).then((res) => {
                const ways = res.data.ways
                console.log(ways)
    
                context.strokeStyle = "#FF0000";
            
                context.clearRect(0, 0, context.canvas.width, context.canvas.height);
                context.beginPath()
            
                ways.map((way) => {
                    const wayObj = {
                        start: {latitude: way[0], longitude: way[1]}, 
                        end: {latitude: way[2], longitude: way[3]}
                    }
                    drawWay(context,wayObj)
                })

                context.stroke();

            }).catch((err) => {
                
            })

        }
        getWays()
        


    }, [corners])


    return (
        <canvas ref = {canvasRef}/>

      
    )
}

export default Map