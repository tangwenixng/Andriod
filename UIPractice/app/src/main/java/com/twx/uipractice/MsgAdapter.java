package com.twx.uipractice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by twx on 2017/3/1.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> mMsgList;

    public MsgAdapter(List<Msg> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public int getItemCount() {
        return  mMsgList.size();
    }

    static  class  ViewHolder extends  RecyclerView.ViewHolder{
        LinearLayout leftLayout,rightLayout;
        TextView leftMsg,rightMsg;
        public ViewHolder(View itemView) {
            super(itemView);
             leftLayout = (LinearLayout)itemView.findViewById(R.id.left_layout);
             rightLayout = (LinearLayout)itemView.findViewById(R.id.left_layout);
             leftMsg = (TextView)itemView.findViewById(R.id.left_msg);
             rightMsg = (TextView)itemView.findViewById(R.id.right_msg);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        if(msg.getType()==Msg.TYPE_RECEIVE){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if (msg.getType()==Msg.TYPE_SEND){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }
    }
}
