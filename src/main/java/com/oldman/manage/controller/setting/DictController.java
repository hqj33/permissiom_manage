package com.oldman.manage.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.NotAction;
import com.jfinal.kit.StrKit;
import com.oldman.manage.common.model.SystemDict;
import com.oldman.manage.common.model.SystemDictList;
import com.oldman.manage.common.model.SystemRole;
import com.oldman.manage.common.model.SystemRouter;
import com.oldman.manage.service.SystemDictListService;
import com.oldman.manage.service.SystemDictService;
import com.oldman.manage.service.SystemRouterService;
import com.oldman.manage.service.impl.SystemRoleServiceImpl;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.NormalResponse;
import io.jboot.db.model.Columns;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@RequestMapping("/system/setting/dict")
public class DictController extends JbootController {

    @Inject
    SystemDictService systemDictService;
    @Inject
    SystemDictListService systemDictListService;
    @Inject
    SystemRouterService systemRouterService;

    private static SystemRoleServiceImpl systemRoleService = Aop.get(SystemRoleServiceImpl.class);

    /**
     * 根据标识获取数据
     *
     * @param dict 标识
     */
    public void getDict(String dict) {
        List<JSONObject> list = new ArrayList<>();
        switch (dict) {
            case "role":
                dictRole(list);
                break;
            case "status":
                dictStatus(list);
                break;
            case "router":
                dictRouter(list);
                break;
            case "menuType":
                dictMenuType(list);
                break;
            default:
                break;
        }
        renderJson(new NormalResponse(Code.SUCCESS).setData(list));
    }

    /**
     * 获取字典列表
     *
     * @param page  页码
     * @param limit 每页数量
     */
    public void getDicts(Integer page, Integer limit) {
        List<SystemDict> systemDicts = systemDictService.paginate(page, limit).getList();
        List<JSONObject> list = new ArrayList<>();
        systemDicts.forEach((systemDict) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", systemDict.getId());
            jsonObject.put("name", systemDict.getName());
            jsonObject.put("code", systemDict.getCode());
            jsonObject.put("remark", systemDict.getRemark());
            list.add(jsonObject);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(list).setCount(list.size()));
    }

