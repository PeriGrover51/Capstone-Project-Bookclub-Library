package learn.bookclub.services;


import learn.bookclub.models.Nomination;
import learn.bookclub.models.User;
import learn.bookclub.repos.NominationRepository;
import learn.bookclub.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NominationService {

    private NominationRepository repository;
    private UserRepository userRepository;

    public NominationService(NominationRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Nomination> findAll() {
        return repository.findAll();
    }

    public Result<Nomination> findById(int nominationId) {
        Result<Nomination> result = new Result<>();
        Nomination exists = repository.findById(nominationId);
        if (exists == null) {
            result.addErrorMessage("nomination not found", ResultType.NOT_FOUND);
        } else {
            result.setpayload(exists);
        }
        return result;
    }

    public Result<Nomination> update(Nomination nomination) {
        Result<Nomination> result = new Result<>();
        validate(result, nomination);

        if (!result.isSuccess()) {
            return result;
        }

        //check nomId is set
        if (nomination.getNominationId() <= 0) {
            result.addErrorMessage("id must be set", ResultType.INVALID);
            return result;
        }

        //check nomId exists
        Nomination existing = repository.findById(nomination.getNominationId());
        if (existing == null) {
            result.addErrorMessage("nomination not in db", ResultType.NOT_FOUND);
            return result;
        }

        //no user mismatch - in nomination body vs in nomination repo
        String existingUserName = existing.getUser().getUsername();
        if (!nomination.getUser().getUsername().equalsIgnoreCase(existingUserName)) {
            result.addErrorMessage("cannot change nomination you don't own", ResultType.INVALID);
        }
        //user mismatch in body vs auth header is handled in the service

        if (result.isSuccess()) {
            if (repository.update(nomination)) {
                result.setpayload(nomination);
            } else {
                result.addErrorMessage("nomination not found in db", ResultType.NOT_FOUND);
            }
        }

        return result;
    }


    public Result<Nomination> create(Nomination nomination) {
        Result<Nomination> result = new Result<>();
        validate(result, nomination);

        if (!result.isSuccess()) {
            return result;
        }

        //check nomId is unset
        if (nomination.getNominationId() != 0) {
            result.addErrorMessage("id cannot be set", ResultType.INVALID);
            return result;
        }

        Nomination created = repository.create(nomination);
        result.setpayload(created);

        return result;
    }

    public Result<Nomination> deleteById(int nominationId) {
        Result<Nomination> result = new Result<>();
        if (!repository.deleteById(nominationId)) {
            result.addErrorMessage("Nomination id not found", ResultType.NOT_FOUND);
        }
        return result;
    }



    private void validate(Result<Nomination> result, Nomination nomination) {
        //create + update validation:
        //not null
        if (nomination == null) {
            result.addErrorMessage("nomination cannot be null", ResultType.INVALID);
            return;
        }

        //title, author, genre not null / blank
        if (nomination.getTitle() == null || nomination.getTitle().isBlank()) {
            result.addErrorMessage("title cannot be empty", ResultType.INVALID);
        }
        if (nomination.getAuthor() == null || nomination.getAuthor().isBlank()) {
            result.addErrorMessage("author cannot be empty", ResultType.INVALID);
        }
        if (nomination.getGenre() == null || nomination.getGenre().isBlank()) {
            result.addErrorMessage("genre cannot be empty", ResultType.INVALID);
        }
        //user not null
        if (nomination.getUser() == null) {
            result.addErrorMessage("user cannot be null", ResultType.INVALID);
        }
        //dupe test and check user exists
        if (result.isSuccess()) {

            User existing = userRepository.findByUsername(nomination.getUser().getUsername());
            if (existing == null) {
                result.addErrorMessage("user does not exist in db", ResultType.INVALID);
            }

            List<Nomination> existingNominations = repository.findAll();

            //check duplicate - user id mismatch + title/author/genre are all the same
            for (Nomination existingNomination : existingNominations) {
                if (existingNomination.getNominationId() != nomination.getNominationId() &&
                        existingNomination.getTitle().equalsIgnoreCase(nomination.getTitle()) &&
                        existingNomination.getAuthor().equalsIgnoreCase(nomination.getAuthor()) &&
                        existingNomination.getGenre().equalsIgnoreCase(nomination.getGenre())) {
                    result.addErrorMessage("nomination cannot be duplicate", ResultType.INVALID);
                }
            }
        }
    }
}
