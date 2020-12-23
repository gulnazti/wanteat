package org.gulnaz.wanteat.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author gulnaz
 */
@Controller
public class RootController {

    @GetMapping(value = "/")
    public String root() {
        return "index";
    }
}
