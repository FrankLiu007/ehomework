package name.wanghwx.ehomework.common.pojo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import name.wanghwx.ehomework.view.activity.LoginActivity;

public interface HttpView{
    Context getContext();
    default void requestFailed(){
        Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
    }
    default void requestFailed(Throwable throwable){
        Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
    }
    default void toLogin(){
        Context context = getContext();
        context.startActivity(new Intent(context,LoginActivity.class));
    }
    void nullData();
    default void showResult(Result result){
        Toast.makeText(getContext(),result.message(),Toast.LENGTH_LONG).show();
    }
}