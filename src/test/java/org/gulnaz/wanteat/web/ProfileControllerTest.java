package org.gulnaz.wanteat.web;

import org.gulnaz.wanteat.model.User;
import org.gulnaz.wanteat.service.UserService;
import org.gulnaz.wanteat.to.UserTo;
import org.gulnaz.wanteat.util.JsonUtil;
import org.gulnaz.wanteat.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.gulnaz.wanteat.TestUtil.readFromJson;
import static org.gulnaz.wanteat.TestUtil.userHttpBasic;
import static org.gulnaz.wanteat.UserTestData.USER_ID;
import static org.gulnaz.wanteat.UserTestData.USER_MATCHER;
import static org.gulnaz.wanteat.UserTestData.admin;
import static org.gulnaz.wanteat.UserTestData.dev;
import static org.gulnaz.wanteat.UserTestData.user;
import static org.gulnaz.wanteat.web.ExceptionInfoHandler.DUPLICATE_EMAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gulnaz
 */
class ProfileControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileController.REST_URL + "/";

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
            .with(userHttpBasic(user)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
            .with(userHttpBasic(user)))
            .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), admin, dev);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "New Name", "newmail@yandex.ru", "newpassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(newTo)))
            .andDo(print())
            .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "New Name", "newmail@yandex.ru", "newpassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
            .with(userHttpBasic(user))
            .content(JsonUtil.writeValue(updatedTo)))
            .andDo(print())
            .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo invalid = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL + "register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        UserTo invalid = new UserTo(USER_ID, null, null, null);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
            .with(userHttpBasic(user))
            .content(JsonUtil.writeValue(invalid)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerDuplicate() throws Exception {
        UserTo duplicate = new UserTo(null, "New Name", user.getEmail(), "newpassword");
        perform(MockMvcRequestBuilders.post(REST_URL + "register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.writeValue(duplicate)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(detailMessage(DUPLICATE_EMAIL));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        UserTo duplicate = new UserTo(USER_ID, "New Name", dev.getEmail(), "newpassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
            .with(userHttpBasic(user))
            .content(JsonUtil.writeValue(duplicate)))
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(detailMessage(DUPLICATE_EMAIL));
    }
}