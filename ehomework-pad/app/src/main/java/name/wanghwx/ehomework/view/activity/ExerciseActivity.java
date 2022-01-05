package name.wanghwx.ehomework.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.common.widget.VerticalSeekBar;
import name.wanghwx.ehomework.presenter.ExercisePresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IExercisePresenter;
import name.wanghwx.ehomework.view.base.BaseActivity;
import name.wanghwx.ehomework.view.iactivity.IExerciseActivity;

public class ExerciseActivity extends BaseActivity<IExerciseActivity, IExercisePresenter> implements IExerciseActivity{

    @BindView(R.id.rl_root)
    RelativeLayout rootRelativeLayout;
    @BindView(R.id.rl_header)
    RelativeLayout headerRelativeLayout;
    @BindView(R.id.icon_backward)
    TextView backwardIcon;
    @BindView(R.id.ll_score)
    LinearLayout scoreLinearLayout;
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
    @BindView(R.id.rl_audio)
    RelativeLayout audioRelativeLayout;
    @BindView(R.id.icon_audio)
    TextView audioIcon;
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
    RelativeLayout toggleRelativeLayout;
    @BindView(R.id.tv_draft)
    TextView toggleTextView;
    @BindView(R.id.icon_draft)
    TextView toggleIcon;
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

    @BindString(R.string.item)
    String item;
    @BindString(R.string.icon_recycle)
    String iconRecycle;
    @BindString(R.string.icon_fullscreen_enter)
    String iconFullscreenEnter;
    @BindString(R.string.icon_fullscreen_exit)
    String iconFullscreenExit;
    @BindString(R.string.answer_board)
    String answerBoard;
    @BindString(R.string.draft_board)
    String draftBoard;
    @BindString(R.string.icon_answer)
    String iconAnswer;
    @BindString(R.string.icon_draft)
    String iconDraft;
    @BindString(R.string.show_reference)
    String showReference;
    @BindString(R.string.hide_reference)
    String hideReference;
    private Thread drawThread;

    @Override
    protected IExercisePresenter createPresenter(){
        return new ExercisePresenter(this);
    }

    @Override
    protected int getResourceId(){
        return R.layout.activity_homework;
    }

