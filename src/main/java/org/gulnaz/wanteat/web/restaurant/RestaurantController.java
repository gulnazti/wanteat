package org.gulnaz.wanteat.web.restaurant;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.repository.DishRepository;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.springframework.data.domain.Sort;
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
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/restaurants";
    static final Sort SORT_BY_NAME_ADDRESS = Sort.by("name", "address");

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public RestaurantController(RestaurantRepository restaurantRepository,
                                     DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping()
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_BY_NAME_ADDRESS);
    }

    @GetMapping("/with-menu")
    public List<Restaurant> getAllWithMenu() {
        return restaurantRepository.getAllWithMenu(LocalDate.now());
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        return checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
    }

    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenu(@PathVariable int id) {
        return checkNotFoundWithId(restaurantRepository.getWithMenu(id, LocalDate.now()), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    @PostMapping(value = "/{id}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Dish> addDish(@RequestBody Dish dish, @PathVariable("restaurantId") int restaurantId) {
        checkNew(dish);
        Dish created = dishRepository.save(dish);
        created.setRestaurant(restaurantRepository.getOne(restaurantId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/dishes/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
