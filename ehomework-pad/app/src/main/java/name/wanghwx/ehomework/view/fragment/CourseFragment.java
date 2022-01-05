package name.wanghwx.ehomework.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.annotation.ItemView;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.Adapter;
import name.wanghwx.ehomework.pojo.Course;
import name.wanghwx.ehomework.presenter.CourseFragmentPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.ICourseFragmentPresenter;
import name.wanghwx.ehomework.view.activity.CourseActivity;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.ICourseFragment;

public class CourseFragment extends BaseFragment<ICourseFragment, ICourseFragmentPresenter> implements ICourseFragment{

    @BindView(R.id.rv_course)
    RecyclerView courseRecyclerView;

    public static CourseFragment getInstance(){
        return new CourseFragment();
    }

    @Override
    protected int getResourceId(){
        return R.layout.fragment_course;
    }

    @Override
    protected ICourseFragmentPresenter createPresenter(){
        return new CourseFragmentPresenter(this);
    }

    @Override
    protected void initView(){
        courseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        presenter.findCourses();
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return null;
    }

    @Override
    protected void refreshView(){}

    @Override
    public void nullData(){}

    @Override
    public void renderRecycler(List<Course> courses){
        if(courseRecyclerView != null) courseRecyclerView.setAdapter(new Adapter<>(Course.class,courses,CourseViewHolder.class));
    }

    @ItemView(R.layout.recycler_course)
    public static class CourseViewHolder extends Adapter.ViewHolder<Course>{
        @BindView(R.id.rl_root)
        RelativeLayout rootRelativeLayout;
        @BindView(R.id.icon_subject)
        TextView subjectIcon;
        @BindView(R.id.tv_name)
        TextView nameTextView;

        @BindString(R.string.icon_chinese)
        String iconChinese;
        @BindString(R.string.icon_maths)
        String iconMaths;
        @BindString(R.string.icon_english)
        String iconEnglish;
        @BindString(R.string.icon_politics)
        String iconPolitics;
        @BindString(R.string.icon_history)
        String iconHistory;
        @BindString(R.string.icon_geography)
        String iconGeography;
        @BindString(R.string.icon_physics)
        String iconPhysics;
        @BindString(R.string.icon_chemistry)
        String iconChemistry;
        @BindString(R.string.icon_biology)
        String iconBiology;

        public CourseViewHolder(@NonNull View itemView, RecyclerView recyclerView, List<Course> list) {
            super(itemView, recyclerView, list);
        }

        @Override
        public List<TextView> getIcons() {
            return Collections.singletonList(subjectIcon);
        }

        @Override
        public void render(int position) {
            Course course = list.get(position);
            rootRelativeLayout.setTag(course.getCourseId());
            nameTextView.setText(course.getName());
            switch(course.getTeacher().getSubject()){
                case CHINESE:
                    subjectIcon.setText(iconChinese);
                    break;
                case MATHEMATICS:
                    subjectIcon.setText(iconMaths);
                    break;
                case ENGLISH:
                    subjectIcon.setText(iconEnglish);
                    break;
                case POLITICS:
                    subjectIcon.setText(iconPolitics);
                    break;
                case HISTORY:
                    subjectIcon.setText(iconHistory);
                    break;
                case GEOGRAPHY:
                    subjectIcon.setText(iconGeography);
                    break;
                case PHYSICS:
                    subjectIcon.setText(iconPhysics);
                    break;
                case CHEMISTRY:
                    subjectIcon.setText(iconChemistry);
                    break;
                case BIOLOGY:
                    subjectIcon.setText(iconBiology);
                    break;
                default:
                    break;
            }
        }

        @OnClick(R.id.rl_root)
        public void enter(View view){
            Context context = recyclerView.getContext();
            context.startActivity(new Intent(context,CourseActivity.class).putExtra(MapKey.COURSE_ID,(int)rootRelativeLayout.getTag()));
        }
    }
}