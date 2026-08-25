//context = a container to hold data that a lot of components need (user, token)
//context holds that data for all components to access without need to pass it down as a prop.

import { createContext, useContext, useState } from 'react'

//creates the context 'box', starts out empty
const AuthContext = createContext(null);

//wraps whole app, provides 'box' contents
export function AuthProvider({ children }) {

    //check localstorage for jwt token - so page refresh doesn't log you out.
    const [token, setToken] = useState(() => localStorage.getItem('token'))

    //check localstorage for user info - many components require this
    const [user, setUser] = useState(() => {
        const stored = localStorage.getItem('user');
        return stored ? JSON.parse(stored) : null;
    })

    function login(newToken, newUser) {
        //on backend, a successful login request sends "token" and "user" with "id" and "username"
        localStorage.setItem('token', newToken)
        localStorage.setItem('user', JSON.stringify(newUser))
        setToken(newToken)
        setUser(newUser)
    }

    function logout() {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        setToken(null)
        setUser(null)
    }

    //everything passed in value becomes available to all children components
    return (
        <AuthContext.Provider value={{ token, user, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

//this lets components avoid importing useContext + AuthContext every time - the function actually called from components
export function useAuth() {
    return useContext(AuthContext)
}