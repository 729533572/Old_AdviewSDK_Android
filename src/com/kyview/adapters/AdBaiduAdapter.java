package com.kyview.adapters;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.baidu.mobads.AdService;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdBaiduAdapter extends AdViewAdapter implements AdViewListener {
	private boolean isFailed=false;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_BAIDU;
	} 

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.baidu.mobads.AdView") != null) {
				registry.registerClass(networkType(), AdBaiduAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		} 
	}

	public AdBaiduAdapter() {
	}

	// private AdView adView;
	// private AdService adService;

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		Context mContext = (Context) adViewLayout.activityReference.get();
		AdService.setChannelId("e498eab7");
		if (AdViewTargeting.getRunMode() == RunMode.TEST) {
			AdView.setAppSid(mContext, "debug");
			AdView.setAppSec(mContext, "debug");
		} else {
			AdView.setAppSid(mContext, ration.key);
			AdView.setAppSec(mContext, ration.key2);
		}

	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into Baidu");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		adViewLayout.removeAllViews();
		adViewLayout.activeRation = adViewLayout.nextRation;
		// if((ration.key3).compareTo("1")==0)
		// new AdService(activity,adViewLayout, new ViewGroup.LayoutParams(-1,
		// -2), this);
		// else
		new AdService(activity, adViewLayout,
				new ViewGroup.LayoutParams(-1, -2), this);
		}

	@Override
	public void onAdClick(JSONObject arg0) {

		AdViewUtil.logInfo("onAdClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	@Override
	public void onAdFailed(String arg0) {
		isFailed=true;
		AdViewUtil.logInfo("AdViewListener.onAdFailed, reason="
				+ arg0);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
//		adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onAdReady(AdView arg0) {	
		AdViewUtil.logInfo("onAdReady");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
		return;
		}
		super.onSuccessed(adViewLayout, ration);
		if(!isFailed){
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
		isFailed=false;
		}
	}

	@Override
	public void onAdShow(JSONObject arg0) {
		AdViewUtil.logInfo("onAdShow");
//		AdViewLayout adViewLayout = adViewLayoutReference.get();
//		if (adViewLayout == null) {
//			return;
//		}
//		adViewLayout.reportBaiduImpression();
	}

	@Override
	public void onAdSwitch() {
		AdViewUtil.logInfo("onAdSwitch");
	}

	@Override
	public void onVideoClickAd() {
		AdViewUtil.logInfo("onAdClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	@Override
	public void onVideoClickClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoClickReplay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoStart() {
		// TODO Auto-generated method stub
	}
	@Override
	public void clean() {
		// TODO Auto-generated method stub
		super.clean();
	}


}
