package name.wanghwx.ehomework.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onyx.android.sdk.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.presenter.ipresenter.ICommentFragmentPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.ICommentFragment;

public class CommentFragment extends BaseFragment<ICommentFragment,ICommentFragmentPresenter> implements ICommentFragment{
    @BindView(R.id.tv_comment)
    TextView commentTextView;
    @BindView(R.id.tv_no_comment)
    TextView noCommentTextView;

    public static CommentFragment getInstance(String comment){
        CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MapKey.COMMENT,comment);
        commentFragment.setArguments(bundle);
        return commentFragment;
    }

    @Override
    protected int getResourceId(){
        return R.layout.fragment_comment;
    }

    @Override
    protected ICommentFragmentPresenter createPresenter(){
        return null;
    }

    @Override
    protected void initView(){
        Bundle bundle = getArguments();
        if(bundle == null){
            commentTextView.setVisibility(View.GONE);
            noCommentTextView.setVisibility(View.VISIBLE);
        }else{
            String comment = bundle.getString(MapKey.COMMENT);
            if(StringUtils.isBlank(comment)){
                commentTextView.setVisibility(View.GONE);
                noCommentTextView.setVisibility(View.VISIBLE);
            }else{
                commentTextView.setText(bundle.getString(MapKey.COMMENT));
                commentTextView.setVisibility(View.VISIBLE);
                noCommentTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return null;
    }

    @Override
    protected void refreshView() {

    }

}