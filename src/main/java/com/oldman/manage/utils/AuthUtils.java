package com.oldman.manage.utils;

import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;
import com.oldman.manage.common.SystemConst;
import com.oldman.manage.common.model.SystemUser;
import com.oldman.manage.service.impl.SystemUserServiceImpl;
import io.jboot.Jboot;
import io.jboot.db.model.Columns;
import io.jboot.support.redis.JbootRedis;

import java.util.Map;

/**
 * token校验
 * @author oldman
 */
public class AuthUtils {

    private static SystemUserServiceImpl systemUserService = Aop.get(SystemUserServiceImpl.class);

    public static boolean tokenVerify(Map map) {
        if (null == map) {
            return false;
        }
        String token = (String) map.get("token");
        String username = (String) map.get("username");
        if (StrKit.isBlank(token) || StrKit.isBlank(username)) {
            return false;
        }
        JbootRedis redis = Jboot.getRedis();
        String cacheToken = redis.get(SystemConst.ACCESS_TOKEN + username);
        if (cacheToken == null) {
            return false;
        }
        if (!cacheToken.equalsIgnoreCase(token)) {
            return false;
        }
        SystemUser systemUser = systemUserService.findFirstByColumns(Columns.create("user", username));
        redis.set(SystemConst.LOGIN_USER + systemUser.getId(), systemUser);
        return true;
    }
}
