package com.example.httpchatdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.httpchatdemo.bean.ChatMessageBean;
import com.example.httpchatdemo.bean.ChatMessageBeanDao;
import com.example.httpchatdemo.bean.DaoMaster;
import com.example.httpchatdemo.bean.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ChatDBManager {
    private final static String dbName = "LiNiu_chat.db";
    private static ChatDBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public ChatDBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);

    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static ChatDBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ChatDBManager.class) {
                if (mInstance == null) {
                    mInstance = new ChatDBManager(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
    /**
     * 插入一条记录
     *
     * @param msg
     */
    public void insertChatMessage(ChatMessageBean msg) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ChatMessageBeanDao msgDao = daoSession.getChatMessageBeanDao();
        msgDao.insert(msg);
    }
    /**
     * 插入消息集合
     *
     * @param msgList
     */
    public void insertMsgList(List<ChatMessageBean> msgList) {
        if (msgList == null || msgList.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ChatMessageBeanDao msgDao = daoSession.getChatMessageBeanDao();
        msgDao.insertInTx(msgList);
    }
    /**
     * 删除一条记录
     *
     * @param msg
     */
    public void deleteUser(ChatMessageBean msg) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ChatMessageBeanDao msgDao = daoSession.getChatMessageBeanDao();
        msgDao.delete(msg);
    }
    /**
     * 更新一条记录
     *
     * @param msg
     */
    public void updateUser(ChatMessageBean msg) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ChatMessageBeanDao msgDao = daoSession.getChatMessageBeanDao();
        msgDao.update(msg);
    }

    /**
     * 查询消息列表
     */
    public List<ChatMessageBean> queryMsgList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ChatMessageBeanDao userDao = daoSession.getChatMessageBeanDao();
        QueryBuilder<ChatMessageBean> qb = userDao.queryBuilder();
        List<ChatMessageBean> list = qb.list();
        return list;
    }
    /**
     * 根据用户id查询消息列表
     */
    public List<ChatMessageBean> queryMsgList(int id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ChatMessageBeanDao msgDao = daoSession.getChatMessageBeanDao();
        QueryBuilder<ChatMessageBean> qb = msgDao.queryBuilder();
        qb.where(ChatMessageBeanDao.Properties.UserId.eq(id)).orderAsc(ChatMessageBeanDao.Properties.UserId);
        List<ChatMessageBean> list = qb.list();
        return list;
    }
}
