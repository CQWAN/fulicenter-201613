package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by clawpo on 2017/3/15.
 */

public interface ICartModel {
    void loadData(Context context, String username, OnCompleteListener<CartBean[]> listener);
    void cartAction(Context context, int action, String cartId, String goodsId, String username, int count,
                    OnCompleteListener<MessageBean> listener);
}
