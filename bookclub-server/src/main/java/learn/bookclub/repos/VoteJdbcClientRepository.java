package learn.bookclub.repos;

import learn.bookclub.models.Vote;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VoteJdbcClientRepository implements VoteRepository {

    private final JdbcClient jdbcClient;

    public VoteJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Vote findByUserIdAndNominationId(int userId, int nominationId) {
        final String sql = """
                select * from votes
                where user_id = :user_id
                and nomination_id = :nomination_id;""";

        return jdbcClient.sql(sql)
                .param("user_id", userId)
                .param("nomination_id", nominationId)
                .query(Vote.class)
                .optional().orElse(null);
    }

    @Override
    public Vote saveVote(Vote vote) {
        //if the vote has a null id, then call create(Vote vote)
        //if the vote has a set id, then call update(Vote vote)
        if (vote.getVoteId() <= 0) {
            return create(vote);
        } else {
            return update(vote);
        }
    }

    private Vote create(Vote vote) {
        final String sql = """
                insert into votes (user_id, nomination_id, score) values (
                :user_id,
                :nomination_id,
                :score
                );""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("user_id", vote.getUserId())
                .param("nomination_id", vote.getNominationId())
                .param("score", vote.getScore())
                .update(keyHolder, "vote_id");

        if (rowsAffected == 0) {
            return null;
        }

        vote.setVoteId(keyHolder.getKey().intValue());

        return vote;
    }

    private Vote update(Vote vote) {
        //cannot change user id or nomination id, only the score itself
        final String sql = """
                update votes set
                score = :score
                where vote_id = :vote_id
                ;""";

        int rowsUpdated = jdbcClient.sql(sql)
                .param("score", vote.getScore())
                .param("vote_id", vote.getVoteId())
                .update();

        if (rowsUpdated > 0) {
            return vote;
        } else {
            return null;
        }
    }

    @Override
    public List<Vote> findByNominationId(int nominationId) {
        final String sql = """
                select * from votes
                where nomination_id = :nomination_id
                ;""";

        return jdbcClient.sql(sql)
                .param("nomination_id", nominationId)
                .query(Vote.class)
                .list();
    }

    @Override
    public List<Vote> findByUserId(int userId) {
        final String sql = """
                select * from votes
                where user_id = :user_id
                ;""";

        return jdbcClient.sql(sql)
                .param("user_id", userId)
                .query(Vote.class)
                .list();
    }
}
