package com.oldman.manage.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.oldman.manage.common.SystemConst;
import com.oldman.manage.common.model.SystemRole;
import com.oldman.manage.common.model.SystemUser;
import com.oldman.manage.common.model.SystemUserRole;
import com.oldman.manage.service.SystemRoleService;
import com.oldman.manage.service.SystemUserRoleService;
import com.oldman.manage.service.SystemUserService;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.NormalResponse;
import io.jboot.db.model.Columns;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 */
@RequestMapping("/system/setting/user")
public class UserController extends JbootController {

    @Inject
    SystemUserRoleService systemUserRoleService;
    @Inject
    SystemUserService systemUserService;
    @Inject
    SystemRoleService systemRoleService;

    /**
     * 获取用户信息列表
     *
     * @param username 用户名
     * @param cDate    创建日期
     * @param status   状态(0=停用,1=启用)
     * @param page     页码
     * @param limit    每页数量
     */
    public void getUsers(String username, String cDate, Integer status, Integer page, Integer limit) {
        List<JSONObject> list = new ArrayList<>();
        String createDate = null;
        if (null != cDate) {
            createDate = cDate.replace("%3A", ":").replace("+", " ");
        }
        List<Record> userListAll;
        if (!StrKit.isBlank(username) || null != cDate || null != status) {
            Kv cond = Kv.by("username", username).set("createDate", createDate).set("status", status);
            userListAll = systemUserService.findUserByCond(cond, page, limit);
        } else {
            userListAll = systemUserService.findUserByCond(null, page, limit);
        }
        userListAll.forEach((user) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getInt("id"));
            jsonObject.put("user", user.getStr("user"));
            jsonObject.put("face", user.getStr("face"));
            jsonObject.put("login_date", user.getStr("login_date"));
            jsonObject.put("create_date", user.getStr("create_date"));
            jsonObject.put("login_ip", user.getStr("login_ip"));
            jsonObject.put("status", user.getInt("status"));
            List<SystemUserRole> userRoleList = systemUserRoleService.findListByColumns(Columns.create("user_id", user.getInt("id")));
            List<JSONObject> roles = new ArrayList<>();
            userRoleList.forEach((userRole) -> {
                SystemRole role = systemRoleService.findFirstByColumns(Columns.create("id", userRole.getRoleId()));
                JSONObject rolesObj = new JSONObject();
                rolesObj.put("id", role.getId());
                rolesObj.put("name", role.getName());
                rolesObj.put("status", role.getStatus());
                JSONObject pivotObj = new JSONObject();
                pivotObj.put("user_id", userRole.getUserId());
                pivotObj.put("role_id", userRole.getRoleId());
                rolesObj.put("pivot", pivotObj);
                roles.add(rolesObj);
            });
            jsonObject.put("roles", roles);
            list.add(jsonObject);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(list).setCount(list.size()));
    }

    /**
     * 新增/修改用户信息
     *
     * @param id     ID
     * @param face   头像Path
     * @param user   用户名
     * @param pass   密码
     * @param status 状态(0=停用,1=启用)
     */
    public void saveUser(Integer id, String face, String user, String pass, Integer status) {
        String[] role = getParaValues("role[]");
        if (null == face) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请上传头像"));
            return;
        }
        if (StrKit.isBlank(user)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "用户名不能为空"));
            return;
        }
        if (null == role) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请至少设置一个角色"));
            return;
        }
        //修改用户信息
        if (null != id) {
            SystemUser systemUser = systemUserService.findById(id);
            if (!face.equals(systemUser.getFace())) {
                systemUser.setFace(face);
            }
            if (!user.equals(systemUser.getUser())) {
                SystemUser systemUser1 = systemUserService.findFirstByColumns(Columns.create("user", user));
                if (null != systemUser1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "用户名已存在"));
                    return;
                }
                systemUser.setUser(user);
            }
            if (!StrKit.isBlank(pass)) {
                String salt = systemUser.getSalt();
                String newPass = HashKit.md5(HashKit.md5(pass) + salt);
                if (!newPass.equals(systemUser.getPass())) {
                    systemUser.setPass(newPass);
                }
            }
            if (!status.equals(systemUser.getStatus())) {
                systemUser.setStatus(status);
            }
            systemUserRoleService.deleteByColumns(Columns.create("user_id", id));
            for (String roleId : role) {
                SystemUserRole systemUserRole = new SystemUserRole();
                systemUserRole.setUserId(id);
                systemUserRole.setRoleId(Integer.parseInt(roleId));
                systemUserRole.save();
            }
            systemUser.update();
            renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
            return;
        }
        if (StrKit.isBlank(pass)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "密码不能为空"));
            return;
        }
        SystemUser systemUser = new SystemUser();
        SystemUser systemUser1 = systemUserService.findFirstByColumns(Columns.create("user", user));
        if (null != systemUser1) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "用户名已存在"));
            return;
        }
        systemUser.setUser(user);
        String salt = HashKit.md5(HashKit.generateSalt(32));
        String newPass = HashKit.md5(HashKit.md5(pass) + salt);
        systemUser.setSalt(salt);
        systemUser.setPass(newPass);
        systemUser.setStatus(status);
        systemUser.setFace(face);
        systemUser.setCreateDate(new Date());
        systemUser.save();
        for (String roleId : role) {
            SystemUserRole systemUserRole = new SystemUserRole();
            systemUserRole.setUserId(systemUser.getId());
            systemUserRole.setRoleId(Integer.parseInt(roleId));
            systemUserRole.save();
        }
        renderJson(new NormalResponse(Code.SUCCESS, "新增成功"));
    }

    /**
     * 删除/批量删除用户
     */
    public void delUser() {
        String[] ids = getParaValues("list[]");
        List<Record> systemUsers = systemUserService.findListUserByIds(ids);
        systemUsers.forEach((systemUser) -> {
            String facePath = systemUser.getStr("face");
            File file = new File(SystemConst.UPLOAD_PATH + facePath);
            if (file.exists()) {
                file.delete();
            }
        });
        systemUserService.batchDeleteByIds(ids);
        renderJson(new NormalResponse(Code.SUCCESS, "删除成功"));
    }
}
