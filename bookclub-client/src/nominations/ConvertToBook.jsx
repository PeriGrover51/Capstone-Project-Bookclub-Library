import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext';

export default function ConvertToBook() {

    const { token } = useAuth()
    const navigate = useNavigate()

    const { id } = useParams() //use to fetch nomination data, if undefined navigate to home page
    if (id === undefined) {
            navigate("/")
            return
    }

    //prepopulate with title / author / genre from nomination data
    const initialBookForm = {
        title: "",
        author: "",
        genre: "",
        whenRead: "",
        link: "",
        imgLink: ""
    }

    const [book, setBook] = useState(initialBookForm)

    const [errors, setErrors] = useState([])

    useEffect(() => {
        if (id === undefined) { //no id in url === error, return to home page
            navigate("/")
            return
        }

        const prepopulate = async function() { //id in url == fetch nomination data and set values in initialBookForm
            const response = await fetch("http://localhost:8080/api/nominations/" + id, {
                headers: {
                Authorization: "Bearer " + token
            }
            })
            const payload = await response.json()

            //set book with nomination data, which leaves some fields empty
            setBook(prevBook => ({
                ...prevBook,
                ...payload
            }))
        }
        prepopulate()
    }, [id])

    function handleChange(event) {
        const value = event.target.value

        setBook({...book, [event.target.name]: value})
    }

    async function handleSubmit(event) {
        event.preventDefault()

        const url = "http://localhost:8080/api/books"
        const method = "POST"

        const payload = { ...book }

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token
            },
            body: JSON.stringify(payload)
        })
        if (response.status >= 200 && response.status < 300) {
            navigate("/books")
        } else {
            const payload = await response.json()
            setErrors(payload)
        }
    }

    
    //form should be the same as BookForm?
    return (
        <div className="max-w-lg mx-auto mt-8 mb-8 p-8 border border-black bg-[#dcbfa3] rounded">
            <h1 className="font-bold text-xl font-serif mb-6 text-center">Add Nomination To Library</h1>
            <form onSubmit={handleSubmit} className="flex flex-col">

                <div className="flex flex-col mb-6">
                    <label htmlFor="title" className="font-semibold font-serif mb-1">
                        Title <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="title"
                        name="title"
                        type="text"
                        required
                        value={book.title} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                 <div className="flex flex-col mb-6">
                    <label htmlFor="author" className="font-semibold font-serif mb-1">
                        Author <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="author"
                        name="author"
                        type="text"
                        required
                        value={book.author} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="genre" className="font-semibold font-serif mb-1">
                        Genre <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="genre"
                        name="genre"
                        type="text"
                        required
                        value={book.genre} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="whenRead" className="font-semibold font-serif mb-1">
                        When Read <span className="text-red-600">*</span>
                    </label>
                    <input
                        id="whenRead"
                        name="whenRead"
                        type="date"
                        required
                        value={book.whenRead} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="link" className="font-semibold font-serif mb-1">
                        GoodReads Link
                    </label>
                    <input
                        id="link"
                        name="link"
                        type="url"
                        value={book.link} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <div className="flex flex-col mb-6">
                    <label htmlFor="imgLink" className="font-semibold font-serif mb-1">
                        Cover Image Link
                    </label>
                    <input
                        id="imgLink"
                        name="imgLink"
                        type="url"
                        value={book.imgLink} onChange={handleChange}
                        className="border border-black rounded px-3 py-2 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                <button
                    type="submit"
                    className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 mt-4 rounded font-semibold font-serif w-full text-center">
                    Save Book
                </button>

            </form>
        </div>
    )
}