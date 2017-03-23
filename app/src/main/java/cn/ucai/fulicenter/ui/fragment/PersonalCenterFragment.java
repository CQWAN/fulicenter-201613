package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by clawpo on 2017/3/21.
 */

public class PersonalCenterFragment extends Fragment {
    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_collect_count)
    TextView mTvCollectCount;
    @BindView(R.id.center_user_order_lis)
    GridView mCenterUserOrderLis;
    User user;

    IUserModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new UserModel();
        initOrderList();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getCurrentUser();
        if (user!=null){
            showUserInfo();
            loadCollectsCount();
        }
    }

    private void loadCollectsCount() {
        model.loadCollectsCount(getContext(), user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        if (msg!=null && msg.isSuccess()){
                            mTvCollectCount.setText(msg.getMsg());
                        }else{
                            mTvCollectCount.setText("0");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        mTvCollectCount.setText("0");
                    }
                });
    }

    private void showUserInfo() {
        mTvUserName.setText(user.getMuserNick());
        ImageLoader.setAvatar(user.getAvatar(),getContext(),mIvUserAvatar);
    }

    @OnClick({R.id.tv_center_settings,R.id.center_user_info})
    public void goSettings(){
        MFGT.gotoSettings(getActivity());
    }

    private void initOrderList() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> order1 = new HashMap<String, Object>();
        order1.put("order", R.drawable.order_list1);
        data.add(order1);
        HashMap<String, Object> order2 = new HashMap<String, Object>();
        order2.put("order", R.drawable.order_list2);
        data.add(order2);
        HashMap<String, Object> order3 = new HashMap<String, Object>();
        order3.put("order", R.drawable.order_list3);
        data.add(order3);
        HashMap<String, Object> order4 = new HashMap<String, Object>();
        order4.put("order", R.drawable.order_list4);
        data.add(order4);
        HashMap<String, Object> order5 = new HashMap<String, Object>();
        order5.put("order", R.drawable.order_list5);
        data.add(order5);
        SimpleAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.simple_adapter,
                new String[]{"order"}, new int[]{R.id.iv_order});
        mCenterUserOrderLis.setAdapter(adapter);
    }

    @OnClick(R.id.layout_center_collect)
    public void collectList(){
        MFGT.gotoCollectsList(getActivity());
    }
}
