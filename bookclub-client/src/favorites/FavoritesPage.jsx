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

            setFaves(payload)
        }
        doFetch()
    }, [])

    return (
            <>
            <div className="p-6 mb-2 flex items-center rounded">
                <h1 className="font-bold text-4xl pl-6">My Favorites</h1>
            </div>
            {faves.length === 0 && 
                <p className="font-semibold text-2xl pl-6">You have no favorites. Click the star icon on any book to add it to favorites!</p>
            }
            <div className="flex flex-wrap m-2 gap-4">
                {faves.map(fav => <BookCard book={fav} isFavorite={true}/>)}
            </div>
            </>
        )
}