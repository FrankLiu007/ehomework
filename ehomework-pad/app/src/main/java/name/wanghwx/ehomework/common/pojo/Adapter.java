package name.wanghwx.ehomework.common.pojo;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import name.wanghwx.ehomework.common.annotation.ItemView;
import name.wanghwx.ehomework.common.constant.Directory;

public class Adapter<T> extends RecyclerView.Adapter<Adapter.ViewHolder<T>>{

    private Class<T> tClass;

    private List<T> list;

    private Class<? extends Adapter.ViewHolder<T>> clazz;

    private Object object;

    public Adapter(Class<T> tClass,List<T> list,Class<? extends Adapter.ViewHolder<T>> clazz){
        this.tClass = tClass;
        this.list = list;
        this.clazz = clazz;
    }

    public Adapter(Class<T> tClass,List<T> list,Class<? extends Adapter.ViewHolder<T>> clazz,Object object){
        this(tClass,list,clazz);
        this.object = object;
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        if(parent instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView)parent;
            int layoutId = 0;
            ItemView annotation = clazz.getAnnotation(ItemView.class);
            if(viewType == 0){
                if(annotation == null){
                    Log.e("RecyclerFragment","请使用@ItemView为ViewHolder指定布局文件");
                }else{
                    layoutId = annotation.value();
                }
            }else{
                layoutId = viewType;
            }
            View itemView = LayoutInflater.from(recyclerView.getContext()).inflate(layoutId,recyclerView,false);
            try{
                ViewHolder<T> viewHolder = object==null?clazz.getConstructor(View.class,RecyclerView.class,List.class).newInstance(itemView,recyclerView,list):clazz.getConstructor(View.class,RecyclerView.class,List.class,Object.class).newInstance(itemView,recyclerView,list,object);
                ButterKnife.bind(viewHolder,itemView);
                return viewHolder;
            }catch(Exception e){
                Log.e(getClass().getName(),"通过反射生成ViewHolder失败",e);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        List<TextView> icons = holder.getIcons();
        if(icons != null && !icons.isEmpty()){
            Typeface typeface = Typeface.createFromAsset(holder.recyclerView.getContext().getAssets(), Directory.ICON_FONT);
            for(TextView icon:icons){
                icon.setTypeface(typeface);
            }
        }
        holder.render(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract static class ViewHolder<T> extends RecyclerView.ViewHolder{

        protected RecyclerView recyclerView;
        protected List<T> list;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
        }

        public ViewHolder(@NonNull View itemView,RecyclerView recyclerView,List<T> list){
            super(itemView);
            this.recyclerView = recyclerView;
            this.list = list;
        }

        public ViewHolder(@NonNull View itemView,RecyclerView recyclerView,List<T> list,Object object){
            super(itemView);
            this.recyclerView = recyclerView;
            this.list = list;
        }

        public abstract List<TextView> getIcons();

        public abstract void render(int position);
    }
}
