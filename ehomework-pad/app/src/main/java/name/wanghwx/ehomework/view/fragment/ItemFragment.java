package name.wanghwx.ehomework.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.onyx.android.sdk.api.device.epd.EpdController;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.DisplayItem;
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.common.widget.ItemWebView;
import name.wanghwx.ehomework.presenter.ItemPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IItemPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IItemFragment;

public class ItemFragment extends BaseFragment<IItemFragment,IItemPresenter> implements IItemFragment{

    @BindView(R.id.wv_item)
    ItemWebView itemWebView;

    private DisplayItem displayItem;
    private OnChoiceChangeListener onChoiceChangeListener;
    private OnClickListener onClickListener;

    private Handler handler = new Handler(message->{
        if(message.what == 1){
            itemWebView.setContentHeight((int)message.obj);
        }
        return true;
    });

    public static ItemFragment getInstance(DisplayItem displayItem,OnClickListener onClickListener){
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapKey.DISPLAY_ITEM,displayItem);
        bundle.putSerializable(MapKey.ON_CLICK_LISTENER,onClickListener);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

    @Override
    protected int getResourceId(){
        return R.layout.fragment_item;
    }

    @Override
    protected IItemPresenter createPresenter() {
        return new ItemPresenter(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(){
        Bundle bundle = getArguments();
        if(bundle == null){
            Toast.makeText(getContext(),"题目数据丢失",Toast.LENGTH_LONG).show();
        }else{
            displayItem = (DisplayItem)bundle.getSerializable(MapKey.DISPLAY_ITEM);
            onClickListener = (OnClickListener)bundle.getSerializable(MapKey.ON_CLICK_LISTENER);
            onChoiceChangeListener = (index,choice)->displayItem.getQuestions().get(index).setChoice(choice);
            if(displayItem == null){
                Toast.makeText(getContext(),"题目数据丢失",Toast.LENGTH_LONG).show();
            }else{
                EpdController.setWebViewContrastOptimize(itemWebView,false);
                itemWebView.getSettings().setJavaScriptEnabled(true);
                itemWebView.loadUrl(Directory.ITEM_HTML);
                itemWebView.addJavascriptInterface(new JSInterface(),MapKey.INTERFACE_ITEM);
                itemWebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url){
                        try{
                            if(itemWebView != null) itemWebView.evaluateJavascript("javascript:render(\""+URLEncoder.encode(GsonUtils.toJson(displayItem),"UTF-8").replace("+","%20")+"\")", value->{});
                        }catch(UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                });
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
    protected void refreshView(){}

    @Override
    public void setHeight(int height){
        itemWebView.setHeight(height);
    }

    @FunctionalInterface
    public interface OnClickListener extends Serializable{
        void click();
    }

    @FunctionalInterface
    public interface OnChoiceChangeListener extends Serializable{
        void choiceChange(int index,String choice);
    }

    public class JSInterface{
        @JavascriptInterface
        public void choiceChange(int index,String choice){
            if(onChoiceChangeListener != null){
                onChoiceChangeListener.choiceChange(index,choice);
            }
        }
        @JavascriptInterface
        public void heightChange(int height){
            if(getContext() != null){
                Message message = new Message();
                message.what = 1;
                message.obj = (int)(height*getContext().getResources().getDisplayMetrics().density+0.5);
                handler.sendMessage(message);
            }
        }
        @JavascriptInterface
        public void click(){
            if(onClickListener != null){
                onClickListener.click();
            }
        }
    }
}