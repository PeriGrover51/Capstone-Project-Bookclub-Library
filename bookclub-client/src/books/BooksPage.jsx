import { useEffect, useState } from "react"
import BookCard from "./BookCard"
import { Link } from "react-router-dom"
import { useAuth } from '../AuthContext';

export default function BooksPage() {
    const { user } = useAuth()
    const { token } = useAuth()

    const [books, setBooks] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/books")

            if (response.status >= 200 && response.status < 300) {
                const payload = await response.json()
                //sort books by most recent
                const sortedBooks = [...payload].sort((a, b) => new Date(b.whenRead) - new Date(a.whenRead));

                setBooks(sortedBooks)
            }

        }
        doFetch()
    }, [])


    const [faves, setFaves] = useState([])

    useEffect(() => {
        if (!user) {
            return
        }
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/favorites/mine", {
                headers: {
                Authorization: "Bearer " + token
            }
            })

            if (response.status >= 200 && response.status < 300) {
                const payload = await response.json()
                setFaves(payload)
            }
        }
        doFetch()
    }, [])


    //search bar functionality
    const [searchQuery, setSearchQuery] = useState('')

    const filteredBooks = books.filter(book => book.title.toLowerCase().includes(searchQuery.toLowerCase()))


    //pagination functionality
    const [currentPage, setCurrentPage] = useState(1)
    const cardsPerPage = 6

    const indexOfLastCard = currentPage * cardsPerPage
    const indexOfFirstCard = indexOfLastCard - cardsPerPage
    const currentCards = filteredBooks.slice(indexOfFirstCard, indexOfLastCard)

    //const totalPages = Math.ceil(filteredBooks.length / cardsPerPage)
    const totalPages = filteredBooks.length > 0 ? Math.ceil(filteredBooks.length / cardsPerPage) : 1

    const handleNext = () => {
        if (currentPage < totalPages) setCurrentPage((prev) => prev + 1)
    }

    const handlePrev = () => {
        if (currentPage > 1) setCurrentPage((prev) => prev - 1)
    }


    

    return (
        <>
        <div className="p-6 mb-2 flex items-center rounded">
            <h1 className="font-bold text-4xl pl-6 taped-note--header taped-note">LIBRARY</h1>
            <div className="ml-6 library-card">
                <input className=" ml-6"
                type="text" placeholder="search by title..." value={searchQuery} 
                onChange={(e) => {
                    setSearchQuery(e.target.value);
                    //add page-reset here
                    setCurrentPage(1)
                }
            } />
            </div>
            {user &&
            <Link to="/books/add" 
                className="text-black text-lg add-button px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                    Add
            </Link>
            }
        </div>
        <div className="flex flex-wrap m-2 gap-4">
            {currentCards.map(book => <BookCard book={book} isFavorite={faves.some((fav) => fav.bookId === book.bookId)} otherUser={false} />)}
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