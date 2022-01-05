package name.wanghwx.ehomework.common.pojo;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import name.wanghwx.ehomework.common.constant.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class HttpCallback<D> implements Callback<Result<D>>{

    private Reference<HttpView> httpViewReference;

    public HttpCallback(HttpView httpView){
        httpViewReference = new WeakReference<>(httpView);
    }

    @Override
    public void onResponse(@NonNull Call<Result<D>> call,@NonNull Response<Result<D>> response){
        if(httpViewReference == null){
            Log.e(getClass().getName(),"需要HttpView处理响应");
        }else{
            HttpView httpView = httpViewReference.get();
            if(httpView == null){
                Log.e(getClass().getName(),"HttpView不能为null");
            }else{
                if(response.code() == 401){
                    httpView.toLogin();
                }else{
                    Result<D> result = response.body();
                    if(result == null){
                        httpView.requestFailed();
                    }else{
                        switch(result.code()){
                            case Category.CODE_SUCCESS:
                                success(result);
                                break;
                            case Category.CODE_MISSING_TOKEN:
                            case Category.CODE_INVALID_TOKEN:
                                httpView.toLogin();
                                break;
                            default:
                                httpView.showResult(result);
                                break;
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onFailure(@NonNull Call<Result<D>> call,@NonNull Throwable t){
        if(httpViewReference == null){
            Log.e(getClass().getName(),"需要HttpView处理响应");
        }else{
            HttpView httpView = httpViewReference.get();
            if(httpView == null){
                Log.e(getClass().getName(),"HttpView不能为null");
            }else{
                httpView.requestFailed(t);
            }
        }
    }

    public abstract void success(Result<D> result);
}