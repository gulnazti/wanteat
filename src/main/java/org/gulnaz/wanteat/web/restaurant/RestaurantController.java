package org.gulnaz.wanteat.web.restaurant;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.repository.DishRepository;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.gulnaz.wanteat.repository.VoteRepository;
import org.gulnaz.wanteat.to.RestaurantTo;
import org.gulnaz.wanteat.util.RestaurantUtil;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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

    private final VoteRepository voteRepository;

    public RestaurantController(RestaurantRepository restaurantRepository,
                                DishRepository dishRepository,
                                VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping()
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(SORT_BY_NAME_ADDRESS);
    }

    @GetMapping("/with-menu")
    @Transactional
    public List<RestaurantTo> getAllWithMenu() {
        List<Restaurant> restaurants = restaurantRepository.getAllWithMenu(LocalDate.now());
        List<Vote> allTodayVotes = voteRepository.getAllVotesForToday(LocalDate.now());
        return RestaurantUtil.getTos(restaurants, allTodayVotes);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
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
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
        restaurantRepository.save(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable int id) {
        checkNotFoundWithId(restaurantRepository.delete(id) != 0, id);
    }

    @PostMapping(value = "/{id}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Dish> addDish(@Valid @RequestBody Dish dish, @PathVariable int id) {
        checkNew(dish);
        dish.setRestaurant(restaurantRepository.getOne(id));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/dishes/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
