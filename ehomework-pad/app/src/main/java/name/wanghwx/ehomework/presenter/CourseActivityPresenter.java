package name.wanghwx.ehomework.presenter;

import java.util.List;

import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.HomeworkModel;
import name.wanghwx.ehomework.model.imodel.IHomeworkModel;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.pojo.Task;
import name.wanghwx.ehomework.presenter.ipresenter.ICourseActivityPresenter;
import name.wanghwx.ehomework.view.iactivity.ICourseActivity;

public class CourseActivityPresenter extends ICourseActivityPresenter{

    private int menu;

    private int courseId;
    private int form;
    private int pageNum;
    private int pageSize = 6;
    private int total;
    private int maxPageNum;

    private IHomeworkModel homeworkModel = HomeworkModel.getInstance();

    public CourseActivityPresenter(ICourseActivity courseActivity){
        attachView(courseActivity);
    }

    @Override
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    @Override
    public void switchForm(int form){
        this.form = form;
        this.pageNum = 1;
        findAndRender();
    }

    private void findAndRender(){
        homeworkModel.findHomeworksByCourse(new HttpCallback<List<Homework>>(getView()){
            @Override
            public void success(Result<List<Homework>> result){
                Object totalObject = result.get(MapKey.TOTAL);
                if(totalObject != null) setTotal(Integer.parseInt((String)totalObject));
                Object maxPageNumObject = result.get(MapKey.MAX_PAGE_NUM);
                if(maxPageNumObject != null) setMaxPageNum(Integer.parseInt((String)maxPageNumObject));
                if(isViewAttached()) getView().render(result.data());
            }
        },courseId,form,pageNum,pageSize);
    }

    private void setTotal(int total){
        this.total = total;
        if(isViewAttached()) getView().renderTotal();
    }

    private void setMaxPageNum(int maxPageNum){
        this.maxPageNum = maxPageNum;
        if(isViewAttached()) getView().renderPage();
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
    public void previousPage(){
        pageNum--;
        validatePageNum();
        findAndRender();
    }

    @Override
    public void nextPage(){
        pageNum++;
        validatePageNum();
        findAndRender();
    }

    @Override
    public void switchMenu(int menu){
        this.menu = menu;
        switch(this.menu){
            case Category.COURSE_MENU_MATERIAL:
                break;
            case Category.COURSE_MENU_HOMEWORK:
                switchForm(Task.Form.ASSIGNMENT.getCode());
                break;
            case Category.COURSE_MENU_TEST:
                switchForm(Task.Form.TEST.getCode());
                break;
            default:
                break;
        }
    }

    private void validatePageNum(){
        if(pageNum <= 1){
            pageNum = 1;
            if(isViewAttached()) getView().disablePreviousPage();
        }else{
            if(isViewAttached()) getView().enablePreviousPage();
        }
        if(pageNum >= maxPageNum){
            pageNum = maxPageNum;
            if(isViewAttached()) getView().disableNextPage();
        }else{
            if(isViewAttached()) getView().enableNextPage();
        }
    }
}