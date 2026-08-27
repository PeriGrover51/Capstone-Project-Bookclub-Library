import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function NominationCard({ nomination }) {

    const { user } = useAuth()

    return (
        <div className="w-150 lg:flex flex-none">
            <div className="border-r border-b border-l border-gray-400 lg:border-l-0 lg:border-t lg:border-gray-400 bg-white rounded-b lg:rounded-b-none lg:rounded-r p-4 flex flex-col justify-between leading-normal">
                <div className="mb-6">
                    <div className="text-gray-900 font-bold text-xl mb-2">{nomination.title} by {nomination.author}</div>
                    <p className="text-gray-700 text-base">{nomination.genre}</p>
                    <p className="text-gray-700 text-base">Nominated By {nomination.user.username}</p>
                </div>
                {user && user.username === nomination.user.username &&
                <div className="px-1 pt-4 pb-2 flex">
                    <Link to={`/nominations/edit/${nomination.nominationId}`} 
                    className="text-black bg-yellow-500 hover:bg-yellow-400 py-2 px-4 mx-2 rounded">
                        Edit
                    </Link>
                </div>
                }
            </div>

        </div>
    )
}