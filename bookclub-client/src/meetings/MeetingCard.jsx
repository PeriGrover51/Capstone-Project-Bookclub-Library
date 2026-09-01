import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function MeetingCard({ meeting }) {

    const { user } = useAuth()

    return (
        <div className="w-150 lg:flex flex-none">
            <div className="h-48 lg:h-auto lg:w-48 flex-none bg-cover rounded-t lg:rounded-t-none lg:rounded-l text-center overflow-hidden"
            style={{backgroundImage: `url(${meeting.book.imgLink})`}}></div>
            <div className="border border-gray-400 bg-[#dcbfa3] hover:bg-gray-100 lg:rounded-r p-4 flex flex-col justify-between leading-normal">
                <div className="mb-6">
                    <div className="text-gray-900 font-bold text-xl mb-2">{meeting.book.title} by {meeting.book.author}</div>
                    <p className="text-gray-800 text-base">{meeting.readingGoal}</p>
                    <p className="text-gray-800 text-base">{meeting.meetingNotes}</p>
                </div>
                <div className="flex items-center">
                    <div className="text-sm bg-gray-200 rounded-full p-2 mb-4">
                        <p className="text-gray-900">{meeting.meetingDate}</p>
                    </div>
                </div>
                {user && 
                <div className="px-1 pt-4 pb-2 flex">
                    <Link to={`/meetings/edit/${meeting.meetingId}`} 
                    className="text-black bg-yellow-500 hover:bg-yellow-400 py-2 px-4 mx-2 rounded">
                        Edit
                    </Link>
                </div>
                }
            </div>

        </div>
    )
}