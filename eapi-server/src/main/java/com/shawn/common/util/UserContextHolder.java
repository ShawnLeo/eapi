package com.shawn.common.util;

import com.shawn.common.vo.UserSession;
import org.springframework.util.Assert;

/**
 * 将 UserContext 与当前线程关联
 *
 */

public class UserContextHolder {

    private static final ThreadLocal<UserSession> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static UserSession getContext() {

        UserSession ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setContext(UserSession context) {

        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");

        contextHolder.set(context);
    }

    public static UserSession createEmptyContext() {
        return new UserSession();
    }
}
