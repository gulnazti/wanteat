package org.gulnaz.wanteat.web.vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.gulnaz.wanteat.util.TimeUtil;
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
    void voteFirstTime() throws Exception {
        fixClock(12);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    void voteBeforeRestrictionTime() throws Exception {
        fixClock(10);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void voteAfterRestrictionTime() throws Exception {
        fixClock(12);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    void cancelVoteBeforeRestrictionTime() throws Exception {
        fixClock(10);
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void cancelVoteAfterRestrictionTime() throws Exception {
        fixClock(12);
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isConflict());
    }

    @Test
    void getHistory() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "votes-history")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(VOTE_MATCHER.contentJson(List.of(vote2, vote1)));
    }

    private void fixClock(int hour) {
        TimeUtil.fixClock(LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0)));
    }
}