package name.wanghwx.ehomework.view.iactivity;

import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.view.base.BaseView;

public interface IHomeworkActivity extends BaseView,HttpView{

    void init();

    void renderItem();

    void disablePrevious();

    void enablePrevious();

    void disableNext();

    void enableNext();

    void hideTool();

    void showTool();

    void renderFullscreen();

    void disableScrollUp();

    void enableScrollUp();

    void disableScrollDown();

    void enableScrollDown();

    void renderScroll();

    void renderDraw(boolean refreshTouchHelper, boolean refreshScreen);

    void showGuidance();

    void hideGuidance();

    void renderCollect();

    void refreshScreen();

    void submitSuccess(int wrong);

    void disableUndo();

    void enableUndo();

    void disableRedo();

    void enableRedo();

    void renderShowDraft();

    void renderShowReference();

    void hideScroll();

    void showScroll();

    void interruptRender();
}