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
        <div className="max-w-lg mx-auto mt-8 mb-8 p-8 border border-black bg-stone-100 rounded">
            <h1 className="font-bold text-xl mb-6 text-center">Are you sure you want to delete this nomination?</h1>

            <p className="border border-black rounded py-3 text-center text-lg mb-4 bg-sky-100">Title: {nomination.title}</p>
            <p className="border border-black rounded py-3 text-center text-lg mb-4 bg-green-100">Author: {nomination.author}</p>
            <p className="border border-black rounded py-3 text-center text-lg mb-4 bg-pink-100">Genre: {nomination.genre}</p>

            <div className="px-1 pt-4 pb-2 flex justify-center">
                <Link to="/nominations"
                className="text-black bg-blue-500 hover:bg-blue-400 py-2 px-4 mx-2 rounded">
                    Cancel
                </Link>
                <button onClick={handleDelete} className="text-black bg-red-500 hover:bg-red-400 py-2 px-4 mx-2 rounded">
                Delete
                </button>
            </div>
        </div>
    )
}