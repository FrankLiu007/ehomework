package name.wanghwx.ehomework.presenter;

import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.MistakeBookModel;
import name.wanghwx.ehomework.model.imodel.IMistakeBookModel;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.presenter.ipresenter.IMistakeBookActivityPresenter;
import name.wanghwx.ehomework.view.iactivity.IMistakeBookActivity;

public class MistakeBookActivityPresenter extends IMistakeBookActivityPresenter{

    private int mistakeBookId;

    private int courseId;
    private int pageNum = 1;
    private static final int PAGE_SIZE = 10;
    private int maxPageNum;

    private IMistakeBookModel mistakeBookModel = MistakeBookModel.getInstance();

    public MistakeBookActivityPresenter(IMistakeBookActivity mistakeBookActivity){
        attachView(mistakeBookActivity);
    }

    @Override
    public void setMistakeBookId(int mistakeBookId){
        this.mistakeBookId = mistakeBookId;
    }

    @Override
    public int getPageNum(){
        return pageNum;
    }

    @Override
    public int getMaxPageNum(){
        return maxPageNum;
    }

    @Override
    public void previousPage(){
        if(pageNum > 1){
            pageNum--;
            findAndRenderItem();
        }
    }

    @Override
    public void nextPage(){
        if(pageNum < maxPageNum){
            pageNum++;
            findAndRenderItem();
        }
    }

    @Override
    public int getNumber(int index) {
        return index+(pageNum-1)*PAGE_SIZE;
    }

    @Override
    public int getMistakeBookId() {
        return mistakeBookId;
    }

    @Override
    public int getCourseId() {
        return courseId;
    }

    @Override
    public void findAndRenderItem(){
        mistakeBookModel.findMistakeBook(new HttpCallback<MistakeBook>(getView()){
            @Override
            public void success(Result<MistakeBook> result){
                Object totalObject = result.get(MapKey.TOTAL);
                if(totalObject != null) setTotal(Integer.parseInt((String)totalObject));
                Object maxPageNumObject = result.get(MapKey.MAX_PAGE_NUM);
                if(maxPageNumObject != null) setMaxPageNum(Integer.parseInt((String)maxPageNumObject));
                Object hasPreviousObject = result.get(MapKey.HAS_PREVIOUS);
                if(hasPreviousObject != null) setHasPrevious(Boolean.parseBoolean((String)hasPreviousObject));
                Object hasNextObject = result.get(MapKey.HAS_NEXT);
                if(hasNextObject != null) setHasNext(Boolean.parseBoolean((String)hasNextObject));
                MistakeBook mistakeBook = result.data();
                courseId = mistakeBook.getCourse().getCourseId();
                if(isViewAttached()) getView().render(mistakeBook);
            }
        },mistakeBookId,pageNum,PAGE_SIZE);
    }

    private void setTotal(int total){
        if(isViewAttached()) getView().renderTotal(total);
    }

    private void setMaxPageNum(int maxPageNum){
        this.maxPageNum = maxPageNum;
        if(isViewAttached()) getView().renderPage();
    }

    private void setHasPrevious(boolean hasPrevious){
        if(isViewAttached()) getView().renderHasPrevious(hasPrevious);
    }

    private void setHasNext(boolean hasNext){
        if(isViewAttached()) getView().renderHasNext(hasNext);
    }

}