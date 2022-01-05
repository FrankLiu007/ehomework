package name.wanghwx.ehomework;

import android.app.Application;
import android.content.SharedPreferences;

import name.wanghwx.ehomework.common.constant.Default;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.ResultConverterFactory;
import name.wanghwx.ehomework.common.service.StudentService;
import retrofit2.Retrofit;

public class EhomeworkApplication extends Application{

    private static StudentService studentService;

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate(){
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://47.110.42.14:7001/student/").addConverterFactory(new ResultConverterFactory()).build();
        studentService = retrofit.create(StudentService.class);
        sharedPreferences = getSharedPreferences(Directory.ACCOUNT,MODE_PRIVATE);
    }

    public static StudentService getStudentService(){
        return studentService;
    }

    public static void setToken(String token){
        sharedPreferences.edit().putString(MapKey.TOKEN,token).apply();
    }

    public static String getToken(){
        return sharedPreferences.getString(MapKey.TOKEN,Default.EMPTY);
    }

    public static void deleteToken() {
        sharedPreferences.edit().remove(MapKey.TOKEN).apply();
    }

    public static void setAccount(String account){
        sharedPreferences.edit().putString(MapKey.ACCOUNT,account).apply();
    }

    public static String getAccount(){
        return sharedPreferences.getString(MapKey.ACCOUNT,Default.EMPTY);
    }

    public static void setPassword(String password){
        sharedPreferences.edit().putString(MapKey.PASSWORD,password).apply();
    }

    public static String getPassword(){
        return sharedPreferences.getString(MapKey.PASSWORD,Default.EMPTY);
    }

    public static void setName(String name){
        sharedPreferences.edit().putString(MapKey.NAME,name).apply();
    }

    public static String getName(){
        return sharedPreferences.getString(MapKey.NAME,Default.EMPTY);
    }
}