package com.oldman.manage.controller.setting;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.kit.StrKit;
import com.oldman.manage.common.model.SystemConfig;
import com.oldman.manage.service.SystemConfigService;
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
@RequestMapping("/system/setting/config")
public class ConfigController extends JbootController {

    @Inject
    SystemConfigService systemConfigService;

    /**
     * 获取配置列表
     *
     * @param page  页码
     * @param limit 每页数量
     */
    public void getConfigs(Integer page, Integer limit) {
        List<SystemConfig> configList = systemConfigService.paginate(page, limit).getList();
        List<JSONObject> list = new ArrayList<>();
        configList.forEach((config) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", config.getId());
            jsonObject.put("key", config.getKey());
            jsonObject.put("name", config.getName());
            jsonObject.put("options", config.getOptions());
            jsonObject.put("isPublic", config.getIsPublic());
            list.add(jsonObject);
        });
        renderJson(new NormalResponse(Code.SUCCESS).setData(list).setCount(list.size()));
    }

    /**
     * 新增/修改配置信息
     *
     * @param id       ID
     * @param options  JSON配置信息
     * @param key      配置键名
     * @param name     配置名称
     * @param isPublic 公开(0=否,1=是)
     */
    public void saveConfig(Integer id, String options, String key, String name, Integer isPublic) {
        if (null != id) {
            SystemConfig systemConfig = systemConfigService.findById(id);
            if (null != isPublic && null == options) {
                if (!isPublic.equals(systemConfig.getIsPublic())) {
                    systemConfig.setIsPublic(isPublic);
                }
                systemConfig.update();
                renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
                return;
            }
            if (!StrKit.isBlank(name) && null == options) {
                SystemConfig systemConfig2 = systemConfigService.findFirstByColumns(Columns.create("name", name));
                if (null != systemConfig2) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "中文名称已存在"));
                    return;
                }
                systemConfig.setName(name);
                systemConfig.update();
                renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
                return;
            }
            String config = options.replace("%7B", "{").replace("%22", "\"").replace("%3A", ":").replace("%2C", ",").replace("%7D", "}");
            if (!config.equals(systemConfig.getOptions())) {
                systemConfig.setOptions(config);
            }
            if (StrKit.isBlank(key)) {
                renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "配置键名不能为空"));
                return;
            }
            if (StrKit.isBlank(name)) {
                renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "中文名称不能为空"));
                return;
            }
            if (!key.equals(systemConfig.getKey())) {
                SystemConfig systemConfig1 = systemConfigService.findFirstByColumns(Columns.create("key", key));
                if (null != systemConfig1) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "配置键名已存在"));
                    return;
                }
                systemConfig.setKey(key);
            }
            if (!name.equals(systemConfig.getName())) {
                SystemConfig systemConfig2 = systemConfigService.findFirstByColumns(Columns.create("name", name));
                if (null != systemConfig2) {
                    renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "中文名称已存在"));
                    return;
                }
                systemConfig.setName(name);
            }
            if (!isPublic.equals(systemConfig.getIsPublic())) {
                systemConfig.setIsPublic(isPublic);
            }
            systemConfig.update();
            renderJson(new NormalResponse(Code.SUCCESS, "修改成功"));
            return;
        }
        if (StrKit.isBlank(key)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "配置键名不能为空"));
            return;
        }
        SystemConfig systemConfig1 = systemConfigService.findFirstByColumns(Columns.create("key", key));
        if (null != systemConfig1) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "配置键名已存在"));
            return;
        }
        if (StrKit.isBlank(name)) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "中文名称不能为空"));
            return;
        }
        SystemConfig systemConfig2 = systemConfigService.findFirstByColumns(Columns.create("name", name));
        if (null != systemConfig2) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "中文名称已存在"));
            return;
        }
        String config = options.replace("%7B", "{").replace("%22", "\"").replace("%3A", ":").replace("%2C", ",").replace("%7D", "}");
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setKey(key);
        systemConfig.setName(name);
        systemConfig.setOptions(config);
        systemConfig.setIsPublic(isPublic);
        systemConfig.save();
        renderJson(new NormalResponse(Code.SUCCESS, "新增成功"));
    }

    /**
     * 删除配置
     */
    public void delConfig() {
        String[] ids = getParaValues("list[]");
        systemConfigService.batchDeleteByIds(ids);
        renderJson(new NormalResponse(Code.SUCCESS, "删除成功"));
    }
}
