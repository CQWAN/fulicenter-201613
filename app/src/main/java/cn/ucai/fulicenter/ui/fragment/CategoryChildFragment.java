package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;

/**
 * Created by clawpo on 2017/4/6.
 */

public class CategoryChildFragment extends Fragment {
    ICategoryModel model;
    int parentId = 344;
    @BindView(R.id.rv_category_child)
    RecyclerView mRvCategoryChild;
    childAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_child, container, false);
        ButterKnife.bind(this, view);
        initView();
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mRvCategoryChild.setHasFixedSize(true);
        StaggeredGridLayoutManager sgm = new StaggeredGridLayoutManager(I.COLUM_NUM,StaggeredGridLayoutManager.VERTICAL);
        mRvCategoryChild.setLayoutManager(sgm);
        adapter = new childAdapter(new ArrayList<CategoryChildBean>());
        mRvCategoryChild.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CategoryModel();
        syncData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(CategoryGroupBean group){
        L.e("child","group="+group);
        if (group!=null) {
            parentId = group.getId();
            syncData();
        }
    }

    public void syncData() {
        model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                if (result != null) {
                    ArrayList<CategoryChildBean> list = ResultUtils.array2List(result);
                    adapter.setChildList(list);
                }
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    class childAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<CategoryChildBean> childList = new ArrayList<>();

        public childAdapter(ArrayList<CategoryChildBean> childList) {
            this.childList = childList;
        }

        public void setChildList(ArrayList<CategoryChildBean> childList) {
            this.childList = childList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_child, parent, false);
            ChildViewHolder vh = new ChildViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ChildViewHolder vh = (ChildViewHolder) holder;
            final CategoryChildBean child = childList.get(position);
            if (child != null) {
                vh.mTvChild.setText(child.getName());
                ImageLoader.downloadImg(getContext(), vh.mIvChild, child.getImageUrl());
            }
        }

        @Override
        public int getItemCount() {
            return childList.size();
        }

        class ChildViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_child)
            ImageView mIvChild;
            @BindView(R.id.tv_child)
            TextView mTvChild;

            ChildViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
