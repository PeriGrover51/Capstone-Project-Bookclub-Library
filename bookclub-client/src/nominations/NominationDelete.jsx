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
        <>
        <h1 className="font-bold text-4xl mb-6 mt-8 text-center w-full">DELETE NOMINATION</h1>
        <div className="flex justify-center">
        <div className="w-96 mx-auto mt-8 mb-8 p-8 library-card">

            <p className="library-card__field">{nomination.title}</p>
            <p className="library-card__field">{nomination.author}</p>
            <p className="library-card__field">{nomination.genre}</p>

            <div className="px-1 pt-4 pb-2 flex justify-center">
                <Link to="/nominations"
                className="text-black add-button py-2 px-4 mx-2">
                    Cancel
                </Link>
                <button onClick={handleDelete} className="text-black delete-button py-2 px-4 mx-2">
                Delete
                </button>
            </div>
        </div>
        </div>
        </>
    )
}