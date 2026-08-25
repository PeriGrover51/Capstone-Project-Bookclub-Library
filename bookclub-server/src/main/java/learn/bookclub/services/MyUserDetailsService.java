package learn.bookclub.services;

import learn.bookclub.models.User;
import learn.bookclub.models.UserPrincipal;
import learn.bookclub.repos.UserJdbcClientRepository;
import learn.bookclub.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService { //custom userdetailsservice for using DB users

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //verify user exists in db
        User user = repository.findByUsername(username);

        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user); //pass user object so UserPrincipal can access username/pw
    }
}
