import { useAuth } from "./AuthContext"

export default function Home() {
    const { user } = useAuth()
    return (
        <div className="w-full flex-none mt-6 text-center uppercase">
            <div className="font-bold text-5xl p-6">The Bookclub Archives</div>
            <div className="p-4">This is the home of our little Science Fiction and Fantasy Bookclub.</div>
            <div className="p-4">This club began as a thriving offshoot of the official CSU Sci-fi and Fantasy Club.</div>
            <div className="p-4">During COVID, the bookclub was nearly wiped out, with most of the members leaving for... well, obvious reasons.</div>
            <div className="p-4">
                The bookclub managed to hold on for several more years at CSU with only a handful of close-knit members, many of whom had already graduated,
                 but slowly drifted apart from the main SFaF Club as it went through several changes of leadership and management.
            </div>
            <div className="p-4">
                Eventually, as no new members were joining while the youngest bookclub members graduated from CSU, and with many of the members moving away from the FoCo area,
                the club moved from in-person meetings at CSU to online meetings over Discord.
            </div>
            <div className="p-4">
                Since the bookclub no longer associates with the official CSU SFaF Club, no longer meets at CSU, nor even has any current members attending CSU,
                it is safe to say that this club is no longer an official CSU club, but instead just a small group of friends who read scifi and fantasy books together,
                and have weekly Discord calls to talk about them - mostly, anyway. The meetings tend to get... a little off topic sometimes.
            </div>
            <div className="p-4">
                This site was created to be an archive and a tool for our little bookclub. It is meant to catalog previous books we've read,
                track past and current meetings, and provide an easier, more streamlined way to nominate and vote for which book the club will read next. 
                {"("}Previously we've just been using Google Forms and counting the votes by hand... Definitely not very efficient.{")"}
            </div>
            <div className="p-4">
                While anyone and everyone is free to view past books and meetings, 
                users must log in as a club member in order to add, edit, or delete anything.
                Additionally, only members can view, add/edit, or vote on new book nominations. No voting fraud allowed!
            </div>
        </div>
    )
}