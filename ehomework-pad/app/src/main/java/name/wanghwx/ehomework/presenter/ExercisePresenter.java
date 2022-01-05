package name.wanghwx.ehomework.presenter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.onyx.android.sdk.pen.RawInputCallback;
import com.onyx.android.sdk.pen.TouchHelper;
import com.onyx.android.sdk.pen.data.TouchPoint;
import com.onyx.android.sdk.pen.data.TouchPointList;
import com.onyx.android.sdk.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.pojo.DisplayItem;
import name.wanghwx.ehomework.common.pojo.DisplayOption;
import name.wanghwx.ehomework.common.pojo.DisplayQuestion;
import name.wanghwx.ehomework.common.pojo.EraserSetting;
import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.PenSetting;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.pojo.Step;
import name.wanghwx.ehomework.common.pojo.Stroke;
import name.wanghwx.ehomework.common.pojo.StrokeBoard;
import name.wanghwx.ehomework.model.MistakeBookModel;
import name.wanghwx.ehomework.model.imodel.IMistakeBookModel;
import name.wanghwx.ehomework.pojo.Answer;
import name.wanghwx.ehomework.pojo.Choice;
import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.pojo.Option;
import name.wanghwx.ehomework.pojo.Question;
import name.wanghwx.ehomework.presenter.ipresenter.IExercisePresenter;
import name.wanghwx.ehomework.view.fragment.EraserFragment;
import name.wanghwx.ehomework.view.fragment.ItemFragment;
import name.wanghwx.ehomework.view.fragment.PenFragment;
import name.wanghwx.ehomework.view.iactivity.IExerciseActivity;

public class ExercisePresenter extends IExercisePresenter{

    private IMistakeBookModel mistakeBookModel = MistakeBookModel.getInstance();

    private int mistakeBookId;
    private int courseId;
    private List<Item> items;
    private int number;

    private TouchHelper touchHelper;
    private Stroke currentStroke;

    private List<StrokeBoard> answerBoards = new ArrayList<>();
    private List<StrokeBoard> draftBoards = new ArrayList<>();

    private boolean fullscreen;
    private boolean showDraft;
    private boolean showReference;

    private int fixedWidth;

    private int scroll;
    private int maxScroll;

    private boolean showTool;
    private int tool;
    private boolean slide;
    private int initialY;

    private Canvas canvas;
    private Bitmap bitmap;
    private Thread redraw;

    private Stack<Step<Stroke>> undoStack = new Stack<>();
    private Stack<Step<Stroke>> redoStack = new Stack<>();

    private PenSetting penSetting = new PenSetting();
    private EraserSetting eraserSetting = new EraserSetting();
    private PenFragment penFragment = PenFragment.getInstance(penSetting);
    private EraserFragment eraserFragment = EraserFragment.getInstance(eraserSetting);

    private List<DisplayItem> displayItems = new ArrayList<>();
    private List<ItemFragment> itemFragments = new ArrayList<>();

    public ExercisePresenter(IExerciseActivity exerciseActivity){
        attachView(exerciseActivity);
        penFragment.setOnDecreaseClickListener(this::decreasePenWeight);
        penFragment.setOnProgressChangeListener(this::setPenWeight);
        penFragment.setOnIncreaseClickListener(this::increasePenWeight);
        eraserFragment.setOnClearClickListener(this::emptyBoard);
    }

    private void decreasePenWeight(){
        penSetting.weight--;
        validatePenWeight();
    }

    private void setPenWeight(Integer progress){
        penSetting.weight = (float)progress;
        validatePenWeight();
    }

    private void increasePenWeight(){
        penSetting.weight++;
        validatePenWeight();
    }

