package org.gulnaz.wanteat.repository;

import java.util.List;

import org.gulnaz.wanteat.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 * @author gulnaz
 */
@Repository
public class UserRepository {
    private static final Sort SORT_BY_NAME_EMAIL = Sort.by("name", "email");

    private final CrudUserRepository repository;

    public UserRepository(CrudUserRepository repository) {
        this.repository = repository;
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User get(int id) {
        return repository.findById(id).orElse(null);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }

    public List<User> getAll() {
        return repository.findAll(SORT_BY_NAME_EMAIL);
    }
}
