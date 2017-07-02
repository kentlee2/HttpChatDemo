package com.example.httpchatdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.httpchatdemo.CustomView.CustomShapeTransformation;
import com.example.httpchatdemo.R;
import com.example.httpchatdemo.bean.ChatMessageBean;
import com.example.httpchatdemo.utils.FileSaveUtil;
import com.example.httpchatdemo.utils.SmileUtils;
import com.example.httpchatdemo.utils.ToastUtils;
import com.example.httpchatdemo.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessageBean> userList = new ArrayList<ChatMessageBean>();
    private ArrayList<String> imageList = new ArrayList<String>();
    private HashMap<Integer, Integer> imagePosition = new HashMap<Integer, Integer>();
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int SENDING = 0;
    public static final int COMPLETED = 1;
    public static final int SENDERROR = 2;
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;
    private Animation an;
    private SendErrorListener sendErrorListener;
    public List<String> unReadPosition = new ArrayList<String>();
    private int voicePlayPosition = -1;
    private LayoutInflater mLayoutInflater;
    private boolean isGif = true;
    public boolean isPicRefresh = true;

    public interface SendErrorListener {
        public void onClick(int position);
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }


    public ChatRecyclerAdapter(Context context, List<ChatMessageBean> userList) {
        this.context = context;
        this.userList = userList;
        mLayoutInflater = LayoutInflater.from(context);
        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.5f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
    }

    public void setIsGif(boolean isGif) {
        this.isGif = isGif;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setImagePosition(HashMap<Integer, Integer> imagePosition) {
        this.imagePosition = imagePosition;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case FROM_USER_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgfrom_list_item, parent, false);
                holder = new FromUserMsgViewHolder(view);
                break;
            case FROM_USER_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_imagefrom_list_item, parent, false);
                holder = new FromUserImageViewHolder(view);
                break;
            case TO_USER_MSG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_msgto_list_item, parent, false);
                holder = new ToUserMsgViewHolder(view);
                break;
            case TO_USER_IMG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_imageto_list_item, parent, false);
                holder = new ToUserImgViewHolder(view);
                break;
        }
        return holder;
    }

    class FromUserMsgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;
        private TextView chat_time;
        private TextView content;

        public FromUserMsgViewHolder(View view) {
            super(view);
            headicon = (ImageView) view.findViewById(R.id.tb_other_user_icon);
            chat_time = (TextView) view.findViewById(R.id.chat_time);
            content = (TextView) view.findViewById(R.id.content);
        }
    }

    class FromUserImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;
        private TextView chat_time;
        private ImageView image_Msg;

        public FromUserImageViewHolder(View view) {
            super(view);
            headicon = (ImageView) view.findViewById(R.id.tb_other_user_icon);
            chat_time = (TextView) view.findViewById(R.id.chat_time);
            image_Msg = (ImageView) view.findViewById(R.id.image_message);
        }
    }


    class ToUserMsgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;
        private TextView chat_time;
        private TextView content;
        private ImageView sendFailImg;

        public ToUserMsgViewHolder(View view) {
            super(view);
            headicon = (ImageView) view
                    .findViewById(R.id.tb_my_user_icon);
            chat_time = (TextView) view
                    .findViewById(R.id.mychat_time);
            content = (TextView) view
                    .findViewById(R.id.mycontent);
            sendFailImg = (ImageView) view
                    .findViewById(R.id.mysend_fail_img);
        }
    }

    class ToUserImgViewHolder extends RecyclerView.ViewHolder {
        private ImageView headicon;
        private TextView chat_time;
        private LinearLayout image_group;
        private ImageView image_Msg;
        private ImageView sendFailImg;

        public ToUserImgViewHolder(View view) {
            super(view);
            headicon = (ImageView) view
                    .findViewById(R.id.tb_my_user_icon);
            chat_time = (TextView) view
                    .findViewById(R.id.mychat_time);
            sendFailImg = (ImageView) view
                    .findViewById(R.id.mysend_fail_img);
            image_group = (LinearLayout) view
                    .findViewById(R.id.image_group);
            image_Msg = (ImageView) view
                    .findViewById(R.id.image_message);
        }
    }


    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessageBean tbub = userList.get(position);
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case FROM_USER_MSG:
                fromMsgUserLayout((FromUserMsgViewHolder) holder, tbub, position);
                break;
            case FROM_USER_IMG:
                fromImgUserLayout((FromUserImageViewHolder) holder, tbub, position);
                break;
            case TO_USER_MSG:
                toMsgUserLayout((ToUserMsgViewHolder) holder, tbub, position);
                break;
            case TO_USER_IMG:
                toImgUserLayout((ToUserImgViewHolder) holder, tbub, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return userList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * 接收方普通消息
     * @param holder
     * @param tbub
     * @param position
     */
    private void fromMsgUserLayout(final FromUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_launcher);
        /* time */
        if (position != 0) {
            String showTime = Utils.getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = Utils.getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
        holder.content.setVisibility(View.VISIBLE);
        Spannable span = SmileUtils.getSmiledText(context,  tbub.getUserContent());
        holder.content.setText(span, TextView.BufferType.SPANNABLE);
    }

    /**
     * 接收方图片消息
     * @param holder
     * @param tbub
     * @param position
     */
    private void fromImgUserLayout(final FromUserImageViewHolder holder, final ChatMessageBean tbub, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.ic_launcher);
        /* time */
        if (position != 0) {
            String showTime = Utils.getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = Utils.getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }
//        if (isPicRefresh) {
            final String imageSrc = tbub.getImageLocal() == null ? "" : tbub
                    .getImageLocal();
            final String imageUrlSrc = tbub.getImageUrl() == null ? "" : tbub
                    .getImageUrl();
            final String imageIconUrl = tbub.getImageIconUrl() == null ? ""
                    : tbub.getImageIconUrl();
            File file = new File(imageSrc);
            final boolean hasLocal = !imageSrc.equals("")
                    && FileSaveUtil.isFileExists(file);
            int res;
            res = R.drawable.chatfrom_bg_focused;
            Glide.with(context).load(imageUrlSrc).transform(new CustomShapeTransformation(context, res)).into(holder.image_Msg);
            holder.image_Msg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ToastUtils.showToast("点击了图片");
//                    Intent intent = new Intent(context, ImageViewActivity.class);
//                    intent.putStringArrayListExtra("images", imageList);
//                    intent.putExtra("clickedIndex", imagePosition.get(position));
//                    context.startActivity(intent);
                }

            });
