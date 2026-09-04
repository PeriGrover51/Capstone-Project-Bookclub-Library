import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext';

export default function BookForm() {

    const { user } = useAuth()
    const { token } = useAuth() //this is the jwt token
    const navigate = useNavigate()

    //id undefined = add (POST), id defined = edit (PUT)
    const { id } = useParams()

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
        if (id === undefined) { //no id in url == create new book
            setBook(initialBookForm)
            return
        }

        const prepopulate = async function() { //else (id in url) == update existing book == fetch book info from db
            const response = await fetch("http://localhost:8080/api/books/" + id)

            const payload = await response.json()
            
            if (response.status >= 200 && response.status < 300) {
                setBook(payload)
            }
        }
        prepopulate()
    }, [id])

    function handleChange(event) {
        const value = event.target.value

        setBook({...book, [event.target.name]: value})
    }

    //send jwt token in "Bearer {token}" auth header in http post/put request

    async function handleSubmit(event) {
        event.preventDefault()

        let url = "http://localhost:8080/api/books"
        let method = "POST"
        if (id !== undefined) {
            url += "/" + id
            method = "PUT"
        }

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

    return (
        <div className="flex justify-center">
        <div className="w-130 mt-8 mb-2 p-8 library-card rounded">
            <h1 className="font-bold text-xl font-serif mb-6 text-center mt-8">{id === undefined ? "Add Book" : "Update Book"}</h1>
            <form onSubmit={handleSubmit} className="flex flex-col">

                <div className="flex flex-col mb-6 library-card__field">
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

                 <div className="flex flex-col mb-6 library-card__field">
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

                <div className="flex flex-col mb-6 library-card__field">
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

                <div className="flex flex-col mb-6 library-card__field">
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

                <div className="flex flex-col mb-6 library-card__field">
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

                <div className="flex flex-col mb-6 library-card__field">
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
                    className="text-black text-lg add-button px-6 py-3 mt-4 font-semibold font-serif text-center w-1/2 mx-auto">
                    Save Book
                </button>

            </form>
        </div>
        </div>
    )
}