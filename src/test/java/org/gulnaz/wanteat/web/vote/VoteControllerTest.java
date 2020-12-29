package org.gulnaz.wanteat.web.vote;

import java.util.List;

import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.gulnaz.wanteat.web.RestaurantDishTestData.RESTAURANT1_ID;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.VOTE_MATCHER;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.vote1;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.vote2;
import static org.gulnaz.wanteat.web.TestUtil.userHttpBasic;
import static org.gulnaz.wanteat.web.UserTestData.dev;
import static org.gulnaz.wanteat.web.UserTestData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + "/";

    @Test
    void vote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes-history")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(VOTE_MATCHER.contentJson(List.of(vote2, vote1)));
    }
}