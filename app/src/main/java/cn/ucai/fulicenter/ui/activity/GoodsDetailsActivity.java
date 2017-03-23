package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by clawpo on 2017/3/16.
 */
public class GoodsDetailsActivity extends AppCompatActivity {
    private static final String TAG = GoodsDetailsActivity.class.getSimpleName();
    IGoodsModel model;
    int goodsId = 0;
    @BindView(R.id.tv_good_name_english)
    TextView mTvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView mTvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView mTvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView mTvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView mSalv;
    @BindView(R.id.indicator)
    FlowIndicator mIndicator;
    @BindView(R.id.wv_good_brief)
    WebView mWvGoodBrief;

    GoodsDetailsBean bean;
    @BindView(R.id.iv_good_collect)
    ImageView mIvGoodCollect;
    boolean isCollects = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            MFGT.finish(GoodsDetailsActivity.this);
            return;
        }
        model = new GoodsModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (bean == null) {
            model.loadData(GoodsDetailsActivity.this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
                @Override
                public void onSuccess(GoodsDetailsBean result) {
                    if (result != null) {
                        bean = result;
                        showDetails();
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(error);
                }
            });
        }
        loadCollectStatus();
    }

    private void loadCollectStatus() {
        User user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            collectAction(I.ACTION_IS_COLLECT,user);
        }
    }

    private void collectAction(final int action, User user) {
        model.collectAction(GoodsDetailsActivity.this,action, goodsId, user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        if (msg != null && msg.isSuccess()) {
                            isCollects = true;
                            if (action==I.ACTION_DELETE_COLLECT){
                                isCollects = false;
                            }
                        } else {
                            isCollects = false;
                            if (action==I.ACTION_DELETE_COLLECT){
                                isCollects = true;
                            }
                        }
                        setCollectStatus();
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG,"error="+error);
                        if (action == I.ACTION_IS_COLLECT) {
                            isCollects = false;
                            setCollectStatus();
                        }
                    }
                });
    }

    private void setCollectStatus() {
        mIvGoodCollect.setImageResource(isCollects?
                R.mipmap.bg_collect_out:R.mipmap.bg_collect_in);
    }

    private void showDetails() {
        mTvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        mTvGoodName.setText(bean.getGoodsName());
        mTvGoodPriceShop.setText(bean.getShopPrice());
        mTvGoodPriceCurrent.setText(bean.getCurrencyPrice());
        mSalv.startPlayLoop(mIndicator, getAblumUrl(), getAblumCount());
        mWvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAblumCount() {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            return bean.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAblumUrl() {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            AlbumsBean[] albums = bean.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[0].getImgUrl();
                }
                return urls;
            }
        }
        return null;
    }

    @OnClick(R.id.iv_good_collect)
    public void collectGoods(){
        User user = FuLiCenterApplication.getCurrentUser();
        if (user==null){
            MFGT.gotoLogin(GoodsDetailsActivity.this,0);
        }else{
            if (isCollects){
                //取消收藏
                collectAction(I.ACTION_DELETE_COLLECT,user);
            }else{
                //添加收藏
                collectAction(I.ACTION_ADD_COLLECT,user);
            }
        }
    }

    @OnClick(R.id.backClickArea)
    public void backClick() {
        MFGT.finish(GoodsDetailsActivity.this);
    }
}
