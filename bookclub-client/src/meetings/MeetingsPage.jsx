import { useEffect, useState } from "react"
import MeetingCard from "./MeetingCard"
import { Link } from "react-router-dom"
import { useAuth } from '../AuthContext';


export default function MeetingsPage() {
    const { user } = useAuth()

    const [meetings, setMeetings] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/meetings")
            const payload = await response.json()

            //sort books by most recent 
            const sortedMeetings = [...payload].sort((a, b) => new Date(b.meetingDate) - new Date(a.meetingDate));

            setMeetings(sortedMeetings)
        }
        doFetch()
    }, [])

    return (
        <>
        <div className="p-6 mb-2 flex items-center rounded">
            <h1 className="font-bold text-4xl pl-6">MEETINGS</h1>
            {user &&
            <Link to="/meetings/add" 
                className="text-black text-lg add-button px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                    Add
            </Link>
            }
        </div>
        <div className="flex flex-wrap m-2 gap-4">
            {meetings.map(meeting => <MeetingCard meeting={meeting} />)}
        </div>
        </>
    )
}