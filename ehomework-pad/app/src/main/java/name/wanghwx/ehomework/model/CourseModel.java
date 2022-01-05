package name.wanghwx.ehomework.model;

import java.util.List;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.service.StudentService;
import name.wanghwx.ehomework.model.imodel.ICourseModel;
import name.wanghwx.ehomework.pojo.Course;
import retrofit2.Callback;

public class CourseModel implements ICourseModel{

    private static CourseModel courseModel;

    public static CourseModel getInstance(){
        return courseModel == null?courseModel = new CourseModel():courseModel;
    }

    private StudentService studentService;

    private CourseModel(){
        studentService = EhomeworkApplication.getStudentService();
    }

    @Override
    public void findCourses(Callback<Result<List<Course>>> callback){
        studentService.findCourses(EhomeworkApplication.getToken()).enqueue(callback);
    }
}
