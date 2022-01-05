package name.wanghwx.ehomework.presenter;

import androidx.fragment.app.Fragment;

import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.HttpCallback;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.model.StudentModel;
import name.wanghwx.ehomework.model.imodel.IStudentModel;
import name.wanghwx.ehomework.pojo.Task;
import name.wanghwx.ehomework.presenter.ipresenter.IMainPresenter;
import name.wanghwx.ehomework.view.fragment.CourseFragment;
import name.wanghwx.ehomework.view.fragment.HomeworkFragment;
import name.wanghwx.ehomework.view.fragment.MeFragment;
import name.wanghwx.ehomework.view.fragment.MistakeBookFragment;
import name.wanghwx.ehomework.view.iactivity.IMainActivity;

public class MainPresenter extends IMainPresenter{

    private int menu;

    private HomeworkFragment assignmentFragment = HomeworkFragment.getInstance(Task.Form.ASSIGNMENT.getCode());
    private HomeworkFragment testFragment = HomeworkFragment.getInstance(Task.Form.TEST.getCode());
    private CourseFragment courseFragment = CourseFragment.getInstance();
    private MistakeBookFragment mistakeBookFragment = MistakeBookFragment.getInstance();
    private MeFragment meFragment = MeFragment.getInstance();

    private IStudentModel studentModel = StudentModel.getInstance();

    public MainPresenter(IMainActivity mainActivity){
        attachView(mainActivity);
    }

    @Override
    public Fragment getFragment(){
        switch(this.menu){
            case Category.MENU_HOMEWORK:
                return assignmentFragment;
            case Category.MENU_TEST:
                return testFragment;
            case Category.MENU_COURSE:
                return courseFragment;
            case Category.MENU_MISTAKE_BOOK:
                return mistakeBookFragment;
            case Category.MENU_ME:
                return meFragment;
            default:
                return null;
        }
    }

    @Override
    public void switchMenu(int menu){
        this.menu = menu;
        if(isViewAttached()) getView().render();
        findAndRenderRemind();
    }

    @Override
    public Integer getMenu(){
        return menu;
    }

    @Override
    public void findAndRenderRemind(){
        studentModel.findRemind(new HttpCallback<Object>(getView()){
            @Override
            public void success(Result<Object> result){
                Object homeworkRemindObject = result.get(MapKey.HOMEWORK_REMIND);
                if(homeworkRemindObject != null) setHomeworkRemind(Boolean.parseBoolean((String)homeworkRemindObject));
                Object testRemindObject = result.get(MapKey.TEST_REMIND);
                if(testRemindObject != null) setTestRemind(Boolean.parseBoolean((String)testRemindObject));
                Object mistakeRemindObject = result.get(MapKey.MISTAKE_REMIND);
                if(mistakeRemindObject != null) setMistakeRemind(Boolean.parseBoolean((String)mistakeRemindObject));
            }
        });
    }

    private void setHomeworkRemind(boolean homeworkRemind){
        if(isViewAttached()) getView().renderHomeworkRemind(homeworkRemind);
    }

    private void setTestRemind(boolean testRemind){
        if(isViewAttached()) getView().renderTestRemind(testRemind);
    }

    private void setMistakeRemind(boolean mistakeRemind){
        if(isViewAttached()) getView().renderMistakeRemind(mistakeRemind);
    }

}