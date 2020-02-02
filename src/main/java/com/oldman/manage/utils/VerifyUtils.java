package com.oldman.manage.utils;

import com.jfinal.kit.StrKit;

import java.math.BigDecimal;

/**
 * 数据校验工具类
 * @author oldman
 */
public class VerifyUtils {
    /**
     * 账号正则表达式
     */
    private static final String PATTERN_USERNAME = "^\\w{3,12}$";
    /**
     * 昵称正则表达式
     */
    private static final String PATTERN_NICKNAME = "^[\u4e00-\u9fa5_A-Za-z0-9]{1,8}$";
    /**
     * QQ正则表达式
     */
    private static final String PATTERN_QQ = "^[1-9]\\d{4,15}$";
    /**
     * 微信正则表达式
     */
    private static final String PATTERN_WeChat = "^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$";
    /**
     * 手机号码正则表达式
     */
    private static final String PATTERN_PHONE = "^1(3|4|5|7|8)\\d{9}$";
    /**
     * 邮箱正则表达式
     */
    private static final String PATTERN_EMAIL = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$";
    /**
     * 密码登录密码正则表达式
     */
    private static final String PATTERN_LOGIN_PASSWORD = "^[A-Za-z0-9]{6,12}$";

    /**
     * 金额验证
     */
    private static final String PATTERN_MONEY = "(^0\\.[1-9]0?$)|(^0\\.\\d[1-9]$)|(^[1-9]\\d*(.[0-9]{1,2})?$)";

    public static boolean verifyUsername(String username) {
        return username == null || !username.matches(PATTERN_USERNAME);
    }

    public static boolean verifyNickName(String nickname) {
        return !nickname.matches(PATTERN_NICKNAME);
    }

    public static boolean verifyQQ(String qq) {
        return !qq.matches(PATTERN_QQ);
    }

    public static boolean verifyWeChat(String weixin) {
        return !weixin.matches(PATTERN_WeChat);
    }

    public static boolean verifyMoney(BigDecimal money) {
        return money == null || !money.toString().matches(PATTERN_MONEY);
    }

    public static boolean verifyPhone(String phone) {
        return StrKit.isBlank(phone) || !phone.matches(PATTERN_PHONE);
    }

    public static boolean verifyPassword(String password) {
        return password == null || !password.matches(PATTERN_LOGIN_PASSWORD);
    }

    public static boolean verifyEmail(String email) {
        return !email.matches(PATTERN_EMAIL);
    }
}
