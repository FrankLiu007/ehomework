package name.wanghwx.ehomework.view.ifragment;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.view.base.BaseView;

public interface IMistakeBookFragment extends BaseView,HttpView{

    void renderRecycler(List<MistakeBook> mistakeBooks);

}
