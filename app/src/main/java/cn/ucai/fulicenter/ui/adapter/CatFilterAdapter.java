package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by clawpo on 2017/3/20.
 */

public class CatFilterAdapter extends BaseAdapter {
    Context mContext;
    List<CategoryChildBean> mlist;

    public CatFilterAdapter(Context context, List<CategoryChildBean> mlist) {
        mContext = context;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist != null ? mlist.size() : 0;
    }

    @Override
    public CategoryChildBean getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        CatFilterViewHolder vh = null;
        //首次加载,生成布局
        if (view==null){
            view = View.inflate(mContext, R.layout.item_cat_filter, null);
            vh = new CatFilterViewHolder(view);
            view.setTag(vh);
        }else{
            vh = (CatFilterViewHolder) view.getTag();
        }
        //viewholder绑定数据
        vh.bind(position);
        return view;
    }

    class CatFilterViewHolder {
        @BindView(R.id.ivCategoryChildThumb)
        ImageView mIvCategoryChildThumb;
        @BindView(R.id.tvCategoryChildName)
        TextView mTvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout mLayoutCategoryChild;

        CatFilterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            CategoryChildBean bean = mlist.get(position);
            mTvCategoryChildName.setText(bean.getName());
            ImageLoader.downloadImg(mContext,mIvCategoryChildThumb,bean.getImageUrl());
        }
    }
}
