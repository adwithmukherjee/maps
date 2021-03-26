import axios from "axios"

export default function (street, cross, callback) {
    axios.post(
        "http://localhost:4567/intersection",
        {
            "street": street,
            "cross": cross
        },
        {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
    ).then((res) => {

        callback(res)

    }).catch((err) => {

    })
}