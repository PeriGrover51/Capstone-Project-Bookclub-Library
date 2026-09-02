import { Link, Outlet } from "react-router-dom";
import Nav from "./Nav";

export default function Layout () {
    return (
    <div className="flex paper-bg">
        <Nav/>
        <main className="flex-1 p-6 ml-8 min-h-screen ml-64">
            <Outlet />
        </main>
    </div>
    )
    
}