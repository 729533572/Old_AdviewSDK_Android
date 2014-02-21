package com.kyview.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kyview.AdViewLayout;

import android.content.Context;
import android.os.Handler;


public class AdViewReqManager implements FetchListener {

	private static AdViewReqManager mInstance = null;

	public java.util.ArrayList<ReqInfoItem> mPendingReqInfos = new java.util.ArrayList<ReqInfoItem>();
	public ReqInfoItem mCurReqInfo;
	public static long mLastReqInfoTime = 0; // second
	public AdViewNetFetchThread mFetcher = null;
	
	private DBOpenHelper mDBHelper = null;
	private AdViewLayout adViewLayout;

	private Object mLockObj = new Object();
	private Context context;
	public static final int REQ_LIMIT_TIME = 60*2;
	public static final int REQ_DELAY_TIME = 30;
	
	private static final int SKIP_TIME_LIMIT = 3600 * 24 * 1;		//means if reqinfo is elder than 1 day ago, then won't upload it.
	// http://report.adview.cn/
	// http://211.103.153.122/ test
	public static final String REQ_URL = "http://report.adview.cn/agent/adview_reqinfo.php";

	private AdViewReqManager(Context context) {
		this.context = context;
		mDBHelper = new DBOpenHelper(context, DBOpenHelper.DBNAME_REQ);
	}

	public static AdViewReqManager getInstance(Context context) {
		if (null == mInstance) {
			if (null == mInstance) {	//prevent thread conflict.
				mInstance = new AdViewReqManager(context);
			}
		}
		return mInstance;
	}

	public static class ReqInfoItem {
		long mDataTime;
		int mId = 0;
		
		boolean mInSend = false;		//do not need to save to db or file.
		
		JSONArray mDataArr = new JSONArray();

		public ReqInfoItem() {
		}

		public boolean isDataEmpty() {
			return mDataArr == null ? true : mDataArr.length() < 1;
		}
		
		public synchronized void setSending(boolean bVal) {
			mInSend = bVal;
		}
		
		public synchronized boolean isSending() {
			return mInSend;
		}
		
		public void saveToDB(DBOpenHelper helper) {
			synchronized(helper) {
				if (0 != mId) return;
				
				try {
					mId = helper.getMaxId() + 1;
					helper.addVariable(mId, Long.toString(mDataTime), mDataArr.toString());
				} catch (Exception e) {
					mId = 0;
				}
			}
		}

		public String makePostBody(AdViewLayout adViewLayout) {
			long time = AdViewUtil.currentSecond();
			String ret = "appid=" + adViewLayout.keyAdView + "&bundle="
					+ AdViewLayout.bundle + "&uuid=0&keydev="
					+ AdViewLayout.keyDev + "&client=0&data=" + mDataArr
					+ "&dataTime=" + mDataTime + "&sdkver="
					+ AdViewUtil.VERSION + "&time=" + time + "&configVer="
					+ AdViewUtil.configVer + "&token="
					+ adViewLayout.getTokenMd5(time);
			return ret;
		}

		public void storeInfo(String keyAdView, int rationName, String typeName) {
			boolean isFound = false;
			JSONObject jsonMap = null;
			try {
				for (int i = 0; i < mDataArr.length(); i++) {
					jsonMap = mDataArr.getJSONObject(i);
					if (jsonMap.getInt("type") == rationName) {
						if (jsonMap.has(typeName))
							jsonMap.put(typeName,
									(jsonMap.getInt(typeName) + 1));
						else
							jsonMap.put(typeName, 1);
						isFound = true;
						break;
					}
				}
				if (!isFound) {
					jsonMap = new JSONObject();
					jsonMap.put("type", rationName);
					jsonMap.put(typeName, 1);
					mDataArr.put(jsonMap);
				}
				mDataTime = AdViewUtil.currentSecond();
			} catch (JSONException e) {
				AdViewUtil.logError("JSONException", e);
			}
		}
	}

	// From file to mPendingReqInfos. the file will be one line of "time=%d",
	// one line of "data=%s"(%s if json array)
	public void loadPendingReqInfos(Context context) {
		synchronized (mLockObj) {
			ArrayList<ReqInfoItem> items = mDBHelper.getReqInfoItems();
			for (ReqInfoItem item:items) {// think if overlap?
				if (isReqInfoExist(mPendingReqInfos, item.mId))
					continue;
				mPendingReqInfos.add(item);
			}
			//call a method to clear too old data.
			clearOldReqInfos();
		}
	}
	
	//clear old reqinfo older than limit time.
	private void clearOldReqInfos() {
		long curTime = AdViewUtil.currentSecond();
		ArrayList<ReqInfoItem> itemDels = new ArrayList<ReqInfoItem>();
		for (ReqInfoItem item:mPendingReqInfos) {// think if overlap?
			if (curTime - item.mDataTime > SKIP_TIME_LIMIT) {
				itemDels.add(item);
			}
		}
		for (ReqInfoItem item:itemDels) {
			mDBHelper.delVariable(item.mId);
			mPendingReqInfos.remove(item);
		}
	}
	
