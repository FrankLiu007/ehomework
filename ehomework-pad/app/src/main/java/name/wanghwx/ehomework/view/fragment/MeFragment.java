package name.wanghwx.ehomework.view.fragment;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.presenter.MeFragmentPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IMeFragmentPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IMeFragment;

public class MeFragment extends BaseFragment<IMeFragment, IMeFragmentPresenter> implements IMeFragment{

    @BindView(R.id.icon_logout)
    TextView logoutIcon;
    @BindView(R.id.rl_logout)
    RelativeLayout logoutRelativeLayout;
    @BindView(R.id.tv_account)
    TextView accountTextView;
    @BindView(R.id.tv_name)
    TextView nameTextView;

    @OnClick(R.id.rl_logout)
    void logout(){
        presenter.logout();
    }

    public static MeFragment getInstance(){
        return new MeFragment();
    }

    @Override
    protected int getResourceId(){
        return R.layout.fragment_me;
    }

    @Override
    protected IMeFragmentPresenter createPresenter() {
        return new MeFragmentPresenter(this);
    }

    @Override
    protected void initView(){
        accountTextView.setText(EhomeworkApplication.getAccount());
        nameTextView.setText(EhomeworkApplication.getName());
    }

    @Override
    protected void bindListener(){

    }

    @Override
    protected List<TextView> getIcons(){
        return Collections.singletonList(logoutIcon);
    }

    @Override
    protected void refreshView() {

    }

    @Override
    public void logoutSuccess(){
        EhomeworkApplication.deleteToken();
        FragmentActivity activity = getActivity();
        if(activity != null) activity.finish();
    }

    @Override
    public void nullData(){}

}