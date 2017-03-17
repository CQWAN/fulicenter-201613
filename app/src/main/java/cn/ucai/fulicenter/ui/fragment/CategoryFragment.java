package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * Created by clawpo on 2017/3/16.
 */

public class CategoryFragment extends Fragment {
    private static final String TAG = CategoryFragment.class.getSimpleName();
    ICategoryModel model;
    @BindView(R.id.elv_category)
    ExpandableListView mElvCategory;
    List<CategoryGroupBean> groupList = new ArrayList<>();
    List<List<CategoryChildBean>> childList = new ArrayList<>();
    CategoryAdapter adapter;
    int loadIndex = 0;
    @BindView(R.id.layout_tips)
    LinearLayout mLayoutTips;
    View loadView;
    View loadFail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        loadView = LayoutInflater.from(getContext()).inflate(R.layout.loading, mLayoutTips,false);
        loadFail = LayoutInflater.from(getContext()).inflate(R.layout.load_fail,mLayoutTips,false);
        mLayoutTips.addView(loadView);
        mLayoutTips.addView(loadFail);
        showDialog(true,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CategoryModel();
        adapter = new CategoryAdapter(getContext());
        mElvCategory.setAdapter(adapter);
        mElvCategory.setGroupIndicator(null);
        loadGroupData();
    }

    private void loadGroupData() {
        model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null) {
                    ArrayList<CategoryGroupBean> list = ResultUtils.array2List(result);
                    groupList.clear();
                    groupList.addAll(list);
                    for (int i = 0; i < list.size(); i++) {
                        childList.add(new ArrayList<CategoryChildBean>());
                        loadChildData(list.get(i).getId(), i);
                    }
                }else{
                    showDialog(false,false);
                }
            }

            @Override
            public void onError(String error) {
                showDialog(false,false);
            }
        });
    }

    private void loadChildData(int parentId, final int index) {
        model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                loadIndex++;
                if (result != null) {
                    ArrayList<CategoryChildBean> list = ResultUtils.array2List(result);
                    childList.set(index, list);
                }
                if (loadIndex == groupList.size()) {
                    adapter.initData(groupList, childList);
                    L.e(TAG, "load add data....");
                    showDialog(false,true);
                }
            }

            @Override
            public void onError(String error) {
                loadIndex++;
                L.e(TAG, "onError,loadChildData," + error);
                showDialog(false,false);
            }
        });
    }

    @OnClick(R.id.layout_tips)
    public void loadAgain(){
        if (loadFail.getVisibility()==View.VISIBLE){
            loadGroupData();
            showDialog(true,false);
        }
    }

    private void showDialog(boolean dialog,boolean success){
        loadView.setVisibility(dialog?View.VISIBLE:View.GONE);
        if (dialog){
            loadFail.setVisibility(View.GONE);
            mLayoutTips.setVisibility(View.VISIBLE);
        }else{
            mLayoutTips.setVisibility(success?View.GONE:View.VISIBLE);
            loadFail.setVisibility(success?View.GONE:View.VISIBLE);
        }
    }
}
