package com.kyview;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.kuaiyou.util.AdViewUtils;
import com.kyview.obj.Ration;

public class SingleModeTask extends AsyncTask<String, String, String> {
	private Context context = null;
	private List<Ration> rationList = null;
	private ProgressDialog progressDialog = null;
	private ListView listView = null;
	private AlertDialog dialog = null;
	private AdViewLayout adViewLayout = null;


	public SingleModeTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		listView = new ListView(context);
		this.adViewLayout = new AdViewLayout((Activity) context, Invoker.sdkKey);
	}

	@Override
	protected String doInBackground(String... params) {
		if (null == adViewLayout)
			return null;
		if (adViewLayout.adViewManager == null) {
			adViewLayout.adViewManager = new AdViewManager(
					new WeakReference<Context>(context), adViewLayout.keyAdView);
		}
		rationList = adViewLayout.adViewManager.getRationList();
		while (null == rationList) {
			rationList = adViewLayout.adViewManager.getRationList();
		}

		publishProgress();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		AdViewUtils.logInfo("onPostExecute");
//		dialog = new AlertDialog.Builder(context).setTitle("单个平台测试")
//				.setView(listView).show();
//		progressDialog.dismiss();
		// listView.setAdapter(new BaseAdapter() {
		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// TextView textView = new TextView(context);
		// textView.setTextSize(28);
		// textView.setText(rationList.get(position).name);
		// return textView;
		// }
		//
		// @Override
		// public long getItemId(int position) {
		// return position;
		// }
		//
		// @Override
		// public Object getItem(int position) {
		// return rationList.get(position);
		// }
		//
		// @Override
		// public int getCount() {
		// return rationList.size();
		// }
		// });
		//
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// // Invoker.adViewLayout.nextRation = (Ration) arg0
		// // .getItemAtPosition(arg2);
		// // AdViewAdapter.handleOne(Invoker.adViewLayout,
		// // (Ration) arg0.getItemAtPosition(arg2));
		// // Invoker.adViewLayout.extra.cycleTime = 9000000;
		// dialog.dismiss();
		// }
		//
		// });

	}

}
