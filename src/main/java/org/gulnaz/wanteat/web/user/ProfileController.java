package org.gulnaz.wanteat.web.user;

import java.net.URI;

import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.gulnaz.wanteat.util.ValidationUtil.assureIdConsistent;
import static org.gulnaz.wanteat.web.SecurityUtil.authUserId;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    static final String REST_URL = "/profile";

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public User get() {
        return userRepository.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userRepository.delete(authUserId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody User user) {
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(REST_URL + "/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        assureIdConsistent(user, authUserId());
        userRepository.save(user);
    }
}
