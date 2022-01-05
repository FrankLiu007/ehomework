package name.wanghwx.ehomework.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.onyx.android.sdk.pen.RawInputCallback;
import com.onyx.android.sdk.pen.TouchHelper;
import com.onyx.android.sdk.pen.data.TouchPoint;
import com.onyx.android.sdk.pen.data.TouchPointList;
import com.onyx.android.sdk.utils.CollectionUtils;
import com.onyx.android.sdk.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.constant.PreSuf;
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
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.model.HomeworkModel;
import name.wanghwx.ehomework.model.MistakeBookModel;
import name.wanghwx.ehomework.model.imodel.IHomeworkModel;
import name.wanghwx.ehomework.model.imodel.IMistakeBookModel;
import name.wanghwx.ehomework.pojo.Answer;
import name.wanghwx.ehomework.pojo.Choice;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.pojo.Option;
import name.wanghwx.ehomework.pojo.Question;
import name.wanghwx.ehomework.pojo.Review;
import name.wanghwx.ehomework.presenter.ipresenter.IHomeworkActivityPresenter;
import name.wanghwx.ehomework.view.fragment.AudioFragment;
import name.wanghwx.ehomework.view.fragment.CommentFragment;
import name.wanghwx.ehomework.view.fragment.EraserFragment;
import name.wanghwx.ehomework.view.fragment.ItemFragment;
import name.wanghwx.ehomework.view.fragment.PenFragment;
import name.wanghwx.ehomework.view.iactivity.IHomeworkActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HomeworkActivityPresenter extends IHomeworkActivityPresenter implements Serializable {

    private static final Bundle bundles = new Bundle();
    private Stack<Step<Stroke>> undoStack = new Stack<>();
    private Stack<Step<Stroke>> redoStack = new Stack<>();

    // 作业id
    private int homeworkId;
    // 查询得到的单次作业
    private Homework homework;
    // 题目序号
    private int number;

    private Handler handler = new Handler(message->{
        if(message.what == number){
            if(isViewAttached()) getView().renderDraw(true,false);
            return true;
        }else{
            return false;
        }
    });

    // 工具栏是否显示
    private boolean showTool;
    // 工具栏工具码
    private int tool;

    // 笔触设置
    private PenSetting penSetting = new PenSetting();
    // 橡皮设置
    private EraserSetting eraserSetting = new EraserSetting();

    private PenFragment penFragment = PenFragment.getInstance(penSetting);
    private EraserFragment eraserFragment = EraserFragment.getInstance(eraserSetting);

    // 指导栏是否显示
    private boolean showGuidance;
    // 指导栏指导码
    private int guidance;

    // 是否全屏显示
    private boolean fullscreen;
    // 是否显示草稿
    private boolean showDraft;
    // 是否显示答案
    private boolean showReference;

    // 手写范围定宽
    private int fixedWidth;
    // 当前滚动高度
    private int scroll;
    // 最大滚动高度
    private int maxScroll;

    private TouchHelper touchHelper;
    // 当前笔画
    private Stroke currentStroke;

    // 滑动是否开启
    private boolean slide;
    // 滑动初始高度
    private int initialY;

    // 当前画布
    private Canvas canvas;
    // 当前位图
    private Bitmap bitmap;
    // 重绘线程
    private Thread redraw;

    // 题目展示数据
    private List<DisplayItem> displayItems = new ArrayList<>();
    // 答题区手写板
    private List<StrokeBoard> answerBoards = new ArrayList<>();
    // 草稿区手写板
    private List<StrokeBoard> draftBoards = new ArrayList<>();
    // 题目展示控件
    private List<ItemFragment> itemFragments = new ArrayList<>();
    // 评语展示控件
    private List<CommentFragment> commentFragments = new ArrayList<>();
    // 留言展示控件
    private List<AudioFragment> audioFragments = new ArrayList<>();

    private IHomeworkModel homeworkModel = HomeworkModel.getInstance();
    private IMistakeBookModel mistakeBookModel = MistakeBookModel.getInstance();

    public HomeworkActivityPresenter(IHomeworkActivity homeworkActivity){
        attachView(homeworkActivity);
        penFragment.setOnDecreaseClickListener(this::decreasePenWeight);
        penFragment.setOnProgressChangeListener(this::setPenWeight);
        penFragment.setOnIncreaseClickListener(this::increasePenWeight);
        eraserFragment.setOnClearClickListener(this::emptyBoard);
    }

    private void decreasePenWeight(){
        penSetting.weight--;
        validatePenWeight();
    }

    private void setPenWeight(int progress){
        penSetting.weight = (float)progress;
        validatePenWeight();
    }

    private void increasePenWeight(){
        penSetting.weight++;
        validatePenWeight();
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
        if(touchHelper != null){
            touchHelper.setStrokeWidth(penSetting.weight);
        }
        penFragment.renderPenSetting(penSetting);
    }

    @Override
    public void getHomework(int homeworkId){
        this.homeworkId = homeworkId;
        homeworkModel.getHomework(new HttpCallback<Homework>(getView()){
            @Override
            public void success(Result<Homework> result){
                Homework data = result.data();
                if(data == null){
                    if(isViewAttached()) getView().nullData();
                }else{
                    homework = data;
                    load();
                }
            }
        },this.homeworkId);
    }

    private void load(){
        Bundle bundle = bundles.getBundle(PreSuf.HOMEWORK_PREFIX + homeworkId);
        List<Item> items = homework.getTask().getItems();
        for(int i=0,il=items.size();i<il;i++){
            int itemId = items.get(i).getItemId();
            displayItems.add(null);
            itemFragments.add(null);
            answerBoards.add((bundle != null && bundle.containsKey(PreSuf.ANSWER_PREFIX+itemId))?(StrokeBoard)bundle.getSerializable(PreSuf.ANSWER_PREFIX+itemId):null);
            draftBoards.add((bundle != null && bundle.containsKey(PreSuf.DRAFT_PREFIX+itemId))?(StrokeBoard)bundle.getSerializable(PreSuf.DRAFT_PREFIX+itemId):null);
            commentFragments.add(null);
            audioFragments.add(null);
        }
        if(isViewAttached()) getView().init();
        loadItem();
    }

    private Item getItem(){
        return homework.getTask().getItems().get(number);
    }

    private void loadItem(){
        if(getDisplayItem() == null){
            homeworkModel.getItemInHomework(new HttpCallback<Item>(getView()){
                @Override
                public void success(Result<Item> result){
                    Item item = result.data();
                    if(item != null){
                        homework.getTask().getItems().set(number,item);
                        displayItems.set(number,transform(item));
                        itemFragments.set(number,ItemFragment.getInstance(getDisplayItem(),HomeworkActivityPresenter.this::refreshTouchHelper));
//                        if(item.isSubjective()) answerBoards.set(number,new StrokeBoard());
//                        draftBoards.set(number,new StrokeBoard());
                        if(item.getAnswer() != null && item.getAnswer().getReviewed() != null && item.getAnswer().getReviewed()){
                            Review review = item.getAnswer().getReview();
                            commentFragments.set(number,CommentFragment.getInstance(review==null?null:review.getComment()));
                            audioFragments.set(number,AudioFragment.getInstance(review==null?null:review.getAudio()));
                        }
                        render();
                    }
                }
            },homework.getHomeworkId(),getItem().getItemId());
        }else{
            render();
        }
    }

    private void render(){
        resetParams();
        if(isViewAttached()) getView().renderItem();
        Item item = getItem();
        switch(getStatus()){
            case UNSUBMITTED:
                if(isViewAttached()) getView().renderDraw(true,false);
                break;
            case UNCORRECTED:
                if(item.isSubjective()){
                    if((bitmap = item.getManuscript()) != null){
                        if(isViewAttached()) getView().renderDraw(true,false);
                    }else{
                        new Thread(()->{
                            try{
                                String url = item.getAnswer().getManuscript();
                                Bitmap manuscript = BitmapFactory.decodeStream(new URL(url).openStream());
                                if(manuscript != null){
                                    bitmap = Bitmap.createBitmap(manuscript.getWidth(),manuscript.getHeight(),Bitmap.Config.ARGB_8888);
                                    canvas = new Canvas(bitmap);
                                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                    canvas.drawBitmap(manuscript,0,0,null);
                                    item.setManuscript(bitmap);
                                    Message message = new Message();
                                    message.what = number;
                                    handler.sendMessage(message);
                                }
                            }catch(IOException ignore){}
                        }).start();
                    }
                }
                break;
            case CORRECTED:
                if(item.isSubjective()){
                    if((bitmap = item.getPostil()) != null){
                        if(isViewAttached()) getView().renderDraw(true,false);
                    }else{
                        new Thread(()->{
                            try{
                                String url = item.getAnswer().getReview().getPostil();
                                Bitmap postil = BitmapFactory.decodeStream(new URL(url).openStream());
                                if(postil != null){
                                    bitmap = Bitmap.createBitmap(postil.getWidth(),postil.getHeight(),Bitmap.Config.ARGB_8888);
                                    canvas = new Canvas(bitmap);
                                    canvas.drawColor(Color.TRANSPARENT,PorterDuff.Mode.CLEAR);
                                    canvas.drawBitmap(postil,0,0,null);
                                    item.setPostil(bitmap);
                                    Message message = new Message();
                                    message.what = number;
                                    handler.sendMessage(message);
                                }
                            }catch(IOException ignore){}
                        }).start();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void resetParams(){
        setShowTool(false);
        setShowGuidance(false);
        setFullscreen(false);
        setShowReference(false);
        StrokeBoard strokeBoard = getStrokeBoard();
        bitmap = strokeBoard == null?StrokeBoard.EMPTY_BOARD.draw():strokeBoard.draw();
        canvas = new Canvas(bitmap);
        setShowDraft(!getItem().isSubjective());
        setScroll(0);
    }

    private DisplayItem getDisplayItem(){
        return displayItems.get(number);
    }

    private DisplayItem transform(Item item){
        DisplayItem displayItem = new DisplayItem();
        displayItem.setHomeworkStatus(homework.getStatus().getCode());
        displayItem.setCategory(item.getCategory());
        displayItem.setTitle(item.getTitle());
        displayItem.setReference(item.getReference());
        List<Question> questions = item.getQuestions();
        if(CollectionUtils.isNonBlank(questions)){
            List<DisplayQuestion> displayQuestions = new ArrayList<>();
            for(Question question:questions){
                DisplayQuestion displayQuestion = new DisplayQuestion();
                displayQuestion.setNumber(question.getNumber());
                displayQuestion.setType(question.getType().getCode());
                displayQuestion.setStem(question.getStem());
                displayQuestion.setSolution(question.getSolution());
                switch(homework.getStatus()){
                    case UNSUBMITTED:
                        Bundle bundle = bundles.getBundle(PreSuf.HOMEWORK_PREFIX+homeworkId);
                        String key = PreSuf.ITEM_PREFIX+item.getItemId()+PreSuf.QUESTION_PREFIX+question.getQuestionId();
                        if(bundle != null && bundle.containsKey(key)) displayQuestion.setChoice(bundle.getString(key));
                        break;
                    case UNCORRECTED:
                    case CORRECTED:
                        Answer answer = item.getAnswer();
                        if(answer != null){
                            List<Choice> choices = answer.getChoices();
                            if(CollectionUtils.isNonBlank(choices)){
                                for(Choice choice:choices){
                                    if(question.getQuestionId().equals(choice.getQuestion().getQuestionId())) displayQuestion.setChoice(choice.getContent());
                                }
                            }
                        }
                        break;
                    default:
                        break;
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

    @Override
    public Homework.Status getStatus(){
        return homework==null?null:homework.getStatus();
    }

    @Override
    public int getNumber(){
        return number+1;
    }

    @Override
    public int getTotal(){
        return homework.getTask().getItems().size();
    }

    @Override
    public ItemFragment getItemFragment() {
        return itemFragments.get(number);
    }

    @Override
    public void previousItem(){
        setNumber(--number);
        loadItem();
    }

    @Override
    public void nextItem(){
        setNumber(++number);
        loadItem();
    }

    private void setNumber(int number){
        this.number = number;
        validateNumber();
    }

    private void validateNumber(){
        if(number <= 0){
            number = 0;
            if(isViewAttached()) getView().disablePrevious();
        }else{
            if(isViewAttached()) getView().enablePrevious();
        }
        if(homework != null){
            if(number >= getTotal()-1){
                number = getTotal()-1;
                if(isViewAttached()) getView().disableNext();
            }else{
                if(isViewAttached()) getView().enableNext();
            }
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

    @Override
    public int getTool(){
        return tool;
    }

    @Override
    public void setShowTool(boolean showTool){
        this.showTool = showTool;
        if(this.showTool){
            disableTouchHelper();
            if(isViewAttached()) getView().showTool();
        }else{
            if(isViewAttached()) getView().hideTool();
            enableTouchHelper();
        }
        redraw();
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
    public void toggleGuidance(int guidance){
        if(showGuidance && this.guidance == guidance){
            setShowGuidance(false);
        }else{
            this.guidance = guidance;
            setShowGuidance(true);
        }
    }

    @Override
    public void setFullscreen(boolean fullscreen){
        this.fullscreen = fullscreen;
        setShowTool(false);
        if(isViewAttached()) getView().renderFullscreen();
    }

    @Override
    public boolean getFullscreen() {
        return fullscreen;
    }

    @Override
    public void toggleFullscreen(){
        setFullscreen(!fullscreen);
        if(isViewAttached()) getView().renderDraw(true,false);
    }

    @Override
    public void setScroll(int scroll){
        this.scroll = scroll;
        validateScroll();
    }

    @Override
    public int getScroll() {
        return scroll;
    }

    @Override
    public int getMaxScroll() {
        return maxScroll;
    }

    @Override
    public void initTouchHelper(View hostView){
        touchHelper = TouchHelper.create(hostView,new ScribbleCallback())
                .openRawDrawing()
                .setStrokeStyle(TouchHelper.STROKE_STYLE_BRUSH)
                .setStrokeWidth(penSetting.weight);
//        // 2.2.1版本需开启
//        EpdController.setViewDefaultUpdateMode(hostView, UpdateMode.HAND_WRITING_REPAINT_MODE);
    }

    @Override
    public void resizeBoard(Rect rect, List<Rect> excludes){
        fixedWidth = rect.width();
        if(touchHelper != null){
            boolean rawDrawingInputEnabled = touchHelper.isRawDrawingInputEnabled();
            touchHelper.setRawDrawingEnabled(false).setLimitRect(rect,excludes).setRawDrawingEnabled(rawDrawingInputEnabled);
        }
        Homework.Status status = getStatus();
        if(status != null){
            switch(getStatus()){
                case UNSUBMITTED:
                    if(getDraftBoard() == null) draftBoards.set(number,new StrokeBoard(fixedWidth));
                    if(getItem().isSubjective() && getAnswerBoard() == null) answerBoards.set(number,new StrokeBoard(rect.width()));
                    maxScroll = getStrokeBoard().getFixedHeight() - rect.height();
                    validateScroll();
                    break;
                case UNCORRECTED:
                case CORRECTED:
                    Bitmap bitmap = getBitmap();
                    if(bitmap != null) maxScroll = rect.width()*bitmap.getHeight()/bitmap.getWidth() - rect.height();
                    validateScroll();
                default:
                    break;
            }
        }
    }

    @Override
    public void refreshTouchHelper() {
        if(touchHelper != null){
            boolean rawDrawingInputEnabled = touchHelper.isRawDrawingInputEnabled();
            touchHelper.setRawDrawingEnabled(false).setRawDrawingEnabled(rawDrawingInputEnabled);
        }
    }

    @Override
    public void scrollUp(){
        scroll-=200;
        validateScroll();
        if(isViewAttached()) getView().renderDraw(true,false);
    }

    @Override
    public void scrollDown(){
        scroll+=200;
        validateScroll();
        if(isViewAttached()) getView().renderDraw(true,false);
    }

    @Override
    public void enableTouchHelper(){
        if(touchHelper != null) touchHelper.setRawDrawingEnabled(true);
    }

    @Override
    public void disableTouchHelper(){
        if(touchHelper != null) touchHelper.setRawDrawingEnabled(false);
    }

    @Override
    public void closeTouchHelper(){
        if(touchHelper != null) touchHelper.closeRawDrawing();
    }

    @Override
    public int getUnfinished(){
        int unfinished = 0;
        List<Item> items = homework.getTask().getItems();
        for(int i=0,l=items.size();i<l;i++){
            DisplayItem displayItem = displayItems.get(i);
            StrokeBoard strokeBoard = answerBoards.get(i);
            if(displayItem == null){
                unfinished++;
            }else{
                for(DisplayQuestion displayQuestion:displayItem.getQuestions()){
                    if(displayQuestion.getType() == Category.QUESTION_GENERAL){
                        if(strokeBoard == null || strokeBoard.isEmpty()){
                            unfinished++;
                            break;
                        }
                    }else{
                        if(StringUtils.isBlank(displayQuestion.getChoice())){
                            unfinished++;
                            break;
                        }
                    }
                }
            }
        }
        return unfinished;
    }

    @Override
    public void submit(){
        List<Item> items = homework.getTask().getItems();
        Item item;
        int itemId;
        DisplayItem displayItem;
        List<Question> questions;
        List<DisplayQuestion> displayQuestions;
        Question question;
        DisplayQuestion displayQuestion;
        StrokeBoard strokeBoard;
        String filename;
        File file;
        Map<String,String> objectiveMap = new HashMap<>();
        Map<String,RequestBody> subjectiveMap = new HashMap<>();
        List<File> files = new ArrayList<>();
        for(int i=0,il=items.size();i<il;i++){
            item = items.get(i);
            if(item != null){
                itemId = item.getItemId();
                // 客观题答案
                questions = item.getQuestions();
                displayItem = CollectionUtils.isNonBlank(displayItems) && i<displayItems.size()?displayItems.get(i):null;
                displayQuestions = displayItem == null?null:displayItem.getQuestions();
                if(CollectionUtils.isNonBlank(questions)){
                    for(int j=0,jl=questions.size();j<jl;j++){
                        question = questions.get(j);
                        if(question != null){
                            Question.Type type = question.getType();
                            if(type == Question.Type.SINGLE || type == Question.Type.MULTIPLE){
                                displayQuestion = CollectionUtils.isNonBlank(displayQuestions) && j<displayQuestions.size()?displayQuestions.get(j):null;
                                if(displayQuestion != null) objectiveMap.put(PreSuf.ITEM_PREFIX+itemId+PreSuf.QUESTION_PREFIX+question.getQuestionId(),displayQuestion.getChoice());
                            }
                        }
                    }
                }
                // 主观题答案
                if(item.isSubjective() && CollectionUtils.isNonBlank(answerBoards) && i<answerBoards.size()){
                    strokeBoard = answerBoards.get(i);
                    filename = PreSuf.HOMEWORK_PREFIX+homeworkId+PreSuf.ITEM_PREFIX+itemId+PreSuf.PNG_SUFFIX;
                    file = strokeBoard == null?StrokeBoard.EMPTY_BOARD.saveAs(filename):strokeBoard.saveAs(filename);
                    if(file != null && file.isFile()){
                        subjectiveMap.put("manuscripts\"; filename=\""+filename,RequestBody.create(MediaType.parse("image/png"),file));
                        files.add(file);
                    }
                }
            }
        }
        homeworkModel.submitHomework(new HttpCallback<Homework>(getView()){
            @Override
            public void success(Result<Homework> result){
                for(File file:files){
                    if(file.isFile() && file.delete()) Log.d(getClass().getName(),"已删除文件"+file.getName());
                }
                bundles.remove(PreSuf.HOMEWORK_PREFIX + homeworkId);
                Object wrongObject = result.get(MapKey.WRONG);
                int wrong = wrongObject == null?0:Integer.parseInt((String)wrongObject);
                setNumber(0);
                if(isViewAttached()) getView().submitSuccess(wrong);
            }
        },homework.getHomeworkId(),GsonUtils.toJson(objectiveMap),subjectiveMap);
    }

    @Override
    public Float getScore(){
        Review review = getItem().getAnswer().getReview();
        return review==null?null:review.getScore();
    }

    @Override
    public void scrollTo(int progress){
        setScroll(progress);
        if(isViewAttached()) getView().renderDraw(true,false);
    }

    @Override
    public boolean isCollected(){
        return getItem().isCollected();
    }

    @Override
    public void toggleCollect(){
        Integer courseId = homework.getTask().getCourse().getCourseId();
        Integer itemId = getItem().getItemId();
        if(isCollected()){
            mistakeBookModel.abandon(new HttpCallback<Object>(getView()){
                @Override
                public void success(Result<Object> result){
                    getItem().setCollected(false);
                    if(isViewAttached()) getView().renderCollect();
                    if(isViewAttached()) getView().showResult(result);
                }
            }, courseId, itemId);
        }else{
            mistakeBookModel.collect(new HttpCallback<MistakeBook>(getView()){
                @Override
                public void success(Result<MistakeBook> result){
                    getItem().setCollected(true);
                    if(isViewAttached()) getView().renderCollect();
                    if(isViewAttached()) getView().showResult(result);
                }
            }, courseId, itemId);
        }
    }

    @Override
    public void enableSlide(int initialY){
        disableTouchHelper();
        this.initialY = initialY;
        slide = true;
    }

    @Override
    public void slide(int y){
        if(slide){
            getItemFragment().setHeight(y-initialY);
        }
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
    public boolean hasComment(){
        Review review = getItem().getAnswer().getReview();
        return review != null && StringUtils.isNotBlank(review.getComment());
    }

    @Override
    public boolean hasAudio(){
        Review review = getItem().getAnswer().getReview();
        return review != null && StringUtils.isNotBlank(review.getAudio());
    }

    @Override
    public int getGuidance(){
        return guidance;
    }

    @Override
    public Fragment getCommentFragment(){
        return commentFragments.get(number);
    }

    @Override
    public Fragment getAudioFragment(){
        return audioFragments.get(number);
    }

    @Override
    public String getReference(){
        return getItem().getReference();
    }

    @Override
    public Bitmap getBitmap(){
        return bitmap;
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
    public void save(){
        if(homework != null){
            String key = PreSuf.HOMEWORK_PREFIX+homeworkId;
            Bundle bundle = bundles.getBundle(key);
            if(bundle == null){
                bundle = new Bundle();
                bundles.putBundle(key,bundle);
            }
            bundle.putInt(MapKey.NUMBER,number);
            List<Item> items = homework.getTask().getItems();
            if(CollectionUtils.isNonBlank(items)){
                for(int i=0,il=items.size();i<il;i++){
                    Item item = items.get(i);
                    DisplayItem displayItem = displayItems.get(i);
                    int itemId = item.getItemId();
                    StrokeBoard strokeBoard;
                    if(i<answerBoards.size()&&(strokeBoard = answerBoards.get(i))!=null) bundle.putSerializable(PreSuf.ANSWER_PREFIX+itemId,strokeBoard);
                    if(i<draftBoards.size()&&(strokeBoard = draftBoards.get(i))!=null) bundle.putSerializable(PreSuf.DRAFT_PREFIX+itemId,strokeBoard);
                    List<Question> questions = item.getQuestions();
                    if(CollectionUtils.isNonBlank(questions)){
                        for(int j=0,jl=questions.size();j<jl;j++){
                            Question question = questions.get(j);
                            if(question!=null && question.getType()!=Question.Type.GENERAL){
                                int questionId = question.getQuestionId();
                                if(displayItem != null){
                                    List<DisplayQuestion> displayQuestions = displayItem.getQuestions();
                                    if(CollectionUtils.isNonBlank(displayQuestions) && j<displayQuestions.size()){
                                        DisplayQuestion displayQuestion = displayQuestions.get(j);
                                        String choice = displayQuestion == null?null:displayQuestion.getChoice();
                                        bundle.putString(PreSuf.ITEM_PREFIX+itemId+PreSuf.QUESTION_PREFIX+questionId,choice);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void toggleShowDraft(){
        if(getItem().isSubjective()) setShowDraft(!showDraft);
    }

    private void setShowDraft(boolean showDraft){
        this.showDraft = showDraft;
        if(isViewAttached()) getView().renderShowDraft();
        this.showTool = false;
        if(isViewAttached()) getView().hideTool();
        undoStack.clear();
        redoStack.clear();
        redraw();
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
    public boolean getShowReference(){
        return showReference;
    }

    @Override
    public void restore(){
        Bundle bundle = bundles.getBundle(PreSuf.HOMEWORK_PREFIX+homeworkId);
        if(bundle != null) setNumber(bundle.getInt(MapKey.NUMBER));
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

    private void setShowGuidance(boolean showGuidance){
        this.showGuidance = showGuidance;
        if(this.showGuidance){
            disableTouchHelper();
            if(isViewAttached()) getView().showGuidance();
        }else{
            if(isViewAttached()) getView().hideGuidance();
            enableTouchHelper();
        }
    }

    private StrokeBoard getStrokeBoard(){
        return showDraft?getDraftBoard():getAnswerBoard();
    }

    private StrokeBoard getAnswerBoard(){
        return answerBoards.get(number);
    }

    private StrokeBoard getDraftBoard(){
        return draftBoards.get(number);
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
        if(isViewAttached()) getView().renderScroll();
    }

    private void handleStroke(){
        Stroke stroke = currentStroke;
        StrokeBoard strokeBoard = getStrokeBoard();
        Step<Stroke> step = null;
        switch(stroke.getMode()){
            case BRUSH:
                step = strokeBoard.addStroke(stroke,canvas);
                if(isViewAttached()) getView().renderDraw(false,false);
                break;
            case MOVE:
                step = strokeBoard.moveErase(stroke,canvas);
                if(isViewAttached()) getView().renderDraw(true,false);
                break;
            case STROKE:
                step = strokeBoard.removeByStroke(stroke,canvas);
                if(isViewAttached()) getView().renderDraw(true,false);
                redraw();
                break;
            default:
                break;
        }
        if(step != null){
            redoStack.clear();
            undoStack.push(step);
            validateUndoRedo();
        }
    }

    private void validateUndoRedo(){
        if(undoStack.empty()){
            if(isViewAttached()) getView().disableUndo();
        }else{
            if(isViewAttached()) getView().enableUndo();
        }
        if(redoStack.empty()){
            if(isViewAttached()) getView().disableRedo();
        }else{
            if(isViewAttached()) getView().enableRedo();
        }
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
                    if(isViewAttached()) getView().renderDraw(true,false);
                }
            }
        });
        redraw.start();
    }

    private class ScribbleCallback extends RawInputCallback{

        // 调用顺序：1
        @Override
        public void onBeginRawDrawing(boolean b, TouchPoint touchPoint){
            currentStroke = new Stroke(penSetting.getMode(),penSetting.weight,scroll,touchPoint);
        }

        // 调用顺序：4
        @Override
        public void onEndRawDrawing(boolean b, TouchPoint touchPoint){
            currentStroke.setEnd(touchPoint);
            handleStroke();
        }

        // 调用顺序：2
        // 移动过程中会多次调用
        @Override
        public void onRawDrawingTouchPointMoveReceived(TouchPoint touchPoint){}

        // 调用顺序：3
        // 接收的点集第一点为起点，最后一点与终点坐标相同，但并非终点
        // 采集点数与接触时间成正比
        @Override
        public void onRawDrawingTouchPointListReceived(TouchPointList touchPointList){
            currentStroke.setPath(touchPointList.getPoints());
        }

        // 调用顺序：1
        @Override
        public void onBeginRawErasing(boolean b,TouchPoint touchPoint){
            currentStroke = new Stroke(eraserSetting.getMode(),eraserSetting.getWidth(),scroll,touchPoint);
        }

        // 调用顺序：4
        @Override
        public void onEndRawErasing(boolean b,TouchPoint touchPoint){
            currentStroke.setEnd(touchPoint);
            handleStroke();
        }

        // 调用顺序：2
        // 移动过程中会多次调用
        @Override
        public void onRawErasingTouchPointMoveReceived(TouchPoint touchPoint){}

        // 调用顺序：3
        // 接收的点集第一点为起点，最后一点与终点坐标相同，但并非终点
        @Override
        public void onRawErasingTouchPointListReceived(TouchPointList touchPointList){
            currentStroke.setPath(touchPointList.getPoints());
        }

    }

}