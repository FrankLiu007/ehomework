package name.wanghwx.ehomework.model.imodel;

import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Student;
import retrofit2.Callback;

public interface IStudentModel{

    void autoLogin(Callback<Result<Student>> callback);

    void login(Callback<Result<Student>> callback,String account, String password);

    void logout(Callback<Result<Object>> callback);

    void findRemind(Callback<Result<Object>> callback);

}