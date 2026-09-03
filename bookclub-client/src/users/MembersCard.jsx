import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function MembersCard({ username, color }) {

    const viewUserFavorites = () => {
        console.log("view selected")
    }

    return (
        <>
        <div className="library-card member-note m-6">
            <div className="library-card__field text-center font-bold">{username}</div>
            <div className=" flex items-center justify-center">
                <button onClick={viewUserFavorites} className={` member-sticky-note sticky-note--${color}`} >VIEW</button>
            </div>
        </div>
        </>
    )
}