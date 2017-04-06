package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by clawpo on 2017/4/6.
 */

public class CategoryChildFragment extends ListFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListAdapter(new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{"1","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2","2"}));
    }
}
