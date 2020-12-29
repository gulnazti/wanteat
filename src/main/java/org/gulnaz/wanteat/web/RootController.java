package org.gulnaz.wanteat.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author gulnaz
 */
@RestController
@ApiIgnore
public class RootController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String root() {
        return "Welcome to WantEat! Make your choice :)";
    }
}
