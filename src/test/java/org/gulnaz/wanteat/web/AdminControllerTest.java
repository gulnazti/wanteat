package org.gulnaz.wanteat.web;

import java.util.List;

import org.gulnaz.wanteat.model.Role;
import org.gulnaz.wanteat.service.UserService;
import org.gulnaz.wanteat.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.gulnaz.wanteat.TestUtil.userHttpBasic;
import static org.gulnaz.wanteat.UserTestData.ADMIN_ID;
import static org.gulnaz.wanteat.UserTestData.NOT_FOUND;
import static org.gulnaz.wanteat.UserTestData.USER_ID;
import static org.gulnaz.wanteat.UserTestData.USER_MATCHER;
import static org.gulnaz.wanteat.UserTestData.admin;
import static org.gulnaz.wanteat.UserTestData.dev;
import static org.gulnaz.wanteat.UserTestData.user;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class AdminControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminController.REST_URL + "/";

    @Autowired
    private UserService userService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
            .with(userHttpBasic(admin)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(USER_MATCHER.contentJson(admin, dev, user));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
            .with(userHttpBasic(admin)))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(USER_MATCHER.contentJson(admin));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER_ID)
            .param("enabled", "false")
            .contentType(MediaType.APPLICATION_JSON)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertFalse(userService.get(USER_ID).isEnabled());
    }

    @Test
    void setRoles() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + USER_ID + "/set")
            .param("roles", "ADMIN", "USER")
            .contentType(MediaType.APPLICATION_JSON)
            .with(userHttpBasic(admin)))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertTrue(userService.get(USER_ID).getRoles().containsAll(List.of(Role.ADMIN, Role.USER)));
    }
}