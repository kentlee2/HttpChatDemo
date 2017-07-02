package com.example.httpchatdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.httpchatdemo.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/2.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private  ArrayList<Map<String, Object>> contactList;
    private OnItemClickListener mOnItemClickListener;
    private  LayoutInflater inflate;

    public ContactAdapter(Context context, ArrayList<Map<String, Object>> contactList) {
        inflate = LayoutInflater.from(context);
        this.contactList = contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflate.inflate(R.layout.item_contact,parent,false);
        ContactViewHolder holder = new ContactViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
               holder.tv_title.setText(contactList.get(position).get("title").toString());
             holder.iv_head.setImageResource((int)contactList.get(position).get("head"));
           holder.tv_content.setText(contactList.get(position).get("content").toString());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private  TextView tv_content;
        private  TextView tv_title;
        private  ImageView iv_head;

        public ContactViewHolder(View itemView) {
            super(itemView);
            iv_head = (ImageView)itemView.findViewById(R.id.iv_head);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            RelativeLayout rl_contact = (RelativeLayout)itemView.findViewById(R.id.rl_contact);
            rl_contact.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(view,getLayoutPosition());
            }
        }
    }


    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
