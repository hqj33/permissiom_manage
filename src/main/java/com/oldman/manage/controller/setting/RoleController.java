package com.oldman.manage.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.oldman.manage.common.model.SystemRole;
import com.oldman.manage.common.model.SystemRoleRouter;
import com.oldman.manage.common.model.SystemRouter;
import com.oldman.manage.service.SystemRoleRouterService;
import com.oldman.manage.service.SystemRoleService;
import com.oldman.manage.service.SystemRouterService;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.NormalResponse;
import io.jboot.db.model.Columns;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@RequestMapping("/system/setting/role")
public class RoleController extends JbootController {

    @Inject
    SystemRoleService systemRoleService;
    @Inject
    SystemRoleRouterService systemRoleRouterService;
    @Inject
    SystemRouterService systemRouterService;

    /**
     * 获取角色列表
     *
     * @param page  页码
     * @param limit 每页数量
     */
    public void getRoles(Integer page, Integer limit) {
        List<SystemRole> roleList = systemRoleService.paginate(page, limit).getList();
        List<JSONObject> list = new ArrayList<>();
        roleList.forEach((role) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", role.getId());
            jsonObject.put("name", role.getName());
            jsonObject.put("status", role.getStatus());
            List<JSONObject> routers = new ArrayList<>();
            List<SystemRoleRouter> roleRouterList = systemRoleRouterService.findListByColumns(Columns.create("role_id", role.getId()));
            roleRouterList.forEach((roleRouter) -> {
                SystemRouter router = systemRouterService.findFirstByColumns(Columns.create("id", roleRouter.getRouterId()));
                JSONObject routerObj = new JSONObject();
                routerObj.put("id", router.getId());
                routerObj.put("path", router.getPath());
                routerObj.put("pid", router.getPid());
                routerObj.put("name", router.getName());
                routerObj.put("status", router.getStatus());
                routerObj.put("face", router.getFace());
                routerObj.put("type", router.getType());
                routerObj.put("rank", router.getRank());
                JSONObject pivotObj = new JSONObject();
                pivotObj.put("role_id", roleRouter.getRoleId());
                pivotObj.put("router_id", roleRouter.getRouterId());
                routerObj.put("pivot", pivotObj);
                routers.add(routerObj);
            });
            jsonObject.put("routers", routers);
            list.add(jsonObject);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(list).setCount(list.size()));
    }

    /**
     * 新增/修改角色信息
     *
     * @param id     ID
     * @param name   角色名称
     * @param status 状态
     */
    public void saveRole(Integer id, String name, Integer status) {
        String[] authIds = getParaValues("auth[]");
        if (null != id) {
            SystemRole systemRole = systemRoleService.findById(id);
            if (null == status && !StrKit.isBlank(name)) {
                SystemRole systemRole1 = systemRoleService.findFirstByColumns(Columns.create("name", name));
                if (null != systemRole1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "角色名称已存在"));
                    return;
                }
                systemRole.setName(name);
                systemRole.update();
                renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
                return;
            }
            if (null == name && !status.equals(systemRole.getStatus())) {
                systemRole.setStatus(status);
                systemRole.update();
                renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
                return;
            }
            if (StrKit.isBlank(name)) {
                renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "角色名称不能为空"));
                return;
            }
            if (!name.equals(systemRole.getName())) {
                SystemRole systemRole1 = systemRoleService.findFirstByColumns(Columns.create("name", name));
                if (null != systemRole1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "角色名称已存在"));
                    return;
                }
                systemRole.setName(name);
            }
            systemRoleRouterService.deleteByColumns(Columns.create("role_id", id));
            if (null != authIds) {
                for (String authId : authIds) {
                    SystemRoleRouter systemRoleRouter = new SystemRoleRouter();
                    systemRoleRouter.setRouterId(Integer.parseInt(authId));
                    systemRoleRouter.setRoleId(id);
                    systemRoleRouter.save();
                }
            }
            if (!status.equals(systemRole.getStatus())) {
                systemRole.setStatus(status);
            }
            systemRole.update();
            renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
            return;
        }
        if (StrKit.isBlank(name)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "角色名称不能为空"));
            return;
        }
        SystemRole systemRole1 = systemRoleService.findFirstByColumns(Columns.create("name", name));
        if (null != systemRole1) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "角色名称已存在"));
            return;
        }
        if (null != authIds) {
            for (String authId : authIds) {
                SystemRoleRouter systemRoleRouter = new SystemRoleRouter();
                systemRoleRouter.setRouterId(Integer.parseInt(authId));
                systemRoleRouter.setRoleId(id);
                systemRoleRouter.save();
            }
        }
        SystemRole systemRole = new SystemRole();
        systemRole.setName(name);
        systemRole.setStatus(status);
        systemRole.save();
        renderJson(new NormalResponse(Code.SUCCESS, "新增成功"));
    }

    /**
     * 删除角色
     */
    public void delRole() {
        String[] ids = getParaValues("list[]");
        systemRoleService.batchDeleteByIds(ids);
        renderJson(new NormalResponse(Code.SUCCESS, "删除成功"));
    }
}
