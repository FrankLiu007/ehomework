package name.wanghwx.ehomework.presenter.ipresenter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.List;

import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.iactivity.IExerciseActivity;

public abstract class IExercisePresenter extends BasePresenter<IExerciseActivity>{

    public abstract void setMistakeBookId(int mistakeBookId);

    public abstract int getTotal();

    public abstract int getNumber();

    public abstract Fragment getItemFragment();

    public abstract boolean getFullscreen();

    public abstract void toggleFullscreen();

    public abstract void toggleDraft();

    public abstract void resizeBoard(Rect rect, List<Rect> excludes);

    public abstract void initTouchHelper(View hostView);

    public abstract int getScroll();

    public abstract int getMaxScroll();

    public abstract void scrollUp();

    public abstract void scrollDown();

    public abstract Bitmap getBitmap();

    public abstract Matrix getMatrix();

    public abstract void refreshTouchHelper();

    public abstract void toggleTool(int tool);

    public abstract Fragment getToolFragment();

    public abstract void abandonItem();

    public abstract void previousItem();

    public abstract void nextItem();

    public abstract void closeTouchHelper();

    public abstract void setCourseId(int courseId);

    public abstract void enableTouchHelper();

    public abstract void disableTouchHelper();

    public abstract void initNumber(int number);

    public abstract void scrollTo(int progress);

    public abstract int getTool();

    public abstract void enableSlide(int initialY);

    public abstract void disableSlide();

    public abstract void slide(int y);

    public abstract String getReference();

    public abstract void undo();

    public abstract void redo();

    public abstract boolean getShowDraft();

    public abstract void toggleShowReference();

    public abstract boolean getShowReference();

}