    @Override
    protected void initView(){
        scoreLinearLayout.setVisibility(View.GONE);
        commentRelativeLayout.setVisibility(View.GONE);
        audioRelativeLayout.setVisibility(View.GONE);
        submitIcon.setVisibility(View.GONE);
        collectIcon.setText(iconRecycle);
        penRelativeLayout.setTag(Category.TOOL_PEN);
        eraserRelativeLayout.setTag(Category.TOOL_ERASER);
        EpdController.setWebViewContrastOptimize(referenceWebView,false);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Log.e(getClass().getName(),"未接收到传入数据");
            finish();
        }else{
            presenter.setMistakeBookId(bundle.getInt(MapKey.MISTAKE_BOOK_ID));
            presenter.setCourseId(bundle.getInt(MapKey.COURSE_ID));
            presenter.initNumber(bundle.getInt(MapKey.NUMBER));
            presenter.initTouchHelper(writeSurfaceView);
        }
    }

    @OnClick(R.id.icon_backward)
    public void backward(View view){
        finish();
    }

    @OnClick(R.id.icon_previous)
    public void previousItem(){
        presenter.previousItem();
    }

    @OnClick(R.id.icon_next)
    public void nextItem(){
        presenter.nextItem();
    }

    @OnClick(R.id.icon_refresh)
    void refresh(){
        renderDraw(false);
    }

    @OnClick(R.id.rl_draft)
    void toggle(){
        presenter.toggleDraft();
    }

    @OnClick({R.id.rl_pen,R.id.rl_eraser})
    public void toggleTool(RelativeLayout relativeLayout){
        presenter.toggleTool((int)relativeLayout.getTag());
    }

    @OnClick(R.id.icon_fullscreen)
    public void toggleFullscreen(){
        presenter.toggleFullscreen();
    }

    @OnClick(R.id.icon_undo)
    void undo(){
        presenter.undo();
    }

    @OnClick(R.id.icon_redo)
    void redo(){
        presenter.redo();
    }

    @OnClick(R.id.tv_reference)
    void toggleShowReference(){
        presenter.toggleShowReference();
    }

    @Override
    public void renderShowReference(){
        boolean show = presenter.getShowReference();
        referenceTextView.setText(show?hideReference:showReference);
        referenceWebView.setVisibility(show?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.icon_up)
    public void scrollUp(){
        presenter.scrollUp();
    }

    @OnClick(R.id.icon_down)
    public void scrollDown(){
        presenter.scrollDown();
    }

    @Override
    public void renderUndoEnabled(boolean undoEnabled){
        undoIcon.setEnabled(undoEnabled);
    }

    @Override
    public void renderRedoEnabled(boolean redoEnabled){
        redoIcon.setEnabled(redoEnabled);
    }

    @Override
    public void hideScroll(){
        scrollLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void showScroll(){
        scrollLinearLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.sv_write)
    public void hideToolIfVisible(){
        if(toolFrameLayout.getVisibility() == View.VISIBLE){
            hideTool();
        }
    }

    @OnClick(R.id.icon_collect)
    public void abandonItem(){
        presenter.disableTouchHelper();
        new AlertDialog.Builder(this)
                .setMessage("确定要从错题本移除此题吗？")
                .setNegativeButton("取消",(dialog,which)->{})
                .setPositiveButton("确定",(dialog,which)->presenter.abandonItem())
                .setOnDismissListener(dialog->presenter.enableTouchHelper()).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void bindListener(){
        writeSurfaceView.addOnLayoutChangeListener((view,l,t,r,b,ol,ot,or,ob)->{
            if(l!=ol||t!=ot||r!=or||b!=ob){
                resizeBoard();
            }
        });
        scrollLinearLayout.addOnLayoutChangeListener((view,l,t,r,b,ol,ot,or,ob)->{
            if(l!=ol||t!=ot||r!=or||b!=ob){
                resizeBoard();
            }
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
        return Arrays.asList(backwardIcon,previousIcon,nextIcon,refreshIcon,commentIcon,audioIcon,collectIcon,toggleIcon,penIcon,eraserIcon,fullscreenIcon,undoIcon,redoIcon,upIcon,downIcon);
    }

    @Override
    protected void refreshView(){
        refreshScreen();
    }

    @Override
    public void disablePrevious(){
        previousIcon.setEnabled(false);
    }

    @Override
    public void disableNext(){
        nextIcon.setEnabled(false);
    }

    @Override
    public void enablePrevious(){
        previousIcon.setEnabled(true);
    }

    @Override
    public void enableNext(){
        nextIcon.setEnabled(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void renderItem(){
        referenceWebView.getSettings().setJavaScriptEnabled(true);
        referenceWebView.loadUrl(Directory.REFERENCE_HTML);
        String reference = presenter.getReference();
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
        itemTextView.setText(String.format(item,presenter.getNumber(),presenter.getTotal()));
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_item,presenter.getItemFragment()).commitAllowingStateLoss();
    }

    @Override
    public void renderFullscreen(){
        if(presenter.getFullscreen()){
            fullscreenIcon.setText(iconFullscreenExit);
            headerRelativeLayout.setVisibility(View.GONE);
            itemFrameLayout.setVisibility(View.GONE);
            dividerRelativeLayout.setVisibility(View.GONE);
            scrollLinearLayout.setVisibility(View.GONE);
        }else{
            fullscreenIcon.setText(iconFullscreenEnter);
            headerRelativeLayout.setVisibility(View.VISIBLE);
            itemFrameLayout.setVisibility(View.VISIBLE);
            dividerRelativeLayout.setVisibility(View.VISIBLE);
            scrollLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderDraft(){
        toggleTextView.setText(presenter.getShowDraft()?"草稿板":"答题板");
    }

    @Override
    public void disableScrollUp() {
        upIcon.setEnabled(false);
    }

    @Override
    public void enableScrollUp() {
        upIcon.setEnabled(true);
    }

    @Override
    public void disableScrollDown() {
        downIcon.setEnabled(false);
    }

    @Override
    public void enableScrollDown() {
        downIcon.setEnabled(true);
    }

    @Override
    public void renderScroll(){
        scrollVerticalSeekBar.setProgress(presenter.getScroll());
        scrollVerticalSeekBar.setMax(presenter.getMaxScroll());
    }

    @Override
    public void renderDraw(boolean refresh){
        if(drawThread != null) drawThread.interrupt();
        drawThread = new Thread(()->{
            draw();
            if(refresh) refreshScreen();
        });
        drawThread.start();
    }

    private void draw(){
        SurfaceHolder surfaceHolder = writeSurfaceView.getHolder();
        Canvas canvas = surfaceHolder.lockCanvas();
        if(!Thread.interrupted() && canvas != null){
            canvas.drawColor(Color.WHITE);
            Bitmap bitmap = presenter.getBitmap();
            if(bitmap != null) canvas.drawBitmap(bitmap,presenter.getMatrix(),null);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
        presenter.refreshTouchHelper();
    }

    @Override
    public void hideTool(){
        toolFrameLayout.setVisibility(View.GONE);
        ViewParent parent = selectorView.getParent();
        if(parent != null) ((RelativeLayout)parent).removeView(selectorView);
    }

    @Override
    public void showTool(){
        selectorView.setVisibility(View.VISIBLE);
        ViewParent parent = selectorView.getParent();
        if(parent != null) ((RelativeLayout)parent).removeView(selectorView);
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
    public void refreshScreen(){
        EpdController.refreshScreen(rootRelativeLayout,UpdateMode.GC);
    }

    @Override
    protected void onResume(){
        super.onResume();
        presenter.enableTouchHelper();
    }

    @Override
    protected void onPause(){
        presenter.disableTouchHelper();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        presenter.closeTouchHelper();
        super.onDestroy();
    }

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public void nullData(){
        Toast.makeText(getContext(),"错题本暂未收藏题目",Toast.LENGTH_SHORT).show();
        finish();
    }
}