package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.FooterViewHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by clawpo on 2017/3/15.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<NewGoodsBean> mList;
    boolean isMore;
    int sortBy = I.SORT_BY_ADDTIME_DESC;

    public GoodsAdapter(Context context, List<NewGoodsBean> list) {
        mContext = context;
        mList = list;
        isMore = true;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = null;
        if (viewType == I.TYPE_FOOTER){
            vh = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        }else {
            vh = new GoodsViewHolder(View.inflate(mContext, R.layout.item_goods, null));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER){
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.setFooter(getFooterString());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        final NewGoodsBean bean = mList.get(position);
        vh.mTvGoodsName.setText(bean.getGoodsName());
        vh.mTvGoodsPrice.setText(bean.getCurrencyPrice());
        ImageLoader.downloadImg(mContext, vh.mIvGoodsThumb, bean.getGoodsThumb());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoDetails(mContext,bean.getGoodsId());
            }
        });
    }

    private int getFooterString() {
        return isMore?R.string.load_more:R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size()+1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void setsortBy(int sy) {
        this.sortBy = sy;
        sortBy();
    }

    private void sortBy(){
        Collections.sort(mList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean l, NewGoodsBean r) {
                int result = 0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (l.getAddTime()-r.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (r.getAddTime()-l.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(l.getCurrencyPrice())-getPrice(r.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(r.getCurrencyPrice())-getPrice(l.getCurrencyPrice());
                        break;
                }
                return result;
            }
        });
        notifyDataSetChanged();
    }

    private int getPrice(String p){
        String pStr = p.substring(p.indexOf("￥")+1);
        return Integer.valueOf(pStr);
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
