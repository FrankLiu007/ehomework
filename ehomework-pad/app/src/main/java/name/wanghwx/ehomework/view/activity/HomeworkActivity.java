package name.wanghwx.ehomework.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onyx.android.sdk.api.device.epd.EpdController;
import com.onyx.android.sdk.api.device.epd.UpdateMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.common.widget.VerticalSeekBar;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.presenter.HomeworkActivityPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IHomeworkActivityPresenter;
import name.wanghwx.ehomework.view.base.BaseActivity;
import name.wanghwx.ehomework.view.iactivity.IHomeworkActivity;

public class HomeworkActivity extends BaseActivity<IHomeworkActivity,IHomeworkActivityPresenter> implements IHomeworkActivity{

    @BindView(R.id.rl_root)
    RelativeLayout rootRelativeLayout;
    @BindView(R.id.rl_header)
    RelativeLayout headerRelativeLayout;
    @BindView(R.id.icon_backward)
    TextView backwardIcon;
    @BindView(R.id.ll_score)
    LinearLayout scoreLinearLayout;
    @BindView(R.id.tv_score)
    TextView scoreTextView;
    @BindView(R.id.icon_previous)
    TextView previousIcon;
    @BindView(R.id.tv_item)
    TextView itemTextView;
    @BindView(R.id.icon_next)
    TextView nextIcon;
    @BindView(R.id.icon_refresh)
    TextView refreshIcon;
    @BindView(R.id.rl_comment)
    RelativeLayout commentRelativeLayout;
    @BindView(R.id.icon_comment)
    TextView commentIcon;
    @BindView(R.id.tv_comment)
    TextView commentTextView;
    @BindView(R.id.rl_audio)
    RelativeLayout audioRelativeLayout;
    @BindView(R.id.icon_audio)
    TextView audioIcon;
    @BindView(R.id.tv_audio)
    TextView audioTextView;
    @BindView(R.id.icon_collect)
    TextView collectIcon;
    @BindView(R.id.icon_submit)
    TextView submitIcon;
    @BindView(R.id.fl_item)
    FrameLayout itemFrameLayout;
    @BindView(R.id.rl_divider)
    RelativeLayout dividerRelativeLayout;
    @BindView(R.id.v_slider)
    View sliderView;
    @BindView(R.id.rl_draft)
    RelativeLayout draftRelativeLayout;
    @BindView(R.id.tv_draft)
    TextView draftTextView;
    @BindView(R.id.icon_draft)
    TextView draftIcon;
    @BindView(R.id.rl_pen)
    RelativeLayout penRelativeLayout;
    @BindView(R.id.icon_pen)
    TextView penIcon;
    @BindView(R.id.v_selector)
    View selectorView;
    @BindView(R.id.rl_eraser)
    RelativeLayout eraserRelativeLayout;
    @BindView(R.id.icon_eraser)
    TextView eraserIcon;
    @BindView(R.id.rl_fullscreen)
    RelativeLayout fullscreenRelativeLayout;
    @BindView(R.id.icon_fullscreen)
    TextView fullscreenIcon;
    @BindView(R.id.icon_undo)
    TextView undoIcon;
    @BindView(R.id.icon_redo)
    TextView redoIcon;
    @BindView(R.id.tv_reference)
    TextView referenceTextView;
    @BindView(R.id.sv_write)
    SurfaceView writeSurfaceView;
    @BindView(R.id.ll_scroll)
    LinearLayout scrollLinearLayout;
    @BindView(R.id.icon_up)
    TextView upIcon;
    @BindView(R.id.vsb_scroll)
    VerticalSeekBar scrollVerticalSeekBar;
    @BindView(R.id.icon_down)
    TextView downIcon;
    @BindView(R.id.wv_reference)
    WebView referenceWebView;
    @BindView(R.id.fl_tool)
    FrameLayout toolFrameLayout;
    @BindView(R.id.fl_guidance)
    FrameLayout guidanceFrameLayout;

