import React, { useState, useCallback, useRef, useEffect } from "react"
import './App.css';
import Route from "./Route"
import Map from "./Map"

function App() {

  const [nw, setNW] = useState({latitude: 41.82686216366997, longitude: -71.40645750837325})
  const [se, setSE] = useState({latitude: 41.82355259953458, longitude: -71.4000750453612})
  
  const canvasRef = useRef(null)
  const isDragging = useRef(false)

  const onMouseDown = useCallback(e => {
    if (canvasRef.current && canvasRef.current.contains(e.target)) {
      console.log("go")
      isDragging.current = true
    }
  }, []);

  const onMouseUp = useCallback(() => {
    if (isDragging.current) {
      console.log("end")
      isDragging.current = false;
    }
  }, []);

  const onMouseMove = useCallback(e => {
    if (isDragging.current) {
      console.log(e.movementX)
      setNW(nw => {
         return {latitude: nw.latitude + 0.0001*e.movementY , longitude: nw.longitude - 1.928*0.0001*e.movementX}
      })
      setSE(se => {
        return {latitude: se.latitude + 0.0001*e.movementY , longitude: se.longitude - 1.928*0.0001*e.movementX }
      })
    }
  }, []);

  useEffect(() => {
    document.addEventListener("mouseup", onMouseUp);
    document.addEventListener("mousedown", onMouseDown);
    document.addEventListener("mousemove", onMouseMove);
    return () => {
      document.removeEventListener("mouseup", onMouseUp);
      document.removeEventListener("mousedown", onMouseDown);
      document.removeEventListener("mousemove", onMouseMove);
    };
  }, [onMouseMove, onMouseDown, onMouseUp]);

  return (
    <div className="App">
      <header className="App-header">
        
        <Route title = "Maps"/>

        <Map corners = {{nw, se}} canvasRef={canvasRef}/>
      </header>
    </div>
  );
}

export default App;