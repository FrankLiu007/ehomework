package name.wanghwx.ehomework.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.onyx.android.sdk.utils.CollectionUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.constant.Category;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.Adapter;
import name.wanghwx.ehomework.common.widget.DrawableTextView;
import name.wanghwx.ehomework.common.widget.NonScrollLinearLayoutManager;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.presenter.CourseActivityPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.ICourseActivityPresenter;
import name.wanghwx.ehomework.view.base.BaseActivity;
import name.wanghwx.ehomework.view.fragment.HomeworkFragment;
import name.wanghwx.ehomework.view.iactivity.ICourseActivity;

public class CourseActivity extends BaseActivity<ICourseActivity, ICourseActivityPresenter> implements ICourseActivity{

    @BindView(R.id.rl_material)
    RelativeLayout materialRelativeLayout;
    @BindView(R.id.v_selector)
    View selectorView;
    @BindView(R.id.rl_homework)
    RelativeLayout homeworkRelativeLayout;
    @BindView(R.id.rl_test)
    RelativeLayout testRelativeLayout;
    @BindView(R.id.dtv_empty)
    DrawableTextView emptyDrawableTextView;
    @BindView(R.id.rl_list)
    RelativeLayout listRelativeLayout;
    @BindView(R.id.rv_homework)
    RecyclerView homeworkRecyclerView;
    @BindView(R.id.tv_total)
    TextView totalTextView;
    @BindView(R.id.icon_previous)
    TextView previousIcon;
    @BindView(R.id.tv_page)
    TextView pageTextView;
    @BindView(R.id.icon_next)
    TextView nextIcon;

    @BindString(R.string.total)
    String total;
    @BindString(R.string.page)
    String page;

    @OnClick(R.id.icon_previous)
    void previousPage(){
        presenter.previousPage();
    }

    @OnClick(R.id.icon_next)
    void nextPage(){
        presenter.nextPage();
    }

    @Override
    protected ICourseActivityPresenter createPresenter(){
        return new CourseActivityPresenter(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_course;
    }

    @OnClick({R.id.rl_material,R.id.rl_homework,R.id.rl_test})
    void switchMenu(RelativeLayout relativeLayout){
        RelativeLayout parentView = (RelativeLayout)selectorView.getParent();
        if(parentView != null) parentView.removeView(selectorView);
        relativeLayout.addView(selectorView);
        presenter.switchMenu((int)relativeLayout.getTag());
    }

    @Override
    protected void initView(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            Toast.makeText(this,"课程信息丢失，请重试",Toast.LENGTH_LONG).show();
        }else{
            presenter.setCourseId(bundle.getInt(MapKey.COURSE_ID));
        }
        materialRelativeLayout.setTag(Category.COURSE_MENU_MATERIAL);
        homeworkRelativeLayout.setTag(Category.COURSE_MENU_HOMEWORK);
        testRelativeLayout.setTag(Category.COURSE_MENU_TEST);
        homeworkRelativeLayout.performClick();
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(previousIcon,nextIcon);
    }

    @Override
    protected void refreshView(){}

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public void nullData(){
        emptyDrawableTextView.setVisibility(View.VISIBLE);
        listRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void render(List<Homework> homeworks){
        if(CollectionUtils.isNullOrEmpty(homeworks)){
            nullData();
        }else{
            totalTextView.setText(String.format(total,presenter.getTotal()));
            pageTextView.setText(String.format(page,presenter.getPageNum(),presenter.getMaxPageNum()));
            homeworkRecyclerView.setTag(null);
            homeworkRecyclerView.setLayoutManager(new NonScrollLinearLayoutManager(getContext()));
            homeworkRecyclerView.setAdapter(new Adapter<>(Homework.class,homeworks,HomeworkFragment.HomeworkViewHolder.class));
            emptyDrawableTextView.setVisibility(View.GONE);
            listRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void disablePreviousPage(){
        previousIcon.setEnabled(false);
    }

    @Override
    public void enablePreviousPage(){
        previousIcon.setEnabled(true);
    }

    @Override
    public void disableNextPage(){
        nextIcon.setEnabled(false);
    }

    @Override
    public void enableNextPage(){
        nextIcon.setEnabled(true);
    }

    @Override
    public void renderTotal(){
        totalTextView.setText(String.format(total,presenter.getTotal()));
    }

    @Override
    public void renderPage() {
        pageTextView.setText(String.format(page,presenter.getPageNum(),presenter.getMaxPageNum()));
    }
}