package org.gulnaz.wanteat.web.dish;

import org.gulnaz.wanteat.model.Dish;
import org.gulnaz.wanteat.util.JsonUtil;
import org.gulnaz.wanteat.util.exception.NotFoundException;
import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.gulnaz.wanteat.web.RestaurantDishTestData.DISH1_ID;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.DISH_MATCHER;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.dish1;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.dishes;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.getUpdatedDish;
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
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(DISH_MATCHER.contentJson(dish1));
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
}