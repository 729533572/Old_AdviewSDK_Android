package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.suizong.mobile.ads.Ad;
import com.suizong.mobile.ads.AdError;
import com.suizong.mobile.ads.AdListener;
import com.suizong.mobile.ads.AdRequest;
import com.suizong.mobile.ads.AdSize;
import com.suizong.mobile.ads.AdView;

public class YunYunAdapter extends AdViewAdapter implements AdListener {
	private AdView adView;
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_YUNYUN;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.suizong.mobile.ads.AdListener") != null) {
				registry.registerClass(networkType(), YunYunAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	} 

	@Override
	public void handle() {

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		AdRequest request = new AdRequest();
		AdViewUtil.logInfo("Into YunYun");
		if (AdViewTargeting.getRunMode() == RunMode.TEST) {

			request.setTesting(true);
		} else {
			request.setTesting(false);
		}
		request.setRefreshTime(0);
		request.addExtra("channel", AdViewUtil.ADVIEW4YUNYUN);
		adView = new AdView(activity, AdSize.BANNER, ration.key);
		adView.loadAd(request);
		// 设置监听器
		adView.setAdListener(this);
		adViewLayout.AddSubView(adView);
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, AdError arg1) {
		if (!arg1.toString().equals("[3]There was an internal error.")) {
			AdViewUtil.logInfo("YunYun onAdLoadFailed");
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if (adViewLayout == null) {
				return;
			}
			super.onFailed(adViewLayout, ration);
			//adViewLayout.rotateThreadedPri(1);
		}
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		if(arg0!=null)
		adView.destroy();
		adView=null;
		AdViewUtil.logInfo("onLeaveApplication");
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		AdViewUtil.logInfo("onPresentScreen");
	}

	@Override
	public void onReceiveAd(Ad arg0, long arg1) {
		AdViewUtil.logInfo("onReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		arg0.setAdListener(null);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
		adViewLayout.reportImpression();
	}
	@Override
	public void clean() {
		// TODO Auto-generated method stub
		super.clean();

	}
}
