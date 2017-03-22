package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by clawpo on 2017/3/22.
 */
public class UpdateNickActivity extends AppCompatActivity {
    private static final String TAG = UpdateNickActivity.class.getSimpleName();
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.et_update_user_name)
    EditText mEtUpdateUserName;

    User user;
    IUserModel model;
    String newnick;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        model = new UserModel();
        mTvCommonTitle.setText(R.string.update_user_nick);
        user = FuLiCenterApplication.getCurrentUser();
        if (user==null){
            backArea();
        }else{
            mEtUpdateUserName.setText(user.getMuserNick());
            mEtUpdateUserName.selectAll();
        }
    }

    @OnClick(R.id.backClickArea)
    public void backArea() {
        MFGT.finish(UpdateNickActivity.this);
    }

    @OnClick(R.id.btn_save)
    public void updateNick(){
        if (checkInput()){
            showDialog();
            model.updateNick(UpdateNickActivity.this, user.getMuserName(), newnick,
                    new OnCompleteListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Result result = ResultUtils.getResultFromJson(s, User.class);
                            if (result!=null){
                                if (result.isRetMsg()){
                                    User u = (User) result.getRetData();
                                    updateSuccess(u);
                                }else{
                                    if (result.getRetCode() == I.MSG_USER_SAME_NICK){
                                        CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
                                    }
                                    if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL){
                                        CommonUtils.showShortToast(R.string.update_fail);
                                    }
                                }
                            }
                            pd.dismiss();
                        }

                        @Override
                        public void onError(String error) {
                            CommonUtils.showShortToast(R.string.update_fail);
                            pd.dismiss();
                        }
                    });
        }
    }

    private void updateSuccess(final User u) {
        L.e(TAG,"loginSuccess,user="+u);
        FuLiCenterApplication.setCurrentUser(u);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(UpdateNickActivity.this).saveUserInfo(u);
                L.e(TAG,"loginSuccess,b="+b);
            }
        }).start();
//        setResult(RESULT_OK);
        MFGT.finish(UpdateNickActivity.this);
    }


    private void showDialog() {
        pd = new ProgressDialog(UpdateNickActivity.this);
        pd.setMessage(getString(R.string.update_user_nick));
        pd.show();
    }

    private boolean checkInput() {
        newnick = mEtUpdateUserName.getText().toString().trim();
        if (TextUtils.isEmpty(newnick)){
            mEtUpdateUserName.requestFocus();
            mEtUpdateUserName.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (newnick.equals(user.getMuserNick())){
            mEtUpdateUserName.requestFocus();
            mEtUpdateUserName.setError(getString(R.string.update_nick_fail_unmodify));
            return false;
        }
        return true;
    }
}
