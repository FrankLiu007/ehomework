package name.wanghwx.ehomework.presenter.ipresenter;

import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.iactivity.ICourseActivity;

public abstract class ICourseActivityPresenter extends BasePresenter<ICourseActivity>{

    public abstract void setCourseId(Integer courseId);

    public abstract void switchForm(int form);

    public abstract int getTotal();

    public abstract int getPageNum();

    public abstract int getMaxPageNum();

    public abstract void previousPage();

    public abstract void nextPage();

    public abstract void switchMenu(int menu);
}