    private void validatePenWeight(){
        if(penSetting.weight <= PenSetting.MIN_WEIGHT){
            penSetting.weight = PenSetting.MIN_WEIGHT;
            penFragment.disableDecrease();
        }else{
            penFragment.enableDecrease();
        }
        if(penSetting.weight >= PenSetting.MAX_WEIGHT){
            penSetting.weight = PenSetting.MAX_WEIGHT;
            penFragment.disableIncrease();
        }else{
            penFragment.enableIncrease();
        }
        touchHelper.setStrokeWidth(penSetting.weight);
        penFragment.renderPenSetting(penSetting);
    }

    // 清空，支持撤销功能
    private void emptyBoard(){
        StrokeBoard strokeBoard = getStrokeBoard();
        if(strokeBoard != null){
            Step<Stroke> step = strokeBoard.clearBoard();
            if(step != null){
                redoStack.clear();
                undoStack.push(step);
                validateUndoRedo();
            }
            redraw();
        }
    }

    @Override
    public void resizeBoard(Rect rect, List<Rect> excludes){
        if(touchHelper != null){
            boolean rawDrawingInputEnabled = touchHelper.isRawDrawingInputEnabled();
            touchHelper.setRawDrawingEnabled(false).setLimitRect(rect,excludes).setRawDrawingEnabled(rawDrawingInputEnabled);
        }
        setFixedWidth(rect.width());
        StrokeBoard strokeBoard = getStrokeBoard();
        if(strokeBoard != null) setMaxScroll(strokeBoard.getFixedHeight()-rect.height());
    }

    private void setFixedWidth(int fixedWidth){
        this.fixedWidth = fixedWidth;
        if(number < draftBoards.size() && getDraftBoard() == null) draftBoards.set(number,new StrokeBoard(fixedWidth));
        if(number < answerBoards.size() && getItem().isSubjective() && getAnswerBoard() == null) answerBoards.set(number,new StrokeBoard(fixedWidth));
    }

    private void setMaxScroll(int maxScroll){
        this.maxScroll = maxScroll;
        validateScroll();
    }

    @Override
    public void setMistakeBookId(int mistakeBookId){
        this.mistakeBookId = mistakeBookId;
        findAndRender();
    }

    private void findAndRender(){
        mistakeBookModel.findItems(new HttpCallback<List<Item>>(getView()){
            @Override
            public void success(Result<List<Item>> result){
                items = result.data();
                load();
            }
        },mistakeBookId);
    }

    private void load(){
        for(Item ignore:items){
            displayItems.add(null);
            itemFragments.add(null);
            answerBoards.add(null);
            draftBoards.add(null);
        }
        setNumber(number);
    }

    private void setNumber(int number){
        this.number = number;
        validateNumber();
        loadItem();
    }

    private void validateNumber(){
        if(number <= 0){
            number = 0;
            getView().disablePrevious();
        }else{
            getView().enablePrevious();
        }
        if(number >= getTotal()-1){
            number = getTotal()-1;
            getView().disableNext();
        }else{
            getView().enableNext();
        }
    }

    private void loadItem(){
        if(getDisplayItem() == null){
            displayItems.set(number,transform());
            itemFragments.set(number,ItemFragment.getInstance(getDisplayItem(),this::refreshTouchHelper));
        }
        resetParams();
        getView().renderItem();
        getView().renderDraw(true);
        mistakeBookModel.clearRemind(new HttpCallback<Object>(getView()){
            @Override
            public void success(Result<Object> result){}
        },mistakeBookId,getItem().getItemId());
    }

    private DisplayItem getDisplayItem(){
        return displayItems.get(number);
    }

