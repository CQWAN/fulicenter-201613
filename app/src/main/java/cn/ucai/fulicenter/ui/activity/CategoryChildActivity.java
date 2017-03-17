package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

/**
 * Created by clawpo on 2017/3/17.
 */
public class CategoryChildActivity extends AppCompatActivity {
    boolean sortPrice;
    boolean sortAddTime;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    NewGoodsFragment mNewGoodsFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mNewGoodsFragment = new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .commit();
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void sortList(View view) {
        switch (view.getId()) {
            case R.id.btn_sort_price:
                sortBy = sortPrice?I.SORT_BY_PRICE_ASC:I.SORT_BY_PRICE_DESC;
                sortPrice  = !sortPrice;
                break;
            case R.id.btn_sort_addtime:
                sortBy = sortAddTime?I.SORT_BY_ADDTIME_ASC:I.SORT_BY_ADDTIME_DESC;
                sortAddTime  = !sortAddTime;
                break;
        }
        mNewGoodsFragment.sortBy(sortBy);
    }
}
