package com.oldman.manage.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.oldman.manage.common.model.SystemRouter;
import com.oldman.manage.service.SystemRouterService;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.NormalResponse;
import io.jboot.db.model.Columns;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@RequestMapping("/system/setting/menu")
public class MenuController extends JbootController {

    @Inject
    SystemRouterService systemRouterService;

    /**
     * 获取菜单列表
     */
    public void getMenus() {
        List<SystemRouter> routerList = systemRouterService.findAll();
        List<JSONObject> list = new ArrayList<>();
        routerList.forEach((router) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", router.getId());
            jsonObject.put("path", router.getPath());
            jsonObject.put("pid", router.getPid());
            jsonObject.put("name", router.getName());
            jsonObject.put("status", router.getStatus());
            jsonObject.put("face", router.getFace());
            jsonObject.put("type", router.getType());
            jsonObject.put("rank", router.getRank());
            list.add(jsonObject);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(list).setCount(list.size()));
    }

    /**
     * 新增/修改菜单信息
     * @param id ID
     * @param pid 上级权限
     * @param face 图标
     * @param name 权限名称
     * @param path 权限路径
     * @param rank 排序
     * @param type 类型(0=菜单,1=路由权限/按钮)
     * @param status 状态(0=停用,1=启用)
     */
    public void saveMenu(Integer id, Integer pid, String face, String name, String path, String rank, Integer type, Integer status) {
        if (null == name) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "权限名称不能为空"));
            return;
        }
        if (null == path) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "权限路径不能为空"));
            return;
        }
        if (!StringUtils.isNumeric(rank)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请输入正确的排序数值"));
            return;
        }
        //修改菜单信息
        if (null != id) {
            SystemRouter systemRouter = systemRouterService.findById(id);
            if (!pid.equals(systemRouter.getPid())){
                systemRouter.setPid(pid);
            }
            if (pid>0){
                systemRouter.setFace("");
            }else{
                if (!face.equals(systemRouter.getFace())) {
                    systemRouter.setFace(face);
                }
            }
            if (!name.equals(systemRouter.getName())) {
                SystemRouter systemRouter1 = systemRouterService.findFirstByColumns(Columns.create("name", name));
                if (null != systemRouter1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "该权限名称已存在"));
                    return;
                }
                systemRouter.setName(name);
            }
            if (!path.equals(systemRouter.getPath())) {
                SystemRouter systemRouter1 = systemRouterService.findFirstByColumns(Columns.create("path", path));
                if (null != systemRouter1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "该权限路径已存在"));
                    return;
                }
                systemRouter.setPath(path);
            }
            if (Integer.parseInt(rank) != systemRouter.getRank()) {
                systemRouter.setRank(Integer.parseInt(rank));
            }
            if (!type.equals(systemRouter.getType())) {
                systemRouter.setType(type);
            }
            if (!status.equals(systemRouter.getStatus())) {
                systemRouter.setStatus(status);
            }
            systemRouter.update();
            renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
            return;
        }
        SystemRouter systemRouter1 = systemRouterService.findFirstByColumns(Columns.create("name", name));
        if (null != systemRouter1) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "该权限名称已存在"));
            return;
        }
        SystemRouter systemRouter2 = systemRouterService.findFirstByColumns(Columns.create("path", path));
        if (null != systemRouter2) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "该权限路径已存在"));
            return;
        }
        if (null == type) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请选择权限类型"));
            return;
        }
        SystemRouter systemRouter = new SystemRouter();
        systemRouter.setPid(null != pid ? pid : 0);
        systemRouter.setFace(null != face ? face : "");
        systemRouter.setName(name);
        systemRouter.setPath(path);
        systemRouter.setRank(Integer.parseInt(rank));
        systemRouter.setType(type);
        systemRouter.setStatus(status);
        systemRouter.save();
        renderJson(new NormalResponse(Code.SUCCESS, "新增成功"));
    }

    /**
     * 删除菜单
     */
    public void delMenu(){
        String[] ids = getParaValues("list[]");
        systemRouterService.batchDeleteByIds(ids);
        renderJson(new NormalResponse(Code.SUCCESS, "删除成功"));
    }
}
