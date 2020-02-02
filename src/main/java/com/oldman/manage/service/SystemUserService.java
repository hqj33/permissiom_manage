package com.oldman.manage.service;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.oldman.manage.common.model.SystemUser;
import io.jboot.db.model.Columns;

import java.util.List;

public interface SystemUserService  {

    /**
     * 根据主键查找Model
     *
     * @param id
     * @return
     */
    public SystemUser findById(Object id);


    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @return
     */
    public SystemUser findFirstByColumns(Columns columns);

    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public SystemUser findFirstByColumns(Columns columns, String orderBy);


    /**
     * 查找全部数据
     *
     * @return
     */
    public List<SystemUser> findAll();


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @return
     */
    public List<SystemUser> findListByColumns(Columns columns);


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public List<SystemUser> findListByColumns(Columns columns, String orderBy);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param count
     * @return
     */
    public List<SystemUser> findListByColumns(Columns columns, Integer count);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @param count
     * @return
     */
    public List<SystemUser> findListByColumns(Columns columns, String orderBy, Integer count);


    /**
     * 根据提交查询数据量
     *
     * @param columns
     * @return
     */
    public long findCountByColumns(Columns columns);


    /**
     * 根据ID 删除model
     *
     * @param id
     * @return
     */
    public boolean deleteById(Object id);


    /**
     * 删除
     *
     * @param model
     * @return
     */
    public boolean delete(SystemUser model);


    /**
     * 根据 多个 id 批量删除
     *
     * @param ids
     * @return
     */
    public boolean batchDeleteByIds(Object... ids);


    /**
     * 根据条件进行删除
     *
     * @param columns
     * @return
     */
    public boolean deleteByColumns(Columns columns);


    /**
     * 保存到数据库
     *
     * @param model
     * @return id if success
     */
    public Object save(SystemUser model);


    /**
     * 保存或更新
     *
     * @param model
     * @return id if success
     */
    public Object saveOrUpdate(SystemUser model);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    public boolean update(SystemUser model);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<SystemUser> paginate(int page, int pageSize);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<SystemUser> paginateByColumns(int page, int pageSize, Columns columns);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @param columns
     * @param orderBy
     * @return
     */
    public Page<SystemUser> paginateByColumns(int page, int pageSize, Columns columns, String orderBy);

    /**
     * 根据条件分页查询
     * @param cond
     * @param page
     * @param limit
     * @return
     */
    List<Record> findUserByCond(Kv cond, int page, int limit);

    /**
     * 根据ID查询
     * @param ids
     * @return
     */
    List<Record> findListUserByIds(String... ids);
}