

const TextBox = ({label, onChange}) => {

    const handleChange = (e) => {
        console.log(e.target.value)
        onChange(e.target.value)
    }

    return(
        <div style = {{margin: 10}}> 
            {label + ":      "}
            <input type = "text" onChange={handleChange}></input>
        </div>
    )
    

}

export default TextBox