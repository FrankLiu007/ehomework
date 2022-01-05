package name.wanghwx.ehomework.model;

import java.util.List;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.service.StudentService;
import name.wanghwx.ehomework.model.imodel.IItemModel;
import name.wanghwx.ehomework.pojo.Item;
import retrofit2.Callback;

public class ItemModel implements IItemModel{

    private static ItemModel itemModel;

    public static ItemModel getInstance(){
        if(itemModel == null){
            itemModel = new ItemModel();
        }
        return itemModel;
    }

    private StudentService studentService;

    private ItemModel(){
        studentService = EhomeworkApplication.getStudentService();
    }

    @Override
    public void findItems(Callback<Result<List<Item>>> callback, Integer taskId) {
        studentService.findItems(EhomeworkApplication.getToken(),taskId).enqueue(callback);
    }

}
