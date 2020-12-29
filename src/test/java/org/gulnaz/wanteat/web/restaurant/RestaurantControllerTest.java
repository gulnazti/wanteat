package org.gulnaz.wanteat.web.restaurant;

import org.gulnaz.wanteat.util.exception.NotFoundException;
import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void getAllWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants/with-menu"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants/1003"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void getWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants/1003/with-menu"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
    }

    @Test
    void update() throws Exception {
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/restaurants/1003"))
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(1003));
    }

    @Test
    void addDish() throws Exception {
    }
}