package name.wanghwx.ehomework.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

@SuppressLint("AppCompatCustomView")
public class VerticalSeekBar extends SeekBar{

    private OnActionDownListener onActionDownListener;
    private OnActionMoveListener onActionMoveListener;
    private OnActionUpListener onActionUpListener;

    public void setOnActionDownListener(OnActionDownListener onActionDownListener) {
        this.onActionDownListener = onActionDownListener;
    }

    public void setOnActionMoveListener(OnActionMoveListener onActionMoveListener) {
        this.onActionMoveListener = onActionMoveListener;
    }

    public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
        this.onActionUpListener = onActionUpListener;
    }

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c){
        c.rotate(90);
        c.translate(0,-getWidth());
        super.onDraw(c);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(isEnabled()){
            int progress = (int)(getMax()*event.getY()/getHeight());
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    setProgress(progress);
                    if(onActionDownListener != null) onActionDownListener.down(progress);
                    break;
                case MotionEvent.ACTION_MOVE:
                    setProgress(progress);
                    if(onActionMoveListener != null) onActionMoveListener.move(progress);
                    break;
                case MotionEvent.ACTION_UP:
                    setProgress(progress);
                    if(onActionUpListener != null) onActionUpListener.up(progress);
                    performClick();
                    break;
                default:
                    break;
            }
        }
        return isEnabled();
    }

    @Override
    public boolean performClick(){
        return super.performClick();
    }

    @FunctionalInterface
    public interface OnActionDownListener{
        void down(int progress);
    }

    @FunctionalInterface
    public interface OnActionMoveListener{
        void move(int progress);
    }

    @FunctionalInterface
    public interface OnActionUpListener{
        void up(int progress);
    }
}