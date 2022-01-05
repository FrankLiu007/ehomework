package name.wanghwx.ehomework.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onyx.android.sdk.api.device.epd.EpdController;
import com.onyx.android.sdk.utils.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.common.widget.DrawableTextView;
import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.presenter.MistakeBookActivityPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IMistakeBookActivityPresenter;
import name.wanghwx.ehomework.view.base.BaseActivity;
import name.wanghwx.ehomework.view.iactivity.IMistakeBookActivity;

public class MistakeBookActivity extends BaseActivity<IMistakeBookActivity, IMistakeBookActivityPresenter> implements IMistakeBookActivity{

    @BindView(R.id.icon_course)
    TextView courseIcon;
    @BindView(R.id.tv_name)
    TextView nameTextView;
    @BindView(R.id.dtv_empty)
    DrawableTextView emptyDrawableTextView;
    @BindView(R.id.rl_list)
    RelativeLayout listRelativeLayout;
    @BindView(R.id.wv_list)
    WebView listWebView;
    @BindView(R.id.tv_total)
    TextView totalTextView;
    @BindView(R.id.icon_previous)
    TextView previousIcon;
    @BindView(R.id.tv_page)
    TextView pageTextView;
    @BindView(R.id.icon_next)
    TextView nextIcon;

    @BindString(R.string.total)
    String total;
    @BindString(R.string.page)
    String page;

    @OnClick(R.id.icon_previous)
    void previousPage(){
        presenter.previousPage();
    }

    @OnClick(R.id.icon_next)
    void nextPage(){
        presenter.nextPage();
    }

    @Override
    protected IMistakeBookActivityPresenter createPresenter() {
        return new MistakeBookActivityPresenter(this);
    }

    @Override
    protected int getResourceId(){
        return R.layout.activity_mistake_book;
    }

    @Override
    protected void initView(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) presenter.setMistakeBookId(bundle.getInt(MapKey.MISTAKE_BOOK_ID));
        EpdController.setWebViewContrastOptimize(listWebView,false);
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(courseIcon,previousIcon,nextIcon);
    }

    @Override
    protected void refreshView(){
        presenter.findAndRenderItem();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void render(MistakeBook mistakeBook){
        nameTextView.setText(mistakeBook.getCourse().getName());
        List<Item> items = mistakeBook.getItems();
        if(CollectionUtils.isNullOrEmpty(items)){
            nullData();
        }else{
            emptyDrawableTextView.setVisibility(View.GONE);
            listWebView.setVisibility(View.VISIBLE);
            listWebView.getSettings().setJavaScriptEnabled(true);
            listWebView.loadUrl(Directory.ITEM_LIST_HTML);
            listWebView.addJavascriptInterface(new JSInterface(),MapKey.INTERFACE_ITEM_LIST);
            listWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view,String url) {
                    try{
                        listWebView.evaluateJavascript("javascript:render(\""+ URLEncoder.encode(GsonUtils.toJson(items),"UTF-8").replace("+","%20")+"\")", value->{});
                    }catch(UnsupportedEncodingException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void renderTotal(int total){
        totalTextView.setText(String.format(this.total,total));
    }

    @Override
    public void renderPage(){
        pageTextView.setText(String.format(page,presenter.getPageNum(),presenter.getMaxPageNum()));
    }

    @Override
    public void renderHasPrevious(boolean hasPrevious){
        previousIcon.setEnabled(hasPrevious);
    }

    @Override
    public void renderHasNext(boolean hasNext){
        nextIcon.setEnabled(hasNext);
    }

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public void nullData(){
        emptyDrawableTextView.setVisibility(View.VISIBLE);
        listRelativeLayout.setVisibility(View.GONE);
    }

    public class JSInterface{
        @JavascriptInterface
        public void itemClick(int index){
            Context context = getContext();
            if(context != null){
                context.startActivity(new Intent(context,ExerciseActivity.class)
                        .putExtra(MapKey.MISTAKE_BOOK_ID,presenter.getMistakeBookId())
                        .putExtra(MapKey.NUMBER,presenter.getNumber(index))
                        .putExtra(MapKey.COURSE_ID,presenter.getCourseId()));
            }
        }
    }

}