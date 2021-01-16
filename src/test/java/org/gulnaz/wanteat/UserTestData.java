package org.gulnaz.wanteat;

import org.gulnaz.wanteat.model.Role;
import org.gulnaz.wanteat.model.User;

import static org.gulnaz.wanteat.model.AbstractBaseEntity.START_SEQ;

/**
 * @author gulnaz
 */
public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingEqualsComparator(User.class);

    public static final int USER_ID = START_SEQ;
    public static final int DEV_ID = START_SEQ + 1;
    public static final int ADMIN_ID = START_SEQ + 2;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User dev = new User(DEV_ID, "Dev", "dev@yandex.ru", "devpassword", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);
}
