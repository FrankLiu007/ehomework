package name.wanghwx.ehomework.view.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onyx.android.sdk.utils.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.presenter.ipresenter.IAudioFragmentPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IAudioFragment;

public class AudioFragment extends BaseFragment<IAudioFragment, IAudioFragmentPresenter> implements IAudioFragment{
    @BindView(R.id.ll_audio)
    LinearLayout audioLinearLayout;
    @BindView(R.id.icon_voice)
    TextView voiceIcon;
    @BindView(R.id.tv_duration)
    TextView durationTextView;
    @BindView(R.id.tv_no_audio)
    TextView noAudioTextView;

    @BindString(R.string.duration_second)
    String durationSecond;

    private MediaPlayer mediaPlayer;
    private AlphaAnimation blinkAnimation;

    @OnClick(R.id.ll_audio)
    void play(){
        if(!(mediaPlayer == null || mediaPlayer.isPlaying())){
            mediaPlayer.start();
            blinkAnimation.start();
        }
    }

    public static AudioFragment getInstance(String audio){
        AudioFragment audioFragment = new AudioFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MapKey.AUDIO,audio);
        audioFragment.setArguments(bundle);
        return audioFragment;
    }

    @Override
    protected int getResourceId(){
        return R.layout.fragment_audio;
    }

    @Override
    protected IAudioFragmentPresenter createPresenter(){
        return null;
    }

    @Override
    protected void initView(){
        Bundle bundle = getArguments();
        if(bundle == null){
            audioLinearLayout.setVisibility(View.GONE);
            noAudioTextView.setVisibility(View.VISIBLE);
        }else{
            String audio = bundle.getString(MapKey.AUDIO);
            if(StringUtils.isBlank(audio)){
                audioLinearLayout.setVisibility(View.GONE);
                noAudioTextView.setVisibility(View.VISIBLE);
            }else{
                blinkAnimation = new AlphaAnimation(1,0);
                blinkAnimation.setDuration(1500);
                blinkAnimation.setRepeatCount(Animation.INFINITE);
                blinkAnimation.setRepeatMode(Animation.REVERSE);
                voiceIcon.setAnimation(blinkAnimation);
                blinkAnimation.cancel();
                blinkAnimation.reset();
                mediaPlayer = MediaPlayer.create(getContext(),Uri.parse(audio));
                mediaPlayer.setOnPreparedListener(mp->durationTextView.setText(String.format(durationSecond,mediaPlayer.getDuration()/1000)));
                mediaPlayer.setOnCompletionListener(mp->{
                    try{
                        blinkAnimation.cancel();
                        blinkAnimation.reset();
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(audio);
                        mediaPlayer.prepareAsync();
                    }catch(IOException e){
                        Log.e(getClass().getName(),"加载音频文件失败",e);
                    }
                });
                audioLinearLayout.setVisibility(View.VISIBLE);
                noAudioTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return Collections.singletonList(voiceIcon);
    }

    @Override
    protected void refreshView() {

    }

    @Override
    public void onDestroy(){
        if(mediaPlayer != null) mediaPlayer.release();
        super.onDestroy();
    }
}