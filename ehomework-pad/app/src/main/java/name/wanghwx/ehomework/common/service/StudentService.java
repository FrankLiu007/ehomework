package name.wanghwx.ehomework.common.service;

import java.util.List;
import java.util.Map;

import name.wanghwx.ehomework.common.constant.MapKey;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Course;
import name.wanghwx.ehomework.pojo.Homework;
import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.pojo.MistakeBook;
import name.wanghwx.ehomework.pojo.Student;
import name.wanghwx.ehomework.pojo.Unit;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StudentService{

    @POST("autoLogin")
    Call<Result<Student>> autoLogin(@Header(MapKey.AUTHENTICATION)String token);

    @POST("login")
    Call<Result<Student>> login(@Query("account")String account,@Query("password")String password);

    @GET("remind")
    Call<Result<Object>> findRemind(@Header(MapKey.AUTHENTICATION)String token);

    @POST("logout")
    Call<Result<Object>> logout(@Header(MapKey.AUTHENTICATION)String token);

    @GET("homework/count")
    Call<Result<Object>> countHomework(@Header(MapKey.AUTHENTICATION)String token,@Query("formCode")Integer formCode);

    @GET("homework")
    Call<Result<List<Homework>>> findHomeworks(@Header(MapKey.AUTHENTICATION)String token,@Query("formCode")Integer formCode,@Query("submitted")Boolean submitted,@Query("subjectCode")Integer subjectCode,@Query("pageNum")Integer pageNum,@Query("pageSize")Integer pageSize);

    @GET("homework/{homeworkId}")
    Call<Result<Homework>> getHomework(@Header(MapKey.AUTHENTICATION)String token,@Path("homeworkId")Integer homeworkId);

    @GET("{homeworkId}/item/{itemId}")
    Call<Result<Item>> getItemInHomework(@Header(MapKey.AUTHENTICATION)String token,@Path("homeworkId")Integer homeworkId,@Path("itemId")Integer itemId);

    @GET("items/{taskId}")
    Call<Result<List<Item>>> findItems(@Header(MapKey.AUTHENTICATION)String token,@Path("taskId")Integer taskId);

    @POST("collect/{courseId}/{itemId}")
    Call<Result<MistakeBook>> collectItem(@Header(MapKey.AUTHENTICATION)String token,@Path("courseId") Integer courseId,@Path("itemId") Integer itemId);

    @DELETE("abandon/{courseId}/{itemId}")
    Call<Result<Object>> abandonItem(@Header(MapKey.AUTHENTICATION)String token,@Path("courseId")Integer courseId,@Path("itemId")Integer itemId);

    @Multipart
    @POST("submit/{homeworkId}")
    Call<Result<Homework>> submitHomework(@Header(MapKey.AUTHENTICATION)String token, @Path("homeworkId")Integer homeworkId, @Part("objectiveJson")String objectiveJson, @PartMap Map<String,RequestBody> map);

    @GET("course")
    Call<Result<List<Course>>> findCourses(@Header(MapKey.AUTHENTICATION)String token);

    @GET("unit/{unitId}")
    Call<Result<List<Unit>>> findUnitsByParent(@Header(MapKey.AUTHENTICATION)String token,@Path("unitId")Integer unitId);

    @GET("{courseId}/homework")
    Call<Result<List<Homework>>> findHomeworksByCourse(@Header(MapKey.AUTHENTICATION)String token,@Path("courseId")Integer courseId,@Query("formCode")Integer formCode,@Query("pageNum")Integer pageNum,@Query("pageSize")Integer pageSize);

    @GET("mistakeBook")
    Call<Result<List<MistakeBook>>> findMistakeBooks(@Header(MapKey.AUTHENTICATION)String token);

    @GET("mistakeBook/{mistakeBookId}")
    Call<Result<MistakeBook>> findMistakeBook(@Header(MapKey.AUTHENTICATION)String token,@Path("mistakeBookId")Integer mistakeBookId,@Query("pageNum")Integer pageNum,@Query("pageSize")Integer pageSize);

    @GET("{mistakeBookId}/item")
    Call<Result<List<Item>>> findItemsByMistakeBook(@Header(MapKey.AUTHENTICATION)String token,@Path("mistakeBookId")Integer mistakeBookId);

    @PUT("clear/{mistakeBookId}/{itemId}")
    Call<Result<Object>> clearRemind(@Header(MapKey.AUTHENTICATION)String token,@Path("mistakeBookId")Integer mistakeBookId,@Path("itemId")Integer itemId);

}