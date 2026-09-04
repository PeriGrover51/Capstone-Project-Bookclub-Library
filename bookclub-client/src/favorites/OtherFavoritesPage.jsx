import { useEffect, useState } from "react"
import BookCard from "../books/BookCard"
import { Link } from "react-router-dom"
import { useNavigate, useParams } from "react-router-dom"
import { useAuth } from '../AuthContext'

export default function OtherFavoritesPage() {

    const { user } = useAuth()
    const { token } = useAuth()

    const { username } = useParams()

    const [faves, setFaves] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/favorites/user/" + username, {
                headers: {
                Authorization: "Bearer " + token
            }
            })
            const payload = await response.json()

            setFaves(payload)
        }
        doFetch()
    }, [])

    //pagination functionality
    const [currentPage, setCurrentPage] = useState(1)
    const cardsPerPage = 6

    const indexOfLastCard = currentPage * cardsPerPage
    const indexOfFirstCard = indexOfLastCard - cardsPerPage
    const currentCards = faves.slice(indexOfFirstCard, indexOfLastCard)

    //const totalPages = Math.ceil(faves.length / cardsPerPage)
    const totalPages = faves.length > 0 ? Math.ceil(faves.length / cardsPerPage) : 1

    const handleNext = () => {
        if (currentPage < totalPages) setCurrentPage((prev) => prev + 1)
    }

    const handlePrev = () => {
        if (currentPage > 1) setCurrentPage((prev) => prev - 1)
    }

    //bug: you can interact with the favorites of other members- you can't delete them from that user's faves (thank god), but
    //I think I want to disable / hide the stars on other user's favorites page.

    return (
                <>
                <div className="p-6 mb-2 flex items-center rounded">
                    <h1 className="font-bold text-4xl pl-6 taped-note--header taped-note">{username}{"'"}s Favorites</h1>
                </div>
                {faves.length === 0 && 
                    <p className="font-semibold text-2xl pl-6">{username} currently has no favorites.</p>
                }
                <div className="flex flex-wrap m-2 gap-4">
                    {currentCards.map(fav => <BookCard book={fav} isFavorite={true}/>)}
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