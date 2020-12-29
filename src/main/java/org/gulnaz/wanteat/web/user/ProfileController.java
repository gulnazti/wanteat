package org.gulnaz.wanteat.web.user;

import java.net.URI;
import javax.validation.Valid;

import org.gulnaz.wanteat.AuthorizedUser;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.gulnaz.wanteat.util.ValidationUtil.assureIdConsistent;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    static final String REST_URL = "/profile";

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        return userRepository.findById(authUser.getId()).orElse(null);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        userRepository.delete(authUser.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(REST_URL + "/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user, @AuthenticationPrincipal AuthorizedUser authUser) {
        assureIdConsistent(user, authUser.getId());
        userRepository.save(user);
    }
}
