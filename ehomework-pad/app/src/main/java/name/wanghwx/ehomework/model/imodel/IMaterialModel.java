package name.wanghwx.ehomework.model.imodel;

import java.util.List;

import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.pojo.Unit;
import retrofit2.Callback;

public interface IMaterialModel{

    void findUnitsByParent(Callback<Result<List<Unit>>> callback, Integer unitId);

}