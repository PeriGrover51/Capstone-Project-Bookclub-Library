import { useEffect, useState } from "react"
import { useAuth } from '../AuthContext';
import { Link } from "react-router-dom"

export default function NominationCard({ nomination, showScore }) {
    const { token } = useAuth()
    const { user } = useAuth()

    //for holding the user's voting scores
    const [myScore, setMyScore] = useState(null)

    //gets the user's vote for each nomination
    useEffect(() => {
        async function fetchMyVote() {
            const response = await fetch(`http://localhost:8080/api/votes/nomination/${nomination.nominationId}/mine`, {
                headers: {
                    Authorization: `Bearer ${token}`
                },
            });
            if (response.status === 204) { //server processed request correctly but user hasn't voted yet
                setMyScore(null)
            } else if (response.status == 200) { //server gives the vote
                const data = await response.json()
                setMyScore(data.score)
            }
        }
        fetchMyVote()
    }, [nomination.nominationId, token])


    async function castVote(score) {
        const response = await fetch(`http://localhost:8080/api/votes/nomination/${nomination.nominationId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({ score }),
        });
        const data = await response.json()
        setMyScore(data.score) //update local score to reflect new vote immediately
    }

    //score tally
    const [totalScore, setTotalScore] = useState(null)

    useEffect(() => {
        if (!showScore) return //do not continue if showScore is false (no need to fetch and then keep hidden)

        async function fetchAndCountVotes() {
            const response = await fetch(`http://localhost:8080/api/votes/nomination/${nomination.nominationId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            const votes = await response.json() //List<Vote> from backend
            const sum = votes.reduce((accumulator, vote) => accumulator + vote.score, 0) //loops thru each vote, adds that vote's score to the accumulator (running total)
            setTotalScore(sum)
        }

        fetchAndCountVotes()
    }, [showScore, nomination.nominationId, token])

    return (
        <div className="w-120 min-w-120 flex-none rounded overflow-hidden shadow-large file-card">
            <div className="h-full rounded p-4 flex flex-col justify-between leading-normal">
                <div className="">
                    <div className="text-gray-900 font-bold text-xl mb-2">{nomination.title} by {nomination.author}</div>
                    <p className="text-gray-700 text-base file-card__tab file-card--nomination">Nominated By {nomination.user.username}</p>
                    <p className="text-gray-700 text-base file-card__tab">{nomination.genre}</p>
                </div>
                {user && user.username === nomination.user.username &&
                <div className="px-1 pt-2 pb-2 flex justify-center items-center">
                    <Link to={`/nominations/edit/${nomination.nominationId}`} 
                    className="text-black edit-button py-2 px-4 mx-2">
                        Edit
                    </Link>
                    <Link to={`/nominations/delete/${nomination.nominationId}`} 
                    className="text-black delete-button py-2 px-4 mx-2">
                        Delete
                    </Link>
                </div>
                }
                <div className="taped-note file-card__voting">
                    <div className="w-full text-center pt-4 font-bold">
                        <h4 className="file-card__voting-label text-2xl">Vote:</h4>
                    </div>
                    <div className="w-full text-center vote-score">
                        {[1, 2, 3, 4, 5].map(score => (
                        <button 
                            key={score}
                            onClick={() => castVote(score)}
                            className={myScore === score ? 'px-6 py-3 mt-4 font-semibold text-center vote-score selected' : 'px-6 py-3 mt-4 vote-score'}
                        >
                            {score}
                        </button>
                        ))}
                    </div>
                </div>
                {showScore &&
                <div className="mt-8 text-lg font-bold flex justify-center items-center">
                    <div className="shadow-md bg-green-500 text-black py-2 w-1/2 text-center final-score-tab">
                        <h3>Final Score: {totalScore}</h3>
                    </div>
                    <Link to={`/nominations/convert/${nomination.nominationId}`} 
                    className="text-black bg-cyan-500 hover:bg-sky-400 py-2 px-4 mx-2 shadow-md final-score-tab">
                        Add to Library
                    </Link>
                </div>
                }
            </div>

        </div>
    )
}