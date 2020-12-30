package org.gulnaz.wanteat.web.dish;

import java.util.List;
import javax.validation.Valid;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.repository.DishRepository;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.gulnaz.wanteat.web.RootController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.gulnaz.wanteat.util.ValidationUtil.assureIdConsistent;
import static org.gulnaz.wanteat.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = RootController.REST_URL + "/dishes";

    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    public DishController(DishRepository dishRepository,
                          RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Dish> getAll() {
        return dishRepository.findAll(Sort.by("created", "id").descending());
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        return checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        assureIdConsistent(dish, id);
        Integer restaurantId = dishRepository.getRestaurantIdByDishId(id);
        checkNotFoundWithId(restaurantId, id);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        dishRepository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable int id) {
        checkNotFoundWithId(dishRepository.delete(id) != 0, id);
    }
}
