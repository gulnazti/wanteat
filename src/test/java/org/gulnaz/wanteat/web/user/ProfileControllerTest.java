package org.gulnaz.wanteat.web.user;

import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class ProfileControllerTest extends AbstractControllerTest {

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/profile"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void delete() throws Exception {
    }

    @Test
    void register() throws Exception {
    }

    @Test
    void update() throws Exception {
    }
}