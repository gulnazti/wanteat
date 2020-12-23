package org.gulnaz.wanteat.repository;

import java.util.List;

import org.gulnaz.wanteat.model.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 * @author gulnaz
 */
@Repository
public class RestaurantRepository {
    private static final Sort SORT_BY_NAME_ADDRESS = Sort.by("name", "address");

    private final CrudRestaurantRepository repository;

    public RestaurantRepository(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_BY_NAME_ADDRESS);
    }

    public Restaurant get(int id) {
        return repository.findById(id)
            .orElse(null);
    }

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }
}
