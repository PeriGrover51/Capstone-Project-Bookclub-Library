import { useEffect, useState } from "react"
import BookCard from "../books/BookCard"
import { Link } from "react-router-dom"
import { useAuth } from '../AuthContext';

export default function FavoritesPage() {
    const { user } = useAuth()
    const { token } = useAuth()

    const [faves, setFaves] = useState([])

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/favorites/mine", {
                headers: {
                Authorization: "Bearer " + token
            }
            })

            
            const payload = await response.json()

            if (response.status >= 200 && response.status < 300) { //success
                setFaves(payload)
            }

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


    return (
            <>
            <div className="p-6 mb-2 flex items-center rounded">
                <h1 className="font-bold text-4xl pl-6 taped-note--header taped-note">My Favorites</h1>
            </div>
            {faves.length === 0 && 
                <p className="font-semibold text-2xl pl-6">You have no favorites. Click the star icon on any book to add it to favorites!</p>
            }
            <div className="flex flex-wrap m-2 gap-4">
                {currentCards.map(fav => <BookCard book={fav} isFavorite={true} otherUser={false}/>)}
            </div>

            <div className="relative flex flex-wrap w-full justify-center">
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