    private DisplayItem transform(){
        Item item = getItem();
        DisplayItem displayItem = new DisplayItem();
        displayItem.setHomeworkStatus(Category.STATUS_UNSUBMITTED);
        displayItem.setCategory(item.getCategory());
        displayItem.setTitle(item.getTitle());
        displayItem.setReference(item.getReference());
        Answer answer = item.getAnswer();
        List<Question> questions = item.getQuestions();
        if(questions != null){
            List<DisplayQuestion> displayQuestions = new ArrayList<>();
            for(Question question:questions){
                DisplayQuestion displayQuestion = new DisplayQuestion();
                displayQuestion.setNumber(question.getNumber());
                displayQuestion.setType(question.getType().getCode());
                displayQuestion.setStem(question.getStem());
                displayQuestion.setSolution(question.getSolution());
                if(answer != null){
                    List<Choice> choices = answer.getChoices();
                    if(choices != null){
                        for(Choice choice:choices){
                            if(question.getQuestionId().equals(choice.getQuestion().getQuestionId())){
                                displayQuestion.setChoice(choice.getContent());
                            }
                        }
                    }
                }
                List<Option> options = question.getOptions();
                if(options != null){
                    List<DisplayOption> displayOptions = new ArrayList<>();
                    for(Option option: options){
                        DisplayOption displayOption = new DisplayOption();
                        displayOption.setLabel(option.getLabel());
                        displayOption.setContent(option.getContent());
                        displayOptions.add(displayOption);
                    }
                    displayQuestion.setOptions(displayOptions);
                }
                displayQuestions.add(displayQuestion);
            }
            displayItem.setQuestions(displayQuestions);
        }
        return displayItem;
    }

    private Item getItem(){
        return items.get(number);
    }

    private void resetParams(){
        setShowTool(false);
        setFullscreen(false);
        setShowDraft(!getItem().isSubjective());
        setShowReference(false);
        setScroll(0);
        StrokeBoard strokeBoard = getStrokeBoard();
        bitmap = strokeBoard == null?StrokeBoard.EMPTY_BOARD.draw():strokeBoard.draw();
        canvas = new Canvas(bitmap);
    }

    @Override
    public int getTotal(){
        return items.size();
    }

    @Override
    public int getNumber(){
        return number+1;
    }

    @Override
    public ItemFragment getItemFragment(){
        return itemFragments.get(number);
    }

    @Override
    public void toggleFullscreen(){
        setFullscreen(!fullscreen);
    }

    private void setFullscreen(boolean fullscreen){
        this.fullscreen = fullscreen;
        if(isViewAttached()) getView().renderFullscreen();
    }

    @Override
    public boolean getFullscreen(){
        return fullscreen;
    }

    @Override
    public void toggleDraft(){
        if(getItem().isSubjective()){
            setShowDraft(!showDraft);
            if(isViewAttached()) getView().renderDraw(false);
        }
    }

    private void setShowDraft(boolean showDraft){
        this.showDraft = showDraft;
        if(isViewAttached()) getView().renderDraft();
    }

    @Override
    public void scrollUp(){
        scrollTo(scroll-200);
    }

    @Override
    public void scrollDown(){
        scrollTo(scroll+200);
    }

    @Override
    public void scrollTo(int progress){
        setScroll(progress);
        if(isViewAttached()) getView().renderDraw(true);
    }

    private void setScroll(int scroll){
        this.scroll = scroll;
        validateScroll();
        if(isViewAttached()) getView().renderScroll();
    }

    private void validateScroll(){
        if(maxScroll <= 0){
            maxScroll = 0;
            if(isViewAttached()) getView().hideScroll();
        }else{
            if(isViewAttached()) getView().showScroll();
        }
        if(scroll <= 0){
            scroll = 0;
            if(isViewAttached()) getView().disableScrollUp();
        }else{
            if(isViewAttached()) getView().enableScrollUp();
        }
        if(scroll >= maxScroll){
            scroll = maxScroll;
            if(isViewAttached()) getView().disableScrollDown();
        }else{
            if(isViewAttached()) getView().enableScrollDown();
        }
    }

    @Override
    public int getScroll(){
        return scroll;
    }

    @Override
    public int getMaxScroll(){
        return maxScroll;
    }

    @Override
    public void initTouchHelper(View hostView){
        touchHelper = TouchHelper.create(hostView,new ScribbleCallback()).openRawDrawing().setStrokeStyle(TouchHelper.STROKE_STYLE_BRUSH).setStrokeWidth(penSetting.weight);
    }

