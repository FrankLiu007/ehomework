package name.wanghwx.ehomework.presenter.ipresenter;

import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.iactivity.IMistakeBookActivity;

public abstract class IMistakeBookActivityPresenter extends BasePresenter<IMistakeBookActivity>{

    public abstract void setMistakeBookId(int mistakeBookId);

    public abstract int getPageNum();

    public abstract int getMaxPageNum();

    public abstract void previousPage();

    public abstract void nextPage();

    public abstract int getNumber(int index);

    public abstract int getMistakeBookId();

    public abstract int getCourseId();

    public abstract void findAndRenderItem();
}