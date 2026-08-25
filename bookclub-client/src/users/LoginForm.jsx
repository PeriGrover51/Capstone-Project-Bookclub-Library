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
            <h4>Log in as a bookclub member</h4>
            <div className="row">
                <div className="col-3" />

                <form className="col-6" onSubmit={handleSubmit}>
                    {errors.length > 0 && <ul>
                        {errors.map(error => <li key={error}>{error}</li>)}    
                    </ul>}
                    
                    <div className="form-control">
                        <label htmlFor="username-input">Username: </label>
                        <input type="text" id="username-input" name="username" onChange={handleChange} value={user.username} />
                    </div>
        
                    <div className="form-control">
                        <label htmlFor="password-input">Password: </label>
                        <input type="password" id="password-input" name="password" onChange={handleChange} value={user.password} />
                    </div>
        
                    <div className="form-control">
                        <button type="submit">Log In!</button>
                    </div>
                </form>

                <div className="col-3" />
            </div>    
        </>
    );
}