	private static boolean isReqInfoExist(ArrayList<ReqInfoItem> arr, int id) {
		for (ReqInfoItem item : arr) {
			if (item.mId == id) return true;
		}
		return false;
	}

	// From mPendingReqInfos to file.
	public void savePendingReqInfos(Context context) {
		synchronized (mLockObj) {
			actSavePendingReqInfos(context);
		}
	}
	
	private void actSavePendingReqInfos(Context context) {
		//AdViewUtil.logInfo("savePendingReqInfos");
		
		if (null!=mCurReqInfo&&!mCurReqInfo.isDataEmpty())
			mPendingReqInfos.add(mCurReqInfo);
		mCurReqInfo = null;		
		for (ReqInfoItem item:mPendingReqInfos)
			item.saveToDB(mDBHelper);
	}	

	// the return value is {"result":0} or {"result":1}
	public void notifyFetchStatus(AdViewNetFetchThread fetcher, int status,
			Object value) {
		boolean bSent = false;
		if (status == AdViewNetFetchThread.FETCH_OK) {
			if (value instanceof String) {
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject((String) value);

					if (null != jsonObj && 1 == jsonObj.optInt("result", 0)) {
						bSent = true;
//						AdViewUtil.logInfo("upload success");
					} else {
						AdViewUtil.logInfo("upload error");
					}

				} catch (JSONException e) {
				}
			}
		} else if (status == AdViewNetFetchThread.FETCH_ERROR) {
			AdViewUtil.logInfo("upload error");
		}
		
		synchronized (mLockObj) {
			changeReqInfoStatus(fetcher, bSent);
			if (fetcher == mFetcher) {
				mFetcher = null; // one done.
			}
		}
		if(status!=101)
		checkReqInfo();
	}

	private  void changeReqInfoStatus(AdViewNetFetchThread fetcher, boolean bSent) {		
		ReqInfoItem item = (ReqInfoItem) fetcher.getUserObject();
		if (null == item) return;
		
		if (!bSent) {
			item.setSending(false);
			return;
		}
		synchronized (mPendingReqInfos) {
			ReqInfoItem itemTemp=null;
			for (ReqInfoItem item1 : mPendingReqInfos) {
				if (item.mId == item1.mId) {
					itemTemp=item1;
				}
			}
			if(itemTemp!=null)
			mPendingReqInfos.remove(itemTemp);
		}
		mDBHelper.delVariable(item.mId);
	}

	// check if time is too old, should set to can sending.
	private void checkReqInfo() {
		synchronized (mLockObj) {
			if (null == mCurReqInfo)
				mCurReqInfo = new ReqInfoItem();
			if (!mCurReqInfo.isDataEmpty()) {
				if (AdViewUtil.currentSecond() - mLastReqInfoTime >= REQ_LIMIT_TIME) {
					actSavePendingReqInfos(context);
					mCurReqInfo = new ReqInfoItem();
				}
			}
			if(adViewLayout==null||!AdViewLayout.isConnectInternet(context))
				return;
			if (mPendingReqInfos.size() > 0) { // should Implement.
				if (null == mFetcher) {
					for (ReqInfoItem item : mPendingReqInfos) {
						if (item.isSending())
							continue;
						item.setSending(true);
						mFetcher = new AdViewNetFetchThread(REQ_URL,
								item.makePostBody(adViewLayout));
						mFetcher.setFetchListener(this);
						mFetcher.setUserObject(item);
						mFetcher.start();
						break;
					}
				}
			}
		}
	}

	public void storeInfo(AdViewLayout adViewLayout, int rationName,
			String typeName) {
		if (adViewLayout == null)
			return;
		this.adViewLayout = adViewLayout;
		checkReqInfo();
		synchronized (mLockObj) {
			if (mCurReqInfo.isDataEmpty()) {
				if (context == null)
					context = adViewLayout.activityReference.get();
				mCurReqInfo = new ReqInfoItem();
				mLastReqInfoTime = AdViewUtil.currentSecond();
			}
			mCurReqInfo.storeInfo(adViewLayout.keyAdView, rationName, typeName);
		}

		delayCheckReqInfo(REQ_DELAY_TIME);
	}

	Handler mSendHandler = new Handler();
	SendRunnable mSendRunnable = null;

	private void delayCheckReqInfo(int waitSeconds) {
		synchronized (mLockObj) {
			if (null != mSendRunnable)
				return; // already in schedule.
			if (mPendingReqInfos.size() < 1 && mCurReqInfo.isDataEmpty())
				return; // empty data.

			mSendHandler.removeCallbacks(mSendRunnable);
			mSendRunnable = new SendRunnable();
			mSendHandler.postDelayed(mSendRunnable, waitSeconds * 1000);
		}
	}

	public class SendRunnable implements Runnable {
		public SendRunnable() {
		}

		public void run() {
			checkReqInfo();

			mSendRunnable = null;

			delayCheckReqInfo(REQ_DELAY_TIME);
		}
	}
}
