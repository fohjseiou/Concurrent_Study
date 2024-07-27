package com.ykdz.utils;


import com.alibaba.fastjson2.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonObjects {
    public static void main(String[] args) {
        String content = "{\"api_key\":\"69026ab43ffc498a80f5951c094daf07\",\"signature\":\"E4DB3DAEEA896A3B6D9B4020FC34F89E\",\"project_code\":\"XM20170510\",\"api_version\":\"1.0\",\"body\":[{\"is_backward_warning\":0,\"magnification\":2,\"runtime\":\"2024-04-11 20:22:09\",\"range\":3.2,\"device_no\":\"7003b742-b838-4a78-bfe4-0a43e096\",\"load_ratio\":0,\"moment\":50.3,\"is_up_warning\":0,\"wind_warn\":0,\"load\":0.057,\"moment_ratio\":0,\"slewing_speed\":346.3,\"wind_speed\":1.3,\"is_forward_warning\":0,\"is_right_warning\":0,\"height\":56.7,\"is_left_warning\":0}],\"eng_code\":\"HG001523\",\"timestamp\":\"2024-04-11 20:22:09\"}";
//        JsonObject
        // 使用 fastjson 解析 JSON 字符串
        JSONObject jsonObject = JSONObject.parseObject(content);

        // 获取 "body" 数组中的第一个元素
        JSONObject bodyObject = jsonObject.getJSONArray("body").getJSONObject(0);

        // 获取 "runtime" 字段的值
        String runtimeValue = bodyObject.getString("runtime");

        // 输出 "runtime" 的值
        System.out.println("Runtime value: " + runtimeValue);

        // 将字符串转换为日期时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date runtimeDate = null;
        try {
            runtimeDate = dateFormat.parse(runtimeValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 输出转换后的日期时间对象
        System.out.println("Runtime date: " + runtimeDate);
    }
}
