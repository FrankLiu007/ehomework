package name.wanghwx.ehomework.common.pojo;

import androidx.annotation.Nullable;

import java.util.HashMap;

import name.wanghwx.ehomework.common.constant.MapKey;

public class Result<D> extends HashMap<String,Object>{

    @Nullable
    @Override
    public Result<D> put(String key, Object value){
        if(!key.equals(MapKey.CODE) && !key.equals(MapKey.MESSAGE) && !key.equals(MapKey.DATA)){
            super.put(key, value);
        }
        return this;
    }

    public Result<D> code(Integer code){
        super.put(MapKey.CODE,code);
        return this;
    }

    public Integer code(){
        return (Integer)get(MapKey.CODE);
    }

    public Result<D> message(String message){
        super.put(MapKey.MESSAGE,message);
        return this;
    }

    public String message(){
        return (String)get(MapKey.MESSAGE);
    }

    public Result<D> data(D data){
        super.put(MapKey.DATA,data);
        return this;
    }

    public D data(){
        return (D)get(MapKey.DATA);
    }

}
