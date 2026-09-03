import { useAuth } from "./AuthContext"

export default function Home() {
    const { user } = useAuth()
    return (
        <>
        <div className="font-bold text-5xl p-6 taped-note w-1/2 mx-auto uppercase">The Bookclub Archives</div>
        <div className="mt-6 text-center flex">
            <div className="sticky-note-stack w-1/4 mt-8">
                <div className="p-4 sticky-note sticky-note--yellow">Home of the Scifi and Fantasy Bookclub</div>
                <div className="p-4 sticky-note sticky-note--blue">Sign in to access classified documents</div>
                <div className="p-4 sticky-note sticky-note--pink">{"("}No longer associated w/ CSU{")"}</div>
            </div>
            <div className="library-card ml-auto w-2/3 uppercase">
                <div className="library-card__field">Books - semi-classified: view-only</div>
                <div className="library-card__field">Meetings - semi-classified: view-only</div>
                <div className="library-card__field">Nominations - classified</div>
                <div className="library-card__field">Voting - classified</div>
                <div className="library-card__field">Favorites - classified</div>
                <div className="library-card__field">Login - Membership required</div>
                <div className="library-card__field">Signup - Gain Access</div>
            </div>
        </div>
        </>
    )
}