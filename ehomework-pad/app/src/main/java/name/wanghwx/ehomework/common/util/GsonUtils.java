package name.wanghwx.ehomework.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import name.wanghwx.ehomework.pojo.Question;

public class GsonUtils{

    private static Gson gson = new Gson();

    public static String toJson(Object object){
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json,Type type){
        return gson.fromJson(json,type);
    }

}