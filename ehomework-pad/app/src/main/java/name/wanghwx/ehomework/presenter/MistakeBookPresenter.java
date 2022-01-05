package name.wanghwx.ehomework.presenter;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.MistakeBookModel;
import name.wanghwx.ehomework.model.imodel.IMistakeBookModel;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.presenter.ipresenter.IMistakeBookPresenter;
import name.wanghwx.ehomework.view.ifragment.IMistakeBookFragment;

public class MistakeBookPresenter extends IMistakeBookPresenter{

    private IMistakeBookModel mistakeBookModel = MistakeBookModel.getInstance();

    public MistakeBookPresenter(IMistakeBookFragment mistakeBookFragment){
        attachView(mistakeBookFragment);
    }

    @Override
    public void findAndRenderMistakeBooks(){
        mistakeBookModel.findMistakeBooks(new HttpCallback<List<MistakeBook>>(getView()){
            @Override
            public void success(Result<List<MistakeBook>> result){
                if(isViewAttached()) getView().renderRecycler(result.data());
            }
        });
    }

}