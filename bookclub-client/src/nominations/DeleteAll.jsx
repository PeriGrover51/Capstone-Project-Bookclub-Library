import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext'
import { Link } from "react-router-dom"

export default function DeleteAll() {

    const { user } = useAuth()
    const { token } = useAuth()

    const navigate = useNavigate()

    async function handleDelete() {
        await fetch("http://localhost:8080/api/nominations", {
            method: "DELETE",
            headers: {
                Authorization: "Bearer " + token
            }
        })
        navigate("/nominations")
    }


    return (
        <div className="max-w-lg mx-auto mt-8 mb-8 p-8 border border-black bg-stone-100 rounded text-center font-bold text-red-900">
            <div className="m-4 text-2xl">Clear All Nominations</div>
            <div className="m-2 text-xl">WARNING: This will clear ALL CURRENT NOMINATIONS. DO NOT USE before the current voting session has concluded.</div>
            <div className="m-2 text-lg">Are you sure you want to clear all nominations?</div>
            <div className="px-1 pt-4 pb-2 flex justify-center">
                <Link to="/nominations"
                className="text-black bg-blue-400 hover:bg-blue-300 py-2 px-4 mx-2 rounded">
                    Cancel
                </Link>
                <button onClick={handleDelete} className="text-black bg-red-500 hover:bg-red-400 py-2 px-4 mx-2 rounded">
                Delete
                </button>
            </div>
        </div>
    )
}