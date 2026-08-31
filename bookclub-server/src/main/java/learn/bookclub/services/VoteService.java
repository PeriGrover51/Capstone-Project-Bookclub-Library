package learn.bookclub.services;

import learn.bookclub.models.Nomination;
import learn.bookclub.models.User;
import learn.bookclub.models.Vote;
import learn.bookclub.repos.NominationRepository;
import learn.bookclub.repos.UserRepository;
import learn.bookclub.repos.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {

    private VoteRepository repository;
    private UserRepository userRepository;
    private NominationRepository nominationRepository;


    public VoteService(VoteRepository repository, UserRepository userRepository, NominationRepository nominationRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.nominationRepository = nominationRepository;
    }

    public List<Vote> findByNominationId(int nominationId) {
        //this gets all votes for a certain nomination - used for tallying total scores on frontend
        return repository.findByNominationId(nominationId);
    }

    public List<Vote> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public Result<Vote> findByUserAndNominationId(String username, int nominationId) {
        Result<Vote> result = new Result<>();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            result.addErrorMessage("user doesnt exist", ResultType.NOT_FOUND);
            return result;
        }

        //will either have existing vote attached to that user+nom combo, or payload will be null
        result.setpayload(repository.findByUserIdAndNominationId(user.getUserId(), nominationId));

        return result;
    }

    public Result<Vote> saveVote(String username, int nominationId, int score) {
        //current thought: frontend / controller doesn't send us a constructed vote - we do that here, then save to the repo / db

        //validation: score is between 1-5, user and nomination exist in db
        Result<Vote> result = new Result<>();

        if (score < 1 || score > 5) {
            result.addErrorMessage("score must be between 1 and 5", ResultType.INVALID);
        }

        User user = userRepository.findByUsername(username);
        Nomination nomination = nominationRepository.findById(nominationId);
        if (user == null) {
            result.addErrorMessage("user not in db", ResultType.INVALID);
        }
        if (nomination == null) {
            result.addErrorMessage("nomination not in db", ResultType.NOT_FOUND);
        }
        if (!result.isSuccess()) {
            return result;
        }

        //check if vote already exists in repo:
        Vote exists = repository.findByUserIdAndNominationId(user.getUserId(), nominationId);

        //vote does not already exist (new vote) == create new vote id with id 0 and save
        if (exists == null) {
            Vote newVote = new Vote(0, user.getUserId(), nominationId, score);
            result.setpayload(repository.saveVote(newVote));
        } else {
            //vote DOES exist (update vote) = set new score and save
            exists.setScore(score);
            result.setpayload(repository.saveVote(exists));
        }

        return result;
    }
}
