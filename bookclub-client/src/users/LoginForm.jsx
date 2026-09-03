import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useAuth } from "../AuthContext"

export default function LoginForm() {
    const navigate = useNavigate()

    const { login } = useAuth()

    const [user, setUser] = useState({
        username: "",
        password: ""
    })
    const [errors, setErrors] = useState([])

    function handleChange(event) {
        setUser({...user, [event.target.name]: event.target.value})
    }

    async function handleSubmit(event) {
        event.preventDefault()

        const response = await fetch("http://localhost:8080/api/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })

        const payload = await response.json();

        if (response.status >= 200 && response.status < 300) { //success
            login(payload.token, payload.user)
            navigate("/")
        } else {
            setErrors(payload)
        }
    }

    return (
        <>
            <div className="flex justify-center items-center h-screen">
                <form className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 library-card" onSubmit={handleSubmit}>
                    <h1 className="flex justify-center items-center mb-4">Bookclub Login</h1>
                    {errors.length > 0 && <ul>
                        {errors.map(error => <li key={error}>{error}</li>)}    
                    </ul>}
                    
                    <div className="mb-4  library-card__field">
                        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username-input">Username: </label>
                        <input className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                             type="text" id="username-input" name="username" onChange={handleChange} value={user.username} />
                    </div>
        
                    <div className="mb-6 library-card__field">
                        <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password-input">Password: </label>
                        <input className="shadow appearance-none border border-red-500 rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                            type="password" id="password-input" name="password" onChange={handleChange} value={user.password} />
                    </div>
        
                    <div className="flex items-center justify-between">
                        <button className="add-button font-bold py-2 px-4 focus:outline-none focus:shadow-outline"
                             type="submit">LOG IN</button>
                    </div>
                </form>
            </div>    
        </>
    );
}