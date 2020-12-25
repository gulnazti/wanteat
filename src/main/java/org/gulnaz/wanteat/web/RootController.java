package org.gulnaz.wanteat.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gulnaz
 */
@RestController
public class RootController {

    @GetMapping(value = "/")
    public String root() {
        return "Welcome to WantEat! Make your choice :)";
    }
}
