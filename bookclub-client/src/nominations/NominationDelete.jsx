import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext'
import { Link } from "react-router-dom"

export default function NominationDelete() {

    const { user } = useAuth()
    const { token } = useAuth()

    const { id } = useParams()

    const navigate = useNavigate()

    const [nomination, setNomination] = useState({})

    useEffect(() => {
        if (id === undefined) {
            navigate("/nominations")
            return
        }

        const prepopulate = async function() {
            const response = await fetch("http://localhost:8080/api/nominations/" + id, {
                headers: {
                Authorization: "Bearer " + token
            } //get request for noms needs auth token
            })
            const payload = await response.json()
            //check that the payload is not empty?
            //check that the payload's user matches the current user
            if (payload.user.username !== user.username) {
                navigate("/nominations")
            }

            setNomination(payload)
        }
        prepopulate()
    }, [id])

    async function handleDelete() {
        await fetch("http://localhost:8080/api/nominations/" + id, {
            method: "DELETE",
            headers: {
                Authorization: "Bearer " + token
            }
        })
        navigate("/nominations")
    }

    return (
        <div>
            <h4>Are you sure you want to delete this nomination?</h4>

            <p>Title: {nomination.title}</p>
            <p>Author: {nomination.author}</p>
            <p>Genre: {nomination.genre}</p>

            <Link to="/nominations"
                className="text-black bg-blue-500 hover:bg-blue-400 py-2 px-4 mx-2 rounded">
                    Cancel
            </Link>
            <button onClick={handleDelete} className="text-black bg-red-500 hover:bg-red-400 py-2 px-4 mx-2 rounded">
                Delete
            </button>
        </div>
    )
}