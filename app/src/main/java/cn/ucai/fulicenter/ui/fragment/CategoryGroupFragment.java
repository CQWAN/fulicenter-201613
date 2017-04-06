package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;

/**
 * Created by clawpo on 2017/4/6.
 */

public class CategoryGroupFragment extends ListFragment {
    ICategoryModel model;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new CategoryModel();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        new WorkThread().start();
    }

    public void setAdapter(ArrayList<CategoryGroupBean> list){
        setListAdapter(new ArrayAdapter<CategoryGroupBean>(getContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                list));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CategoryGroupBean groupBean = (CategoryGroupBean) getListView().getItemAtPosition(position);
        CommonUtils.showLongToast(groupBean.getId()+","+groupBean.getName());
    }

    class WorkThread extends Thread{
        @Override
        public void run() {
            model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
                @Override
                public void onSuccess(CategoryGroupBean[] result) {
                    if (result != null) {
                        ArrayList<CategoryGroupBean> list = ResultUtils.array2List(result);
                        setAdapter(list);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
}