    private AlertDialog waitDialog;

    @BindString(R.string.icon_collected)
    String iconCollected;
    @BindString(R.string.icon_uncollected)
    String iconUncollected;
    @BindString(R.string.icon_fullscreen_enter)
    String iconFullscreenEnter;
    @BindString(R.string.icon_fullscreen_exit)
    String iconFullscreenExit;
    @BindString(R.string.icon_answer)
    String iconAnswer;
    @BindString(R.string.icon_draft)
    String iconDraft;
    @BindString(R.string.item)
    String item;
    @BindString(R.string.score_value)
    String scoreValue;
    @BindString(R.string.confirm_submit0)
    String confirmSubmit0;
    @BindString(R.string.confirm_submit)
    String confirmSubmit;
    @BindString(R.string.confirm_wrong)
    String confirmWrong;
    @BindString(R.string.answer_board)
    String answerBoard;
    @BindString(R.string.draft_board)
    String draftBoard;
    @BindString(R.string.show_reference)
    String showReference;
    @BindString(R.string.hide_reference)
    String hideReference;
    @BindString(R.string.loading)
    String loading;
    @BindString(R.string.answer)
    String answer;
    @BindString(R.string.integer)
    String integer;

    @BindDrawable(R.drawable.bg_bottom_thick)
    Drawable bgBottomThick;

    private Thread drawThread;

    // 退出
    @OnClick(R.id.icon_backward)
    public void backward(){
        finish();
    }

    // 上一题
    @OnClick(R.id.icon_previous)
    public void previousItem(){
        presenter.previousItem();
    }

    // 下一题
    @OnClick(R.id.icon_next)
    public void nextItem(){
        presenter.nextItem();
    }

    // 刷新
    @OnClick(R.id.icon_refresh)
    void refresh(){
        renderDraw(true,true);
    }

    // 评语、留言
    @OnClick({R.id.rl_comment,R.id.rl_audio})
    void toggleGuidance(RelativeLayout relativeLayout){
        if(presenter.getStatus() == Homework.Status.CORRECTED) presenter.toggleGuidance((int)relativeLayout.getTag());
    }

    // 收藏题目
    @OnClick(R.id.icon_collect)
    public void toggleCollect(){
        Homework.Status status = presenter.getStatus();
        if(status == Homework.Status.UNCORRECTED || status == Homework.Status.CORRECTED) presenter.toggleCollect();
    }

