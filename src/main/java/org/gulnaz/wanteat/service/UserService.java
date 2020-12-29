package org.gulnaz.wanteat.service;

import java.util.List;

import org.gulnaz.wanteat.AuthorizedUser;
import org.gulnaz.wanteat.model.Role;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.repository.UserRepository;
import org.gulnaz.wanteat.to.UserTo;
import org.gulnaz.wanteat.util.UserUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.gulnaz.wanteat.util.UserUtil.prepareToSave;
import static org.gulnaz.wanteat.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author gulnaz
 */
@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                               PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User create(User user) {
        return prepareAndSave(user);
    }

    public User update(UserTo userTo) {
        User user = get(userTo.id());
        return prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public List<User> getAll() {
        return repository.findAll(Sort.by("name", "email"));
    }

    @Transactional
    public void setRole(int id, Role role) {
        User user = get(id);
        user.getRoles().add(role);
    }

    @Transactional
    public void deleteRole(int id, Role role) {
        User user = get(id);
        user.getRoles().remove(role);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}
