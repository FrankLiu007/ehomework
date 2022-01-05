package name.wanghwx.ehomework.model;

import java.util.List;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.service.StudentService;
import name.wanghwx.ehomework.model.imodel.IMistakeBookModel;
import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.pojo.MistakeBook;
import retrofit2.Callback;

public class MistakeBookModel implements IMistakeBookModel{

    private static MistakeBookModel mistakeBookModel;

    public static MistakeBookModel getInstance(){
        if(mistakeBookModel == null){
            mistakeBookModel = new MistakeBookModel();
        }
        return mistakeBookModel;
    }

    private StudentService studentService;

    private MistakeBookModel(){
        studentService = EhomeworkApplication.getStudentService();
    }

    @Override
    public void collect(Callback<Result<MistakeBook>> callback, Integer courseId, Integer itemId) {
        studentService.collectItem(EhomeworkApplication.getToken(),courseId, itemId).enqueue(callback);
    }

    @Override
    public void findMistakeBooks(Callback<Result<List<MistakeBook>>> callback) {
        studentService.findMistakeBooks(EhomeworkApplication.getToken()).enqueue(callback);
    }

    @Override
    public void findMistakeBook(Callback<Result<MistakeBook>> callback, Integer mistakeBookId,Integer pageNum,Integer pageSize){
        studentService.findMistakeBook(EhomeworkApplication.getToken(),mistakeBookId,pageNum,pageSize).enqueue(callback);
    }

    @Override
    public void abandon(Callback<Result<Object>> callback,Integer courseId,Integer itemId){
        studentService.abandonItem(EhomeworkApplication.getToken(),courseId,itemId).enqueue(callback);
    }

    @Override
    public void findItems(Callback<Result<List<Item>>> callback,Integer mistakeBookId){
        studentService.findItemsByMistakeBook(EhomeworkApplication.getToken(),mistakeBookId).enqueue(callback);
    }

    @Override
    public void clearRemind(Callback<Result<Object>> callback,Integer mistakeBookId,Integer itemId){
        studentService.clearRemind(EhomeworkApplication.getToken(),mistakeBookId,itemId).enqueue(callback);
    }

}