    // 提交作业
    @OnClick(R.id.icon_submit)
    public void submit(){
        if(presenter.getStatus() == Homework.Status.UNSUBMITTED){
            presenter.disableTouchHelper();
            int unfinished = presenter.getUnfinished();
            new AlertDialog.Builder(this)
                    .setMessage(unfinished == 0?confirmSubmit0:String.format(confirmSubmit,unfinished))
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定",(dialog,which)->{
                        waitDialog = new AlertDialog.Builder(this).setMessage("正在提交作业，请稍候...").create();
                        waitDialog.show();
                        presenter.submit();
                    })
                    .setOnDismissListener(dialog->presenter.enableTouchHelper()).show();

        }
    }

    // 切换草稿板
    @OnClick(R.id.rl_draft)
    void toggleShowDraft(){
        presenter.refreshTouchHelper();
        draftRelativeLayout.setEnabled(false);
        if(presenter.getStatus() == Homework.Status.UNSUBMITTED) presenter.toggleShowDraft();
    }

    @Override
    public void renderShowDraft(){
        draftTextView.setText(presenter.getShowDraft()&&presenter.getStatus()==Homework.Status.UNSUBMITTED?draftBoard:answerBoard);
        draftRelativeLayout.setEnabled(true);
    }

    // 切换工具
    @OnClick({R.id.rl_pen,R.id.rl_eraser})
    public void toggleTool(RelativeLayout relativeLayout){
        presenter.toggleTool((int)relativeLayout.getTag());
    }

    // 切换全屏
    @OnClick(R.id.icon_fullscreen)
    public void toggleFullscreen(){
        presenter.toggleFullscreen();
    }

    // 撤销
    @OnClick(R.id.icon_undo)
    public void undo(){
        presenter.undo();
    }

    // 恢复
    @OnClick(R.id.icon_redo)
    public void redo(){
        presenter.redo();
    }

    // 切换答案
    @OnClick(R.id.tv_reference)
    void toggleShowReference(){
        presenter.refreshTouchHelper();
        referenceTextView.setEnabled(false);
        Homework.Status status = presenter.getStatus();
        if(status == Homework.Status.UNCORRECTED || status == Homework.Status.CORRECTED) presenter.toggleShowReference();
    }

    @Override
    public void renderShowReference(){
        boolean showReference = presenter.getShowReference();
        referenceTextView.setText(showReference?hideReference:this.showReference);
        referenceWebView.setVisibility(showReference?View.VISIBLE:View.GONE);
        referenceTextView.setEnabled(true);
    }

    @Override
    public void hideScroll() {
        scrollLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void showScroll() {
        scrollLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void interruptRender(){
        if(drawThread != null) drawThread.interrupt();
    }

    //
    @OnClick(R.id.sv_write)
    public void hideToolIfVisible(View view){
        if(toolFrameLayout.getVisibility() == View.VISIBLE){
            presenter.setShowTool(false);
        }
    }

    //
    @OnClick(R.id.icon_up)
    public void scrollUp(){
        presenter.scrollUp();
    }

    //
    @OnClick(R.id.icon_down)
    public void scrollDown(){
        presenter.scrollDown();
    }

    @Override
    protected int getResourceId(){
        return R.layout.activity_homework;
    }

    @Override
    protected IHomeworkActivityPresenter createPresenter(){
        return new HomeworkActivityPresenter(this);
    }

    @Override
    protected void initView(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) presenter.getHomework(bundle.getInt(MapKey.HOMEWORK_ID));
        penRelativeLayout.setTag(Category.TOOL_PEN);
        eraserRelativeLayout.setTag(Category.TOOL_ERASER);
        commentRelativeLayout.setTag(Category.GUIDANCE_COMMENT);
        audioRelativeLayout.setTag(Category.GUIDANCE_AUDIO);
        EpdController.setWebViewContrastOptimize(referenceWebView,false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void bindListener(){
        writeSurfaceView.addOnLayoutChangeListener((view,l,t,r,b,ol,ot,or,ob)->{
            if(l!=ol||t!=ot||r!=or||b!=ob) resizeBoard();
        });
        scrollLinearLayout.addOnLayoutChangeListener((view,l,t,r,b,ol,ot,or,ob)->{
            if(l!=ol||t!=ot||r!=or||b!=ob) resizeBoard();
        });
        scrollVerticalSeekBar.setOnActionDownListener(progress->presenter.disableTouchHelper());
        scrollVerticalSeekBar.setOnActionUpListener(progress->{
            presenter.scrollTo(progress);
            presenter.enableTouchHelper();
        });
        sliderView.setOnTouchListener((view,event)->{
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                presenter.enableSlide((int)event.getY());
            }
            view.performClick();
            return false;
        });
        rootRelativeLayout.setOnTouchListener((view,event)->{
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                Rect rect = new Rect();
                itemFrameLayout.getGlobalVisibleRect(rect);
                presenter.slide((int)event.getRawY()-rect.top);
            }
            if(event.getAction() == MotionEvent.ACTION_UP){
                presenter.disableSlide();
            }
            return true;
        });
    }

    private void resizeBoard(){
        Rect rect = new Rect();
        writeSurfaceView.getLocalVisibleRect(rect);
        List<Rect> excludes = new ArrayList<>();
        excludes.add(getRelativeRect(writeSurfaceView,scrollLinearLayout));
        presenter.resizeBoard(rect,excludes);
    }

    private Rect getRelativeRect(final View parentView, final View childView){
        if(childView.getVisibility() != View.VISIBLE) return new Rect();
        Rect parentRect = new Rect(),childRect = new Rect();
        parentView.getGlobalVisibleRect(parentRect);
        childView.getGlobalVisibleRect(childRect);
        childRect.offset(-parentRect.left,-parentRect.top);
        return childRect;
    }

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(backwardIcon,previousIcon,nextIcon,refreshIcon,commentIcon,audioIcon,collectIcon,submitIcon,draftIcon,penIcon,eraserIcon,fullscreenIcon,undoIcon,redoIcon,upIcon,downIcon);
    }

    @Override
    protected void refreshView(){}

    @Override
    public void init(){
        switch(presenter.getStatus()){
            case UNSUBMITTED:
                scoreLinearLayout.setVisibility(View.GONE);
                commentRelativeLayout.setVisibility(View.GONE);
                audioRelativeLayout.setVisibility(View.GONE);
                collectIcon.setVisibility(View.GONE);
                referenceTextView.setVisibility(View.GONE);
                presenter.initTouchHelper(writeSurfaceView);
                break;
            case UNCORRECTED:
                scoreLinearLayout.setVisibility(View.GONE);
                commentRelativeLayout.setVisibility(View.GONE);
                audioRelativeLayout.setVisibility(View.GONE);
                submitIcon.setVisibility(View.GONE);
                penRelativeLayout.setVisibility(View.GONE);
                eraserRelativeLayout.setVisibility(View.GONE);
                undoIcon.setVisibility(View.GONE);
                redoIcon.setVisibility(View.GONE);
                break;
            case CORRECTED:
                submitIcon.setVisibility(View.GONE);
                penRelativeLayout.setVisibility(View.GONE);
                eraserRelativeLayout.setVisibility(View.GONE);
                undoIcon.setVisibility(View.GONE);
                redoIcon.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        writeSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder){}
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
                renderDraw(true,false);
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder){}
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void renderItem(){
        if(presenter.getStatus() == Homework.Status.CORRECTED){
            Float score = presenter.getScore();
            if(score != null) scoreTextView.setText(String.format(scoreValue,score));
            commentTextView.setText(String.format(integer,presenter.hasComment()?1:0));
            audioTextView.setText(String.format(integer,presenter.hasAudio()?1:0));
        }
        renderCollect();
        String reference = presenter.getReference();
        referenceWebView.getSettings().setJavaScriptEnabled(true);
        referenceWebView.loadUrl(Directory.REFERENCE_HTML);
        referenceWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                try{
                    referenceWebView.evaluateJavascript("javascript:render(\""+ URLEncoder.encode(GsonUtils.toJson(reference),"UTF-8").replace("+","%20")+"\")", value->{});
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        });
        referenceWebView.setVisibility(presenter.getShowReference()?View.VISIBLE:View.GONE);
        itemTextView.setText(String.format(item,presenter.getNumber(),presenter.getTotal()));
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_item,presenter.getItemFragment()).commitAllowingStateLoss();
    }

    @Override
    public void disablePrevious(){
        previousIcon.setVisibility(View.INVISIBLE);
    }

    @Override
    public void enablePrevious(){
        previousIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableNext() {
        nextIcon.setVisibility(View.INVISIBLE);
    }

    @Override
    public void enableNext() {
        nextIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTool(){
        ViewParent viewParent = selectorView.getParent();
        if(viewParent != null) ((RelativeLayout)viewParent).removeView(selectorView);
        toolFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void showTool(){
        ViewParent viewParent = selectorView.getParent();
        if(viewParent != null) ((RelativeLayout)viewParent).removeView(selectorView);
        switch(presenter.getTool()){
            case Category.TOOL_PEN:
                penRelativeLayout.addView(selectorView);
                break;
            case Category.TOOL_ERASER:
                eraserRelativeLayout.addView(selectorView);
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_tool,presenter.getToolFragment()).commitAllowingStateLoss();
        toolFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderFullscreen(){
        if(presenter.getFullscreen()){
            fullscreenIcon.setText(iconFullscreenExit);
            headerRelativeLayout.setVisibility(View.GONE);
            itemFrameLayout.setVisibility(View.GONE);
            dividerRelativeLayout.setVisibility(View.GONE);
        }else{
            fullscreenIcon.setText(iconFullscreenEnter);
            headerRelativeLayout.setVisibility(View.VISIBLE);
            itemFrameLayout.setVisibility(View.VISIBLE);
            dividerRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disableScrollUp(){
        upIcon.setEnabled(false);
    }

    @Override
    public void enableScrollUp(){
        upIcon.setEnabled(true);
    }

    @Override
    public void disableScrollDown(){
        downIcon.setEnabled(false);
    }

    @Override
    public void enableScrollDown(){
        downIcon.setEnabled(true);
    }

    @Override
    public void renderScroll(){
        scrollVerticalSeekBar.setProgress(presenter.getScroll());
        scrollVerticalSeekBar.setMax(presenter.getMaxScroll());
    }

    @Override
    public void renderCollect(){
        collectIcon.setText(presenter.isCollected()?iconCollected:iconUncollected);
    }

    @Override
    public void refreshScreen(){
        EpdController.refreshScreen(rootRelativeLayout,UpdateMode.GC);
    }

    @Override
    public void submitSuccess(int wrong){
        waitDialog.hide();
        presenter.disableTouchHelper();
        if(wrong > 0){
            new AlertDialog.Builder(this)
                    .setMessage(String.format(confirmWrong,wrong))
                    .setNegativeButton("退出",(dialog,which)->finish())
                    .setPositiveButton("查看",(dialog,which)->{
                        finish();
                        startActivity(getIntent());
                    }).show();
        }else{
            Toast.makeText(this,"提交成功",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void disableUndo(){

    }

    @Override
    public void enableUndo(){

    }

    @Override
    public void disableRedo(){

    }

    @Override
    public void enableRedo(){

    }

    @Override
    public void showGuidance(){
        switch(presenter.getGuidance()){
            case Category.GUIDANCE_COMMENT:
                commentRelativeLayout.setBackground(bgBottomThick);
                audioRelativeLayout.setBackground(null);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_guidance,presenter.getCommentFragment()).commitAllowingStateLoss();
                guidanceFrameLayout.setVisibility(View.VISIBLE);
                break;
            case Category.GUIDANCE_AUDIO:
                commentRelativeLayout.setBackground(null);
                audioRelativeLayout.setBackground(bgBottomThick);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_guidance,presenter.getAudioFragment()).commitAllowingStateLoss();
                guidanceFrameLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void hideGuidance(){
        commentRelativeLayout.setBackground(null);
        audioRelativeLayout.setBackground(null);
        guidanceFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void renderDraw(boolean refreshTouchHelper,boolean refreshScreen){
        if(drawThread != null) drawThread.interrupt();
        drawThread = new Thread(()->{
            SurfaceHolder surfaceHolder = writeSurfaceView.getHolder();
            Canvas canvas = surfaceHolder.lockCanvas();
            if(!Thread.interrupted() && canvas != null){
                canvas.drawColor(Color.WHITE);
                Bitmap bitmap = presenter.getBitmap();
                if(bitmap != null) canvas.drawBitmap(bitmap,presenter.getMatrix(),null);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
            if(refreshTouchHelper) presenter.refreshTouchHelper();
            if(refreshScreen) refreshScreen();
        });
        drawThread.start();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void nullData(){
        Toast.makeText(this,R.string.null_data,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();
        presenter.restore();
        presenter.enableTouchHelper();
    }

    @Override
    protected void onPause(){
        presenter.disableTouchHelper();
        presenter.save();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        presenter.closeTouchHelper();
        super.onDestroy();
    }

}