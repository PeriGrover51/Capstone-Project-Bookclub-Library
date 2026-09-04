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

    //search by date range
    const [startDate, setStartDate] = useState('')
    const [endDate, setEndDate] = useState('')

    const filteredMeetings = meetings.filter((meeting) => {
        //if no dates selected, no filtering
        if (!startDate && !endDate) {
            return true
        }

        const meetingDate = new Date(meeting.meetingDate)
        const start = startDate ? new Date(startDate) : null
        const end = endDate ? new Date(endDate) : null

        if (start && meetingDate < start) {
            return false
        }
        if (end && meetingDate > end) { 
            return false
        }

        return true
    })

    const clearDates = () => {
        setStartDate('')
        setEndDate('')
    }


    //pagination functionality
    const [currentPage, setCurrentPage] = useState(1)
    const cardsPerPage = 4

    const indexOfLastCard = currentPage * cardsPerPage
    const indexOfFirstCard = indexOfLastCard - cardsPerPage
    const currentCards = filteredMeetings.slice(indexOfFirstCard, indexOfLastCard)

    //const totalPages = Math.ceil(filteredMeetings.length / cardsPerPage)
    const totalPages = filteredMeetings.length > 0 ? Math.ceil(filteredMeetings.length / cardsPerPage) : 1

    const handleNext = () => {
        if (currentPage < totalPages) setCurrentPage((prev) => prev + 1)
    }

    const handlePrev = () => {
        if (currentPage > 1) setCurrentPage((prev) => prev - 1)
    }

    return (
        <>
        <div className="p-6 mb-2 flex items-center rounded">
            <h1 className="font-bold text-4xl pl-6 taped-note--header taped-note">MEETINGS</h1>
            <div className="ml-6 library-card">
                <label className="mr-4">
                Start Date:
                </label>
                <input className="" type="date" value={startDate} onChange={(e) => {
                    setStartDate(e.target.value);
                    setCurrentPage(1);
                }} />
            </div>
            <div className="ml-1 library-card">
                <label className="mr-4">
                    End Date:     
                </label>
                <input className="" type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
            </div>
            <button className="delete-button p-3" onClick={clearDates}>Clear Dates</button>
            {user &&
            <Link to="/meetings/add" 
                className="text-black text-lg add-button px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                    Add
            </Link>
            }
        </div>
        <div className="flex flex-wrap m-2 gap-4">
            {currentCards.map(meeting => <MeetingCard meeting={meeting} />)}
        </div>

        <div className="sticky-note-stack w-full justify-center">
            <button className="sticky-note sticky-note--blue flex justify-center items-center text-5xl disabled:invisible" 
                onClick={handlePrev}
                disabled={currentPage === 1}>
                {"<"}-
            </button>
            <button className="sticky-note sticky-note--blue flex justify-center items-center text-5xl disabled:invisible" 
                onClick={handleNext}
                disabled={currentPage === totalPages}>
                -{">"}
            </button>
        </div>
        </>
    )
}