package name.wanghwx.ehomework.model.imodel;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Course;
import retrofit2.Callback;

public interface ICourseModel{

    void findCourses(Callback<Result<List<Course>>> callback);

}