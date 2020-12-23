package org.gulnaz.wanteat.web.restaurant;

import java.util.List;

import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.gulnaz.wanteat.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/restaurants";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping()
    public List<Restaurant> getAll() {
        return restaurantRepository.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return checkNotFoundWithId(restaurantRepository.get(id), id);
    }
}
