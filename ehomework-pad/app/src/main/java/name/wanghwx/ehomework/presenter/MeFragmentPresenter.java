package name.wanghwx.ehomework.presenter;

import android.content.Intent;

import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.StudentModel;
import name.wanghwx.ehomework.model.imodel.IStudentModel;
import name.wanghwx.ehomework.presenter.ipresenter.IMeFragmentPresenter;
import name.wanghwx.ehomework.view.fragment.MeFragment;
import name.wanghwx.ehomework.view.ifragment.IMeFragment;

public class MeFragmentPresenter extends IMeFragmentPresenter{

    private IStudentModel studentModel = StudentModel.getInstance();

    public MeFragmentPresenter(IMeFragment meFragment){
        attachView(meFragment);
    }

    @Override
    public void logout(){
        studentModel.logout(new HttpCallback<Object>(getView()){
            @Override
            public void success(Result<Object> result){
                if(isViewAttached()) getView().logoutSuccess();
            }
        });
    }

}