package name.wanghwx.ehomework.common.constant;

public interface Permission{

    int STORAGE_CODE = 1;

    String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    String[] STORAGE = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE};

}