package name.wanghwx.ehomework.view.iactivity;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.view.base.BaseView;

public interface IMainActivity extends BaseView,HttpView{

    void render();

    void renderHomeworkRemind(boolean homeworkRemind);

    void renderTestRemind(boolean testRemind);

    void renderMistakeRemind(boolean mistakeRemind);
}