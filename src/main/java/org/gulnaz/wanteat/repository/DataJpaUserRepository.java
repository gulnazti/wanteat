package org.gulnaz.wanteat.repository;

import org.gulnaz.wanteat.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gulnaz
 */
@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_BY_NAME_EMAIL = Sort.by("name", "email");

    private final CrudUserRepository crudRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_BY_NAME_EMAIL);
    }
}
