package com.ping.chatdemo.dao;

import android.content.Context;
import android.util.Log;

import com.ping.chatdemo.entity.Msg;
import com.ping.chatdemo.gen.MsgDao;
import com.ping.chatdemo.listener.OnDbUpdateListener;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Mr.sorrow on 2017/5/6.
 */

public class MsgDaoUtil {
    private static final String TAG = MsgDaoUtil.class.getSimpleName();
    private DaoManager mManager;
    private OnDbUpdateListener mUpdateListener;

    public void setUpdateListener(OnDbUpdateListener updateListener) {
        mUpdateListener = updateListener;
    }

    public MsgDaoUtil(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成msg记录的插入，如果表未创建，先创建Msg表
     *
     * @param msg
     * @return
     */
    public boolean insertMsg(Msg msg) {
        boolean flag = false;
        flag = mManager.getDaoSession().getMsgDao().insert(msg) == -1 ? false : true;
        if (flag)
            mUpdateListener.onUpdate(msg);
        Log.i(TAG, "insert Msg :" + flag + "-->" + msg.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param msgList
     * @return
     */
    public boolean insertMultMsg(final List<Msg> msgList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Msg msg : msgList) {
                        mManager.getDaoSession().insertOrReplace(msg);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<Msg> queryAllMsg() {
        return mManager.getDaoSession().loadAll(Msg.class);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<Msg> queryMsgByQueryBuilder(long id) {
        QueryBuilder<Msg> queryBuilder = mManager.getDaoSession().queryBuilder(Msg.class);
        return queryBuilder.where(MsgDao.Properties._id.eq(id)).list();
    }

}
