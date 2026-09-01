import { Link, Outlet } from "react-router-dom";
import Nav from "./Nav";

export default function Layout () {
    return (
    <div className="flex">
        <Nav/>
        <main className="flex-1 p-6 ml-64 bg-[#f4ecd8] min-h-screen">
            <Outlet />
        </main>
    </div>
    )
    
}