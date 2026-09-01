import { useEffect, useState } from "react"
import BookCard from "./BookCard"
import { Link } from "react-router-dom"
import { useAuth } from '../AuthContext';

export default function BooksPage() {
    const { user } = useAuth()

    const [books, setBooks] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/books")
            const payload = await response.json()

            //sort books by most recent 
            const sortedBooks = [...payload].sort((a, b) => new Date(b.whenRead) - new Date(a.whenRead));

            setBooks(sortedBooks)
        }
        doFetch()
    }, [])

    

    return (
        <>
        <div className="p-6 mb-2 flex items-center rounded">
            <h1 className="font-bold text-4xl pl-6">Bookclub's Library</h1>
            {user &&
            <Link to="/books/add" 
                className="text-black text-lg bg-blue-500 hover:bg-blue-400 px-6 py-3 m-4  ml-auto rounded font-semibold w-50 text-center">
                    Add
            </Link>
            }
        </div>
        <div className="flex flex-wrap m-2 gap-4">
            {books.map(book => <BookCard book={book}/>)}
        </div>
        </>
    )
}