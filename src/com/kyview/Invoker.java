package com.kyview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kyview.AdViewTargeting.SwitcherMode;
import com.kyview.AdViewTargeting.UpdateMode;
import com.kyview.statistics.LogInterface;
import com.kyview.statistics.StatisticsBean;
import com.kyview.statistics.StatisticsInterface;
import com.kyview.util.AdViewUtil;

public class Invoker extends Activity implements AdViewInterface,
		OnClickListener, LogInterface, StatisticsInterface {
	public static AdViewLayout adViewLayout;
	public static LinearLayout layout;
	public static String sdkKey = "SDK201310111003303e4rx5msd7cn1pa";
	private ListView statisticsList;

	private BaseAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		layout = (LinearLayout) findViewById(R.id.layout_main);
		statisticsList = (ListView) findViewById(R.id.staticlayout);
		Button ceshi = (Button) findViewById(R.id.ceshi_btn);
		Button normal = (Button) findViewById(R.id.normal_btn);
		Button clear = (Button) findViewById(R.id.clear_btn);
		clear.setOnClickListener(this);
		ceshi.setOnClickListener(this);
		normal.setOnClickListener(this);
		if (AdViewUtil.statisticsList == null)
			AdViewUtil.statisticsList = new ArrayList<StatisticsBean>();
		adapter = getAdapter(this);
		statisticsList.setAdapter(adapter);
		AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
//		AdViewTargeting.setRunMode(RunMode.TEST);
		AdViewTargeting.setSwitcherMode(SwitcherMode.CANCLOSED);
		AdViewUtil.setLogInterface(this);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			case 1:
				TextView tv1 = (TextView) findViewById(R.id.logtext);
				tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
				tv1.setText((String) msg.obj);
				break;
			}
		};
	};

	public void notifyMsg(int status, String msgs) {
		Message msg = new Message();
		if (null == msgs)
			msgs = "result is null";
		msg.obj = (Object) msgs;
		msg.what = status;
		try {
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClickAd() {
		AdViewUtil.logInfo("onClickAd");
	}

	@Override
	public void onDisplayAd() {
		AdViewUtil.logInfo("onDisplayAd");
	}

	@Override
	public void onClosedAd() {
		createCloseDialog(Invoker.this, adViewLayout);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal_btn:
			if (layout == null)
				return;
			AdViewLayout.isTest = false;
			adViewLayout = new AdViewLayout(this, sdkKey);
			break;
		case R.id.ceshi_btn:
			AdViewLayout.isTest = true;
			adViewLayout = new AdViewLayout(this, sdkKey);
			SingleModeTask singleModeTask=new SingleModeTask(this);
			singleModeTask.execute();
			break;

		case R.id.clear_btn:
			if (adViewLayout.cleanList())
				adapter.notifyDataSetChanged();
			break;
		}
		adViewLayout.setStatisticsInterface(this);
		adViewLayout.setAdViewInterface(this);
		layout.removeAllViews();
		layout.addView(adViewLayout);
		layout.invalidate();
	}

	@Override
	public void onLogChange(StringBuilder log) {
		Log.i("LogOut", log.toString());
		notifyMsg(1, log.toString());
	}

	@Override
	public void onListChange(List<StatisticsBean> statisticsList) {
		notifyMsg(0, null);
	}
	
	@Override
	protected void onDestroy() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (null != adViewLayout)
					adViewLayout.saveStatistics();
			}
		}).start();
		super.onDestroy();
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

	public BaseAdapter getAdapter(final Context context) {
		BaseAdapter adapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.listitem, null);
				TextView text1 = (TextView) convertView
						.findViewById(R.id.textView1);
				TextView text2 = (TextView) convertView
						.findViewById(R.id.textView2);
				TextView text3 = (TextView) convertView
						.findViewById(R.id.textView3);
				TextView text4 = (TextView) convertView
						.findViewById(R.id.textView4);
				text1.setText(AdViewUtil.statisticsList.get(position)
						.getAdName() + "");
				text2.setText(AdViewUtil.statisticsList.get(position)
						.getImpression() + "");
				text3.setText(AdViewUtil.statisticsList.get(position)
						.getClick() + "");
				text4.setText(AdViewUtil.statisticsList.get(position)
						.getFailed() + "");
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return AdViewUtil.statisticsList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return AdViewUtil.statisticsList.size();
			}
		};
		return adapter;
	}
	
	public static void getRationList(){
		
	}
}
