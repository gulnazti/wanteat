package org.gulnaz.wanteat.web;

import java.time.LocalDate;
import java.util.List;

import org.gulnaz.wanteat.AuthorizedUser;
import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.gulnaz.wanteat.repository.UserRepository;
import org.gulnaz.wanteat.repository.VoteRepository;
import org.gulnaz.wanteat.util.exception.VoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.gulnaz.wanteat.util.TimeUtil.RESTRICTION_TIME;
import static org.gulnaz.wanteat.util.TimeUtil.getCurrentTime;
import static org.gulnaz.wanteat.util.exception.VoteException.VOTING_TIME_EXPIRED;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_URL = RootController.REST_URL + "/restaurants";

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    public VoteController(RestaurantRepository restaurantRepository,
        UserRepository userRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @PostMapping("/{id}/vote")
    @Transactional
    public ResponseEntity<Vote> vote(@PathVariable("id") int restaurantId,
                                     @AuthenticationPrincipal AuthorizedUser authUser)
        throws VoteException {
        int userId = authUser.getId();
        Vote vote = voteRepository.getByUserIdAndDate(userId, LocalDate.now());
        boolean allowed = getCurrentTime().isBefore(RESTRICTION_TIME);

        if (vote != null && !allowed) {
            throw new VoteException(VOTING_TIME_EXPIRED);
        }

        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        log.info("vote for restaurant {}", restaurantId);
        if (vote == null) {
            User user = userRepository.getOne(userId);
            Vote saved = voteRepository.save(new Vote(restaurant, user));
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } else {
            vote.setRestaurant(restaurant);
            voteRepository.save(vote);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/votes-history")
    public List<Vote> getVotesHistory(@AuthenticationPrincipal AuthorizedUser authUser) {
        int userId = authUser.getId();
        log.info("getVotesHistory for user {}", userId);
        return voteRepository.getAllVotesByUserId(userId);
    }
}
