import axios from "axios"

export default function(lat, long, callback) {
    axios.post(
        "http://localhost:4567/nearest",
        {
            "nodeLat": lat,
            "nodeLong": long
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