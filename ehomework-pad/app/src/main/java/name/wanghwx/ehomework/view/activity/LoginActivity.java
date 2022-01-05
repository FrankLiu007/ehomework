package name.wanghwx.ehomework.view.activity;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onyx.android.sdk.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import name.wanghwx.ehomework.EhomeworkApplication;
import name.wanghwx.ehomework.R;
import name.wanghwx.ehomework.presenter.LoginPresenter;
import name.wanghwx.ehomework.presenter.ipresenter.ILoginActivityPresenter;
import name.wanghwx.ehomework.view.base.BaseActivity;
import name.wanghwx.ehomework.view.iactivity.ILoginActivity;

public class LoginActivity extends BaseActivity<ILoginActivity, ILoginActivityPresenter> implements ILoginActivity{

    @BindView(R.id.icon_user)
    TextView userIcon;
    @BindView(R.id.et_account)
    EditText accountEditText;
    @BindView(R.id.icon_lock)
    TextView lockIcon;
    @BindView(R.id.et_password)
    EditText passwordEditText;
    @BindView(R.id.icon_eye)
    TextView eyeIcon;
    @BindView(R.id.btn_login)
    Button loginButton;

    @OnClick(R.id.icon_eye)
    public void toggleVisible(){
        presenter.toggleVisible();
    }

    @OnClick(R.id.btn_login)
    public void login(){
        String account = accountEditText.getText().toString();
        if(StringUtils.isBlank(account)){
            Toast.makeText(this,"账号不能为空",Toast.LENGTH_SHORT).show();
        }else{
            String password = passwordEditText.getText().toString();
            if(StringUtils.isBlank(password)){
                Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            }else{
                presenter.login(account,password);
            }
        }
    }

    @Override
    protected ILoginActivityPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getResourceId(){
        return R.layout.activity_login;
    }

    @Override
    protected void initView(){
        presenter.autoLogin();
    }

    @Override
    protected void bindListener(){}

    @Override
    protected List<TextView> getIcons(){
        return Arrays.asList(userIcon,lockIcon,eyeIcon);
    }

    @Override
    protected void refreshView(){
        String account = EhomeworkApplication.getAccount();
        if(StringUtils.isNotBlank(account)){
            accountEditText.setText(account);
            accountEditText.setSelection(account.length());
        }
        String password = EhomeworkApplication.getPassword();
        if(StringUtils.isNotBlank(password)){
            passwordEditText.setText(password);
            passwordEditText.setSelection(password.length());
        }
    }

    @Override
    public void renderVisible(){
        if(presenter.getVisible()){
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeIcon.setText(R.string.icon_eye_close);
        }else{
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeIcon.setText(R.string.icon_eye_open);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    @Override
    public void loginSuccess(){
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public void nullData(){}

}