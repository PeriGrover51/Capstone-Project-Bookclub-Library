package learn.bookclub.repos;

import learn.bookclub.models.Nomination;
import learn.bookclub.repos.mappers.NominationMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
        final String sql = """
                insert into nominations (title, author, genre, user_id) values (
                :title,
                :author,
                :genre,
                :user_id
                );""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("title", nomination.getTitle())
                .param("author", nomination.getAuthor())
                .param("genre", nomination.getGenre())
                .param("user_id", nomination.getUser().getUserId())
                .update(keyHolder, "nomination_id");

        if (rowsAffected == 0) {
            return null;
        }

        nomination.setNominationId(keyHolder.getKey().intValue());

        return nomination;
    }

    @Override
    public boolean update(Nomination nomination) {
        final String sql = """
                update nominations set 
                title = :title,
                author = :author,
                genre = :genre
                where nomination_id = :nomination_id 
                ;""";

        int rowsUpdated = jdbcClient.sql(sql)
                .param("title", nomination.getTitle())
                .param("author", nomination.getAuthor())
                .param("genre", nomination.getGenre())
                .param("nomination_id", nomination.getNominationId())
                .update();

        return rowsUpdated > 0;
    }

    @Override
    public boolean deleteById(int nominationId) {
        final String sql = "delete from nominations where nomination_id = ?;";

        return jdbcClient.sql(sql).param(nominationId).update() > 0;
    }

    @Override
    public boolean deleteAll() {
        final String sql = "delete from nominations;";

        return jdbcClient.sql(sql).update() > 0;
    }
}
