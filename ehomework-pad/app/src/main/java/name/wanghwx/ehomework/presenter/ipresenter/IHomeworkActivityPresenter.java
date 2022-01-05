package name.wanghwx.ehomework.presenter.ipresenter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.List;

import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.presenter.base.BasePresenter;
import name.wanghwx.ehomework.view.iactivity.IHomeworkActivity;

public abstract class IHomeworkActivityPresenter extends BasePresenter<IHomeworkActivity>{

    public abstract void getHomework(int homeworkId);

    public abstract Homework.Status getStatus();

    public abstract int getNumber();

    public abstract int getTotal();

    public abstract Fragment getItemFragment();

    public abstract void previousItem();

    public abstract void nextItem();

    public abstract void toggleTool(int tool);

    public abstract int getTool();

    public abstract void setShowTool(boolean showTool);

    public abstract Fragment getToolFragment();

    public abstract void toggleGuidance(int guidance);

    public abstract void setFullscreen(boolean fullscreen);

    public abstract boolean getFullscreen();

    public abstract void toggleFullscreen();

    public abstract void setScroll(int scroll);

    public abstract int getScroll();

    public abstract int getMaxScroll();

    public abstract void initTouchHelper(View hostView);

    public abstract void resizeBoard(Rect rect, List<Rect> excludes);

    public abstract void refreshTouchHelper();

    public abstract void scrollUp();

    public abstract void scrollDown();

    public abstract void enableTouchHelper();

    public abstract void disableTouchHelper();

    public abstract void closeTouchHelper();

    public abstract int getUnfinished();

    public abstract void submit();

    public abstract Float getScore();

    public abstract void scrollTo(int progress);

    public abstract boolean isCollected();

    public abstract void toggleCollect();

    public abstract void enableSlide(int initialY);

    public abstract void slide(int y);

    public abstract void disableSlide();

    public abstract boolean hasComment();

    public abstract boolean hasAudio();

    public abstract int getGuidance();

    public abstract Fragment getCommentFragment();

    public abstract Fragment getAudioFragment();

    public abstract String getReference();

    public abstract Bitmap getBitmap();

    public abstract Matrix getMatrix();

    public abstract void save();

    public abstract void restore();

    public abstract void undo();

    public abstract void redo();

    public abstract void toggleShowDraft();

    public abstract boolean getShowDraft();

    public abstract void toggleShowReference();

    public abstract boolean getShowReference();

}