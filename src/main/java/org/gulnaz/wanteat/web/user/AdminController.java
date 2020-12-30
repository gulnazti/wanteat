package org.gulnaz.wanteat.web.user;

import java.util.List;
import java.util.Set;

import org.gulnaz.wanteat.model.Role;
import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.service.UserService;
import org.gulnaz.wanteat.web.RootController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author gulnaz
 */
@RestController
@RequestMapping(value = AdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    static final String REST_URL = RootController.REST_URL + "/users";

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        userService.enable(id, enabled);
    }

    @PatchMapping("/{id}/set")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setRoles(@PathVariable int id, @RequestParam Set<Role> roles) {
        userService.setRoles(id, roles);
    }
}
