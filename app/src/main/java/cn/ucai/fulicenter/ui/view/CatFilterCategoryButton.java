package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.ui.adapter.CatFilterAdapter;

/**
 * Created by clawpo on 2017/3/17.
 */

public class CatFilterCategoryButton extends Button {
    private static final String TAG = "CatFilterCategoryButton";

    Context mContext;
    boolean isExpan = false;
    PopupWindow mPopupWindow;
    GridView gv;
    CatFilterAdapter adapter;
    ArrayList<CategoryChildBean> list = new ArrayList<>();

    //butterknife实例化
    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCatFilterOnClickListener();
    }

    private void setCatFilterOnClickListener() {

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpan) {
                    initPop();
                } else {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                showArrow();
            }
        });
    }

    private void initPop() {
        //popwin只实例化一次
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mContext);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            mPopupWindow.setContentView(gv);
        }
        //显示的位置
        mPopupWindow.showAsDropDown(this);
    }

    private void showArrow() {
        Drawable end = getResources().getDrawable(isExpan ?
                R.drawable.arrow2_up : R.drawable.arrow2_down);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
        isExpan = !isExpan;
    }

    public void initView(String groupName, ArrayList<CategoryChildBean> l) {
        if (groupName==null || l==null){
            CommonUtils.showShortToast("小类数据获取异常");
            return;
        }
        this.setText(groupName);
        list = l;

        //实例化列表控件
        gv = new GridView(mContext);
        gv.setHorizontalSpacing(10);
        gv.setVerticalSpacing(10);
        gv.setNumColumns(GridView.AUTO_FIT);
        //列表里面显示数据的adapter适配器
        adapter = new CatFilterAdapter(mContext,list,groupName);
        gv.setAdapter(adapter);
    }


    public void reseale() {
        if (mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }
}
