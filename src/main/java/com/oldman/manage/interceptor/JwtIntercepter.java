package com.oldman.manage.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.oldman.manage.utils.AuthUtils;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.NormalResponse;
import io.jboot.support.jwt.JwtManager;

import java.util.Map;

/**
 * jwt校验器
 * @author oldman
 */
public class JwtIntercepter implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        String jwt = controller.getHeader("Jwt");
        if (StrKit.isBlank(jwt)) {
            controller.renderJson(new NormalResponse(Code.TOKEN_INVALID,"Jwt token invalid!"));
            return;
        }
        String jwToken = controller.getHeader(JwtManager.me().getHttpHeaderName());
        Map map = JwtManager.me().parseJwtToken(jwToken);
        if (!AuthUtils.tokenVerify(map)){
            controller.renderJson(new NormalResponse(Code.TOKEN_INVALID,"Jwt token invalid!"));
            return;
        }
        inv.invoke();
    }
}
