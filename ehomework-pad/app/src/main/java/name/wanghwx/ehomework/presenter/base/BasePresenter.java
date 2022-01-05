package name.wanghwx.ehomework.presenter.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import name.wanghwx.ehomework.view.base.BaseView;

public abstract class BasePresenter<V extends BaseView>{

    protected Reference<V> viewReference;

    public void attachView(V v) {
        viewReference = new WeakReference<>(v);
    }

    public void detachView() {
        if(viewReference != null){
            viewReference.clear();
            viewReference = null;
        }
    }

    public boolean isViewAttached() {
        return viewReference != null && viewReference.get() != null;
    }

    public V getView(){
        return viewReference.get();
    }

}
