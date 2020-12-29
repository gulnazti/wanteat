package org.gulnaz.wanteat.web.vote;

import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class VoteControllerTest extends AbstractControllerTest {

    @Test
    void vote() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants/1004/vote"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders.get("/restaurants/votes-history"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}