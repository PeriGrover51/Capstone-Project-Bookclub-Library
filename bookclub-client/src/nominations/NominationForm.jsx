import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext';

export default function NominationForm() {
    const { user } = useAuth()
    const { token } = useAuth() //this is the jwt token
    const navigate = useNavigate()

    //id undefined = add (POST), id defined = edit (PUT)
    const { id } = useParams()

    const initialNominationForm = {
        title: "",
        author: "",
        genre: "",
        user: user
    }

    const [nomination, setNomination] = useState(initialNominationForm)

    const [errors, setErrors] = useState([])

    useEffect(() => {
        if (id === undefined) { //no id in url == create new nom
            setNomination(initialNominationForm)
            return
        }

        const prepopulate = async function() { //else (id in url) == update existing nom == fetch nom info from db
            const response = await fetch("http://localhost:8080/api/nominations/" + id, {
                headers: {
                Authorization: "Bearer " + token
            } //get request for noms needs auth token
            })
            const payload = await response.json()
            //we want to set the form with db nomination info for update, BUT keep the current user (not overwrite them)
            setNomination({ ...payload, user})
        }
        prepopulate()
    }, [id])


    function handleChange(event) {
        const value = event.target.value

        setNomination({...nomination, [event.target.name]: value})
    }


    //send jwt token in "Bearer {token}" auth header in http post/put request
    async function handleSubmit(event) {
        event.preventDefault()

        let url = "http://localhost:8080/api/nominations"
        let method = "POST"
        if (id !== undefined) {
            url += "/" + id
            method = "PUT"
        }

        const payload = { ...nomination }

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(payload)
        })
        if (response.status >= 200 && response.status < 300) {
            navigate("/nominations")
        } else {
            const payload = await response.json()
            setErrors(payload)
        }
    }

    return (
        <div className="max-w-lg mx-auto mt-8 mb-8 p-8 border border-black bg-[#dcbfa3] rounded">
            <h1 className="font-bold text-xl font-serif mb-6 text-center">{id === undefined ? "Add Nomination" : "Update Nomination"}</h1>
            <form onSubmit={handleSubmit} className="flex flex-col">

                <div className="flex flex-col mb-6">
                    <label htmlFor="title" className="font-semibold font-serif mb-1">
                        Title <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="title"
                        name="title"
                        type="text"
                        required
                        value={nomination.title} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="author" className="font-semibold font-serif mb-1">
                        Author <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="author"
                        name="author"
                        type="text"
                        required
                        value={nomination.author} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                 <div className="flex flex-col mb-6">
                    <label htmlFor="genre" className="font-semibold font-serif mb-1">
                        Genre <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="genre"
                        name="genre"
                        type="text"
                        required
                        value={nomination.genre} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <button
                    type="submit"
                    className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 mt-4 rounded font-semibold font-serif w-full text-center">
                    Save Nomination
                </button>

            </form>
        </div>
    )
}