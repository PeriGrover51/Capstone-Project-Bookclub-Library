package learn.bookclub.services;

import learn.bookclub.models.Vote;
import learn.bookclub.repos.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private VoteRepository repository;
    //may need to add userrepo and nomrepo here for saveVote method

    public VoteService(VoteRepository repository) {
        this.repository = repository;
    }

    public List<Vote> findByNominationId(int nominationId) {
        //this gets all votes for a certain nomination - used for tallying total scores on frontend
        return repository.findByNominationId(nominationId);
    }

    public List<Vote> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public Vote findByUserIdAndNominationId(int userId, int nominationId) {
        //this is
        return repository.findByUserIdAndNominationId(userId, nominationId);
    }

    public Result<Vote> saveVote(String username, int nominationId, int score) {
        //current thought: frontend / controller doesn't send us a constructed vote - we do that here, then save to the repo / db
        //validation: score is between 1-5, user and nomination exist in db
        //check if vote already exists in repo:
            //if yes, then fetch that vote, set the new score, and send to repo.save
            //if no, then create new vote with a null id, set userid + nomid, set score, and send to repo.save
        return null;
    }
}
