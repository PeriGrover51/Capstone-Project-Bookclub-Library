import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext';


export default function MeetingForm() {

    const { user } = useAuth()
    const { token } = useAuth() //this is the jwt token
    const navigate = useNavigate()

    //id undefined = add (POST), id defined = edit (PUT)
    const { id } = useParams()


    //Fetching all books here to populate the form / insert into meeting form on selection
    const [books, setBooks] = useState([])


    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/books")
            const payload = await response.json()
            setBooks(payload)
        }
        doFetch()
    }, [])


    const initialMeetingForm = {
        readingGoal: "",
        meetingDate: "",
        meetingNotes: "",
        book: ""
    }

    const [meeting, setMeeting] = useState(initialMeetingForm)

    const [errors, setErrors] = useState([])

    useEffect(() => {
        if (id === undefined) { //no id in url == create new book
            setMeeting(initialMeetingForm)
            return
        }

        const prepopulate = async function() { //else (id in url) == update existing meeting == fetch meeting info from db
            const response = await fetch("http://localhost:8080/api/meetings/" + id)
            const payload = await response.json()
            setMeeting(payload)
        }
        prepopulate()
    }, [id])

    function handleChange(event) {
        //this handles the book object - use the bookId from the options element values to lookup the book object in books
        if (event.target.name === "book") {
            const selectedBook = books.find(b => String(b.bookId) === event.target.value)
            setMeeting({...meeting, book: selectedBook})
            return
        }

        const value = event.target.value

        setMeeting({...meeting, [event.target.name]: value})
    }


    //send jwt token in "Bearer {token}" auth header in http post/put request
    async function handleSubmit(event) {
        event.preventDefault()

        let url = "http://localhost:8080/api/meetings"
        let method = "POST"
        if (id !== undefined) {
            url += "/" + id
            method = "PUT"
        }

        const payload = { ...meeting }

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(payload)
        })
        if (response.status >= 200 && response.status < 300) {
            navigate("/meetings")
        } else {
            const payload = await response.json()
            setErrors(payload)
        }
    }

    return (
        <div className="max-w-lg mx-auto mt-8 mb-8 p-8 border border-black bg-stone-100 rounded">
            <h1 className="font-bold text-xl font-serif mb-6 text-center">{id === undefined ? "Add Meeting" : "Update Meeting"}</h1>
            <form onSubmit={handleSubmit} className="flex flex-col">
                {/* PUT BOOK DROP DOWN HERE */}
                <div className="flex flex-col mb-6">
                    <label htmlFor="book" className="font-semibold font-serif mb-1">
                        Meeting Date <span className="text-red-600">*</span>
                    </label>
                    <select
                        id="book"
                        name="book"
                        required
                        value={meeting.book?.bookId ?? ""} onChange={handleChange}>
                            <option value="" disabled>Select a book</option>
                            {books.map((book) => (
                                <option key={book.bookId} value={book.bookId}>{book.title}</option>
                            ))}
                    </select>
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="meetingDate" className="font-semibold font-serif mb-1">
                        Meeting Date <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="meetingDate"
                        name="meetingDate"
                        type="date"
                        required
                        value={meeting.meetingDate} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="readingGoal" className="font-semibold font-serif mb-1">
                        Reading Goal <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="readingGoal"
                        name="readingGoal"
                        type="text"
                        required
                        value={meeting.readingGoal} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                 <div className="flex flex-col mb-6">
                    <label htmlFor="meetingNotes" className="font-semibold font-serif mb-1">
                        Meeting Notes <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="meetingNotes"
                        name="meetingNotes"
                        type="text"
                        required
                        value={meeting.meetingNotes} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <button
                    type="submit"
                    className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 mt-4 rounded font-semibold font-serif w-full text-center">
                    Save Meeting
                </button>

            </form>
        </div>
    )
}