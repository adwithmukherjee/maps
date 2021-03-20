import React, { useState, useCallback, useRef, useEffect } from "react"
import './App.css';
import Route from "./Route"
import Map from "./Map"

function App() {

  const [nw, setNW] = useState({ latitude: 41.82686216366997, longitude: -71.40645750837325 })
  const [se, setSE] = useState({ latitude: 41.82355259953458, longitude: -71.4000750453612 })

  const [tempNW, setTempNW] = useState(nw)
  const [tempSE, setTempSE] = useState(se)

  const dragStartX = useRef(0)
  const dragStartY = useRef(0)

  const canvasRef = useRef(null)
  const isDragging = useRef(false)

  const onMouseDown = useCallback(e => {
    if (canvasRef.current && canvasRef.current.contains(e.target)) {
      //console.log("go")
      isDragging.current = true
      dragStartX.current = e.pageX
      dragStartY.current = e.pageY
    }
  }, []);

  const onMouseUp = useCallback(e => {
    if (isDragging.current) {
      console.log("end")
      isDragging.current = false;

      setNW(nw => {
        return { latitude: nw.latitude + 0.000005 * -1 * (dragStartY.current - e.pageY), longitude: nw.longitude - 1.928 * 0.000005 * (e.pageX - dragStartX.current) }
      })
      setSE(se => {
        return { latitude: se.latitude + 0.000005 * -1 * (dragStartY.current - e.pageY), longitude: se.longitude - 1.928 * 0.000005 * (e.pageX - dragStartX.current) }
      })
    }
  }, []);

  const onMouseMove = useCallback(e => {
    if (isDragging.current) {
      //console.log(e.movementX)
      // setTempNW(nw => {
      //   return { latitude: nw.latitude + 0.00001 * e.movementY, longitude: nw.longitude - 1.928 * 0.00001 * e.movementX }
      // })
      // setTempSE(se => {
      //   return { latitude: se.latitude + 0.00001 * e.movementY, longitude: se.longitude - 1.928 * 0.00001 * e.movementX }
      // })
    }
  }, []);

  useEffect(() => {
    document.addEventListener("mouseup", onMouseUp);
    document.addEventListener("mousedown", onMouseDown);
    //document.addEventListener("mousemove", onMouseMove);
    return () => {
      document.removeEventListener("mouseup", onMouseUp);
      document.removeEventListener("mousedown", onMouseDown);
      //document.removeEventListener("mousemove", onMouseMove);
    };
  }, [onMouseDown, onMouseUp]);

  return (
    <div className="App">
      <header className="App-header">

        <Route title="Maps" />

        <Map corners={{ nw, se }} canvasRef={canvasRef} />
      </header>
    </div>
  );
}

export default App;