package com.oldman.manage.service.impl;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.jboot.aop.annotation.Bean;
import com.oldman.manage.service.SystemUserService;
import com.oldman.manage.common.model.SystemUser;
import io.jboot.service.JbootServiceBase;

import java.util.List;


@Bean
public class SystemUserServiceImpl extends JbootServiceBase<SystemUser> implements SystemUserService {

    @Override
    public List<Record> findUserByCond(Kv cond, int page, int limit) {
        return null != cond ? Db.template("findUserByCond", cond).paginate(page, limit).getList() : Db.find(Db.getSql("findUserByCond"));
    }

    @Override
    public List<Record> findListUserByIds(String... ids) {
        Kv cond = Kv.by("ids",ids);
        return Db.template("findListUserByIds",cond).find();
    }
}