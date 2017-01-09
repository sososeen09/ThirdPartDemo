package com.longge.thirdpartdemo.websocket;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class GsonTools {

    public GsonTools() {

    }

    /**
     * 描述：将传递进来的参数变为json格式的字符串
     * 返回：json格式的字符串
     */
    public static String createGsonString(Object object) {
        Gson gson = new Gson();
        String gsonString = gson.toJson(object);
        return gsonString;
    }

    /**
     * 描述：参数一：json格式的字符串，参数二用来承载解析json后的数据的对象，该对象是根据参数一json格式的字符串进行创建的
     * 返回：存储有参数一数据的参数二类型的对象
     */
    public static <T> T changeGsonToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(gsonString, cls);
        return t;
    }

    /**
     * 描述：参数一：json格式的字符串，参数二用来承载解析json后的数据的对象，该对象是根据参数一json格式的字符串进行创建的
     * 返回：存储有参数一数据的参数二类型的对象的List集合
     * 这个方法其实就是对上面的方法的补充，当上面方法的json格式的数据有多个并被[]包裹起来则可以使用此方法来获取对象集合
     */
    public static <T> List<T> changeGsonToList(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
        return list;
    }

    /**
     * 描述：参数一：json格式的字符串，
     * 返回：返回Map集合
     * 在json数据中有可能会出现这么一种情况：在{}中,有以数字作为key，那么在根据数据去创建对象的时候，作为key的数字无法成为成员变量的名字
     * 那就让作为key的数字成为Map的key来进行解析
     */
    public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
        Map<String, T> map = null;
        Gson gson = new Gson();
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    /**
     * 描述：参数一：json格式的字符串，
     * 返回：这个方法其实就是对上面的方法的补充，当上面方法的json格式的数据有多个并被[]包裹起来则可以使用此方法来获取Map集合
     */
    public static <T> List<Map<String, T>> changeGsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        Gson gson = new Gson();
        list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }
}
