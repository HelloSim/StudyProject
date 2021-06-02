package com.sim.user.utils;

import com.sim.user.bean.NewsBean;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @ author: Sim
 * @ time： 2021/6/2 18:14
 * @ description：
 */
public class NewsUtil {

    /**
     * 获取用户收藏的所有NewsBean
     *
     * @param successOrFailListener
     */
    public static void getNewsBean(SuccessOrFailListener successOrFailListener) {
        BmobQuery<NewsBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("user", UserUtil.getInstance().getUser());
        bmobQuery.findObjects(new FindListener<NewsBean>() {
            @Override
            public void done(List<NewsBean> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    successOrFailListener.success(list);
                } else if (e == null && (list == null || list.size() == 0)) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }
        });
    }

    /**
     * 删除收藏的NewsBean
     *
     * @param bean
     * @param successOrFailListener
     */
    public static void deleteNewsBean(NewsBean bean, SuccessOrFailListener successOrFailListener) {
        bean.setObjectId(bean.getObjectId());
        bean.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    successOrFailListener.success();
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }

        });
    }

}
