package org.gulnaz.wanteat.web.vote;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.gulnaz.wanteat.model.Restaurant;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.repository.RestaurantRepository;
import org.gulnaz.wanteat.repository.UserRepository;
import org.gulnaz.wanteat.repository.VoteRepository;
import org.gulnaz.wanteat.web.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    static final String REST_URL = "/restaurants";
    static final LocalTime RESTRICTION_TIME = LocalTime.of(11, 0);

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
    public ResponseEntity<Vote> vote(@PathVariable("id") int restaurantId) {
        int userId = SecurityUtil.authUserId();
        Vote vote = voteRepository.getByUserIdAndDate(userId, LocalDate.now());
        boolean allowed = LocalTime.now().isBefore(RESTRICTION_TIME);

        if (vote != null && !allowed) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(vote);
        }

        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        User user = userRepository.getOne(userId);
        Vote voteToSave = vote == null
                          ? new Vote(restaurant, user)
                          : new Vote(vote.getId(), restaurant, user);
        Vote saved = voteRepository.save(voteToSave);
        return vote == null
               ? ResponseEntity.status(HttpStatus.CREATED).body(saved)
               : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/votes")
    public List<Vote> getHistory() {
        return voteRepository.getAllVotesByUserId(SecurityUtil.authUserId());
    }
}
