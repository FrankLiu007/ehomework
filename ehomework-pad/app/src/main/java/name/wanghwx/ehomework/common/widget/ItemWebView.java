package name.wanghwx.ehomework.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import name.wanghwx.ehomework.R;

public class ItemWebView extends WebView{

    private int minHeight;
    private int maxHeight;
    private int contentHeight;
    private int height;

    public ItemWebView(Context context) {
        super(context);
    }

    public ItemWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadCustomAttrs(context.obtainStyledAttributes(attrs,R.styleable.ItemWebView));
    }

    public ItemWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadCustomAttrs(context.obtainStyledAttributes(attrs,R.styleable.ItemWebView,defStyleAttr,0));
    }

    public ItemWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadCustomAttrs(context.obtainStyledAttributes(attrs,R.styleable.ItemWebView,defStyleAttr,defStyleRes));
    }

    private void loadCustomAttrs(TypedArray typedArray){
        for(int i=0,count=typedArray.getIndexCount();i<count;i++){
            int index = typedArray.getIndex(i);
            int value = typedArray.getDimensionPixelSize(index,0);
            switch(index){
                case R.styleable.ItemWebView_min_height:
                    minHeight = value;
                    break;
                case R.styleable.ItemWebView_max_height:
                    maxHeight = value;
                    break;
                case R.styleable.ItemWebView_content_height:
                    contentHeight = value;
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        if(contentHeight > 0){
            int h;
            if(height > 0){
                if(minHeight > 0){
                    if(maxHeight > 0){
                        if(maxHeight > minHeight){
                            if(minHeight > contentHeight){
                                h = contentHeight;
                            }else if(maxHeight > contentHeight){
                                h = range(height,minHeight,contentHeight);
                            }else{
                                h = range(height,minHeight,maxHeight);
                            }
                        }else{
                            h = height>contentHeight?contentHeight:height;
                        }
                    }else{
                        if(minHeight > contentHeight){
                            h = contentHeight;
                        }else{
                            h = range(height,minHeight,contentHeight);
                        }
                    }
                }else{
                    if(maxHeight > 0){
                        if(maxHeight > contentHeight){
                            h = height>contentHeight?contentHeight:height;
                        }else{
                            h = height>maxHeight?maxHeight:height;
                        }
                    }else{
                        h = height>contentHeight?contentHeight:height;
                    }
                }
            }else{
                if(maxHeight > 0){
                    h = contentHeight>maxHeight?maxHeight:contentHeight;
                }else{
                    h = contentHeight;
                }
            }
            setMeasuredDimension(widthMeasureSpec,h);
        }else{
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    public void setMinHeight(int minHeight){
        this.minHeight = minHeight;
        requestLayout();
        invalidate();
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
        invalidate();
    }

    public void setContentHeight(int contentHeight){
        this.contentHeight = contentHeight;
        requestLayout();
        invalidate();
    }

    public void setHeight(int height){
        View parent = (View)getParent();
        int newHeight = height - parent.getPaddingTop() - parent.getPaddingBottom();
        if(newHeight > 0){
            this.height = newHeight;
            requestLayout();
            invalidate();
        }
    }

    private int range(int value,int min,int max){
        if(max > min){
            if(min > value){
                return min;
            }else if(max > value){
                return value;
            }else{
                return max;
            }
        }else{
            return value;
        }
    }

}