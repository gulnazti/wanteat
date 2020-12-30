package org.gulnaz.wanteat.web.restaurant;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.util.JsonUtil;
import org.gulnaz.wanteat.util.exception.NotFoundException;
import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.gulnaz.wanteat.web.dish.DishController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.gulnaz.wanteat.web.RestaurantDishTestData.DISH_MATCHER;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.NOT_FOUND;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.RESTAURANT1_ID;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.RESTAURANT_MATCHER;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.RESTAURANT_TO_MATCHER;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.dish1;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.getNewDish;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.getNewRestaurant;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.getRestaurant1WithMenu;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.getRestaurantsWithMenu;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.getUpdatedRestaurant;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.restaurant1;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.restaurant2;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.restaurants;
import static org.gulnaz.wanteat.web.TestUtil.readFromJson;
import static org.gulnaz.wanteat.web.TestUtil.userHttpBasic;
import static org.gulnaz.wanteat.web.UserTestData.admin;
import static org.gulnaz.wanteat.web.UserTestData.user;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + "/";

    @Autowired
    private RestaurantController restaurantController;

    @Autowired
    private DishController dishController;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

    @Test
    void getAllWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "with-menu")
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(RESTAURANT_TO_MATCHER.contentJson(getRestaurantsWithMenu()));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/with-menu")
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(RESTAURANT_MATCHER.contentJson(getRestaurant1WithMenu()));
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getNewRestaurant();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(newRestaurant))
            .with(userHttpBasic(admin)));

        Restaurant created = readFromJson(action, Restaurant.class);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantController.get(newId), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updated))
            .with(userHttpBasic(admin)))
            .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantController.get(RESTAURANT1_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID)
            .with(userHttpBasic(admin)))
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantController.get(RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(invalid))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT1_ID, null, null);
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(invalid))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Restaurant duplicate = new Restaurant(restaurant1.getName(), restaurant1.getAddress());
        perform(MockMvcRequestBuilders.post(REST_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(duplicate))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT1_ID, restaurant2.getName(), restaurant2.getAddress());
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(invalid))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void addDish() throws Exception {
        Dish newDish = getNewDish();
        ResultActions action =
            perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish))
                .with(userHttpBasic(admin)));

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishController.get(newId), newDish);
    }

    @Test
    void addInvalidDish() throws Exception {
        Dish invalid = new Dish(null, 0, null);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/dishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(invalid))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void addDuplicateDish() throws Exception {
        Dish duplicate = new Dish(dish1.getName(), 100, dish1.getCreated());
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/dishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(duplicate))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }
}