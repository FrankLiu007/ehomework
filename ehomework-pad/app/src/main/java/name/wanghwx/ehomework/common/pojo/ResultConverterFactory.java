package name.wanghwx.ehomework.common.pojo;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import name.wanghwx.ehomework.common.constant.MapKey;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultConverterFactory extends Converter.Factory{

    @Nullable
    @Override
    public Converter<ResponseBody,?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit){
        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if(Result.class.equals(parameterizedType.getRawType())){
                return new ResultConverter(parameterizedType.getActualTypeArguments()[0]);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit){
        return GsonConverterFactory.create().requestBodyConverter(type,parameterAnnotations,methodAnnotations,retrofit);
    }

    public class ResultConverter<D> implements Converter<ResponseBody,Result<D>>{

        private Type type;
        private Gson gson;

        public ResultConverter(Type type){
            this.type = type;
            this.gson = new Gson();
        }

        @Nullable
        @Override
        public Result<D> convert(ResponseBody responseBody) throws IOException{
            String string = responseBody.string();
            JsonObject jsonObject = gson.fromJson(string,JsonObject.class);
            Result<D> result = new Result<>();
            for(Map.Entry<String,JsonElement> entry:jsonObject.entrySet()){
                String key = entry.getKey();
                JsonElement value = entry.getValue();
                if(MapKey.CODE.equals(key)){
                    result.code(value.getAsInt());
                }
                if(MapKey.MESSAGE.equals(key)){
                    result.message(value.toString());
                }
                if(MapKey.DATA.equals(key)){
                    result.data(gson.fromJson(value.toString(),type));
                }
                result.put(key,value.toString());
            }
            return result;
        }

    }
}
