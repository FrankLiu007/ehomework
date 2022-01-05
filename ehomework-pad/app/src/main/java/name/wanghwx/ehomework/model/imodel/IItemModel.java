package name.wanghwx.ehomework.model.imodel;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Item;
import retrofit2.Callback;

public interface IItemModel{

    void findItems(Callback<Result<List<Item>>> callback, Integer taskId);

}
