package name.wanghwx.ehomework.view.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.EraserSetting;
import name.wanghwx.ehomework.common.pojo.Stroke;
import name.wanghwx.ehomework.common.widget.HorizontalSeekBar;
import name.wanghwx.ehomework.presenter.ipresenter.IEraserPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IToolFragment;

public class EraserFragment extends BaseFragment<IToolFragment,IEraserPresenter> implements IToolFragment{

    @BindView(R.id.ll_move)
    LinearLayout moveLinearLayout;
    @BindView(R.id.ll_stroke)
    LinearLayout strokeLinearLayout;
    @BindView(R.id.icon_stroke)
    TextView strokeIcon;
    @BindView(R.id.ll_lasso)
    LinearLayout lassoLinearLayout;
    @BindView(R.id.icon_lasso)
    TextView lassoIcon;
    @BindView(R.id.ll_clear)
    LinearLayout clearLinearLayout;
    @BindView(R.id.icon_clear)
    TextView clearIcon;
    @BindView(R.id.tv_width)
    TextView widthTextView;
    @BindView(R.id.tv_decrease)
    TextView decreaseTextView;
    @BindView(R.id.hsb_width)
    HorizontalSeekBar widthHorizontalSeekBar;
    @BindView(R.id.tv_increase)
    TextView increaseTextView;

    @BindString(R.string.integer)
    String integer;

    @BindDrawable(R.drawable.bg_grey_r1)
    Drawable bgGreyR1;

    @OnClick({R.id.ll_move,R.id.ll_stroke})
    void switchMode(LinearLayout linearLayout){
        eraserSetting.setMode((Stroke.Mode)linearLayout.getTag());
        render();
    }

    @OnClick(R.id.tv_decrease)
    void decrease(){
        eraserSetting.decreaseWidth();
        render();
    }

    @OnClick(R.id.tv_increase)
    void increase(){
        eraserSetting.increaseWidth();
        render();
    }

    private EraserSetting eraserSetting;

    private OnClearClickListener onClearClickListener;

    public void setOnClearClickListener(OnClearClickListener onClearClickListener){
        this.onClearClickListener = onClearClickListener;
    }

    public static EraserFragment getInstance(EraserSetting eraserSetting){
        EraserFragment eraserFragment = new EraserFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapKey.ERASER_SETTING,eraserSetting);
        eraserFragment.setArguments(bundle);
        return eraserFragment;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_eraser;
    }

    @Override
    protected IEraserPresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(){
        moveLinearLayout.setTag(Stroke.Mode.MOVE);
        strokeLinearLayout.setTag(Stroke.Mode.STROKE);
        lassoLinearLayout.setVisibility(View.INVISIBLE);
        Bundle bundle = getArguments();
        if(bundle != null) eraserSetting = (EraserSetting)bundle.getSerializable(MapKey.ERASER_SETTING);
        if(eraserSetting != null) render();
    }

    @OnClick(R.id.ll_clear)
    void empty(){
        if(onClearClickListener != null) onClearClickListener.clear();
    }

    @Override
    protected void bindListener(){
        widthHorizontalSeekBar.setOnActionUpListener(progress->{
            eraserSetting.setWidth(progress);
            render();
        });
    }

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(strokeIcon,lassoIcon,clearIcon);
    }

    @Override
    protected void refreshView(){

    }

    private void render(){
        moveLinearLayout.setBackground(null);
        strokeLinearLayout.setBackground(null);
        lassoLinearLayout.setBackground(null);
        switch(eraserSetting.getMode()){
            case MOVE:
                moveLinearLayout.setBackground(bgGreyR1);
                break;
            case STROKE:
                strokeLinearLayout.setBackground(bgGreyR1);
                break;
            default:
                break;
        }
        int width = (int)eraserSetting.getWidth();
        widthTextView.setText(String.format(integer,width));
        widthHorizontalSeekBar.setProgress(width);
    }

    @FunctionalInterface
    public interface OnClearClickListener{
        void clear();
    }

}