import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function BookCard({ book, isFavorite }) {
    const { user } = useAuth()
    const { token } = useAuth()

    //necessary since the card manages its own toggle
    const [isFave, setIsFave] = useState(isFavorite)
    useEffect(() => {
        setIsFave(isFavorite)
    }, [isFavorite])

    async function setFavorite(event) {
        //update the favorites list
        //if isFave = true, do a delete, else do a post
        const url = "http://localhost:8080/api/favorites/" + book.bookId
        let method = "POST"
        if (isFave) {
            method = "DELETE"
        }

        const response = await fetch(url, {
            method: method,
            headers: {
                Authorization: "Bearer " + token
            }
        })
        if (response.status >= 200 && response.status < 300) { //post / delete succeeded = toggle isFave (star icon)
            setIsFave(!isFave)
        }
    }

    return (
        <div className="relative w-96 flex-none rounded overflow-hidden shadow-lg file-card file-card--book">
            <div className="file-card__content">
            <div className="absolute m-2 z-10" onClick={setFavorite}>
                {!isFave && user &&
                <svg className="w-8 h-8 text-gray-800 dark:text-white filter-none" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                    <path stroke="currentColor" strokeWidth="2" d="M11.083 5.104c.35-.8 1.485-.8 1.834 0l1.752 4.022a1 1 0 0 0 .84.597l4.463.342c.9.069 1.255 1.2.556 1.771l-3.33 2.723a1 1 0 0 0-.337 1.016l1.03 4.119c.214.858-.71 1.552-1.474 1.106l-3.913-2.281a1 1 0 0 0-1.008 0L7.583 20.8c-.764.446-1.688-.248-1.474-1.106l1.03-4.119A1 1 0 0 0 6.8 14.56l-3.33-2.723c-.698-.571-.342-1.702.557-1.771l4.462-.342a1 1 0 0 0 .84-.597l1.753-4.022Z"/>
                </svg>
                }
                {isFave && user &&
                <svg className="w-8 h-8 text-gray-800 dark:text-white filter-none" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M13.849 4.22c-.684-1.626-3.014-1.626-3.698 0L8.397 8.387l-4.552.361c-1.775.14-2.495 2.331-1.142 3.477l3.468 2.937-1.06 4.392c-.413 1.713 1.472 3.067 2.992 2.149L12 19.35l3.897 2.354c1.52.918 3.405-.436 2.992-2.15l-1.06-4.39 3.468-2.938c1.353-1.146.633-3.336-1.142-3.477l-4.552-.36-1.754-4.17Z"/>
                </svg>
                }
            </div>
            <img className="w-full h-72 object-cover sepia-100 z-0" src={book.imgLink} alt={book.title} />
            <div className="px-6 py-4">
                <div className="font-bold text-m mb-2 text-center file-card__tab">{book.genre}</div>
                <div className="font-bold text-xl mb-2 text-center file-card__title">{book.title}</div>
                <div className="font-bold text-l mb-2 text-center file-card__meta">{book.author}</div>
                <div className="font-bold text-m mb-2 text-center file-card__meta">{book.whenRead}</div>
                <a href={book.link} className="font-medium text-blue-600 hover:underline flex justify-center">GoodReads Link</a>
                {user && 
                <div className="px-6 pt-4 pb-2 flex justify-center">
                <Link to={`/books/edit/${book.bookId}`} 
                className="text-black edit-button py-2 px-4 mx-2 rounded">
                    Edit
                </Link>
                </div>
                }
            </div>
            </div>
        </div>
        
    )
}