package com.example.httpchatdemo;

import android.os.Bundle;
import android.view.View;

import com.example.httpchatdemo.adapter.ChatRecyclerAdapter;
import com.example.httpchatdemo.bean.ChatMessageBean;
import com.example.httpchatdemo.utils.KeyBoardUtils;
import com.example.httpchatdemo.utils.Utils;

public class ChatActivity extends BaseActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        initView();
//        initData();
    }





    protected void sendMessage() {
        ChatMessageBean tbub = new ChatMessageBean();
        tbub.setUserContent(mEditTextContent.getText().toString());
        tbub.setUserName("kent");
        String time = Utils.returnTime();
        tbub.setTime(time);
        tbub.setType(ChatRecyclerAdapter.TO_USER_MSG);
        tbub.setImageIconUrl(null);
        tbub.setSendState(ChatRecyclerAdapter.COMPLETED);
        tbub.setImageLocal(null);
        tblist.add(tbub);
        tbAdapter.notifyDataSetChanged();
        mList.smoothScrollToPosition(tbAdapter.getItemCount());
    }

    protected void sendImage(String galPicPath) {
        ChatMessageBean tbub = new ChatMessageBean();
        tbub.setUserName("kent");
        String time = Utils.returnTime();
        tbub.setTime(time);
        tbub.setType(ChatRecyclerAdapter.TO_USER_IMG);
        tbub.setImageIconUrl(null);
        tbub.setImageUrl(galPicPath);
        tbub.setSendState(ChatRecyclerAdapter.COMPLETED);
        tbub.setImageLocal(null);
        tblist.add(tbub);
        tbAdapter.notifyDataSetChanged();
        mList.smoothScrollToPosition(tbAdapter.getItemCount());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mess_iv:
                emoji_group.setVisibility(View.GONE);
                if (tbbv.getVisibility() == View.GONE) {
                    mEditTextContent.setVisibility(View.VISIBLE);
                    mess_iv.setFocusable(true);
                    emoji.setBackgroundResource(R.mipmap.emoji);
                    voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                    tbbv.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(ChatActivity.this,
                            mEditTextContent);
                    mess_iv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                } else {
                    tbbv.setVisibility(View.GONE);
                    KeyBoardUtils.showKeyBoard(ChatActivity.this, mEditTextContent);
                    mess_iv.setBackgroundResource(R.mipmap.tb_more);

                }
                break;
            case R.id.send_emoji_icon:
                sendMessage();
                break;
            case R.id.emoji:
                tbbv.setVisibility(View.GONE);
                if (emoji_group.getVisibility() == View.GONE) {
                    mEditTextContent.setVisibility(View.VISIBLE);
                    voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                    mess_iv.setBackgroundResource(R.mipmap.tb_more);
                    emoji_group.setVisibility(View.VISIBLE);
                    emoji.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                    KeyBoardUtils.hideKeyBoard(ChatActivity.this,
                            mEditTextContent);
                } else {
                    emoji_group.setVisibility(View.GONE);
                    emoji.setBackgroundResource(R.mipmap.emoji);
                    KeyBoardUtils.showKeyBoard(ChatActivity.this, mEditTextContent);
                }
                break;
            case R.id.mess_et:
                emoji_group.setVisibility(View.GONE);
                tbbv.setVisibility(View.GONE);
                emoji.setBackgroundResource(R.mipmap.emoji);
                mess_iv.setBackgroundResource(R.mipmap.tb_more);
                voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                break;
        }
    }

}
