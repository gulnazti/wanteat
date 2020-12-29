package org.gulnaz.wanteat.service;

import org.gulnaz.wanteat.AuthorizedUser;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author gulnaz
 */
@Service()
public class UserSecurityService implements UserDetailsService {

    private final UserRepository repository;

    public UserSecurityService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
