package name.wanghwx.ehomework.model;

import java.util.List;

import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.common.pojo.Result;
import name.wanghwx.ehomework.common.service.StudentService;
import name.wanghwx.ehomework.model.imodel.IMaterialModel;
import name.wanghwx.ehomework.pojo.Material;
import name.wanghwx.ehomework.pojo.Unit;
import retrofit2.Callback;

public class MaterialModel implements IMaterialModel{

    private static MaterialModel materialModel;

    public static MaterialModel getInstance(){
        return materialModel == null?materialModel = new MaterialModel():materialModel;
    }

    private StudentService studentService;

    private MaterialModel(){
        studentService = EhomeworkApplication.getStudentService();
    }

    @Override
    public void findUnitsByParent(Callback<Result<List<Unit>>> callback, Integer unitId){
        studentService.findUnitsByParent(EhomeworkApplication.getToken(),unitId).enqueue(callback);
    }
}