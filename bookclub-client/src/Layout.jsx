import { Link, Outlet } from "react-router-dom";

export default function Layout () {
    return (
    <>
        <h4>hello from Layour</h4>
        <h4>Nav goes in here</h4>
        <main>
            <Outlet />
        </main>
    </>
    )
    
}