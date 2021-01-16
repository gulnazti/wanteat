package org.gulnaz.wanteat.web;

import java.net.URI;
import javax.validation.Valid;

import org.gulnaz.wanteat.AuthorizedUser;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.service.UserService;
import org.gulnaz.wanteat.to.UserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.gulnaz.wanteat.util.UserUtil.createNewFromTo;
import static org.gulnaz.wanteat.util.ValidationUtil.assureIdConsistent;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_URL = RootController.REST_URL + "/profile";

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        int userId = authUser.getId();
        log.info("get user {}", userId);
        return userService.get(userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authUser) {
        int userId = authUser.getId();
        log.info("delete user {}", userId);
        userService.delete(userId);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("create {}", userTo);
        User created = userService.create(createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(REST_URL + "/{id}")
            .buildAndExpand(created.getId())
            .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authUser) {
        int userId = authUser.getId();
        log.info("update user {}", userId);
        assureIdConsistent(userTo, userId);
        userService.update(userTo);
    }
}
