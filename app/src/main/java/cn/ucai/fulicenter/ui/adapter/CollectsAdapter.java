package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.FooterViewHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by clawpo on 2017/3/15.
 */

public class CollectsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<CollectBean> mList;
    boolean isMore;
    IGoodsModel model;

    public CollectsAdapter(Context context, List<CollectBean> list) {
        mContext = context;
        mList = list;
        isMore = true;
        model = new GoodsModel();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = null;
        if (viewType == I.TYPE_FOOTER){
            vh = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        }else {
            vh = new GoodsViewHolder(View.inflate(mContext, R.layout.item_collects, null));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER){
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.setFooter(getFooterString());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        vh.bind(position);

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


    class GoodsViewHolder extends ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView mIvGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView mTvGoodsName;
        @BindView(R.id.iv_collect_del)
        ImageView mIvCollectDel;
        @BindView(R.id.layout_goods)
        RelativeLayout mLayoutGoods;


        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bind(final int position) {
            final CollectBean bean = mList.get(position);
            mTvGoodsName.setText(bean.getGoodsName());
            ImageLoader.downloadImg(mContext, mIvGoodsThumb, bean.getGoodsThumb());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MFGT.gotoDetails(mContext,bean.getGoodsId());
                }
            });

            mIvCollectDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeCollect(position,bean.getGoodsId());
                }
            });
        }

        private void removeCollect(final int position, int goodsId){
            model.collectAction(mContext, I.ACTION_DELETE_COLLECT, goodsId,
                    FuLiCenterApplication.getCurrentUser().getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result!=null && result.isSuccess()){
                                mList.remove(position);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
