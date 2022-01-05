package name.wanghwx.ehomework.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onyx.android.sdk.api.device.epd.EpdController;
import com.onyx.android.sdk.api.device.epd.UpdateMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.annotation.ItemView;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.Adapter;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.util.DateUtil;
import name.wanghwx.ehomework.common.widget.DrawableTextView;
import name.wanghwx.ehomework.common.widget.NonScrollLinearLayoutManager;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.pojo.Subject;
import name.wanghwx.ehomework.pojo.Task;
import name.wanghwx.ehomework.presenter.HomeworkFragmentPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IHomeworkFragmentPresenter;
import name.wanghwx.ehomework.view.activity.HomeworkActivity;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IHomeworkFragment;

public class HomeworkFragment extends BaseFragment<IHomeworkFragment, IHomeworkFragmentPresenter> implements IHomeworkFragment{

    public static HomeworkFragment getInstance(Integer form){
        HomeworkFragment homeworkFragment = new HomeworkFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MapKey.TASK_FORM,form);
        homeworkFragment.setArguments(bundle);
        return homeworkFragment;
    }

    @BindView(R.id.ll_root)
    LinearLayout rootLinearLayout;
    @BindView(R.id.rl_unsubmitted)
    RelativeLayout unsubmittedRelativeLayout;
    @BindView(R.id.rl_unsubmitted_sup)
    RelativeLayout unsubmittedSupRelativeLayout;
    @BindView(R.id.tv_unsubmitted)
    TextView unsubmittedTextView;
    @BindView(R.id.rl_submitted)
    RelativeLayout submittedRelativeLayout;
    @BindView(R.id.rl_submitted_sup)
    RelativeLayout submittedSupRelativeLayout;
    @BindView(R.id.tv_submitted)
    TextView submittedTextView;
    @BindView(R.id.v_selector)
    View selectorView;
    @BindView(R.id.s_subject)
    Spinner subjectSpinner;
    @BindView(R.id.icon_refresh)
    TextView refreshIcon;
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

    @BindString(R.string.integer)
    String integer;
    @BindString(R.string.total)
    String total;
    @BindString(R.string.page)
    String page;

    @BindArray(R.array.subjects)
    String[] subjects;

    @Override
    protected int getResourceId(){
        return R.layout.fragment_homework;
    }

    @Override
    protected IHomeworkFragmentPresenter createPresenter(){
        return new HomeworkFragmentPresenter(this);
    }

    @Override
    protected void initView(){
        Bundle bundle = getArguments();
        if(bundle != null) presenter.setForm(bundle.getInt(MapKey.TASK_FORM));
        unsubmittedRelativeLayout.setTag(false);
        submittedRelativeLayout.setTag(true);
        unsubmittedRelativeLayout.performClick();
    }

    @OnClick({R.id.rl_unsubmitted,R.id.rl_submitted})
    void toggleSubmitted(RelativeLayout relativeLayout){
        ((RelativeLayout)selectorView.getParent()).removeView(selectorView);
        relativeLayout.addView(selectorView);
        presenter.toggleSubmitted((boolean)relativeLayout.getTag());
    }

    @OnClick(R.id.icon_refresh)
    void refresh(){
        presenter.refresh();
    }

    @OnItemSelected(R.id.s_subject)
    void selectSubject(AdapterView<?> parent,View view,int position,long id){
        Subject subject = Subject.getByName(subjects[position]);
        presenter.toggleSubject(subject==null?0:subject.getCode());
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(refreshIcon,previousIcon,nextIcon);
    }

    @Override
    protected void refreshView(){
        presenter.refresh();
    }

    @Override
    public void renderData(List<Homework> homeworks){
        emptyDrawableTextView.setVisibility(View.GONE);
        homeworkRecyclerView.setTag(null);
        homeworkRecyclerView.setLayoutManager(new NonScrollLinearLayoutManager(getContext()));
        homeworkRecyclerView.setAdapter(new Adapter<>(Homework.class,homeworks,HomeworkViewHolder.class));
        listRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult(Result result) {
        Toast.makeText(getActivity(),result.message(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void nullData(){
        listRelativeLayout.setVisibility(View.GONE);
        emptyDrawableTextView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.icon_previous)
    void previousPage(){
        presenter.previousPage();
    }

    @OnClick(R.id.icon_next)
    void nextPage(){
        presenter.nextPage();
    }

    @Override
    public void renderTotal(){
        totalTextView.setText(String.format(total,presenter.getTotal()));
    }

    @Override
    public void renderPage(){
        pageTextView.setText(String.format(page,presenter.getPageNum(),presenter.getMaxPageNum()));
    }

    @Override
    public void renderHasPrevious(){
        previousIcon.setEnabled(presenter.getHasPrevious());
    }

    @Override
    public void renderHasNext(){
        nextIcon.setEnabled(presenter.getHasNext());
    }

    @Override
    public void refreshScreen(){
        EpdController.refreshScreen(rootLinearLayout,UpdateMode.GC);
    }

    @Override
    public void renderUnsubmittedRemind(int unsubmittedRemind){
        if(unsubmittedRemind == 0){
            unsubmittedSupRelativeLayout.setVisibility(View.INVISIBLE);
        }else{
            unsubmittedTextView.setText(String.format(integer,unsubmittedRemind));
            unsubmittedSupRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderSubmittedRemind(int submittedRemind) {
        if(submittedRemind == 0){
            submittedSupRelativeLayout.setVisibility(View.INVISIBLE);
        }else{
            submittedTextView.setText(String.format(integer,submittedRemind));
            submittedSupRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @ItemView(R.layout.recycler_homework)
    public static class HomeworkViewHolder extends Adapter.ViewHolder<Homework>{

        @BindView(R.id.tv_status)
        TextView statusTextView;
        @BindView(R.id.tv_title)
        TextView titleTextView;
        @BindView(R.id.icon_remind)
        TextView remindIcon;
        @BindView(R.id.icon_expand)
        TextView expandIcon;
        @BindView(R.id.rl_expand)
        RelativeLayout expandRelativeLayout;
        @BindView(R.id.rv_display)
        RecyclerView displayRecyclerView;
        @BindView(R.id.ll_operate)
        LinearLayout operateLinearLayout;
        @BindView(R.id.icon_operate)
        TextView operateIcon;
        @BindView(R.id.tv_operate)
        TextView operateTextView;

        @BindString(R.string.title)
        String title;
        @BindString(R.string.icon_up)
        String iconUp;
        @BindString(R.string.icon_down)
        String iconDown;
        @BindString(R.string.icon_course_s)
        String iconCourseS;
        @BindString(R.string.icon_clock_s)
        String iconClockS;
        @BindString(R.string.icon_remark_s)
        String iconRemarkS;
        @BindString(R.string.icon_score_s)
        String iconScoreS;
        @BindString(R.string.icon_pen)
        String iconPen;
        @BindString(R.string.icon_eye_open)
        String iconEyeOpen;

        @BindString(R.string.course)
        String course;
        @BindString(R.string.deadline)
        String deadline;
        @BindString(R.string.submittedAt)
        String submittedAt;
        @BindString(R.string.correctedAt)
        String correctedAt;
        @BindString(R.string.score)
        String score;
        @BindString(R.string.accuracy)
        String accuracy;
        @BindString(R.string.write)
        String write;
        @BindString(R.string.check)
        String check;
        @BindString(R.string.remark)
        String remark;
        @BindString(R.string.decimal)
        String decimal;
        @BindString(R.string.percent)
        String percent;

        public HomeworkViewHolder(@NonNull View itemView,RecyclerView recyclerView,List<Homework> list){
            super(itemView,recyclerView,list);
        }

        @Override
        public List<TextView> getIcons(){
            return Arrays.asList(remindIcon,expandIcon,operateIcon);
        }

        @Override
        public void render(int position){
            Object expandPosition = recyclerView.getTag();
            boolean expand = expandPosition != null && expandPosition.equals(position);
            Homework homework = list.get(position);
            Task task = homework.getTask();
            Homework.Status status = homework.getStatus();
            statusTextView.setText(status.getMessage());
            titleTextView.setText(String.format(title,task.getCourse().getTeacher().getSubject().getName(),task.getBirthline(),task.getBirthline(),task.getTitle()));
            operateLinearLayout.setTag(homework.getHomeworkId());
            remindIcon.setVisibility(homework.isRemind()?View.VISIBLE:View.INVISIBLE);
            expandIcon.setText(expand?iconUp:iconDown);
            expandRelativeLayout.setVisibility(expand?View.VISIBLE:View.GONE);
            String courseName = task.getCourse().getName();
            List<List> list = new ArrayList<>();
            list.add(Arrays.asList(iconCourseS,course,courseName));
            switch(status){
                case UNSUBMITTED:
                    list.add(Arrays.asList(iconClockS,deadline,DateUtil.format(task.getDeadline())));
                    list.add(Arrays.asList(iconRemarkS,remark,task.getRemark()));
                    operateIcon.setText(iconPen);
                    operateTextView.setText(write);
                    break;
                case UNCORRECTED:
                    list.add(Arrays.asList(iconClockS,submittedAt,DateUtil.format(homework.getSubmitted())));
                    operateIcon.setText(iconEyeOpen);
                    operateTextView.setText(check);
                    break;
                case CORRECTED:
                    list.add(Arrays.asList(iconScoreS,task.getForm()==Task.Form.TEST?score:accuracy,task.getForm()==Task.Form.TEST?String.format(decimal,homework.getScore()):String.format(percent,(int)(homework.getScore()*20))));
                    list.add(Arrays.asList(iconClockS,correctedAt,DateUtil.format(homework.getCorrected())));
                    operateIcon.setText(iconEyeOpen);
                    operateTextView.setText(check);
                    break;
                default:
                    break;
            }
            displayRecyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()){
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            displayRecyclerView.setAdapter(new Adapter<>(List.class,list,RecyclerFragment.DisplayViewHolder.class));
        }

        @OnClick(R.id.icon_expand)
        void toggleExpand(){
            Object expandPosition = recyclerView.getTag();
            int position = getLayoutPosition();
            if(expandPosition == null){
                recyclerView.setTag(position);
                expandIcon.setText(iconUp);
                expandRelativeLayout.setVisibility(View.VISIBLE);
            }else{
                if(expandPosition.equals(position)){
                    recyclerView.setTag(null);
                    expandIcon.setText(iconDown);
                    expandRelativeLayout.setVisibility(View.GONE);
                }else{
                    recyclerView.setTag(position);
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    if(adapter != null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }

        @OnClick(R.id.ll_operate)
        void operate(){
            Context context = recyclerView.getContext();
            context.startActivity(new Intent(context,HomeworkActivity.class).putExtra(MapKey.HOMEWORK_ID,(int)operateLinearLayout.getTag()));
        }
    }
}