package com.kyview.adapters;

import android.app.Activity;

import com.adwo.adsdk.AdListener;
import com.adwo.adsdk.AdwoAdView;
import com.adwo.adsdk.ErrorCode;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

//import com.kyview.AdViewLayout.ViewAdRunnable;

public class AdwoAdapter extends AdViewAdapter implements AdListener {

	byte chId = 2;
	byte mkId = 3;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADWO;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.adwo.adsdk.AdwoAdView") != null) {
				registry.registerClass(networkType(), AdwoAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public AdwoAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Into Adwo");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		Activity activity = adViewLayout.activityReference.get();
		if (activity == null)
			return;
		AdwoAdView adView = null;
		AdwoAdView.setBannerMatchScreenWidth(true);
		AdwoAdView.setMarketId(mkId);
		AdwoAdView.setAggChannelId(chId);
		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			adView = new AdwoAdView((Activity) adViewLayout.getContext(),
					ration.key, true, 0);
		else
			adView = new AdwoAdView((Activity) adViewLayout.getContext(),
					ration.key, false, 0);


		adView.setListener(this);
		adViewLayout.AddSubView(adView);

	}

	@Override
	public void onFailedToReceiveAd(AdwoAdView arg0, ErrorCode arg1) {
		AdViewUtil.logInfo("onFailedToReceiveAd, arg1=" + arg1);
		arg0.setListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null)
			return;
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onReceiveAd(AdwoAdView arg0) {
		AdViewUtil.logInfo("onReceiveAd");
		arg0.setListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) 
			return;
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

}
