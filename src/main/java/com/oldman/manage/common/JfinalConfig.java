package com.oldman.manage.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Routes;
import com.jfinal.template.Engine;
import com.oldman.manage.interceptor.JwtIntercepter;
import io.jboot.aop.jfinal.JfinalHandlers;
import io.jboot.aop.jfinal.JfinalPlugins;
import io.jboot.core.listener.JbootAppListener;
import io.jboot.web.fixedinterceptor.FixedInterceptors;

/**
 * JFinal配置类
 * @author oldman
 */
public class JfinalConfig implements JbootAppListener {
    @Override
    public void onInit() {

    }

    @Override
    public void onConstantConfig(Constants constants) {
        constants.setBaseUploadPath(SystemConst.UPLOAD_PATH);
    }

    @Override
    public void onRouteConfig(Routes routes) {

    }

    @Override
    public void onEngineConfig(Engine engine) {

    }

    @Override
    public void onPluginConfig(JfinalPlugins plugins) {

    }

    @Override
    public void onInterceptorConfig(Interceptors interceptors) {
        interceptors.addGlobalActionInterceptor(new JwtIntercepter());
    }

    @Override
    public void onFixedInterceptorConfig(FixedInterceptors fixedInterceptors) {

    }

    @Override
    public void onHandlerConfig(JfinalHandlers handlers) {

    }

    @Override
    public void onStartBefore() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
