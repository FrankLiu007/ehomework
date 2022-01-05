package name.wanghwx.ehomework.view.ifragment;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.view.base.BaseView;

public interface IHomeworkFragment extends BaseView,HttpView{

    void renderData(List<Homework> homeworks);

    void renderTotal();

    void renderPage();

    void renderHasPrevious();

    void renderHasNext();

    void refreshScreen();

    void renderUnsubmittedRemind(int unsubmittedRemind);

    void renderSubmittedRemind(int submittedRemind);
}
