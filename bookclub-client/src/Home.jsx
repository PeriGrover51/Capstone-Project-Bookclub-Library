import { useAuth } from "./AuthContext"

export default function Home() {
    const { user } = useAuth()
    return (
        <>
        <h1>Hello Home!</h1>
        </>
    )
}