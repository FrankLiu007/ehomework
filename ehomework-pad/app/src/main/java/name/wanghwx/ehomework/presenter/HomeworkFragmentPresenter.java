package name.wanghwx.ehomework.presenter;

import com.onyx.android.sdk.utils.CollectionUtils;

import java.util.List;

import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.HomeworkModel;
import name.wanghwx.ehomework.model.imodel.IHomeworkModel;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.presenter.ipresenter.IHomeworkFragmentPresenter;
import name.wanghwx.ehomework.view.ifragment.IHomeworkFragment;

public class HomeworkFragmentPresenter extends IHomeworkFragmentPresenter{

    private final IHomeworkModel homeworkModel = HomeworkModel.getInstance();

    // 任务形式码
    private int formCode;
    // false-未提交 true-已提交
    private boolean submitted;
    // 学科码
    private int subjectCode;

    // 页码
    private int pageNum = 1;
    // 页容量
    private static final int PAGE_SIZE = 6;
    // 总页码
    private int maxPageNum = 1;
    // 总数量
    private int total;
    // 是否有上一页
    private boolean hasPrevious;
    // 是否有下一页
    private boolean hasNext;

    public HomeworkFragmentPresenter(IHomeworkFragment homeworkFragment){
        attachView(homeworkFragment);
    }

    @Override
    public void previousPage(){
        if(pageNum > 1){
            pageNum--;
            findAndRenderHomework();
        }
    }

    @Override
    public void nextPage(){
        if(pageNum < maxPageNum){
            pageNum++;
            findAndRenderHomework();
        }
    }

    @Override
    public int getTotal(){
        return total;
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
    public void setForm(int formCode){
        this.formCode = formCode;
    }

    @Override
    public void toggleSubmitted(boolean submitted){
        this.submitted = submitted;
        pageNum = 1;
        findAndRenderHomework();
    }

    @Override
    public void toggleSubject(int subjectCode) {
        this.subjectCode = subjectCode;
        pageNum = 1;
        findAndRenderHomework();
    }

    @Override
    public boolean getHasPrevious() {
        return hasPrevious;
    }

    @Override
    public boolean getHasNext() {
        return hasNext;
    }

    @Override
    public void refresh(){
        homeworkModel.findHomeworks(new HttpCallback<List<Homework>>(getView()){
            @Override
            public void success(Result<List<Homework>> result){
                List<Homework> homeworks = result.data();
                if(CollectionUtils.isNullOrEmpty(homeworks)){
                    Object unsubmittedRemindObject = result.get(MapKey.UNSUBMITTED_REMIND);
                    if(unsubmittedRemindObject != null) setUnsubmittedRemind(Integer.parseInt((String)unsubmittedRemindObject));
                    Object submittedRemindObject = result.get(MapKey.SUBMITTED_REMIND);
                    if(submittedRemindObject != null) setSubmittedRemind(Integer.parseInt((String)submittedRemindObject));
                    if(isViewAttached()) getView().nullData();
                }else{
                    Object totalObject = result.get(MapKey.TOTAL);
                    if(totalObject != null) setTotal(Integer.parseInt((String)totalObject));
                    Object maxPageNumObject = result.get(MapKey.MAX_PAGE_NUM);
                    if(maxPageNumObject != null) setMaxPageNum(Integer.parseInt((String)maxPageNumObject));
                    Object hasPreviousObject = result.get(MapKey.HAS_PREVIOUS);
                    if(hasPreviousObject != null) setHasPrevious(Boolean.parseBoolean((String)hasPreviousObject));
                    Object hasNextObject = result.get(MapKey.HAS_NEXT);
                    if(hasNextObject != null) setHasNext(Boolean.parseBoolean((String)hasNextObject));
                    Object unsubmittedRemindObject = result.get(MapKey.UNSUBMITTED_REMIND);
                    if(unsubmittedRemindObject != null) setUnsubmittedRemind(Integer.parseInt((String)unsubmittedRemindObject));
                    Object submittedRemindObject = result.get(MapKey.SUBMITTED_REMIND);
                    if(submittedRemindObject != null) setSubmittedRemind(Integer.parseInt((String)submittedRemindObject));
                    if(isViewAttached()) getView().renderData(homeworks);
                }
                if(isViewAttached()) getView().refreshScreen();
            }
        },formCode,submitted,subjectCode,pageNum,PAGE_SIZE);
    }

    private void findAndRenderHomework(){
        homeworkModel.findHomeworks(new HttpCallback<List<Homework>>(getView()){
            @Override
            public void success(Result<List<Homework>> result){
                List<Homework> homeworks = result.data();
                if(CollectionUtils.isNullOrEmpty(homeworks)){
                    Object unsubmittedRemindObject = result.get(MapKey.UNSUBMITTED_REMIND);
                    if(unsubmittedRemindObject != null) setUnsubmittedRemind(Integer.parseInt((String)unsubmittedRemindObject));
                    Object submittedRemindObject = result.get(MapKey.SUBMITTED_REMIND);
                    if(submittedRemindObject != null) setSubmittedRemind(Integer.parseInt((String)submittedRemindObject));
                    if(isViewAttached()) getView().nullData();
                }else{
                    Object totalObject = result.get(MapKey.TOTAL);
                    if(totalObject != null) setTotal(Integer.parseInt((String)totalObject));
                    Object maxPageNumObject = result.get(MapKey.MAX_PAGE_NUM);
                    if(maxPageNumObject != null) setMaxPageNum(Integer.parseInt((String)maxPageNumObject));
                    Object hasPreviousObject = result.get(MapKey.HAS_PREVIOUS);
                    if(hasPreviousObject != null) setHasPrevious(Boolean.parseBoolean((String)hasPreviousObject));
                    Object hasNextObject = result.get(MapKey.HAS_NEXT);
                    if(hasNextObject != null) setHasNext(Boolean.parseBoolean((String)hasNextObject));
                    Object unsubmittedRemindObject = result.get(MapKey.UNSUBMITTED_REMIND);
                    if(unsubmittedRemindObject != null) setUnsubmittedRemind(Integer.parseInt((String)unsubmittedRemindObject));
                    Object submittedRemindObject = result.get(MapKey.SUBMITTED_REMIND);
                    if(submittedRemindObject != null) setSubmittedRemind(Integer.parseInt((String)submittedRemindObject));
                    if(isViewAttached()) getView().renderData(homeworks);
                }
            }
        },formCode,submitted,subjectCode,pageNum,PAGE_SIZE);
    }

    private void setTotal(int total){
        this.total = total;
        if(isViewAttached()) getView().renderTotal();
    }

    private void setMaxPageNum(int maxPageNum){
        this.maxPageNum = maxPageNum;
        if(isViewAttached()) getView().renderPage();
    }

    private void setHasPrevious(boolean hasPrevious){
        this.hasPrevious = hasPrevious;
        if(isViewAttached()) getView().renderHasPrevious();
    }

    private void setHasNext(boolean hasNext){
        this.hasNext = hasNext;
        if(isViewAttached()) getView().renderHasNext();
    }

    private void setUnsubmittedRemind(int unsubmittedRemind){
        if(isViewAttached()) getView().renderUnsubmittedRemind(unsubmittedRemind);
    }

    private void setSubmittedRemind(int submittedRemind){
        if(isViewAttached()) getView().renderSubmittedRemind(submittedRemind);
    }

}