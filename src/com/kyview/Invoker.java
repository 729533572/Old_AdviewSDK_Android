package com.kyview;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kyview.AdViewTargeting.RunMode;
import com.kyview.AdViewTargeting.SwitcherMode;
import com.kyview.AdViewTargeting.UpdateMode;
import com.kyview.adapters.AdViewAdapter;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class Invoker extends Activity implements AdViewInterface,
		OnClickListener {
	public AdViewLayout adViewLayout;
	public static LinearLayout layout;
	// SDK201310111003303e4rx5msd7cn1pa
	public static String sdkKey = "SDK201310111003303e4rx5msd7cn1pa";
	int count = 0;
	boolean autoTest = false;
	BaseAdapter adapter = null;
	ListView listView = null;
	List<Ration> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		layout = (LinearLayout) findViewById(R.id.layout_main);
		Button normal = (Button) findViewById(R.id.normal_btn);
		Button clear = (Button) findViewById(R.id.clear_btn);
		Button start = (Button) findViewById(R.id.start_btn);

		listView = (ListView) findViewById(R.id.adlist);
		start.setOnClickListener(this);
		clear.setOnClickListener(this);
		normal.setOnClickListener(this);
		AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
		AdViewTargeting.setRunMode(RunMode.TEST);
		AdViewTargeting.setSwitcherMode(SwitcherMode.CANCLOSED);
	}

	@Override
	public void onClickAd() {
		AdViewUtil.logInfo("onClickAd");
	}

	@Override
	public void onDisplayAd() {
		AdViewUtil.logInfo("onDisplayAd");

		if (null != list && autoTest) {
			adViewLayout.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (count < list.size()) {
						list.get(count - 1).nid = "OK";
						adapter.notifyDataSetChanged();
						adViewLayout.nextRation = (Ration) list.get(count);
						AdViewAdapter.handleOne(adViewLayout,
								(Ration) list.get(count));
						adViewLayout.extra.cycleTime = 9000000;
						count++;
					} else if (count == list.size()) {
						list.get(count - 1).nid = "OK";
						adapter.notifyDataSetChanged();
					}
				}
			}, 4 * 1000);
		}
	}

	@Override
	public void onClosedAd() {
		createCloseDialog(Invoker.this, adViewLayout);
	}

	@Override
	public void onClick(View v) {
		autoTest = false;
		switch (v.getId()) {
		case R.id.normal_btn:
			if (layout == null)
				return;
			AdViewLayout.isTest = false;
			adViewLayout = new AdViewLayout(this, sdkKey);

			if (null != adViewLayout) {
				AdViewUtil.logInfo("setInterFace");
				adViewLayout.setAdViewInterface(this);
				layout.removeAllViews();
				layout.addView(adViewLayout);
				layout.invalidate();
			}
			break;
		case R.id.clear_btn:
			 if (layout == null)
			 return;
			 AdViewLayout.isTest = true;
			 adViewLayout = new AdViewLayout(this, sdkKey);
			break;
		case R.id.start_btn:
			autoTest = true;
			count = 0;
			list = adViewLayout.adViewManager.getRationList();

			adapter = new BaseAdapter() {
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					LayoutInflater layoutInflater = LayoutInflater
							.from(Invoker.this);
					convertView = layoutInflater.inflate(R.layout.listitem,
							null);
					TextView name = (TextView) convertView
							.findViewById(R.id.textView1);
					TextView status = (TextView) convertView
							.findViewById(R.id.textView3);
					name.setTextSize(20);
					status.setTextSize(20);
					name.setText(list.get(position).name);
					status.setText(list.get(position).nid);
					return convertView;
				}

				@Override
				public long getItemId(int position) {
					return position;
				}

				@Override
				public Object getItem(int position) {
					return list.get(position);
				}

				@Override
				public int getCount() {
					return list.size();
				}
			};

			listView.setAdapter(adapter);

			adViewLayout.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (count < list.size()) {
						adViewLayout.nextRation = (Ration) list.get(count);
						AdViewAdapter.handleOne(adViewLayout,
								(Ration) list.get(count));
						adViewLayout.extra.cycleTime = 9000000;
						count++;
					}
				}
			}, 2 * 1000);
			break;
		}
	}

	
	
	public void createCloseDialog(Context context,
			final AdViewLayout adViewLayout) {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("确定要关闭广告？")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// *无论是否关闭，必须调用
						adViewLayout.setClosed(false);
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// *无论是否关闭，必须调用
						adViewLayout.setClosed(true);
					}
				}).show();
		// 防止误点击关闭
		dialog.setCanceledOnTouchOutside(false);
	}

}
