package name.wanghwx.ehomework.common.pojo;

import androidx.annotation.Nullable;

import com.onyx.android.sdk.pen.data.TouchPoint;
import com.onyx.android.sdk.utils.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import name.wanghwx.ehomework.common.util.UUIDUtils;

/**
 * 笔画
 * 1. 本地存储功能需要实现序列化接口
 * 2. 唯一标识保证序列化前后相关数据一致性
 */
public class Stroke implements Serializable{

    private static final long serialVersionUID = 1L;

    // 唯一标识
    final String id = UUIDUtils.generate();
    // 笔画模式
    final Mode mode;
    // 笔画粗细
    final double width;
    // 纵向偏移
    final double offsetY;
    // 起点
    final TouchPoint begin;
    // 路径
    List<TouchPoint> path;
    // 终点
    TouchPoint end;

    public Stroke(Mode mode,double width,double offsetY,TouchPoint begin){
        this.mode = mode;
        this.width = width;
        this.offsetY = offsetY;
        begin.y += offsetY;
        this.begin = begin;
    }

    @Override
    public boolean equals(@Nullable Object obj){
        return obj instanceof Stroke && id.equals(((Stroke)obj).id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    public Mode getMode(){
        return mode;
    }

    public double getWidth(){
        return width;
    }

    public TouchPoint getBegin(){
        return begin;
    }

    public List<TouchPoint> getPath(){
        return path;
    }

    public void setPath(List<TouchPoint> path){
        if(CollectionUtils.isNonBlank(path)){
            for(TouchPoint touchPoint:path){
                touchPoint.y += offsetY;
            }
            this.path = path;
        }
    }

    public TouchPoint getEnd(){
        return end;
    }

    public void setEnd(TouchPoint end){
        end.y += offsetY;
        this.end = end;
    }

    public enum Mode{
        // 钢笔
        BRUSH,
        // 移动擦除
        MOVE,
        // 笔画擦除
        STROKE
    }

}