package learn.bookclub.repos;

import learn.bookclub.models.Nomination;

import java.util.List;

public interface NominationRepository {
    List<Nomination> findAll();

    Nomination findById(int nominationId);

    Nomination create(Nomination nomination);

    boolean update(Nomination nomination);

    boolean deleteById(int nominationId);
}
