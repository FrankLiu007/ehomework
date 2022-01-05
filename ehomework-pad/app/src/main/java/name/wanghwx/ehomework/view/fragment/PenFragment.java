package name.wanghwx.ehomework.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.PenSetting;
import name.wanghwx.ehomework.common.widget.HorizontalSeekBar;
import name.wanghwx.ehomework.presenter.ipresenter.IPenPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IToolFragment;

public class PenFragment extends BaseFragment<IToolFragment, IPenPresenter> implements IToolFragment{

    public static PenFragment getInstance(PenSetting penSetting){
        PenFragment penFragment = new PenFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapKey.PEN_SETTING,penSetting);
        penFragment.setArguments(bundle);
        return penFragment;
    }

    @BindView(R.id.tv_weight)
    TextView weightTextView;
    @BindView(R.id.tv_decrease)
    TextView decreaseTextView;
    @BindView(R.id.hsb_weight)
    HorizontalSeekBar weightHorizontalSeekBar;
    @BindView(R.id.tv_increase)
    TextView increaseTextView;

    private OnDecreaseClickListener onDecreaseClickListener;
    private OnProgressChangeListener onProgressChangeListener;
    private OnIncreaseClickListener onIncreaseClickListener;

    public void setOnDecreaseClickListener(OnDecreaseClickListener onDecreaseClickListener) {
        this.onDecreaseClickListener = onDecreaseClickListener;
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public void setOnIncreaseClickListener(OnIncreaseClickListener onIncreaseClickListener) {
        this.onIncreaseClickListener = onIncreaseClickListener;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_pen;
    }

    @Override
    protected IPenPresenter createPresenter(){
        return null;
    }

    @Override
    protected void initView(){
        Bundle bundle = getArguments();
        if(bundle!=null){
            PenSetting penSetting = (PenSetting)bundle.getSerializable(MapKey.PEN_SETTING);
            if(penSetting!=null){
                renderPenSetting(penSetting);
            }
        }
    }

    @Override
    protected void bindListener(){
        decreaseTextView.setOnClickListener(view->{
            if(onDecreaseClickListener != null){
                onDecreaseClickListener.decrease();
            }
        });
        weightHorizontalSeekBar.setOnActionUpListener(progress->{
            if(onProgressChangeListener != null){
                onProgressChangeListener.change(progress);
            }
        });
        increaseTextView.setOnClickListener(view->{
            if(onIncreaseClickListener != null){
                onIncreaseClickListener.increase();
            }
        });
    }

    @Override
    protected List<TextView> getIcons() {
        return null;
    }

    @Override
    protected void refreshView() {

    }

    public void renderPenSetting(PenSetting penSetting){
        weightTextView.setText(String.valueOf(penSetting.weight));
        weightHorizontalSeekBar.setProgress(penSetting.weight.intValue());
    }

    public void disableDecrease(){
        decreaseTextView.setEnabled(false);
    }

    public void enableDecrease(){
        decreaseTextView.setEnabled(true);
    }

    public void disableIncrease(){
        increaseTextView.setEnabled(false);
    }

    public void enableIncrease(){
        increaseTextView.setEnabled(true);
    }

    @FunctionalInterface
    public interface OnDecreaseClickListener{
        void decrease();
    }
    @FunctionalInterface
    public interface OnProgressChangeListener{
        void change(int progress);
    }
    @FunctionalInterface
    public interface OnIncreaseClickListener{
        void increase();
    }

}