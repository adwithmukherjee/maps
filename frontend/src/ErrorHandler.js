import React from "react"

const ErrorHandler = ({error}) => {

    if(error != ""){
        return(
        
            <div style={{position: "absolute", marginLeft:"auto", bottom: "5vh", backgroundColor: "white", borderRadius: 20, color: "red", padding:10, fontSize: 15}}>
              {error}
            </div>
            
        )
    } else {
        return null
    }

}

export default ErrorHandler