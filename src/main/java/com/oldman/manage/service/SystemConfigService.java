package com.oldman.manage.service;

import com.jfinal.plugin.activerecord.Page;
import com.oldman.manage.common.model.SystemConfig;
import io.jboot.db.model.Columns;

import java.util.List;

public interface SystemConfigService  {

    /**
     * 根据主键查找Model
     *
     * @param id
     * @return
     */
    public SystemConfig findById(Object id);


    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @return
     */
    public SystemConfig findFirstByColumns(Columns columns);

    /**
     * 根据 Columns 查找单条数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public SystemConfig findFirstByColumns(Columns columns, String orderBy);


    /**
     * 查找全部数据
     *
     * @return
     */
    public List<SystemConfig> findAll();


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @return
     */
    public List<SystemConfig> findListByColumns(Columns columns);


    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @return
     */
    public List<SystemConfig> findListByColumns(Columns columns, String orderBy);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param count
     * @return
     */
    public List<SystemConfig> findListByColumns(Columns columns, Integer count);

    /**
     * 根据 Columns 查找数据
     *
     * @param columns
     * @param orderBy
     * @param count
     * @return
     */
    public List<SystemConfig> findListByColumns(Columns columns, String orderBy, Integer count);


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
    public boolean delete(SystemConfig model);


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
    public Object save(SystemConfig model);


    /**
     * 保存或更新
     *
     * @param model
     * @return id if success
     */
    public Object saveOrUpdate(SystemConfig model);

    /**
     * 更新
     *
     * @param model
     * @return
     */
    public boolean update(SystemConfig model);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<SystemConfig> paginate(int page, int pageSize);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    public Page<SystemConfig> paginateByColumns(int page, int pageSize, Columns columns);


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @param columns
     * @param orderBy
     * @return
     */
    public Page<SystemConfig> paginateByColumns(int page, int pageSize, Columns columns, String orderBy);


}