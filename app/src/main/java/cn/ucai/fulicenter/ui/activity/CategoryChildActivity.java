package cn.ucai.fulicenter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.CatFilterCategoryButton;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by clawpo on 2017/3/17.
 */
public class CategoryChildActivity extends AppCompatActivity {
    boolean sortPrice;
    boolean sortAddTime;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    NewGoodsFragment mNewGoodsFragment;
    @BindView(R.id.btn_sort_price)
    Button mBtnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button mBtnSortAddtime;
    String groupName;
    @BindView(R.id.cat_filter)
    CatFilterCategoryButton mCfcbFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mNewGoodsFragment = new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewGoodsFragment)
                .commit();
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        initView();
    }

    private void initView() {
        mCfcbFilter.setText(groupName);
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void sortList(View view) {
        Drawable end = null;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                sortBy = sortPrice ? I.SORT_BY_PRICE_ASC : I.SORT_BY_PRICE_DESC;
                sortPrice = !sortPrice;
                end = getResources().getDrawable(sortPrice ?
                        R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                mBtnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.btn_sort_addtime:
                sortBy = sortAddTime ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                sortAddTime = !sortAddTime;
                end = getResources().getDrawable(sortAddTime ?
                        R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                mBtnSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
        }
        mNewGoodsFragment.sortBy(sortBy);
    }

    @OnClick(R.id.backClickArea)
    public void backOnClick() {
        MFGT.finish(CategoryChildActivity.this);
    }
}