    @Override
    public Bitmap getBitmap(){
        return getStrokeBoard()==null?null:getStrokeBoard().draw();
    }

    @Override
    public Matrix getMatrix(){
        Matrix matrix = new Matrix();
        float scale = 1f*fixedWidth/getBitmap().getWidth();
        matrix.postScale(scale,scale);
        matrix.postTranslate(0,-scroll);
        return matrix;
    }

    @Override
    public void refreshTouchHelper(){
        if(touchHelper != null){
            boolean rawDrawingInputEnabled = touchHelper.isRawDrawingInputEnabled();
            touchHelper.setRawDrawingEnabled(false).setRawDrawingEnabled(rawDrawingInputEnabled);
        }
    }

    @Override
    public void toggleTool(int tool){
        if(showTool && this.tool == tool){
            setShowTool(false);
        }else{
            this.tool = tool;
            setShowTool(true);
        }
    }

    private void setShowTool(Boolean showTool){
        this.showTool = showTool;
        if(this.showTool){
            disableTouchHelper();
            if(isViewAttached()) getView().showTool();
        }else{
            if(isViewAttached()) getView().hideTool();
            enableTouchHelper();
        }
    }

    @Override
    public int getTool(){
        return tool;
    }

    @Override
    public void enableSlide(int initialY){
        disableTouchHelper();
        slide = true;
        this.initialY = initialY;
    }

    @Override
    public void disableSlide(){
        if(slide){
            slide = false;
            if(isViewAttached()) getView().refreshScreen();
            enableTouchHelper();
        }
    }

    @Override
    public void slide(int y){
        if(slide) getItemFragment().setHeight(y-initialY);
    }

    @Override
    public String getReference() {
        return getItem().getReference();
    }

    @Override
    public void undo(){
        if(!undoStack.empty()){
            Step<Stroke> step = undoStack.pop();
            redoStack.push(step);
            validateUndoRedo();
            handleStep(step,false);
        }
    }

    @Override
    public void redo(){
        if(!redoStack.empty()){
            Step<Stroke> step = redoStack.pop();
            undoStack.push(step);
            validateUndoRedo();
            handleStep(step,true);
        }
    }

    private void handleStep(Step<Stroke> step,boolean forward){
        StrokeBoard strokeBoard = getStrokeBoard();
        if(strokeBoard != null){
            List<Stroke> extra = step.getExtra();
            List<Stroke> absence = step.getAbsence();
            strokeBoard.addAll(forward?absence:extra);
            strokeBoard.removeAll(forward?extra:absence);
            redraw();
        }
    }

    @Override
    public boolean getShowDraft(){
        return showDraft;
    }

    @Override
    public void toggleShowReference(){
        setShowReference(!showReference);
    }

    private void setShowReference(boolean showReference){
        this.showReference = showReference;
        if(isViewAttached()) getView().renderShowReference();
    }

    @Override
    public boolean getShowReference() {
        return showReference;
    }

    @Override
    public Fragment getToolFragment(){
        switch(tool){
            case Category.TOOL_PEN:
                return penFragment;
            case Category.TOOL_ERASER:
                return eraserFragment;
            default:
                return null;
        }
    }

    @Override
    public void enableTouchHelper(){
        if(touchHelper != null) touchHelper.setRawDrawingEnabled(true).setRawDrawingRenderEnabled(true);
    }

    @Override
    public void disableTouchHelper(){
        if(touchHelper != null) touchHelper.setRawDrawingRenderEnabled(false).setRawDrawingEnabled(false);
    }

    @Override
    public void initNumber(int number){
        this.number = number;
    }

    @Override
    public void abandonItem(){
        mistakeBookModel.abandon(new HttpCallback<Object>(getView()){
            @Override
            public void success(Result<Object> result){
                items.remove(number);
                if(CollectionUtils.isNullOrEmpty(items)){
                    if(isViewAttached()) getView().nullData();
                }else{
                    itemFragments.remove(number);
                    displayItems.remove(number);
                    answerBoards.remove(number);
                    draftBoards.remove(number);
                    setNumber(number);
                }
            }
        },courseId,getItem().getItemId());
    }

