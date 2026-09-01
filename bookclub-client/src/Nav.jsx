import { Link, NavLink } from 'react-router-dom';
import { useAuth } from './AuthContext';
import NavItem from './NavItem';

export default function Nav() {
    const { user } = useAuth()
    const { logout } = useAuth()

    return (
        <>
        <aside className="flex h-screen w-64 flex-col justify-between bg-[#734d26] p-4 fixed">
            <div>
                <h1 className="mb-6 px-2 text-lg font-bold text-white">Book Club</h1>
                <nav className="flex flex-col gap-1">
                    <NavItem to="/books">Books</NavItem>
                    <NavItem to="/meetings">Meetings</NavItem>
                    <NavItem to="/meetings/current">Current Meeting</NavItem>

                    {user && (
                        <>
                        <NavItem to="/nominations">Nominations</NavItem>
                        </>
                    )}

                    {!user && (
                        <>
                        <NavItem to="/user/login">Login</NavItem>
                        <NavItem to="/user/register">Sign up</NavItem>
                        </>
                    )}
                </nav>
            </div>

                    {user && (
                        <div className="border-t border-gray-100 pt-4">
                            <p className="px-2 text-m text-slate-100">
                                Signed in as <span className="font-medium text-white">{user.username}</span>
                            </p>
                            <button
                                onClick={logout}
                                className="mt-2 w-full rounded-md px-3 py-2 text-left text-m text-slate-100 hover:bg-[#663300] hover:text-white"
                            >
                                Log out
                            </button>
                        </div>
                    )}
        </aside>
        </>
    )
}