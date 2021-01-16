package org.gulnaz.wanteat.web;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.util.JsonUtil;
import org.gulnaz.wanteat.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.gulnaz.wanteat.RestaurantDishTestData.DISH1_ID;
import static org.gulnaz.wanteat.RestaurantDishTestData.DISH_MATCHER;
import static org.gulnaz.wanteat.RestaurantDishTestData.NOT_FOUND;
import static org.gulnaz.wanteat.RestaurantDishTestData.dish1;
import static org.gulnaz.wanteat.RestaurantDishTestData.dish2;
import static org.gulnaz.wanteat.RestaurantDishTestData.dishes;
import static org.gulnaz.wanteat.RestaurantDishTestData.getUpdatedDish;
import static org.gulnaz.wanteat.TestUtil.userHttpBasic;
import static org.gulnaz.wanteat.UserTestData.admin;
import static org.gulnaz.wanteat.UserTestData.user;
import static org.gulnaz.wanteat.web.ExceptionInfoHandler.DUPLICATE_DISH_NAME_TODAY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishController.REST_URL + "/";

    @Autowired
    private DishController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(DISH_MATCHER.contentJson(dishes));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(DISH_MATCHER.contentJson(dish1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdatedDish();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(updated))
            .with(userHttpBasic(admin)))
            .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(controller.get(DISH1_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID)
            .with(userHttpBasic(admin)))
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(DISH1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH1_ID, null, 0, null);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(invalid))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Dish duplicate = new Dish(DISH1_ID, dish2.getName(), 100, dish2.getCreated());
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(duplicate))
            .with(userHttpBasic(admin)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(detailMessage(DUPLICATE_DISH_NAME_TODAY));
    }
}