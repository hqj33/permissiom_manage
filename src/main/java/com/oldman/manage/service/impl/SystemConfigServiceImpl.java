package com.oldman.manage.service.impl;

import io.jboot.aop.annotation.Bean;
import com.oldman.manage.service.SystemConfigService;
import com.oldman.manage.common.model.SystemConfig;
import io.jboot.service.JbootServiceBase;


@Bean
public class SystemConfigServiceImpl extends JbootServiceBase<SystemConfig> implements SystemConfigService {

}