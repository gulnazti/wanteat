package org.gulnaz.wanteat.web;

import org.gulnaz.wanteat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author gulnaz
 */
@Controller
public class RootController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getAll(Model model) {
        model.addAttribute("users", repository.getAll());
        return "users";
    }
}
