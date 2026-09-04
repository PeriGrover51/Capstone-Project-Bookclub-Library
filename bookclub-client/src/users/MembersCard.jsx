import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function MembersCard({ username, color }) {
    const { user } = useAuth()

    return (
        <>
        <div className="library-card member-note m-6">
            <div className="library-card__field text-center font-bold">{username}</div>
            {user && 
            <div className=" flex items-center justify-center">
                {user.username === username ? 
                <Link to="favorites/mine" 
                className={` member-sticky-note sticky-note--${color}`}>
                    View Faves
                </Link>
                : <Link to={`/favorites/user/${username}`} 
                className={` member-sticky-note sticky-note--${color}`}>
                    View Faves
                </Link>
                }
            </div>
            }
        </div>
        </>
    )
}