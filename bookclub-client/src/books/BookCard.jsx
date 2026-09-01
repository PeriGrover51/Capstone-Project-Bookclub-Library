import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function BookCard({ book }) {
    const { user } = useAuth()

    return (
        <div className="w-96 flex-none rounded overflow-hidden shadow-lg hover:bg-gray-100 bg-[#dcbfa3]">
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