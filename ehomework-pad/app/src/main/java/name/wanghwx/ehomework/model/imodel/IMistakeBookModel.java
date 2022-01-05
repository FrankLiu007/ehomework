package name.wanghwx.ehomework.model.imodel;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Item;
import name.wanghwx.ehomework.pojo.MistakeBook;
import retrofit2.Callback;

public interface IMistakeBookModel{

    void collect(Callback<Result<MistakeBook>> callback, Integer courseId, Integer itemId);

    void findMistakeBooks(Callback<Result<List<MistakeBook>>> callback);

    void findMistakeBook(Callback<Result<MistakeBook>> callback,Integer mistakeBookId,Integer pageNum,Integer pageSize);

    void abandon(Callback<Result<Object>> callback,Integer courseId,Integer itemId);

    void findItems(Callback<Result<List<Item>>> callback,Integer mistakeBookId);

    void clearRemind(Callback<Result<Object>> callback,Integer mistakeBookId,Integer itemId);

}