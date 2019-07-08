package com.hdh.lifeup.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hdh.lifeup.config.JacksonSerializerConfig;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;

import java.util.ArrayList;
import java.util.List;

/**
 * JsonUtil class<br/>
 *
 * @author hdonghong
 * @since 2018/09/07
 */
public class JsonUtil {

    /** 定义jackson对象 */
    private static final ObjectMapper MAPPER = new JacksonSerializerConfig().objectMapper();

    /**
     * 将对象转换成json字符串。
     * @param value 对象
     * @return json字符串
     */
    public static String toJson(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }
    }

    /**
     * 将json结果集转化为对象
     * @param jsonData json数据
     * @param valueType 对象中的object类型
     * @return object类型的对象
     */
    public static <T> T jsonToObject(String jsonData, Class<T> valueType) {
        try {
            return MAPPER.readValue(jsonData, valueType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }
    }

    /**
     * 将json数据转换成对象list
     * @param jsonData json数据
     * @param valueType 对象中的object类型
     * @return 对象list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> valueType) {
        if (jsonData == null) {
            return Lists.newArrayList();
        }
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, valueType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        String json = JsonUtil.toJson(list);
        System.out.println("json:" + json);

        // 有警告：Unchecked assignment: 'java.util.List' to 'java.util.List<java.lang.Integer>
        List<Integer> intList = jsonToObject(json, List.class);
        System.out.println("toObject: " + intList);

        List<Integer> inteList2 = jsonToList(json, Integer.TYPE);
        System.out.println("jsonToList: " + inteList2);
    }
}
