package com.oldman.manage.common;

import java.io.File;

public interface SystemConst {
    public static final String LOGIN_USER = "user:";
    public static final String ACCESS_TOKEN = "token:";
    public static final String UPLOAD_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "webapp";

}
