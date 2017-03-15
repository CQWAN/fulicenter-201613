package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by clawpo on 2017/3/15.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<NewGoodsBean> mList;

    public GoodsAdapter(Context context, List<NewGoodsBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        NewGoodsBean bean = mList.get(position);
        vh.mTvGoodsName.setText(bean.getGoodsName());
        vh.mTvGoodsPrice.setText(bean.getCurrencyPrice());
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    class GoodsViewHolder extends ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView mIvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView mTvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView mTvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout mLayoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
