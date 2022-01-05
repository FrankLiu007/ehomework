package name.wanghwx.ehomework.view.ifragment;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Course;
import name.wanghwx.ehomework.view.base.BaseView;

public interface ICourseFragment extends BaseView,HttpView{

    void renderRecycler(List<Course> courses);

}
