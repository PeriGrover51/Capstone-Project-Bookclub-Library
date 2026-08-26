import { useEffect, useState } from "react"
import MeetingCard from "./MeetingCard"

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
        {meetings.map(meeting => <MeetingCard meeting={meeting} />)}
        </>
    )
}