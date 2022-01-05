package name.wanghwx.ehomework.presenter.ipresenter;

import androidx.fragment.app.Fragment;

import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.iactivity.IMainActivity;

public abstract class IMainPresenter extends BasePresenter<IMainActivity>{

    public abstract Fragment getFragment();

    public abstract void switchMenu(int menu);

    public abstract Integer getMenu();

    public abstract void findAndRenderRemind();
}
