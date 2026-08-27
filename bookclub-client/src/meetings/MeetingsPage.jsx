import { useEffect, useState } from "react"
import MeetingCard from "./MeetingCard"
import { Link } from "react-router-dom"


export default function MeetingsPage() {

    const [meetings, setMeetings] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/meetings")
            const payload = await response.json()
            setMeetings(payload)
        }
        doFetch()
    }, [])

    return (
        <>
        <div className="p-6 mb-2 flex items-center rounded">
            <h1 className="font-bold text-4xl pl-6">Bookclub's Meetings</h1>
            <Link to="/meetings/add" 
                className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                    Add
            </Link>
        </div>
        <div className="flex flex-wrap m-2 gap-4">
            {meetings.map(meeting => <MeetingCard meeting={meeting} />)}
        </div>
        </>
    )
}