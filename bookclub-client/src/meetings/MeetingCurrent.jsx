import { useState, useEffect } from "react"
import { Link } from "react-router-dom"


export default function MeetingCurrent() {

    const [meeting, setMeeting] = useState(null)

    useEffect(() => {
        const fetchCurrent = async () => {
            const response = await fetch("http://localhost:8080/api/meetings/current")
            const payload = await response.json()
            setMeeting(payload)
        }
        fetchCurrent()
    }, [])

    return (
        <div>
            <div className="w-full px-6 py-8 sm:px-10">
                <div className="text-4xl font-bold uppercase tracking-wide">{meeting !== null ? "Upcoming Meeting" : "No Upcoming Meeting"}</div>
            </div>
            {meeting && 
            <div className="mx-auto max-w-5xl px-6 py-10 sm:px-10">
                <div className="flex flex-col gap-8 md:flex-row">
                    {/* Book Cover */}
                    <div className="flex-shrink-0 md:w-64 mt-8">
                        <img
                            src={meeting.book?.imgLink || "https://placehold.co/400x600?text=No+Cover"}
                            alt={meeting.book?.title ? `Cover of ${meeting.book.title}` : "Book cover"}
                            className="w-full rounded-lg shadow-lg object-cover aspect-[2/3] sepia-50 hover:filter-none"
                        />
                    </div>

                    {/* Meeting Info */}
                    <div className="flex-1 space-y-6 file-card">
                        <div>
                            <h2 className="text-2xl file-card--meeting current-meeting__tab text-center mb-4 p-3">
                                {meeting.meetingDate}
                            </h2>
                            <h2 className="text-2xl font-semibold text-slate-900">
                                {meeting.book.title}
                            </h2>
                            <p className="mt-1 text-lg text-slate-800">
                                {meeting.book.author}
                            </p>
                            <p className="mt-1 text-m text-slate-800">
                                {meeting.book.genre}
                            </p>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-slate-800">Reading Goal:</dt>
                            <dd className="mt-1 text-base text-slate-700">{meeting.readingGoal}</dd>
                        </div>

                        <div>
                            <dt className="text-sm font-medium text-slate-800">Notes:</dt>
                            <dd className="mt-1 text-base text-slate-700">{meeting.meetingNotes}</dd>
                        </div>

                        <div>
                            <a href={meeting.book.link} className="font-medium text-blue-600 hover:underline flex">GoodReads Link</a>
                        </div>
                    </div>
                </div>
            </div>
            }
        </div>
    )
}