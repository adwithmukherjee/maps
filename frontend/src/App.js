
import axios from "axios"
import { useEffect, useState } from "react"
import './App.css';
import CheckinTable from "./CheckinTable"
import ErrorHandler from "./ErrorHandler";
import MapController from "./MapController"


function App() {

    const [error, setError] = useState("hello")

    useEffect(() => {
        const timer = setTimeout(() => {
          setError("")
        }, 5000);

        return () => clearTimeout(timer);
    }, [error]);

    
    return (
        <div className="App-header">
            <MapController setError={setError}/>
            <CheckinTable setError={setError}/>
            <ErrorHandler error={error}/>
        </div>
    )
}

export default App