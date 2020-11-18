package org.gulnaz.wanteat.repository;

import org.gulnaz.wanteat.model.User;

import java.util.List;

/**
 * @author gulnaz
 */
public interface UserRepository {

    User save(User user);

    User get(int id);

    boolean delete(int id);

    List<User> getAll();
}
