package name.wanghwx.ehomework.presenter.ipresenter;

import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.iactivity.ILoginActivity;

public abstract class ILoginActivityPresenter extends BasePresenter<ILoginActivity>{

    public abstract void toggleVisible();

    public abstract boolean getVisible();

    public abstract void autoLogin();

    public abstract void login(String account, String password);

}