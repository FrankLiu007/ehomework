package name.wanghwx.ehomework.view.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

import butterknife.ButterKnife;
import name.wanghwx.ehomework.common.constant.Directory;
import name.wanghwx.ehomework.common.constant.Permission;
import name.wanghwx.ehomework.presenter.base.BasePresenter;

public abstract class BaseActivity<V extends BaseView,P extends BasePresenter<V>> extends AppCompatActivity{

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        verifyPermissions(Permission.STORAGE_CODE);
        setContentView(getResourceId());
        smoothFullscreen();
        presenter = createPresenter();
        ButterKnife.bind(this);
        initView();
        setIconFont();
        bindListener();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshView();
    }

    @Override
    protected void onDestroy(){
        presenter.detachView();
        super.onDestroy();
    }

    public Context getContext(){
        return this;
    }

    protected abstract int getResourceId();

    protected abstract P createPresenter();

    protected abstract void initView();

    protected abstract void bindListener();

    protected abstract List<TextView> getIcons();

    protected abstract void refreshView();

    private void setIconFont(){
        List<TextView> icons = getIcons();
        if(icons != null && icons.size() > 0){
            Typeface typeface = Typeface.createFromAsset(getAssets(), Directory.ICON_FONT);
            for(TextView icon:icons){
                icon.setTypeface(typeface);
            }
        }
    }

    protected void verifyPermissions(int requestCode){
        switch(requestCode){
            case Permission.STORAGE_CODE:
                if(ActivityCompat.checkSelfPermission(this,Permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,Permission.STORAGE,requestCode);
                }
                break;
            default:
                break;
        }
    }

    private void smoothFullscreen(){
        TypedValue typedValue = new TypedValue();
        this.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowFullscreen}).getValue(0,typedValue);
        if(typedValue.type == TypedValue.TYPE_INT_BOOLEAN){
            if(typedValue.data == 0){
                View rootView = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
                this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                int statusBarHeight = getResources().getDimensionPixelSize(resId);
                rootView.setPadding(0,statusBarHeight,0,0);
                this.sendBroadcast(new Intent("show_status_bar"));
            }
        }
    }

}