import React, { useState, useCallback, useRef, useEffect } from "react"
import './App.css';
import Map from "./Map"

function MapController({setError}) {

  const [nw, setNW] = useState({ latitude: 41.82686216366997, longitude: -71.40645750837325 })
  const [se, setSE] = useState({ latitude: 41.82355259953458, longitude: -71.4000750453612 })

  const [zoom, setZoom] = useState(1)

  const dragStartX = useRef(0)
  const dragStartY = useRef(0)

  const canvasRef = useRef(null)
  const isDragging = useRef(false)

  const onMouseDown = useCallback(e => {
    if (canvasRef.current && canvasRef.current.contains(e.target)) {
      isDragging.current = true
      dragStartX.current = e.pageX
      dragStartY.current = e.pageY
    }
  }, []);

  const onMouseUp = useCallback(e => {
    if (isDragging.current) {
      isDragging.current = false;

      setNW(nw => {
        return { latitude: nw.latitude + 0.000005 * -1 * (dragStartY.current - e.pageY), longitude: nw.longitude - 1.928 * 0.000005 * (e.pageX - dragStartX.current) }
      })
      setSE(se => {
        return { latitude: se.latitude + 0.000005 * -1 * (dragStartY.current - e.pageY), longitude: se.longitude - 1.928 * 0.000005 * (e.pageX - dragStartX.current) }
      })
    }
  }, []);


  const onZoom = useCallback(y => {
    //console.log(y)
    setZoom(z => z + 0.01 * y)
    setNW(nw => {
      return { latitude: nw.latitude + 0.0000001 * -1 * (y), longitude: nw.longitude + 1.928 * 0.0000001 * (y) }
    })
    setSE(se => {
      return { latitude: se.latitude + 0.0000001 * (y), longitude: se.longitude - 1.928 * 0.0000001 * (y) }
    })

  }, [zoom])



  useEffect(() => {
    document.addEventListener("mouseup", onMouseUp);
    document.addEventListener("mousedown", onMouseDown);
    document.body.style.overflow = "hidden"
    return () => {
      document.removeEventListener("mouseup", onMouseUp);
      document.removeEventListener("mousedown", onMouseDown);
    };
  }, [onMouseDown, onMouseUp]);

  return (

    <div style={{ display: "flex", flexDirection: "row", textAlign: "center", zIndex: 20 }}>

      <Map corners={{ nw, se }} onZoom={onZoom} canvasRef={canvasRef} setError={setError}/>

    </div>

  );
}

export default MapController;