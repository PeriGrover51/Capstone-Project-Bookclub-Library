import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function BookCard({ book }) {
    const { user } = useAuth()
    return (
        <div className="max-w-sm rounded overflow-hidden shadow-lg">
            <img className="w-full" src={book.imgLink} alt={book.title} />
            <div className="px-6 py-4">
                <div className="font-bold text-xl mb-2 text-center">{book.title}</div>
                <div className="font-bold text-l mb-2 text-center">{book.author}</div>
                <div className="font-bold text-m mb-2 text-center">{book.genre}</div>
                <a href={book.link} className="font-medium text-blue-600 hover:underline flex justify-center">GoodReads Link</a>
                {user && 
                <div className="px-6 pt-4 pb-2 flex justify-center">
                <Link to="/" 
                className="text-black bg-yellow-500 hover:bg-yellow-400 py-2 px-4 mx-2 rounded">
                    Edit
                </Link>
                </div>
                }
            </div>
        </div>
        
    )
}