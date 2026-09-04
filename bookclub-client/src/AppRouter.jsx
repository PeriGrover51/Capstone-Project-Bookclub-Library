import { createBrowserRouter, Navigate, RouterProvider } from "react-router-dom"
import Layout from "./Layout"
import LoginForm from "./users/LoginForm"
import Home from "./Home"
import { useAuth } from "./AuthContext"
import SignupForm from "./users/SignupForm"
import BooksPage from "./books/BooksPage"
import MeetingsPage from "./meetings/MeetingsPage"
import BookForm from "./books/BookForm"
import MeetingForm from "./meetings/MeetingForm"
import NominationsPage from "./nominations/NominationsPage"
import NominationForm from "./nominations/NominationForm"
import NominationDelete from "./nominations/NominationDelete"
import MeetingCurrent from "./meetings/MeetingCurrent"
import ConvertToBook from "./nominations/ConvertToBook"
import DeleteAll from "./nominations/DeleteAll"
import FavoritesPage from "./favorites/FavoritesPage"
import MembersPage from "./users/MembersPage"
import OtherFavoritesPage from "./favorites/OtherFavoritesPage"

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
                },
                {
                    path: "books/add",
                    element: user ? <BookForm /> : <Navigate to="/"/>
                },
                {
                    path: "books/edit/:id",
                    element: user ? <BookForm /> : <Navigate to="/"/>
                },
                {
                    path: "meetings",
                    element: <MeetingsPage/>
                },
                {
                    path: "meetings/add",
                    element: user ? <MeetingForm /> : <Navigate to="/"/>
                },
                {
                    path: "meetings/edit/:id",
                    element: user ? <MeetingForm /> : <Navigate to="/"/>
                },
                {
                    path: "nominations",
                    element: user ? <NominationsPage /> : <Navigate to="/"/>
                },
                {
                    path: "nominations/add",
                    element: user ? <NominationForm /> : <Navigate to="/"/>
                },
                {
                    path: "nominations/edit/:id",
                    element: user ? <NominationForm /> : <Navigate to="/"/>
                },
                {
                    path: "nominations/delete/:id",
                    element: user ? <NominationDelete /> : <Navigate to="/"/>
                },
                {
                    path: "nominations/convert/:id",
                    element: user ? <ConvertToBook /> : <Navigate to="/"/>
                },
                {
                    path: "nominations/delete/all",
                    element: user ? <DeleteAll /> : <Navigate to="/" />
                },
                {
                    path: "meetings/current",
                    element: <MeetingCurrent />
                },
                {
                    path: "favorites/mine",
                    element: user ? <FavoritesPage /> : <Navigate to="/"/>
                },
                {
                    path: "favorites/user/:username",
                    element: user ? <OtherFavoritesPage /> : <Navigate to="/"/>
                },
                {
                    path: "members",
                    element: <MembersPage />
                }
            ]
        }
    ]
    
    const router = createBrowserRouter(routes)

    return <RouterProvider router={router} />
}