//        }

    }

    /**
     * 发送方普通消息
     * @param holder
     * @param tbub
     * @param position
     */
    private void toMsgUserLayout(final ToUserMsgViewHolder holder, final ChatMessageBean tbub, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.test_icon);
//        holder.headicon.setImageDrawable(context.getResources()
//                .getDrawable(R.mipmap.test_icon));
        /* time */
        if (position != 0) {
            String showTime = Utils.getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = Utils.getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }

        holder.content.setVisibility(View.VISIBLE);
        Spannable span = SmileUtils.getSmiledText(context,  tbub.getUserContent());
        holder.content.setText(span, TextView.BufferType.SPANNABLE);
    }

    /**
     * 发送方图片消息
     * @param holder
     * @param tbub
     * @param position
     */
    private void toImgUserLayout(final ToUserImgViewHolder holder, final ChatMessageBean tbub, final int position) {
        holder.headicon.setBackgroundResource(R.mipmap.test_icon);
        switch (tbub.getSendState()) {
            case SENDING:
                an = AnimationUtils.loadAnimation(context,
                        R.anim.update_loading_progressbar_anim);
                LinearInterpolator lin = new LinearInterpolator();
                an.setInterpolator(lin);
                an.setRepeatCount(-1);
                holder.sendFailImg
                        .setBackgroundResource(R.mipmap.xsearch_loading);
                holder.sendFailImg.startAnimation(an);
                an.startNow();
                holder.sendFailImg.setVisibility(View.VISIBLE);
                break;

            case COMPLETED:
                holder.sendFailImg.clearAnimation();
                holder.sendFailImg.setVisibility(View.GONE);
                break;

            case SENDERROR:
                holder.sendFailImg.clearAnimation();
                holder.sendFailImg
                        .setBackgroundResource(R.mipmap.msg_state_fail_resend_pressed);
                holder.sendFailImg.setVisibility(View.VISIBLE);
                holder.sendFailImg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        if (sendErrorListener != null) {
                            sendErrorListener.onClick(position);
                        }
                    }

                });
                break;
            default:
                break;
        }
        holder.headicon.setImageResource(R.mipmap.test_icon);

        /* time */
        if (position != 0) {
            String showTime = Utils.getTime(tbub.getTime(), userList.get(position - 1)
                    .getTime());
            if (showTime != null) {
                holder.chat_time.setVisibility(View.VISIBLE);
                holder.chat_time.setText(showTime);
            } else {
                holder.chat_time.setVisibility(View.GONE);
            }
        } else {
            String showTime = Utils.getTime(tbub.getTime(), null);
            holder.chat_time.setVisibility(View.VISIBLE);
            holder.chat_time.setText(showTime);
        }

//        if (isPicRefresh) {
            holder.image_group.setVisibility(View.VISIBLE);
            final String imageSrc = tbub.getImageLocal() == null ? "" : tbub
                    .getImageLocal();
            final String imageUrlSrc = tbub.getImageUrl() == null ? "" : tbub
                    .getImageUrl();
            final String imageIconUrl = tbub.getImageIconUrl() == null ? ""
                    : tbub.getImageIconUrl();
            File file = new File(imageSrc);
            final boolean hasLocal = !imageSrc.equals("")
                    && FileSaveUtil.isFileExists(file);
            int res;
            res = R.drawable.chatto_bg_focused;
            Glide.with(context).load(imageUrlSrc).transform(new CustomShapeTransformation(context, res)).into(holder.image_Msg);
            holder.image_Msg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ToastUtils.showToast("点击了图片");

//                    Intent intent = new Intent(context, ImageViewActivity.class);
//                    intent.putStringArrayListExtra("images", imageList);
//                    intent.putExtra("clickedIndex", imagePosition.get(position));
//                    context.startActivity(intent);
                }

            });
//        }
    }





}
