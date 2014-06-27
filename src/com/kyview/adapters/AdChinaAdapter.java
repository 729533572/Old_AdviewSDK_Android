package com.kyview.adapters;

import android.view.View;

import com.adchina.android.ads.AdManager;
import com.adchina.android.ads.api.AdBannerListener;
import com.adchina.android.ads.api.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class AdChinaAdapter extends AdViewAdapter implements AdBannerListener {

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADCHINA;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.adchina.android.ads.api.AdBannerListener") != null) {
				registry.registerClass(networkType(), AdChinaAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}	
	}

	public AdChinaAdapter() {
	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into AdChina");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}


		AdManager.setRelateScreenRotate(adViewLayout.getContext(), true);
		AdView mAdView = new AdView(adViewLayout.activityReference.get(), ration.key,
				true, false);
		mAdView.setAdBannerListener(this);
		mAdView.setAdRefreshTime(-1);
		mAdView.setVisibility(View.VISIBLE);
		mAdView.start();

	}

	public void onFailedToReceiveAd(AdView arg0) {
		AdViewUtil.logInfo("onFailedToReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}

	public void onReceiveAd(AdView adView) {
		AdViewUtil.logInfo("onReceiveAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
	}


	@Override
	public void onClickBanner(AdView arg0) {
		AdViewUtil.logInfo("onAdClick");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
		
	}

}
