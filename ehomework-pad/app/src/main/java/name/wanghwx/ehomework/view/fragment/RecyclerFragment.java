package name.wanghwx.ehomework.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.onyx.android.sdk.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.common.annotation.ItemView;
import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.Adapter;
import name.wanghwx.ehomework.common.pojo.HttpView;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.util.GsonUtils;
import name.wanghwx.ehomework.model.MaterialModel;
import name.wanghwx.ehomework.model.imodel.IMaterialModel;
import name.wanghwx.ehomework.pojo.Material;
import name.wanghwx.ehomework.pojo.Unit;
import name.wanghwx.ehomework.presenter.ipresenter.IRecyclerPresenter;
import name.wanghwx.ehomework.view.base.BaseFragment;
import name.wanghwx.ehomework.view.ifragment.IRecyclerFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerFragment<T> extends BaseFragment<IRecyclerFragment, IRecyclerPresenter> implements IRecyclerFragment{

    @BindView(R.id.rv_root)
    RecyclerView rootRecyclerView;

    public static <T> RecyclerFragment<T> getInstance(Class<T> tClass, List<T> list, Class<? extends Adapter.ViewHolder> clazz,Class<? extends RecyclerView.LayoutManager> mClass,int orientation,int count,boolean reverse){
        RecyclerFragment<T> recyclerFragment = new RecyclerFragment<>();
        Bundle bundle = new Bundle();
        bundle.putString(MapKey.T_CLASS,tClass.getName());
        bundle.putString(MapKey.LIST, GsonUtils.toJson(list));
        bundle.putString(MapKey.CLAZZ,clazz.getName());
        bundle.putString(MapKey.M_CLASS,mClass.getName());
        bundle.putInt(MapKey.ORIENTATION,orientation);
        bundle.putInt(MapKey.TOTAL,count);
        bundle.putBoolean(MapKey.REVERSE,reverse);
        recyclerFragment.setArguments(bundle);
        return recyclerFragment;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected IRecyclerPresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(){
        Bundle bundle = getArguments();
        if(bundle != null){
            try{
                Class<T> tClass = (Class<T>)Class.forName(bundle.getString(MapKey.T_CLASS));
                List<T> list = GsonUtils.fromJson(bundle.getString(MapKey.LIST),TypeToken.getParameterized(List.class,tClass).getType());
                Class<? extends Adapter.ViewHolder<T>> clazz = (Class<? extends Adapter.ViewHolder<T>>)Class.forName(bundle.getString(MapKey.CLAZZ));
                if(list != null && list.size() > 0){
                    if(LinearLayoutManager.class.getName().equals(bundle.getString(MapKey.M_CLASS))){
                        rootRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),bundle.getInt(MapKey.ORIENTATION),bundle.getBoolean(MapKey.REVERSE)));
                    }
                    if(GridLayoutManager.class.getName().equals(bundle.getString(MapKey.M_CLASS))){
                        rootRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),bundle.getInt(MapKey.TOTAL),bundle.getInt(MapKey.ORIENTATION),bundle.getBoolean(MapKey.REVERSE)));
                    }
                    if(StaggeredGridLayoutManager.class.getName().equals(bundle.getString(MapKey.M_CLASS))){
                        rootRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(bundle.getInt(MapKey.ORIENTATION),bundle.getInt(MapKey.TOTAL)));
                    }
                    rootRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    rootRecyclerView.setAdapter(new Adapter<>(tClass,list,clazz));
                }
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void bindListener(){

    }

    @Override
    protected List<TextView> getIcons() {
        return null;
    }

    @Override
    protected void refreshView() {

    }

    @ItemView(R.layout.recycler_material)
    public static class MaterialViewHolder extends Adapter.ViewHolder<Material>{

        private IMaterialModel materialModel = MaterialModel.getInstance();

        @BindView(R.id.tv_name)
        TextView nameTextView;
        @BindView(R.id.icon_expand)
        TextView expandIcon;
        @BindView(R.id.rv_unit)
        RecyclerView unitRecyclerView;

        private boolean expand;
        private List<Unit> units;

        public MaterialViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public MaterialViewHolder(@NonNull View itemView, RecyclerView recyclerView, List<Material> list,Object object) {
            super(itemView, recyclerView, list,object);
        }

        @OnClick({R.id.icon_expand,R.id.tv_name})
        public void expand(TextView textView){
            if((boolean)nameTextView.getTag()){
                if(expand){
                    unitRecyclerView.setVisibility(View.GONE);
                    expandIcon.setText(R.string.icon_right);
                    expand = false;
                }else{
                    if(units == null || units.size() == 0){
                        materialModel.findUnitsByParent(new Callback<Result<List<Unit>>>() {
                            @Override
                            public void onResponse(Call<Result<List<Unit>>> call, Response<Result<List<Unit>>> response) {
                                Result<List<Unit>> result = response.body();
                                if(result != null && result.code() == 1){
                                    unitRecyclerView.setTag(result.get(MapKey.LEVEL));
                                    unitRecyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                                    unitRecyclerView.setAdapter(new Adapter<>(Unit.class,result.data(),UnitViewHolder.class));
                                }
                            }
                            @Override
                            public void onFailure(Call<Result<List<Unit>>> call, Throwable t) {
                                Log.e("MaterialViewHolder","请求失败");
                            }
                        },(int)expandIcon.getTag());
                    }
                    unitRecyclerView.setVisibility(View.VISIBLE);
                    expandIcon.setText(R.string.icon_down);
                    expand = true;
                }
            }else{
                System.err.println("进入内容");
            }
        }

        @Override
        public List<TextView> getIcons(){
            List<TextView> icons = new ArrayList<>();
            icons.add(expandIcon);
            return icons;
        }

        @Override
        public void render(int position){
            Material material = list.get(position);
            nameTextView.setText(material.getName());
            Unit root = material.getRoot();
            expandIcon.setTag(root.getUnitId());
            Boolean isParent = root.getIsParent();
            nameTextView.setTag(isParent);
            expandIcon.setVisibility(isParent?View.VISIBLE:View.INVISIBLE);
        }
    }

    @ItemView(R.layout.recycler_unit)
    public static class UnitViewHolder extends Adapter.ViewHolder<Unit> implements HttpView{
        @BindView(R.id.tv_name)
        TextView nameTextView;
        @BindView(R.id.icon_expand)
        TextView expandIcon;
        @BindView(R.id.rv_unit)
        RecyclerView unitRecyclerView;
        @BindColor(R.color.dark2)
        int dark2;
        @BindColor(R.color.dark3)
        int dark3;

        private Boolean expand = false;
        private List<Unit> units;

        private void setUnits(List<Unit> units){
            this.units = units;
            unitRecyclerView.setAdapter(new Adapter<>(Unit.class,this.units,UnitViewHolder.class));
        }

        private IMaterialModel materialModel = MaterialModel.getInstance();

        @OnClick({R.id.icon_expand,R.id.tv_name})
        public void expand(View view){
            if((boolean)nameTextView.getTag()){
                if(expand){
                    unitRecyclerView.setVisibility(View.GONE);
                    expandIcon.setText(R.string.icon_right);
                    expand = false;
                }else{
                    if(CollectionUtils.isNullOrEmpty(units)){
                        materialModel.findUnitsByParent(new Callback<Result<List<Unit>>>() {
                            @Override
                            public void onResponse(Call<Result<List<Unit>>> call, Response<Result<List<Unit>>> response){
                                Result<List<Unit>> result = response.body();
                                if(result == null){
                                    requestFailed();
                                }else{
                                    if(result.code() == 1){
                                        unitRecyclerView.setTag(result.get(MapKey.LEVEL));
                                        List<Unit> units = result.data();
                                        if(CollectionUtils.isNullOrEmpty(units)){
                                            nullData();
                                        }else{
                                            setUnits(units);
                                        }
                                    }else{
                                        showResult(result);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<Result<List<Unit>>> call, Throwable t){
                                requestFailed(t);
                            }
                        },(int)expandIcon.getTag());
                    }
                    unitRecyclerView.setVisibility(View.VISIBLE);
                    expandIcon.setText(R.string.icon_down);
                    expand = true;
                }
            }else{
                System.err.println("进入内容");
            }
        }

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public UnitViewHolder(@NonNull View itemView, RecyclerView recyclerView, List<Unit> list,Object object) {
            super(itemView, recyclerView, list,object);
        }

        @Override
        public List<TextView> getIcons(){
            List<TextView> icons = new ArrayList<>();
            icons.add(expandIcon);
            return icons;
        }

        @Override
        public void render(int position){
            int level = Integer.valueOf((String)recyclerView.getTag());
            if(level == 1){
                expandIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX,40);
                expandIcon.setTextColor(dark2);
                nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,32);
                nameTextView.setTextColor(dark2);
            }else{
                expandIcon.setTextSize(TypedValue.COMPLEX_UNIT_PX,38);
                expandIcon.setTextColor(dark3);
                nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
                nameTextView.setTextColor(dark3);
            }
            Unit unit = list.get(position);
            nameTextView.setText(unit.getName());
            expandIcon.setTag(unit.getUnitId());
            Boolean isParent = unit.getIsParent();
            nameTextView.setTag(isParent);
            expandIcon.setVisibility(isParent?View.VISIBLE:View.INVISIBLE);
            unitRecyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }

        @Override
        public Context getContext() {
            return recyclerView.getContext();
        }

        @Override
        public void nullData(){}

    }

    @ItemView(R.layout.recycler_display)
    public static class DisplayViewHolder extends Adapter.ViewHolder<List>{
        @BindView(R.id.icon_title)
        TextView titleIcon;
        @BindView(R.id.tv_title)
        TextView titleTextView;
        @BindView(R.id.tv_content)
        TextView contentTextView;

        public DisplayViewHolder(@NonNull View itemView,RecyclerView recyclerView,List<List> list){
            super(itemView,recyclerView,list);
        }

        public DisplayViewHolder(@NonNull View itemView,RecyclerView recyclerView,List<List> list,Object object){
            super(itemView,recyclerView,list,object);
        }

        @Override
        public List<TextView> getIcons(){
            return Collections.singletonList(titleIcon);
        }

        @Override
        public void render(int position){
            List<String> display = list.get(position);
            titleIcon.setText(display.get(0));
            titleTextView.setText(display.get(1));
            contentTextView.setText(display.get(2));
        }
    }
}