    @Override
    public void previousItem(){
        number--;
        validateNumber();
        loadItem();
    }

    @Override
    public void nextItem(){
        number++;
        validateNumber();
        loadItem();
    }

    @Override
    public void closeTouchHelper(){
        if(touchHelper != null) touchHelper.closeRawDrawing();
    }

    @Override
    public void setCourseId(int courseId){
        this.courseId = courseId;
    }

    private void handleStroke(){
        StrokeBoard strokeBoard = getStrokeBoard();
        Stroke stroke = currentStroke;
        Step<Stroke> step = null;
        switch(stroke.getMode()){
            case BRUSH:
                step = strokeBoard.addStroke(stroke,canvas);
                break;
            case STROKE:
                step = strokeBoard.removeByStroke(stroke,canvas);
                break;
            default:
                break;
        }
        if(step != null) saveStep(step);
    }

    private void saveStep(Step<Stroke> step){
        undoStack.push(step);
        redoStack.clear();
        validateUndoRedo();
    }

    private void validateUndoRedo(){
        setUndoEnabled(!undoStack.empty());
        setRedoEnabled(!redoStack.empty());
    }

    private void setUndoEnabled(boolean undoEnabled){
        if(isViewAttached()) getView().renderUndoEnabled(undoEnabled);
    }

    private void setRedoEnabled(boolean redoEnabled){
        if(isViewAttached()) getView().renderRedoEnabled(redoEnabled);
    }

    private StrokeBoard getStrokeBoard(){
        return showDraft?getDraftBoard():getAnswerBoard();
    }

    private StrokeBoard getDraftBoard(){
        return CollectionUtils.isNonBlank(draftBoards) && number < draftBoards.size()?draftBoards.get(number):null;
    }

    private StrokeBoard getAnswerBoard(){
        return CollectionUtils.isNonBlank(answerBoards) && number < answerBoards.size()?answerBoards.get(number):null;
    }

    private void redraw(){
        if(redraw != null) redraw.interrupt();
        redraw = new Thread(()->{
            StrokeBoard strokeBoard = getStrokeBoard();
            if(strokeBoard != null){
                Bitmap bm = strokeBoard.draw();
                if(!Thread.interrupted()){
                    if(bitmap != null) bitmap.recycle();
                    bitmap = bm;
                    canvas = new Canvas(bitmap);
                    if(isViewAttached()) getView().renderDraw(false);
                }
            }
        });
        redraw.start();
    }

    private class ScribbleCallback extends RawInputCallback{
        @Override
        public void onBeginRawDrawing(boolean b, TouchPoint touchPoint){
            currentStroke = new Stroke(penSetting.getMode(),penSetting.weight,scroll,touchPoint);
        }

        @Override
        public void onEndRawDrawing(boolean b, TouchPoint touchPoint){
            currentStroke.setEnd(touchPoint);
            handleStroke();
        }

        @Override
        public void onRawDrawingTouchPointMoveReceived(TouchPoint touchPoint){}

        @Override
        public void onRawDrawingTouchPointListReceived(TouchPointList touchPointList) {
            currentStroke.setPath(touchPointList.getPoints());
        }

        @Override
        public void onBeginRawErasing(boolean b, TouchPoint touchPoint) {
            currentStroke = new Stroke(eraserSetting.getMode(),0f,scroll,touchPoint);
        }

        @Override
        public void onEndRawErasing(boolean b, TouchPoint touchPoint) {
            currentStroke.setEnd(touchPoint);
            handleStroke();
            if(isViewAttached()) getView().renderDraw(false);
            redraw();
        }

        @Override
        public void onRawErasingTouchPointMoveReceived(TouchPoint touchPoint){
        }

        @Override
        public void onRawErasingTouchPointListReceived(TouchPointList touchPointList){
            currentStroke.setPath(touchPointList.getPoints());
        }
    }
}