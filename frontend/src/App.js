import React, { useState } from "react"
import './App.css';
import Route from "./Route"
import Map from "./Map"

function App() {

  const [nw, setNW] = useState({latitude: 41.82686216366997, longitude: -71.40645750837325})
  const [se, setSE] = useState({latitude: 41.82355259953458, longitude: -71.4000750453612})
  

  return (
    <div className="App">
      <header className="App-header">
        
        <Route title = "Maps"/>

        <Map corners = {{nw, se}}/>
      </header>
    </div>
  );
}

export default App;