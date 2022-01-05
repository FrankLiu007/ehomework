package name.wanghwx.ehomework.view.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.presenter.MainPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IMainPresenter;
import name.wanghwx.ehomework.view.base.BaseActivity;
import name.wanghwx.ehomework.view.iactivity.IMainActivity;

public class MainActivity extends BaseActivity<IMainActivity,IMainPresenter> implements IMainActivity{

    @BindView(R.id.rl_homework)
    RelativeLayout homeworkRelativeLayout;
    @BindView(R.id.rl_test)
    RelativeLayout testRelativeLayout;
    @BindView(R.id.rl_course)
    RelativeLayout courseRelativeLayout;
    @BindView(R.id.rl_mistake_book)
    RelativeLayout mistakeBookRelativeLayout;
    @BindView(R.id.rl_me)
    RelativeLayout meRelativeLayout;
    @BindView(R.id.fl_content)
    FrameLayout contentFrameLayout;
    @BindView(R.id.icon_homework)
    TextView homeworkIcon;
    @BindView(R.id.v_homework_remind)
    View homeworkRemindView;
    @BindView(R.id.icon_test)
    TextView testIcon;
    @BindView(R.id.v_test_remind)
    View testRemindView;
    @BindView(R.id.icon_course)
    TextView courseIcon;
    @BindView(R.id.icon_mistake_book)
    TextView mistakeBookIcon;
    @BindView(R.id.v_mistake_remind)
    View mistakeRemindView;
    @BindView(R.id.icon_me)
    TextView meIcon;
    @BindView(R.id.icon_selector)
    TextView selectorIcon;

    @Override
    protected IMainPresenter createPresenter(){
        return new MainPresenter(this);
    }

    @Override
    protected int getResourceId(){
        return R.layout.activity_main;
    }

    @Override
    protected void initView(){
        homeworkRelativeLayout.setTag(Category.MENU_HOMEWORK);
        testRelativeLayout.setTag(Category.MENU_TEST);
        courseRelativeLayout.setTag(Category.MENU_COURSE);
        mistakeBookRelativeLayout.setTag(Category.MENU_MISTAKE_BOOK);
        meRelativeLayout.setTag(Category.MENU_ME);
        homeworkRelativeLayout.performClick();
    }

    @OnClick({R.id.rl_homework,R.id.rl_test,R.id.rl_course,R.id.rl_mistake_book,R.id.rl_me})
    public void switchMenu(RelativeLayout relativeLayout){
        presenter.switchMenu((int)relativeLayout.getTag());
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(homeworkIcon,testIcon,courseIcon,mistakeBookIcon,meIcon,selectorIcon);
    }

    @Override
    protected void refreshView(){
        presenter.findAndRenderRemind();
    }

    @Override
    public void render(){
        RelativeLayout relativeLayout = null;
        switch(presenter.getMenu()){
            case Category.MENU_HOMEWORK:
                relativeLayout = homeworkRelativeLayout;
                break;
            case Category.MENU_TEST:
                relativeLayout = testRelativeLayout;
                break;
            case Category.MENU_COURSE:
                relativeLayout = courseRelativeLayout;
                break;
            case Category.MENU_MISTAKE_BOOK:
                relativeLayout = mistakeBookRelativeLayout;
                break;
            case Category.MENU_ME:
                relativeLayout = meRelativeLayout;
            default:
                break;
        }
        if(relativeLayout != null){
            ((RelativeLayout)selectorIcon.getParent()).removeView(selectorIcon);
            relativeLayout.addView(selectorIcon);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,presenter.getFragment()).commitAllowingStateLoss();
        }
    }

    @Override
    public void renderHomeworkRemind(boolean homeworkRemind){
        homeworkRemindView.setVisibility(homeworkRemind?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void renderTestRemind(boolean testRemind){
        testRemindView.setVisibility(testRemind?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void renderMistakeRemind(boolean mistakeRemind){
        mistakeRemindView.setVisibility(mistakeRemind?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void nullData(){

    }
}