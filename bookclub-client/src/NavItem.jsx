import { Link, NavLink } from 'react-router-dom';


export default function NavItem({ to, children }) {
  return (
    <NavLink
      to={to} end
      className={({ isActive }) =>
        `block rounded-md px-3 py-2 text-m font-medium ${
          isActive
            ? "active-navitem text-white"
            : "text-slate-100 hover:active-navitem"
        }`
      }
    >
      {children}
    </NavLink>
  );
}