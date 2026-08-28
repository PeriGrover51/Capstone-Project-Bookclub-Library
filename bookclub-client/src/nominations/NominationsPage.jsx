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

    return (
            <>
            <div className="p-6 mb-2 flex items-center rounded">
                <h1 className="font-bold text-4xl pl-6">Book Nominations</h1>
                <Link to="/nominations/add" 
                    className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                        Add Nomination
                </Link>
            </div>
            <div className="flex flex-wrap m-2 gap-4">
                {nominations.map(nomination => <NominationCard nomination={nomination} />)}
            </div>
            </>
        )
}