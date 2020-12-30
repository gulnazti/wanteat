package org.gulnaz.wanteat.web.vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.repository.VoteRepository;
import org.gulnaz.wanteat.util.TimeUtil;
import org.gulnaz.wanteat.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.gulnaz.wanteat.web.RestaurantDishTestData.RESTAURANT1_ID;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.VOTE_MATCHER;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.vote1;
import static org.gulnaz.wanteat.web.RestaurantDishTestData.vote2;
import static org.gulnaz.wanteat.web.TestUtil.userHttpBasic;
import static org.gulnaz.wanteat.web.UserTestData.DEV_ID;
import static org.gulnaz.wanteat.web.UserTestData.USER_ID;
import static org.gulnaz.wanteat.web.UserTestData.dev;
import static org.gulnaz.wanteat.web.UserTestData.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class VoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteController.REST_URL + "/";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void voteFirstTime() throws Exception {
        fixClock(12);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isCreated());

        Vote created = voteRepository.getByUserIdAndDate(USER_ID, LocalDate.now());
        assertEquals(RESTAURANT1_ID, created.getRestaurant().getId());
    }

    @Test
    void voteBeforeRestrictionTime() throws Exception {
        fixClock(10);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isNoContent());

        Vote updated = voteRepository.getByUserIdAndDate(DEV_ID, LocalDate.now());
        assertEquals(RESTAURANT1_ID, updated.getRestaurant().getId());
    }

    @Test
    void voteAfterRestrictionTime() throws Exception {
        fixClock(12);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isConflict());

        Vote notUpdated = voteRepository.getByUserIdAndDate(DEV_ID, LocalDate.now());
        assertNotEquals(RESTAURANT1_ID, notUpdated.getRestaurant().getId());
    }

    @Test
    void cancelVoteBeforeRestrictionTime() throws Exception {
        fixClock(10);
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertNull(voteRepository.getByUserIdAndDate(DEV_ID, LocalDate.now()));
    }

    @Test
    void cancelVoteAfterRestrictionTime() throws Exception {
        fixClock(12);
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/vote")
            .with(userHttpBasic(dev)))
            .andDo(print())
            .andExpect(status().isConflict());

        assertNotNull(voteRepository.getByUserIdAndDate(DEV_ID, LocalDate.now()));
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