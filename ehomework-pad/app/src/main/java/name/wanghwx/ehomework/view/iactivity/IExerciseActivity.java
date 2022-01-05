package name.wanghwx.ehomework.view.iactivity;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.view.base.BaseView;

public interface IExerciseActivity extends BaseView,HttpView{

    void disablePrevious();

    void disableNext();

    void enablePrevious();

    void enableNext();

    void renderItem();

    void renderFullscreen();

    void renderDraft();

    void disableScrollUp();

    void enableScrollUp();

    void disableScrollDown();

    void enableScrollDown();

    void renderScroll();

    void renderDraw(boolean refresh);

    void hideTool();

    void showTool();

    void refreshScreen();

    void renderShowReference();

    void renderUndoEnabled(boolean undoEnabled);

    void renderRedoEnabled(boolean redoEnabled);

    void hideScroll();

    void showScroll();
}