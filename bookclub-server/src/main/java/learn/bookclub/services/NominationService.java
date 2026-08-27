package learn.bookclub.services;


import learn.bookclub.models.Nomination;
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
}
