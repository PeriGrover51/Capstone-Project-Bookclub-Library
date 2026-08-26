import { createBrowserRouter, Navigate, RouterProvider } from "react-router-dom"
import Layout from "./Layout"
import LoginForm from "./users/LoginForm"
import Home from "./Home"
import { useAuth } from "./AuthContext"
import SignupForm from "./users/SignupForm"
import BooksPage from "./books/BooksPage"

export default function AppRouter() {
    const { user } = useAuth() //get user from context

    const routes = [
        {
            path: "",
            element: <Layout />,
            children: [
                {
                    path: "/",
                    element: <Home />
                },
                {
                    path: "user/login",
                    element: user ? <Navigate to="/"/> : <LoginForm />
                },
                {
                    path: "user/register",
                    element: user ? <Navigate to="/"/> : <SignupForm />
                },
                {
                    path: "books",
                    element: <BooksPage/>
                }
            ]
        }
    ]
    
    const router = createBrowserRouter(routes)

    return <RouterProvider router={router} />
}