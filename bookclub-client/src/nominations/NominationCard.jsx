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
        <div className="w-120 flex-none rounded overflow-hidden shadow-large">
            <div className="h-full border border-gray-400 bg-[#dcbfa3] hover:bg-gray-100 rounded p-4 flex flex-col justify-between leading-normal">
                <div className="mb-4">
                    <div className="text-gray-900 font-bold text-xl mb-2">{nomination.title} by {nomination.author}</div>
                    <p className="text-gray-700 text-base">{nomination.genre}</p>
                    <p className="text-gray-700 text-base">Nominated By {nomination.user.username}</p>
                </div>
                {user && user.username === nomination.user.username &&
                <div className="px-1 pt-4 pb-2 flex justify-center items-center">
                    <Link to={`/nominations/edit/${nomination.nominationId}`} 
                    className="text-black bg-yellow-500 hover:bg-yellow-400 py-2 px-4 mx-2 rounded">
                        Edit
                    </Link>
                    <Link to={`/nominations/delete/${nomination.nominationId}`} 
                    className="text-black bg-red-500 hover:bg-red-400 py-2 px-4 mx-2 rounded">
                        Delete
                    </Link>
                </div>
                }
                <div className="border border-black rounded bg-green-200">
                    <div className="w-full text-center pt-4 font-bold">
                        <h4 className="">Vote:</h4>
                    </div>
                    <div className="w-full text-center">
                        {[1, 2, 3, 4, 5].map(score => (
                        <button 
                            key={score}
                            onClick={() => castVote(score)}
                            className={myScore === score ? 'text-black bg-blue-500 hover:bg-blue-400 px-6 py-3 mt-4 rounded font-semibold text-center' : 'text-black bg-white-500 hover:bg-gray-400 px-6 py-3 mt-4 rounded font-semibold text-center'}
                        >
                            {score}
                        </button>
                        ))}
                    </div>
                </div>
                {showScore &&
                <div className="mt-4 text-lg font-bold flex justify-center items-center">
                    <div className="rounded-full bg-green-500 py-2 w-1/2 text-center">
                        <h3>Final Score: {totalScore}</h3>
                    </div>
                    <Link to={`/nominations/convert/${nomination.nominationId}`} 
                    className="text-black bg-cyan-500 hover:bg-sky-400 py-2 px-4 mx-2 rounded-full">
                        Add to Library
                    </Link>
                </div>
                }
            </div>

        </div>
    )
}