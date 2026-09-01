import { useEffect, useState } from "react"
import NominationCard from "./NominationCard"
import { Link } from "react-router-dom"
import { useAuth } from '../AuthContext';


export default function NominationsPage() {

    const { token } = useAuth()
    const [nominations, setNominations] = useState([])

    //for this page, we need to send the auth token in order to fetch nominations
    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/nominations", {
            headers: {
                Authorization: "Bearer " + token
            }
            })
            const payload = await response.json()
            setNominations(payload)
        }
        doFetch()
    }, [])

    const [showScore, setShowScore] = useState(false)

    return (
            <>
            <div className="p-6 mb-2 flex items-center rounded">
                <h1 className="font-bold text-4xl pl-6">Book Nominations</h1>
                <Link to="/nominations/add" 
                    className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                        Add Nomination
                </Link>
                {nominations.length > 0 && 
                <button className="text-black text-lg bg-green-500 hover:bg-green-400 px-6 py-3 m-4  ml-auto rounded font-semibold w-75 text-center"
                    onClick={() => setShowScore(!showScore)}>
                    Show Voting Results
                </button>
                }
            </div>
            <div className="flex flex-wrap m-2 gap-4">
                {nominations.map(nomination => <NominationCard nomination={nomination} showScore={showScore}/>)}
            </div>
            {nominations.length > 0 &&
            <div className="p-6 mb-2 flex justify-center items-center rounded">
                <Link to="/nominations/delete/all" 
                    className="text-black text-lg bg-red-500 hover:bg-red-400 px-6 py-3 m-4 rounded-full font-semibold w-50 text-center">
                        Clear All Nominations
                </Link>
            </div>
            }
            </>
        )
}