package name.wanghwx.ehomework.model;

import java.util.List;
import java.util.Map;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.service.StudentService;
import name.wanghwx.ehomework.model.imodel.IHomeworkModel;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.pojo.Item;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class HomeworkModel implements IHomeworkModel{

    private static HomeworkModel homeworkModel;

    public static HomeworkModel getInstance(){
        if(homeworkModel == null){
            homeworkModel = new HomeworkModel();
        }
        return homeworkModel;
    }

    private HomeworkModel(){
        this.studentService = EhomeworkApplication.getStudentService();
    }

    private StudentService studentService;

    @Override
    public void countHomework(Callback<Result<Object>> callback,Integer formCode) {
        studentService.countHomework(EhomeworkApplication.getToken(),formCode).enqueue(callback);
    }

    @Override
    public void findHomeworks(Callback<Result<List<Homework>>> callback,Integer formCode,Boolean submitted,Integer subjectCode,Integer pageNum,Integer pageSize) {
        studentService.findHomeworks(EhomeworkApplication.getToken(),formCode,submitted,subjectCode,pageNum,pageSize).enqueue(callback);
    }

    @Override
    public void getHomework(Callback<Result<Homework>> callback, Integer homeworkId){
        studentService.getHomework(EhomeworkApplication.getToken(),homeworkId).enqueue(callback);
    }

    @Override
    public void getItemInHomework(Callback<Result<Item>> callback, Integer homeworkId, Integer itemId) {
        studentService.getItemInHomework(EhomeworkApplication.getToken(),homeworkId,itemId).enqueue(callback);
    }

    @Override
    public void submitHomework(Callback<Result<Homework>> callback, Integer homeworkId,String answers, Map<String, RequestBody> map) {
        studentService.submitHomework(EhomeworkApplication.getToken(),homeworkId,answers,map).enqueue(callback);
    }

    @Override
    public void findHomeworksByCourse(Callback<Result<List<Homework>>> callback, Integer courseId, Integer formCode, Integer pageNum, Integer pageSize) {
        studentService.findHomeworksByCourse(EhomeworkApplication.getToken(),courseId,formCode,pageNum,pageSize).enqueue(callback);
    }

}