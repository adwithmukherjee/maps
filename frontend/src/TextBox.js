

const TextBox = ({ label, value, inputRef }) => {



    return (
        <div style={{ margin: 10 }}>
            {label + ":      "}
            <input ref={inputRef}></input>
        </div>
    )


}

export default TextBox