    /**
     * 新增/修改字典信息
     *
     * @param id     ID
     * @param name   字典名称
     * @param code   字典编码
     * @param remark 字典注释
     */
    public void saveDict(Integer id, String name, String code, String remark) {
        if (null != id) {
            SystemDict systemDict = systemDictService.findById(id);
            if (!StrKit.isBlank(name)) {
                SystemDict systemDict1 = systemDictService.findFirstByColumns(Columns.create("name", name));
                if (null != systemDict1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "字典名称已存在"));
                    return;
                }
                systemDict.setName(name);
            }
            if (!StrKit.isBlank(code)) {
                SystemDict systemDict2 = systemDictService.findFirstByColumns(Columns.create("code", code));
                if (null != systemDict2) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "字典编码已存在"));
                    return;
                }
                systemDict.setCode(code);
            }
            if (!(null != remark ? remark : "").equals(systemDict.getRemark())) {
                systemDict.setRemark(remark);
            }
            systemDict.update();
            renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
            return;
        }
        if (StrKit.isBlank(name)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "字典名称不能为空"));
            return;
        }
        SystemDict systemDict1 = systemDictService.findFirstByColumns(Columns.create("name", name));
        if (null != systemDict1) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "字典名称已存在"));
            return;
        }
        if (StrKit.isBlank(code)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "字典编码不能为空"));
            return;
        }
        SystemDict systemDict2 = systemDictService.findFirstByColumns(Columns.create("code", code));
        if (null != systemDict2) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "字典编码已存在"));
            return;
        }
        SystemDict systemDict = new SystemDict();
        systemDict.setName(name);
        systemDict.setCode(code);
        systemDict.setRemark(null != remark ? remark : "");
        systemDict.save();
        renderJson(new NormalResponse(Code.SUCCESS, "新增成功"));
    }

    /**
     * 获取字典数据列表
     *
     * @param id    字典ID
     * @param page  页码
     * @param limit 每页数量
     */
    public void getDictLists(Integer id, Integer page, Integer limit) {
        List<SystemDictList> systemDictLists = systemDictListService.paginateByColumns(page, limit, Columns.create("dict_id", id)).getList();
        List<JSONObject> list = new ArrayList<>();
        systemDictLists.forEach((systemDictList) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", systemDictList.getId());
            jsonObject.put("name", systemDictList.getName());
            jsonObject.put("dict_id", systemDictList.getDictId());
            jsonObject.put("val", systemDictList.getVal());
            jsonObject.put("status", systemDictList.getStatus());
            jsonObject.put("rank", systemDictList.getRank());
            jsonObject.put("create_date", systemDictList.getCreateDate());
            list.add(jsonObject);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(list).setCount(list.size()));
    }

    /**
     * 删除字典
     */
    public void delDict() {
        String[] ids = getParaValues("list[]");
        systemDictService.batchDeleteByIds(ids);
        renderJson(new NormalResponse(Code.SUCCESS, "删除成功"));
    }

    /**
     * 新增/修改字典数据列表
     *
     * @param id     ID
     * @param dictId 字典ID
     * @param name   数据名称
     * @param val    数据值
     * @param status 数据状态(0=停用,1=启用)
     * @param rank   排序
     */
    public void saveDictValue(Integer id, String dictId, String name, String val, Integer status, String rank) {
        if (null != id) {
            SystemDictList systemDictList = systemDictListService.findById(id);
            if (!StrKit.isBlank(name)) {
                if (!name.equals(systemDictList.getName())) {
                    SystemDictList systemDictList1 = systemDictListService.findFirstByColumns(Columns.create("name", name));
                    if (null != systemDictList1) {
                        renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "数据名称已存在"));
                        return;
                    }
                }
                systemDictList.setName(name);
            }
            if (!StrKit.isBlank(val)) {
                systemDictList.setVal(val);
            }
            if (null != status) {
                systemDictList.setStatus(status);
            }
            if (null != rank) {
                if (!StringUtils.isNumeric(rank)) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请输入正确的排序数值"));
                    return;
                }
                systemDictList.setRank(Integer.parseInt(rank));
            }
            systemDictList.update();
            renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
            return;
        }
        if (StrKit.isBlank(name)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "数据名称不能为空"));
            return;
        }
        SystemDictList systemDictList1 = systemDictListService.findFirstByColumns(Columns.create("name", name));
        if (null != systemDictList1) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "数据名称已存在"));
            return;
        }
        if (StrKit.isBlank(val)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "数据值不能为空"));
            return;
        }
        if (!StringUtils.isNumeric(rank)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请输入正确的排序数值"));
            return;
        }
        SystemDictList systemDictList = new SystemDictList();
        systemDictList.setName(name);
        systemDictList.setDictId(Integer.parseInt(dictId));
        systemDictList.setVal(val);
        systemDictList.setStatus(status);
        systemDictList.setRank(Integer.parseInt(rank));
        systemDictList.setCreateDate(new Date());
        systemDictList.save();
        renderJson(new NormalResponse(Code.SUCCESS, "新增成功"));
    }

    /**
     * 删除字典数据列表
     */
    public void delDictValue() {
        String[] ids = getParaValues("list[]");
        systemDictListService.batchDeleteByIds(ids);
        renderJson(new NormalResponse(Code.SUCCESS, "删除成功"));
    }

    @NotAction
    private void dictRole(List<JSONObject> list) {
        List<SystemRole> systemRoleList = systemRoleService.findAll();
        systemRoleList.forEach((systemRole) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", systemRole.getId());
            jsonObject.put("name", systemRole.getName());
            list.add(jsonObject);
        });
    }

    @NotAction
    private void dictStatus(List<JSONObject> list) {
        SystemDict systemDict = systemDictService.findFirstByColumns(Columns.create("code", "system_user_status"));
        List<SystemDictList> systemDictLists = systemDictListService.findListByColumns(Columns.create("dict_id", systemDict.getId()));
        systemDictLists.forEach((systemDicts) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", systemDicts.getId());
            jsonObject.put("val", systemDicts.getVal());
            jsonObject.put("dict_id", systemDicts.getDictId());
            jsonObject.put("name", systemDicts.getName());
            list.add(jsonObject);
        });
    }

    @NotAction
    private void dictRouter(List<JSONObject> list) {
        List<SystemRouter> oneRouters = systemRouterService.findListByColumns(Columns.create("pid", 0));
        oneRouters.forEach((oneRouter) -> {
            JSONObject oneRouterObj = new JSONObject();
            oneRouterObj.put("id", oneRouter.getId());
            oneRouterObj.put("name", oneRouter.getName());
            oneRouterObj.put("pid", oneRouter.getPid());
            List<SystemRouter> twoRouters = systemRouterService.findListByColumns(Columns.create("pid", oneRouter.getId()));
            List<JSONObject> childrenList = new ArrayList<>();
            twoRouters.forEach((twoRouter) -> {
                JSONObject twoRouterObj = new JSONObject();
                twoRouterObj.put("id", twoRouter.getId());
                twoRouterObj.put("name", twoRouter.getName());
                twoRouterObj.put("pid", twoRouter.getPid());
                List<SystemRouter> routerList = systemRouterService.findListByColumns(Columns.create("pid", twoRouter.getId()));
                List<JSONObject> twoChildrenList = new ArrayList<>();
                routerList.forEach((router) -> {
                    JSONObject routerObj = new JSONObject();
                    routerObj.put("id", router.getId());
                    routerObj.put("name", router.getName());
                    routerObj.put("pid", router.getPid());
                    twoChildrenList.add(routerObj);
                });
                if (!twoChildrenList.isEmpty()) {
                    twoRouterObj.put("children", twoChildrenList);
                }
                childrenList.add(twoRouterObj);
            });
            oneRouterObj.put("children", childrenList);
            list.add(oneRouterObj);
        });
    }

    @NotAction
    private void dictMenuType(List<JSONObject> list) {
        SystemDict systemDict = systemDictService.findFirstByColumns(Columns.create("code", "system_menu_type"));
        List<SystemDictList> systemDictLists = systemDictListService.findListByColumns(Columns.create("dict_id", systemDict.getId()));
        systemDictLists.forEach((systemDicts) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", systemDicts.getId());
            jsonObject.put("val", Integer.parseInt(systemDicts.getVal()));
            jsonObject.put("dict_id", systemDicts.getDictId());
            jsonObject.put("name", systemDicts.getName());
            list.add(jsonObject);
        });
    }
}
