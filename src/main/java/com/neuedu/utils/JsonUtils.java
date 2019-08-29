package com.neuedu.utils;

import com.google.common.collect.Lists;
import com.neuedu.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {

        //所有对象序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认timestamps
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转json
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //设置日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //忽略json中有，但java中没有的属性，防止出错
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    //对象转字符串
    public static <T> String objToString(T object){

        if (object==null){
            return null;
        }

        try {
            return object instanceof String?(String) object : objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //对象转字符串(重载，格式化)
    public static <T> String objToStringPretty(T object){

        if (object==null){
            return null;
        }

        try {
            return object instanceof String?(String) object : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //字符串转对象
    public static <T> T stringToObject(String str,Class<T> tClass){

        if (StringUtils.isEmpty(str)||tClass==null){
            return null;
        }

        try {
            return tClass.equals(String.class)?(T)str : objectMapper.readValue(str,tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //json数组转字符串
    public static <T> T stringToObject(String str, TypeReference<T> typeReference){

        if (StringUtils.isEmpty(str)||typeReference==null){
            return null;
        }

        try {
            return typeReference.getType().equals(String.class)?(T) str : objectMapper.readValue(str,typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T stringToObject(String str,Class<?> collectionClass,Class<?>... elements){

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elements);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static void main(String[] args) {
//        UserInfo user1 = new UserInfo();
//        user1.setUsername("石永");
//        user1.setPassword("123456");
//        user1.setEmail("999999");
//
//        UserInfo user2 = new UserInfo();
//        user2.setUsername("石永");
//        user2.setPassword("123456");
//        user2.setEmail("999999");
//
//        List<UserInfo> userInfoList = Lists.newArrayList();
//        userInfoList.add(user1);
//        userInfoList.add(user2);
//
//        String a = objToStringPretty(userInfoList);
//
//        userInfoList = stringToObject(a, new TypeReference<List<UserInfo>>() {});
//        System.out.println(userInfoList);
//    }

}
