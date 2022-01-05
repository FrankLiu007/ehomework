package name.wanghwx.ehomework.presenter;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.CourseModel;
import name.wanghwx.ehomework.model.imodel.ICourseModel;
import name.wanghwx.ehomework.pojo.Course;
import name.wanghwx.ehomework.presenter.ipresenter.ICourseFragmentPresenter;
import name.wanghwx.ehomework.view.ifragment.ICourseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseFragmentPresenter extends ICourseFragmentPresenter {

    private ICourseModel courseModel;

    public CourseFragmentPresenter(ICourseFragment courseFragment){
        attachView(courseFragment);
        courseModel = CourseModel.getInstance();
    }

    @Override
    public void findCourses(){
        courseModel.findCourses(new HttpCallback<List<Course>>(getView()){
            @Override
            public void success(Result<List<Course>> result) {
                if(isViewAttached()) getView().renderRecycler(result.data());
            }
        });
    }
}