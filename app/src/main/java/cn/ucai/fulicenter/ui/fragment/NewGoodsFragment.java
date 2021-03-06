package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.NewGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by clawpo on 2017/3/15.
 */

public class NewGoodsFragment extends Fragment {
    private static final String TAG = NewGoodsFragment.class.getSimpleName();

    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    Unbinder bind;
    INewGoodsModel model;
    int pageId = 1;
    GridLayoutManager gm;
    GoodsAdapter adapter;
    List<NewGoodsBean> mList = new ArrayList<>();
    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_nomore)
    TextView mTvNomore;

    int catId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_good, container, false);
        bind = ButterKnife.bind(this, view);

        return view;
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                if (position == mList.size()){
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
        mRvGoods.setLayoutManager(gm);
        mRvGoods.setHasFixedSize(true);
        adapter = new GoodsAdapter(getContext(), mList);
        mRvGoods.setAdapter(adapter);
        mRvGoods.addItemDecoration(new SpaceItemDecoration(12));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new NewGoodsModel();
        catId = getActivity().getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID,catId);
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void initData(final int action) {
        model.loadData(getContext(),catId, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                setRefresh(false);
                adapter.setMore(true);
                L.e(TAG, "initData,result = " + result);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ResultUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mList.clear();
                    }
                    mList.addAll(list);
                    if (list.size() < I.PAGE_SIZE_DEFAULT){
                        adapter.setMore(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "initData,error = " + error);
                CommonUtils.showShortToast(error);
                setRefresh(false);
            }
        });
    }

    private void setRefresh(boolean refresh){
        if (mSrl!=null) {
            mSrl.setRefreshing(refresh);
        }
        if (mTvRefresh!=null) {
            mTvRefresh.setVisibility(refresh ? View.VISIBLE : View.GONE);
        }
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                pageId = 1;
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        mRvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if(newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == adapter.getItemCount()-1
                        && adapter.isMore()){
                    pageId++;
                    initData(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = gm.findFirstVisibleItemPosition();
                mSrl.setEnabled(firstPosition==0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
    }

    public void sortBy(int sortBy) {
        adapter.setsortBy(sortBy);
    }
}
