package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by clawpo on 2017/3/20.
 */
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.nick)
    EditText mNick;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvCommonTitle.setText(R.string.register_title);
    }

    @OnClick({R.id.backClickArea, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(RegisterActivity.this);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {

    }
}
