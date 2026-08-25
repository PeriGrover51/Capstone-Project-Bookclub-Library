import { useAuth } from "./AuthContext"

export default function Home() {
    const { user } = useAuth()
    return (
        <>
        <h2>home stub</h2>
        {user && 
            <h2>hello, {user.username}</h2>
        }
        </>
    )
}