package name.wanghwx.ehomework.presenter;

import name.wanghwx.ehomework.presenter.ipresenter.IItemPresenter;
import name.wanghwx.ehomework.view.ifragment.IItemFragment;

public class ItemPresenter extends IItemPresenter{

    public ItemPresenter(IItemFragment itemFragment){
        attachView(itemFragment);
    }

}
