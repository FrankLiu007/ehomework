package name.wanghwx.ehomework.presenter;

import androidx.annotation.NonNull;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.model.StudentModel;
import name.wanghwx.ehomework.model.imodel.IStudentModel;
import name.wanghwx.ehomework.pojo.Student;
import name.wanghwx.ehomework.presenter.ipresenter.ILoginActivityPresenter;
import name.wanghwx.ehomework.view.iactivity.ILoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends ILoginActivityPresenter{

    private boolean visible;

    private IStudentModel studentModel = StudentModel.getInstance();

    public LoginPresenter(ILoginActivity loginActivity){
        attachView(loginActivity);
    }

    @Override
    public void toggleVisible(){
        visible = !visible;
        if(isViewAttached()) getView().renderVisible();
    }

    @Override
    public boolean getVisible(){
        return visible;
    }

    @Override
    public void autoLogin(){
        studentModel.autoLogin(new Callback<Result<Student>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Student>> call,@NonNull Response<Result<Student>> response) {
                if(response.code() == 200){
                    Result<Student> result = response.body();
                    if(result != null && result.code() == Category.CODE_SUCCESS){
                        if(isViewAttached()) getView().loginSuccess();
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Result<Student>> call,@NonNull Throwable t){}
        });
    }

    @Override
    public void login(String account,String password){
        studentModel.login(new HttpCallback<Student>(getView()){
            @Override
            public void success(Result<Student> result){
                EhomeworkApplication.setAccount(account);
                EhomeworkApplication.setPassword(password);
                EhomeworkApplication.setName(result.data().getName());
                EhomeworkApplication.setToken(GsonUtils.fromJson((String)result.get(MapKey.TOKEN),String.class));
                if(isViewAttached()) getView().loginSuccess();
            }
        },account,password);
    }

}