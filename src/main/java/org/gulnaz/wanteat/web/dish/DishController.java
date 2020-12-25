package org.gulnaz.wanteat.web.dish;

import java.util.List;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.repository.DishRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.gulnaz.wanteat.util.ValidationUtil.assureIdConsistent;
import static org.gulnaz.wanteat.util.ValidationUtil.checkNotFoundWithId;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    static final String REST_URL = "/dishes";

    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        return checkNotFoundWithId(dishRepository.findById(id).orElse(null), id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id) {
        assureIdConsistent(dish, id);
        dishRepository.save(dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        checkNotFoundWithId(dishRepository.delete(id) != 0, id);
    }
}
