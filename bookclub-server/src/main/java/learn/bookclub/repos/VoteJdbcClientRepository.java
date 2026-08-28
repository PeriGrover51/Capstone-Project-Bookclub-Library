package learn.bookclub.repos;

import learn.bookclub.models.Vote;
import org.springframework.jdbc.core.simple.JdbcClient;
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

        return null;
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
