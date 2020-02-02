package com.oldman.manage.controller.other;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.upload.UploadFile;
import com.oldman.manage.utils.Code;
import com.oldman.manage.utils.NormalResponse;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author admin
 */
@RequestMapping("/system/other")
public class OtherController extends JbootController {

    /**
     * 头像上传
     */
    public void upload() {
        UploadFile uploadFile = getFile();
        File file = uploadFile.getFile();
        if (null == uploadFile) {
            renderJson(new NormalResponse(Code.ARGUMENT_ERROR, "请选择文件在进行上传"));
            return;
        }
        String fileName = uploadFile.getOriginalFileName();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String date = dateTime.format(formatter);
        String newName = date + fileName.substring(fileName.indexOf("."));
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File imageFile = new File(uploadFile.getUploadPath() + File.separator + "images" + File.separator + newName);
            fis = new FileInputStream(file);
            fos = new FileOutputStream(imageFile);
            byte[] b = new byte[1024];
            while (fis.read(b, 0, 1024) != -1) {
                fos.write(b, 0, 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (file.exists()) {
            file.delete();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uploadUrlName", "/images/" + newName);
        renderJson(new NormalResponse(Code.SUCCESS, "上传成功").setData(jsonObject));
    }
}
