
import axios from "axios"
import { useEffect } from "react"
import './App.css';
import CheckinTable from "./CheckinTable"
import MapController from "./MapController"

function App() {



    return (
        <div className="App-header">
            <MapController />
            <CheckinTable/>
        </div>
    )
}

export default App