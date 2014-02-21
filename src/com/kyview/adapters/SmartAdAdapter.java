package com.kyview.adapters;

import android.app.Activity;
import cn.smartmad.ads.android.SMAdBannerListener;
import cn.smartmad.ads.android.SMAdBannerView;
import cn.smartmad.ads.android.SMAdManager;
import cn.smartmad.ads.android.SMRequestEventCode;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class SmartAdAdapter extends AdViewAdapter implements SMAdBannerListener {
	private SMAdBannerView smAdBannerView;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_SMARTAD;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("cn.smartmad.ads.android.SMAdBannerView") != null) {
				registry.registerClass(networkType(), SmartAdAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public SmartAdAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into SmartAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		SMAdManager.setApplicationId(activity, ration.key);
		SMAdManager.setAdRefreshInterval(1);
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			SMAdManager.setDebuMode(true);
		else
			SMAdManager.setDebuMode(false);
		smAdBannerView = new SMAdBannerView(activity, ration.key2,
				SMAdBannerView.AUTO_AD_MEASURE);
		// AdViewUtil.writeLogtoFile("adview_adrequest_log", "adRequest");
		smAdBannerView.setSMAdBannerListener(this);
	}

	@Override
	public void onAppResumeFromAd(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onAppResumeFromAd");
	}

	@Override
	public void onAppSuspendForAd(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onAppSuspendForAd");
	}

	@Override
	public void onClickedAd() {
		AdViewUtil.logInfo("smartmad onClickedAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		adViewLayout.reportClick();
		// AdViewUtil.writeLogtoFile("adview_adclick_log", "adClick");
	}

	@Override
	public void onClosedAdExpand(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onClosedAdExpand");
	}

	@Override
	public void onDetachedFromScreen(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onDetachedFromScreen");
	}

	@Override
	public void onExpandedAd(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onExpandedAd");
	}

	@Override
	public void onLeaveApplication(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onLeaveApplication");
	}

	@Override
	public void onReceivedAd(SMAdBannerView arg0) {
		AdViewUtil.logInfo("onReceivedAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		super.onSuccessed(adViewLayout, ration);
		// AdViewUtil.writeLogtoFile("adview_adreceived_log", "adonReceived");
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onAttachedToScreen(SMAdBannerView arg0, SMRequestEventCode arg1) {
		AdViewUtil.logInfo("onAttachedToScreen");
		// AdViewUtil.writeLogtoFile("adview_adattached_log", "adonAttached");

	}

	@Override
	public void onFailedToReceiveAd(SMAdBannerView arg0, SMRequestEventCode arg1) {
		AdViewUtil
				.logInfo("onFailedToReceiveAd smartmad failure, SMRequestEventCode="
						+ arg1);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
		// AdViewUtil.writeLogtoFile("adview_adfailed_log",
		// "adonFailedToReceive");
	}

}
