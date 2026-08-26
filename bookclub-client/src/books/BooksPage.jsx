import { useEffect, useState } from "react"
import BookCard from "./BookCard"

export default function BooksPage() {

    const [books, setBooks] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/books")
            const payload = await response.json()
            setBooks(payload)
        }
        doFetch()
    }, [])

    return (
        <>
            {books.map(book => <BookCard book={book}/>)}
        </>
    )
}