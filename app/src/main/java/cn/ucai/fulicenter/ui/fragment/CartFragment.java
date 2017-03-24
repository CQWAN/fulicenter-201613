package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by clawpo on 2017/3/24.
 */

public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";

    ICartModel model;
    @BindView(R.id.tv_cart_sum_price)
    TextView mTvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView mTvCartSavePrice;
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.tv_nothing)
    TextView mTvNothing;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    User user;
    ArrayList<CartBean> cartList = new ArrayList<>();
    LinearLayoutManager gm;
    CartAdapter adapter;
    @BindView(R.id.layout_cart)
    RelativeLayout mLayoutCart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CartModel();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
    }

    private void setRefresh(boolean refresh) {
        mSrl.setRefreshing(refresh);
        mTvRefresh.setVisibility(refresh ? View.VISIBLE : View.GONE);
    }

    private void setCartListLayut(boolean isShow) {
        mTvNothing.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mLayoutCart.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();
            }
        });
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(gm);
        mRv.setHasFixedSize(true);
        adapter = new CartAdapter(getContext(), cartList);
        mRv.setAdapter(adapter);
        mRv.addItemDecoration(new SpaceItemDecoration(12));
        setCartListLayut(false);
    }

    private void initData() {
        user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            showCartList();
        }
    }

    private void showCartList() {
        model.loadData(getContext(), user.getMuserName(),
                new OnCompleteListener<CartBean[]>() {
                    @Override
                    public void onSuccess(CartBean[] result) {
                        setRefresh(false);
                        setCartListLayut(true);
                        if (result != null) {
                            cartList.clear();
                            if (result.length > 0) {
                                ArrayList<CartBean> list = ResultUtils.array2List(result);
                                cartList.addAll(list);
                                adapter.notifyDataSetChanged();
                            }else{
                                setCartListLayut(false);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        setRefresh(false);
                        Log.e(TAG, "onError,error=" + error);
                    }
                });
    }
}
