import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function MeetingCard({ meeting }) {

    const { user } = useAuth()

    return (
        <div className="max-w-lg min-w-md lg:flex lg:w-[60rem] flex-none file-card file-card--meeting">
            <div className="h-48 lg:h-auto lg:w-48 flex-none bg-cover rounded-t lg:rounded-t-none lg:rounded-l text-center overflow-hidden sepia-100"
            style={{backgroundImage: `url(${meeting.book.imgLink})`}}></div>
            <div className="lg:rounded-r p-4 flex flex-col justify-between leading-normal">
                <div className="flex items-center">
                    <div className="file-card__tab">
                        <p className="">{meeting.meetingDate}</p>
                    </div>
                </div>
                <div className="mb-6">
                    <div className="text-gray-900 font-bold text-xl mb-2">{meeting.book.title} by {meeting.book.author}</div>
                    <p className="text-gray-800 text-base">{meeting.readingGoal}</p>
                    <p className="text-gray-800 text-base">{meeting.meetingNotes}</p>
                </div>
                {user && 
                <div className="px-1 pt-4 pb-2 flex">
                    <Link to={`/meetings/edit/${meeting.meetingId}`} 
                    className="text-black edit-button py-2 px-4 mx-2">
                        Edit
                    </Link>
                </div>
                }
            </div>

        </div>
    )
}