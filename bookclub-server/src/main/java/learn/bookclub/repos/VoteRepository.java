package learn.bookclub.repos;

import learn.bookclub.models.Vote;

import java.util.List;

public interface VoteRepository {

    Vote findByUserIdAndNominationId(int userId, int nominationId);

    Vote saveVote(Vote vote); //handles both add + update based on whether the user has previously voted on this nomination

    List<Vote> findByNominationId(int nominationId); //for counting total votes of a nomination

    List<Vote> findByUserId(int userId);
}
