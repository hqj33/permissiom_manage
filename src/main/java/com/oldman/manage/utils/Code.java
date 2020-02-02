package com.oldman.manage.utils;

/**
 * @author oldman
 */
public class Code {
    /**
     * 服务维护
     */
    public static final int SERVER = -1;
    /**
     * 成功
     */
    public static final int SUCCESS = 1;

    /**
     * 失败
     */
    public static final int FAIL = 0;

    /**
     * 参数错误: 一般是缺少或参数值不符合要求
     */
    public static final int ARGUMENT_ERROR = 2;

    /**
     * 服务器错误
     */
    public static final int ERROR = 500;

    /**
     * 接口不存在
     */
    public static final int NOT_FOUND = 404;

    /**
     * token无效
     */
    public static final int TOKEN_INVALID = 1001;

    /**
     * 帐号已存在*
     */
    public static final int ACCOUNT_EXISTS = 3;

    /**
     * 验证码错误
     */
    public static final int CODE_ERROR = 4;
    /**
     * 签名无效
     */
    public static final int SIGN_INVALID = 5;
    /**
     * 时间戳超过范围
     */
    public static final int TIMESTAMP_INVALID = 6;
    /**
     * 包含敏感字
     */
    public static final int SENSITIVE_WORD = 7;
    /**
     * 服务结束
     */
    public static final int SERVICE_END = 8;
}