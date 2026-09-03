import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { useAuth } from '../AuthContext';
import MembersCard from "./MembersCard";

export default function MembersPage() {
    const { user } = useAuth()
    const { token } = useAuth()

    const [members, setMembers] = useState([])

    const COLORS = ['yellow', 'pink', 'blue', 'green']

    useEffect(() => {
        const doFetch = async () => {
            const response = await fetch("http://localhost:8080/api/user")
            const payload = await response.json()

            //sort members alphabetically? 
            const sortedMembers = [...payload].sort((a, b) => a.localeCompare(b));

            setMembers(sortedMembers)

        }
        doFetch()
    }, [])

    return (
        <>
        <div className="p-6 mb-2 flex items-center rounded">
            <h1 className="font-bold text-4xl pl-6 taped-note--header taped-note">MEMBERS</h1>
        </div>
        <div className="member-note-stack">
            {members.map((member, index) => <MembersCard username={member} color={COLORS[index % COLORS.length]}/>)}
        </div>
        </>
    )
}