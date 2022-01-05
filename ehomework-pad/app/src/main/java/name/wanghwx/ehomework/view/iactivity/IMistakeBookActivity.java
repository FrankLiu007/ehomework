package name.wanghwx.ehomework.view.iactivity;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.view.base.BaseView;

public interface IMistakeBookActivity extends BaseView,HttpView{

    void render(MistakeBook mistakeBook);

    void renderTotal(int total);

    void renderPage();

    void renderHasPrevious(boolean hasPrevious);

    void renderHasNext(boolean hasNext);
}