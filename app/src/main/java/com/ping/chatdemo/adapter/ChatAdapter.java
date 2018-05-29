package com.ping.chatdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.palmaplus.nagrand.api_demo.R;
import com.ping.chatdemo.entity.Msg;

import java.util.List;


/**
 * Created by Mr.sorrow on 2017/5/6.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Msg> mDatas;

    public ChatAdapter(Context context, List<Msg> datas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDatas = datas;
    }

    public void addItem(Msg msg) {
        mDatas.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Msg.TYPE_BLE) {
            View view = mLayoutInflater.inflate(R.layout.item_chat_left, parent, false);
            return new ChatLeftViewHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.item_chat_right, parent, false);
            return new ChatRightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Msg msg = mDatas.get(position);
        String time = msg.getTime();
        String content = msg.getContent();
        if (holder instanceof ChatLeftViewHolder) {
            ((ChatLeftViewHolder) holder).mTvLeftTime.setText(time);
            ((ChatLeftViewHolder) holder).mTvMsgLeft.setText(content);
        } else if (holder instanceof ChatRightViewHolder) {
            ((ChatRightViewHolder) holder).mTvRightTime.setText(time);
            ((ChatRightViewHolder) holder).mTvMsgRight.setText(content);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ChatLeftViewHolder extends RecyclerView.ViewHolder {

        TextView mTvLeftTime;

        TextView mTvMsgLeft;

        ChatLeftViewHolder(View view) {
            super(view);
            mTvLeftTime = (TextView) view.findViewById(R.id.tv_left_time);
            mTvMsgLeft = (TextView) view.findViewById(R.id.tv_msg_left);
        }
    }

    static class ChatRightViewHolder extends RecyclerView.ViewHolder {

        TextView mTvRightTime;

        TextView mTvMsgRight;

        ChatRightViewHolder(View view) {
            super(view);
            mTvRightTime = (TextView) view.findViewById(R.id.tv_right_time);
            mTvMsgRight = (TextView) view.findViewById(R.id.tv_msg_right);

        }
    }
}
