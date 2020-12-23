package org.gulnaz.wanteat.web.user;

import java.net.URI;
import java.util.List;

import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.gulnaz.wanteat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.gulnaz.wanteat.util.ValidationUtil.assureIdConsistent;
import static org.gulnaz.wanteat.util.ValidationUtil.checkNew;
import static org.gulnaz.wanteat.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    static final String REST_URL = "/admin";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @PostMapping(value = "/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(REST_URL + "/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        checkNotFoundWithId(restaurantRepository.delete(id), id);
    }
}
