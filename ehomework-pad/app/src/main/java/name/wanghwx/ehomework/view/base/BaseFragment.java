package name.wanghwx.ehomework.view.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onyx.android.sdk.utils.CollectionUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.presenter.base.BasePresenter;

public abstract class BaseFragment<V extends BaseView,P extends BasePresenter<V>> extends Fragment{

    protected P presenter;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(getResourceId(), container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = createPresenter();
        initView();
        setIconFont();
        bindListener();
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshView();
    }

    @Override
    public void onDestroy(){
        unbinder.unbind();
        if(presenter != null) presenter.detachView();
        super.onDestroy();
    }

    protected abstract int getResourceId();

    protected abstract P createPresenter();

    protected abstract void initView();

    protected abstract void bindListener();

    protected abstract List<TextView> getIcons();

    protected abstract void refreshView();

    private void setIconFont(){
        List<TextView> icons = getIcons();
        if(getContext() != null && CollectionUtils.isNonBlank(icons)){
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),Directory.ICON_FONT);
            for(TextView icon:icons){
                icon.setTypeface(typeface);
            }
        }
    }

}