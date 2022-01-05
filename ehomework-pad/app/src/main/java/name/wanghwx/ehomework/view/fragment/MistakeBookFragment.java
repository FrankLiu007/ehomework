package name.wanghwx.ehomework.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.presenter.MistakeBookPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.IMistakeBookPresenter;
import name.wanghwx.ehomework.view.activity.MistakeBookActivity;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IMistakeBookFragment;

public class MistakeBookFragment extends BaseFragment<IMistakeBookFragment, IMistakeBookPresenter> implements IMistakeBookFragment{

    public static MistakeBookFragment getInstance(){
        MistakeBookFragment mistakeBookFragment = new MistakeBookFragment();
        Bundle bundle = new Bundle();
        mistakeBookFragment.setArguments(bundle);
        return mistakeBookFragment;
    }

    @BindView(R.id.rv_mistake_book)
    RecyclerView mistakeBookRecyclerView;

    @Override
    protected int getResourceId(){
        return R.layout.fragment_mistake_book;
    }

    @Override
    protected IMistakeBookPresenter createPresenter(){
        return new MistakeBookPresenter(this);
    }

    @Override
    protected void initView(){
        mistakeBookRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return null;
    }

    @Override
    protected void refreshView(){
        presenter.findAndRenderMistakeBooks();
    }

    @Override
    public void renderRecycler(List<MistakeBook> mistakeBooks){
        if(mistakeBookRecyclerView != null) mistakeBookRecyclerView.setAdapter(new Adapter<>(MistakeBook.class,mistakeBooks,MistakeBookViewHolder.class));
    }

    @Override
    public void nullData(){}

    @ItemView(R.layout.recycler_course)
    public static class MistakeBookViewHolder extends Adapter.ViewHolder<MistakeBook>{

        @BindView(R.id.rl_root)
        RelativeLayout rootRelativeLayout;
        @BindView(R.id.icon_subject)
        TextView subjectIcon;
        @BindView(R.id.rl_remind)
        RelativeLayout remindRelativeLayout;
        @BindView(R.id.tv_remind)
        TextView remindTextView;
        @BindView(R.id.tv_name)
        TextView nameTextView;

        @BindString(R.string.integer)
        String integer;

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

        public MistakeBookViewHolder(@NonNull View itemView, RecyclerView recyclerView, List<MistakeBook> list) {
            super(itemView, recyclerView, list);
        }

        @Override
        public void render(int position){
            MistakeBook mistakeBook = list.get(position);
            rootRelativeLayout.setTag(mistakeBook.getMistakeBookId());
            nameTextView.setText(mistakeBook.getCourse().getName());
            int remind = mistakeBook.getRemind();
            remindTextView.setText(String.format(integer,remind));
            remindRelativeLayout.setVisibility(remind == 0?View.INVISIBLE:View.VISIBLE);
            switch(mistakeBook.getCourse().getTeacher().getSubject()){
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

        @Override
        public List<TextView> getIcons(){
            return Collections.singletonList(subjectIcon);
        }

        @OnClick(R.id.rl_root)
        public void enter(View view){
            Context context = recyclerView.getContext();
            context.startActivity(new Intent(context,MistakeBookActivity.class).putExtra(MapKey.MISTAKE_BOOK_ID,(int)rootRelativeLayout.getTag()));
        }
    }
}
