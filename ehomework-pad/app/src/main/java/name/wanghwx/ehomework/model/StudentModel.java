package name.wanghwx.ehomework.model;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.service.StudentService;
import name.wanghwx.ehomework.model.imodel.IStudentModel;
import name.wanghwx.ehomework.pojo.Student;
import retrofit2.Callback;

public class StudentModel implements IStudentModel{

    private static StudentModel studentModel;

    public static StudentModel getInstance(){
        if(studentModel == null){
            studentModel = new StudentModel();
        }
        return studentModel;
    }

    private StudentService studentService;

    private StudentModel(){
        studentService = EhomeworkApplication.getStudentService();
    }

    @Override
    public void autoLogin(Callback<Result<Student>> callback) {
        studentService.autoLogin(EhomeworkApplication.getToken()).enqueue(callback);
    }

    @Override
    public void login(Callback<Result<Student>> callback, String account, String password){
        studentService.login(account,password).enqueue(callback);
    }

    @Override
    public void logout(Callback<Result<Object>> callback){
        studentService.logout(EhomeworkApplication.getToken()).enqueue(callback);
    }

    @Override
    public void findRemind(Callback<Result<Object>> callback){
        studentService.findRemind(EhomeworkApplication.getToken()).enqueue(callback);
    }
}