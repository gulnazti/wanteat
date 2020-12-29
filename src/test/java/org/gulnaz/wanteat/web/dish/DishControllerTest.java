package org.gulnaz.wanteat.web.dish;

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
class DishControllerTest extends AbstractControllerTest {

    @Autowired
    private DishController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/dishes"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/dishes/1007"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/dishes/1007"))
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(1007));
    }
}