package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdRequest.VpadnErrorCode;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;

public class VponAdapter extends AdViewAdapter implements VpadnAdListener {
	VpadnBanner vponBanner = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_VPON;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.vpadn.ads.VpadnAdListener") != null) {
				registry.registerClass(networkType(), VponAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public VponAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into Vpon");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		// calcAdSize(adViewLayout);
		try {
			// VponBanner vponBanner = new VponBanner(activity, ration.key,
			// VponAdSize.SMART_BANNER, VponPlatform.CN);
			VpadnAdRequest adRequest = new VpadnAdRequest();
			// 設定可以auto refresh去要banner
			adRequest.setEnableAutoRefresh(true);

			// AdView adView = new AdView(activity, adWidth, adHeight);
			if (adViewLayout.adViewManager.bLocationForeign == false)
				vponBanner = new VpadnBanner(activity, ration.key,
						VpadnAdSize.SMART_BANNER, "CN");
			// adView.setLicenseKey(ration.key, AdOnPlatform.CN, autoRefreshAd);
			else
				vponBanner = new VpadnBanner(activity, ration.key,
						VpadnAdSize.SMART_BANNER, "TW");
			// adView.setLicenseKey(ration.key, AdOnPlatform.TW, autoRefreshAd);
			vponBanner.loadAd(adRequest);
			vponBanner.setAdListener(this);
			adViewLayout.AddSubView(vponBanner);

		} catch (IllegalArgumentException e) {
			adViewLayout.rollover();
			return;
		}
	}

	@Override
	public void onVpadnDismissScreen(VpadnAd arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVpadnFailedToReceiveAd(VpadnAd arg0, VpadnErrorCode arg1) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Vpon fail");
		arg0.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		// adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onVpadnLeaveApplication(VpadnAd arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVpadnPresentScreen(VpadnAd arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVpadnReceiveAd(VpadnAd arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Vpon success");
		arg0.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		// adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
	}

}
