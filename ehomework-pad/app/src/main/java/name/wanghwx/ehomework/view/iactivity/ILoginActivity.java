package name.wanghwx.ehomework.view.iactivity;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.view.base.BaseView;

public interface ILoginActivity extends BaseView, HttpView{

    void renderVisible();

    void loginSuccess();
}