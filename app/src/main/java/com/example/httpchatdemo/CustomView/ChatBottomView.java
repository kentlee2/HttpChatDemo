package com.example.httpchatdemo.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.httpchatdemo.R;


public class ChatBottomView extends LinearLayout {
	private View baseView;
	private LinearLayout imageGroup;
	private LinearLayout cameraGroup;
	private OnHeadIconClickListener onHeadIconClickListener;
	public static final int FROM_CAMERA = 1;
	public static final int FROM_GALLERY = 2;
	public ChatBottomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		findView();
		init();
	}
	
	private void findView(){
		baseView = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_view, this);
		imageGroup = (LinearLayout) baseView.findViewById(R.id.image_bottom_group);
		cameraGroup = (LinearLayout) baseView.findViewById(R.id.camera_group);
	}
	private void init(){
		cameraGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != onHeadIconClickListener) {
					onHeadIconClickListener.onClick(FROM_CAMERA);
				}
			}
		});
		imageGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != onHeadIconClickListener) {
					onHeadIconClickListener.onClick(FROM_GALLERY);
				}
			}
		});
	}

	public void setOnHeadIconClickListener(
			OnHeadIconClickListener onHeadIconClickListener) {
		// TODO Auto-generated method stub
		this.onHeadIconClickListener = onHeadIconClickListener;
	}

	public interface OnHeadIconClickListener {
		public void onClick(int from);
	}
}
