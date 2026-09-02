import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function BookCard({ book }) {
    const { user } = useAuth()

    //TODO: make isFave, setIsFave props passed down from BooksPage - connected to GET call to favorites controller
    const [isFave, setIsFave] = useState(false)

    function handleClick(event) {
        console.log("star clicked");
        setIsFave(!isFave)
    }

    return (
        <div className="relative w-96 flex-none rounded overflow-hidden shadow-lg hover:bg-gray-100 bg-[#dcbfa3]">
            <div className="absolute m-2" onClick={handleClick}>
                {!isFave && user &&
                <svg className="w-8 h-8 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                    <path stroke="currentColor" strokeWidth="2" d="M11.083 5.104c.35-.8 1.485-.8 1.834 0l1.752 4.022a1 1 0 0 0 .84.597l4.463.342c.9.069 1.255 1.2.556 1.771l-3.33 2.723a1 1 0 0 0-.337 1.016l1.03 4.119c.214.858-.71 1.552-1.474 1.106l-3.913-2.281a1 1 0 0 0-1.008 0L7.583 20.8c-.764.446-1.688-.248-1.474-1.106l1.03-4.119A1 1 0 0 0 6.8 14.56l-3.33-2.723c-.698-.571-.342-1.702.557-1.771l4.462-.342a1 1 0 0 0 .84-.597l1.753-4.022Z"/>
                </svg>
                }
                {isFave && user &&
                <svg className="w-8 h-8 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M13.849 4.22c-.684-1.626-3.014-1.626-3.698 0L8.397 8.387l-4.552.361c-1.775.14-2.495 2.331-1.142 3.477l3.468 2.937-1.06 4.392c-.413 1.713 1.472 3.067 2.992 2.149L12 19.35l3.897 2.354c1.52.918 3.405-.436 2.992-2.15l-1.06-4.39 3.468-2.938c1.353-1.146.633-3.336-1.142-3.477l-4.552-.36-1.754-4.17Z"/>
                </svg>
                }
            </div>
            <img className="w-full h-72 object-cover" src={book.imgLink} alt={book.title} />
            <div className="px-6 py-4">
                <div className="font-bold text-xl mb-2 text-center">{book.title}</div>
                <div className="font-bold text-l mb-2 text-center">{book.author}</div>
                <div className="font-bold text-m mb-2 text-center">{book.genre}</div>
                <div className="font-bold text-m mb-2 text-center rounded-full bg-gray-400">{book.whenRead}</div>
                <a href={book.link} className="font-medium text-blue-600 hover:underline flex justify-center">GoodReads Link</a>
                {user && 
                <div className="px-6 pt-4 pb-2 flex justify-center">
                <Link to={`/books/edit/${book.bookId}`} 
                className="text-black bg-yellow-500 hover:bg-yellow-400 py-2 px-4 mx-2 rounded">
                    Edit
                </Link>
                </div>
                }
            </div>
        </div>
        
    )
}