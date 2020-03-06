package com.oldman.manage.controller.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.oldman.manage.common.SystemConst;
import com.oldman.manage.common.model.*;
import com.oldman.manage.service.*;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.GoogleAuthenticator;
import com.oldman.manage.utils.NormalResponse;
import com.oldman.manage.utils.VerifyUtils;
import io.jboot.Jboot;
import io.jboot.db.model.Columns;
import io.jboot.support.redis.JbootRedis;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@RequestMapping("/system/user")
public class UserController extends JbootController {

    private static final int EXP = Integer.parseInt(Jboot.configValue("token.exp"));
    private static final String SECRET = Jboot.configValue("google.secret");
    private static JbootRedis redis = Jboot.getRedis();
    @Inject
    SystemRouterService systemRouterService;
    @Inject
    SystemUserService systemUserService;
    @Inject
    SystemUserRoleService systemUserRoleService;
    @Inject
    SystemRoleService systemRoleService;
    @Inject
    SystemRoleRouterService systemRoleRouterService;

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @Clear
    public void login(String username, String password, String credential) {
        if (StrKit.isBlank(username) || StrKit.isBlank(password)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "用户名或密码为空或包含空格"));
            return;
        }
        if (!StringUtils.isNumeric(credential)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "令牌格式错误或为空"));
            return;
        }

        long timeMillis = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5);
        boolean result = ga.check_code(SECRET, Long.parseLong(credential), timeMillis);
        if (!result) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请输入正确的令牌"));
            return;
        }

        SystemUser systemUser = systemUserService.findFirstByColumns(Columns.create("user", username));
        if (null == systemUser) {
            renderJson(new NormalResponse(Code.FAIL, "用户不存在"));
            return;
        }
        String salt = systemUser.getSalt();
        String pass = HashKit.md5(HashKit.md5(password) + salt);
        if (!pass.equals(systemUser.getPass())) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "密码错误"));
            return;
        }
        systemUser.setLoginDate(new Date());
        systemUser.setLoginIp(getIPAddress());
        systemUser.update();
        List<SystemUserRole> systemUserRoles = systemUserRoleService.findListByColumns(Columns.create("user_id", systemUser.getId()));
        List<Integer> roleIds = systemUserRoles.stream().map(SystemUserRole::getRoleId).collect(Collectors.toList());
        Map<String, String> userMap = new HashMap<>(16);
        String token = HashKit.generateSalt(16);
        userMap.put("username", username);
        userMap.put("id", systemUser.getId().toString());
        userMap.put("token", token);
        userMap.put("roleIds", roleIds.toString().substring(1, roleIds.toString().length() - 1));
        redis.setex(SystemConst.ACCESS_TOKEN + username, EXP, token);
        setJwtMap(userMap);
        renderJson(new NormalResponse(Code.SUCCESS));
    }

    /**
     * 获取菜单列表
     */
    public void getMenu() {
        List<String> roleIds = Arrays.asList(getJwtParas().get("roleIds").toString().split(","));

        List<JSONObject> allList = new ArrayList<>();
        roleIds.forEach(rid -> {
            List<SystemRoleRouter> systemRoleRouters = systemRoleRouterService.findListByColumns(Columns.create("role_id", rid));
            systemRoleRouters.forEach(systemRoleRouter -> {
                JSONObject all = new JSONObject();
                SystemRouter systemRouter = systemRouterService.findFirstByColumns(Columns.create("id", systemRoleRouter.getRouterId()).add("status", 1).add("type", 0).add("pid", 0));
                if (null == systemRouter) {
                    return;
                }
                all.put("oneRouters",systemRouter);
                List<SystemRouter> routers = systemRouterService.findListByColumns(Columns.create("pid", systemRouter.getId()).add("status", 1).add("type", 0));
                all.put("twoRouters",routers);
                allList.add(all);
            });
        });
        List<JSONObject> menuList = new ArrayList<>();
        allList.forEach((jsonObject) -> {
            SystemRouter oneRouter = (SystemRouter)jsonObject.get("oneRouters");
            int id = oneRouter.getId();
            JSONObject oneMenuJson = new JSONObject();
            oneMenuJson.put("id", id);
            oneMenuJson.put("title", oneRouter.getName());
            oneMenuJson.put("name", oneRouter.getPath());
            oneMenuJson.put("icon", oneRouter.getFace());
            oneMenuJson.put("pid", oneRouter.getPid());
            List<JSONObject> lists = new ArrayList<>();
            JSONArray twoRouters = jsonObject.getJSONArray("twoRouters");
            twoRouters.forEach(obj -> {
                SystemRouter twoRouter = (SystemRouter) obj;
                JSONObject twoMenuJson = new JSONObject();
                twoMenuJson.put("id", twoRouter.getId());
                twoMenuJson.put("title", twoRouter.getName());
                twoMenuJson.put("name", twoRouter.getPath());
                twoMenuJson.put("icon", twoRouter.getFace());
                twoMenuJson.put("pid", twoRouter.getPid());
                lists.add(twoMenuJson);
            });
            oneMenuJson.put("list", lists);
            menuList.add(oneMenuJson);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(menuList));
    }

    /**
     * 获取用户个人信息
     */
    public void getMeInfo() {
        SystemUser systemUser = redis.get(SystemConst.LOGIN_USER + getJwtParas().get("id"));
        int id = systemUser.getId();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("face", systemUser.getFace());
        jsonObject.put("login_date", systemUser.getLoginDate());
        jsonObject.put("login_ip", systemUser.getLoginIp());
        jsonObject.put("user", systemUser.getUser());
        jsonObject.put("nickname", systemUser.getNickname());
        jsonObject.put("phone", systemUser.getPhone());
        List<JSONObject> list = new ArrayList<>();
        List<SystemUserRole> userRoleList = systemUserRoleService.findListByColumns(Columns.create("user_id", id));
        userRoleList.forEach((userRole) -> {
            SystemRole role = systemRoleService.findFirstByColumns(Columns.create("id", userRole.getRoleId()));
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", role.getId());
            jsonObj.put("name", role.getName());
            jsonObj.put("status", role.getStatus());
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("user_id", userRole.getUserId());
            jsonObject1.put("role_id", userRole.getRoleId());
            jsonObj.put("pivot", jsonObject1);
            list.add(jsonObj);
        });
        jsonObject.put("roles", list);
        renderJson(new NormalResponse(Code.SUCCESS).setData(jsonObject));
    }

    /**
     * 修改用户个人信息
     *
     * @param face        头像Path
     * @param nickname    昵称
     * @param phone       手机号
     * @param newPassword 新密码
     * @param password    原密码
     */
    public void saveMeInfo(String face, String nickname, String phone, String newPassword, String password) {
        boolean flag = false;
        SystemUser systemUser = systemUserService.findById(getJwtParas().get("id"));
        if (!face.equals(systemUser.getFace())) {
            systemUser.setFace(face);
        }
        if (!StrKit.isBlank(nickname)) {
            if (!nickname.equals(systemUser.getNickname())) {
                SystemUser systemUser1 = systemUserService.findFirstByColumns(Columns.create("nickname", nickname));
                if (null != systemUser1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "该昵称已被使用"));
                    return;
                }
                systemUser.setNickname(nickname);
            }
        }
        if (VerifyUtils.verifyPhone(phone)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "手机号格式不正确"));
            return;
        }
        systemUser.setPhone(phone);
        if (!StrKit.isBlank(newPassword)) {
            String salt = systemUser.getSalt();
            String oldPass = HashKit.md5(HashKit.md5(password) + salt);
            if (!oldPass.equals(systemUser.getPass())) {
                renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "原密码输入错误"));
                return;
            }
            String newPass = HashKit.md5(HashKit.md5(newPassword) + salt);
            systemUser.setPass(newPass);
            flag = true;
        }
        systemUser.update();
        renderJson(new NormalResponse(Code.SUCCESS, "修改成功").setData(flag));
    }

    /**
     * 用户登出
     */
    public void logout() {
        redis.del(SystemConst.ACCESS_TOKEN + getJwtParas().get("username"));
        renderJson(new NormalResponse(Code.SUCCESS));
    }
}
