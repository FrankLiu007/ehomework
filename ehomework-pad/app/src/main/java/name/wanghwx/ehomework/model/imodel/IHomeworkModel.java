package name.wanghwx.ehomework.model.imodel;

import java.util.List;
import java.util.Map;

import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.pojo.Item;
import okhttp3.RequestBody;
import retrofit2.Callback;

public interface IHomeworkModel{

    void countHomework(Callback<Result<Object>> callback,Integer formCode);

    void findHomeworks(Callback<Result<List<Homework>>> callback,Integer formCode,Boolean submitted,Integer subjectCode,Integer pageNum,Integer pageSize);

    void getHomework(Callback<Result<Homework>> callback,Integer homeworkId);

    void getItemInHomework(Callback<Result<Item>> callback,Integer homeworkId,Integer itemId);

    void submitHomework(Callback<Result<Homework>> callback,Integer homeworkId,String answers,Map<String, RequestBody> map);

    void findHomeworksByCourse(Callback<Result<List<Homework>>> callback,Integer courseId,Integer formCode,Integer pageNum,Integer pageSize);

}