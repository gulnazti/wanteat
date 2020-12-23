package org.gulnaz.wanteat.web;

import org.gulnaz.wanteat.model.AbstractBaseEntity;

/**
 * @author gulnaz
 */
public class SecurityUtil {

    private static int id = AbstractBaseEntity.START_SEQ;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }
}
