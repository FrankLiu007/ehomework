package name.wanghwx.ehomework.presenter.ipresenter;

import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.ifragment.IHomeworkFragment;

public abstract class IHomeworkFragmentPresenter extends BasePresenter<IHomeworkFragment>{

    public abstract void previousPage();

    public abstract void nextPage();

    public abstract int getTotal();

    public abstract int getPageNum();

    public abstract int getMaxPageNum();

    public abstract void setForm(int formCode);

    public abstract void toggleSubmitted(boolean submitted);

    public abstract void toggleSubject(int subjectCode);

    public abstract boolean getHasPrevious();

    public abstract boolean getHasNext();

    public abstract void refresh();
}
