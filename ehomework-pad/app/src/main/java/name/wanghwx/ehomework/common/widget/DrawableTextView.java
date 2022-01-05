package name.wanghwx.ehomework.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import name.wanghwx.ehomework.R;

@SuppressLint("AppCompatCustomView")
public class DrawableTextView extends TextView{

    private Drawable left;
    private Drawable top;
    private Drawable right;
    private Drawable bottom;

    private int drawableWidth;
    private int drawableHeight;
    private int leftWidth;
    private int leftHeight;
    private int topWidth;
    private int topHeight;
    private int rightWidth;
    private int rightHeight;
    private int bottomWidth;
    private int bottomHeight;

    public DrawableTextView(Context context){
        super(context);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        readCustomAttrs(context.obtainStyledAttributes(attrs,R.styleable.DrawableTextView));

    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        readCustomAttrs(context.obtainStyledAttributes(attrs,R.styleable.DrawableTextView,defStyleAttr,0));
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        readCustomAttrs(context.obtainStyledAttributes(attrs,R.styleable.DrawableTextView,defStyleAttr,defStyleRes));
    }

    private void readCustomAttrs(TypedArray typedArray){
        for(int i=0,count=typedArray.getIndexCount();i<count;i++){
            int index = typedArray.getIndex(i);
            int value = typedArray.getDimensionPixelSize(index,0);
            switch(index){
                case R.styleable.DrawableTextView_drawable_width:
                    drawableWidth = value;
                    break;
                case R.styleable.DrawableTextView_drawable_height:
                    drawableHeight = value;
                    break;
                case R.styleable.DrawableTextView_left_width:
                    leftWidth = value;
                    break;
                case R.styleable.DrawableTextView_left_height:
                    leftHeight = value;
                    break;
                case R.styleable.DrawableTextView_top_width:
                    topWidth = value;
                    break;
                case R.styleable.DrawableTextView_top_height:
                    topHeight = value;
                    break;
                case R.styleable.DrawableTextView_right_width:
                    rightWidth = value;
                    break;
                case R.styleable.DrawableTextView_right_height:
                    rightHeight = value;
                    break;
                case R.styleable.DrawableTextView_bottom_width:
                    bottomWidth = value;
                    break;
                case R.styleable.DrawableTextView_bottom_height:
                    bottomHeight = value;
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        setCompoundDrawablesWithIntrinsicBounds(left,top,right,bottom);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left,@Nullable Drawable top,@Nullable Drawable right,@Nullable Drawable bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        if(left != null){
            left.setBounds(0,0,leftWidth==0?(drawableWidth==0?left.getIntrinsicWidth():drawableWidth):leftWidth,leftHeight==0?(drawableHeight==0?left.getIntrinsicHeight():drawableHeight):leftHeight);
        }
        if(top != null){
            top.setBounds(0,0,topWidth==0?(drawableWidth==0?top.getIntrinsicWidth():drawableWidth):topWidth,topHeight==0?(drawableHeight==0?top.getIntrinsicHeight():drawableHeight):topHeight);
        }
        if(right != null){
            right.setBounds(0,0,rightWidth==0?(drawableWidth==0?right.getIntrinsicWidth():drawableWidth):rightWidth,rightHeight==0?(drawableHeight==0?right.getIntrinsicHeight():drawableHeight):rightHeight);
        }
        if(bottom != null){
            bottom.setBounds(0,0,bottomWidth==0?(drawableWidth==0?bottom.getIntrinsicWidth():drawableWidth):bottomWidth,bottomHeight==0?(drawableHeight==0?bottom.getIntrinsicHeight():drawableHeight):bottomHeight);
        }
        setCompoundDrawables(left,top,right,bottom);
    }

}