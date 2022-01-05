package name.wanghwx.ehomework.view.iactivity;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.view.base.BaseView;

public interface ICourseActivity extends BaseView,HttpView{

    void render(List<Homework> homeworks);

    void disablePreviousPage();

    void enablePreviousPage();

    void disableNextPage();

    void enableNextPage();

    void renderTotal();

    void renderPage();
}