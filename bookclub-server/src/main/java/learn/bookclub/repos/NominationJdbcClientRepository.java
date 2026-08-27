package learn.bookclub.repos;

import learn.bookclub.models.Nomination;
import learn.bookclub.repos.mappers.NominationMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NominationJdbcClientRepository implements NominationRepository {

    private final JdbcClient jdbcClient;

    public NominationJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Nomination> findAll() {
        final String sql = """
                select nomination_id, title, author, genre, u.user_id, u.username
                from nominations n join user u
                on n.user_id = u.user_id
                ;""";

        return jdbcClient.sql(sql)
                .query(new NominationMapper())
                .list();
    }

    @Override
    public Nomination findById(int nominationId) {
        final String sql = """
                select nomination_id, title, author, genre, u.user_id, u.username
                from nominations n join user u
                on n.user_id = u.user_id
                where n.nomination_id = ?
                ;""";

        return jdbcClient.sql(sql)
                .param(nominationId)
                .query(new NominationMapper())
                .optional().orElse(null);
    }

    @Override
    public Nomination create(Nomination nomination) {
        return null;
    }

    @Override
    public boolean update(Nomination nomination) {
        return false;
    }

    @Override
    public boolean deleteById(int nominationId) {
        return false;
    }
}
