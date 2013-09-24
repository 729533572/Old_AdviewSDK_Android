package com.kyview;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kyview.AdViewTargeting.RunMode;
import com.kyview.AdViewTargeting.UpdateMode;






public class Invoker extends Activity implements AdViewInterface ,OnClickListener{
	public static AdViewLayout adViewLayout;
	public static LinearLayout layout;
	public static String sdkKey="SDK201310111003303e4rx5msd7cn1pa";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        layout = (LinearLayout)findViewById(R.id.layout_main);
        Button ceshi=(Button) findViewById(R.id.ceshi_btn);
        Button normal=(Button) findViewById(R.id.normal_btn);
        ceshi.setOnClickListener(this);
        normal.setOnClickListener(this);
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
        AdViewTargeting.setRunMode(RunMode.TEST);
//        AdViewTargeting.setStatusMode(StatusMode.CANCLOSED);
    }

	

	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void onClosedAd() {
//		// TODO Auto-generated method stub
//		AdViewUtil.logInfo("onClickClosed");
//	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal_btn:

	        if (layout == null) 
	            return; 
			AdViewLayout.isTest=false;
	        adViewLayout = new AdViewLayout(this, sdkKey);   		
			break;
		case R.id.ceshi_btn:
			AdViewLayout.isTest=true;		
	        adViewLayout = new AdViewLayout(this, sdkKey);		
			Intent intent=new Intent();
			intent.setClass(this, TestModeActivity.class);
			startActivity(intent);
			break;
		}
        adViewLayout.setAdViewInterface(this); 
        layout.removeAllViews();
        layout.addView(adViewLayout);
        layout.invalidate();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("onDestroy", "onDestroy");
//		adViewLayout.release();
	}



	//可在该方法中处理广告关闭事件
	@Override
	public void onClosedAd() {
		//如果想立即关闭直接调用：
		//adViewLayout.setClosed(true);
		
		//弹出对话框，要求二次确认
		Dialog dialog=new AlertDialog.Builder(this).setTitle("确定要关闭广告？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//无论是否关闭广告，请务必调用下一行方法，否则广告将停止切换
						//传入false，广告将不会关闭
						adViewLayout.setClosed(false);
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//无论是否关闭广告，请务必调用下一行方法，否则广告将停止切换
						//传入true，广告将关闭
						adViewLayout.setClosed(true);
					}
				}).show();
		//防止误点击关闭对话框，可能使 adViewLayout.setClosed(boolean);不被调用
		dialog.setCanceledOnTouchOutside(false);
	}
	

	
}
