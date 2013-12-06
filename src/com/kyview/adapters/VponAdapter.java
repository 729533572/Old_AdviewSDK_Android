package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.vpon.adon.android.AdListener;
import com.vpon.adon.android.AdOnPlatform;
import com.vpon.adon.android.AdView;

public class VponAdapter extends AdViewAdapter implements AdListener{
	private int adHeight;
	private int adWidth;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_VPON;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.vpon.adon.android.AdView") != null) {
				registry.registerClass(networkType(), VponAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public VponAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	private void calcAdSize(AdViewLayout adViewLayout) {
		int width=320;
		int height=48;
		int screenWidth = adViewLayout.adViewManager.width;
		if (screenWidth < 480) {
			width = 320;
			height = 48;
		} else if (screenWidth < 720) {
			width = 480;
			height = 72;
		} else if (screenWidth >= 720) {
			width = 720;
			height = 108;
		}

		adHeight = height;//AdViewUtil.convertToScreenPixels(height, adViewLayout.mDensity);
		adWidth = width;//AdViewUtil.convertToScreenPixels(width, adViewLayout.mDensity);
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
		calcAdSize(adViewLayout);
		try {
			AdView adView = new AdView(activity, adWidth, adHeight);
			boolean autoRefreshAd = false;
			if (adViewLayout.adViewManager.bLocationForeign == false)
				adView.setLicenseKey(ration.key, AdOnPlatform.CN, autoRefreshAd);
			else
				adView.setLicenseKey(ration.key, AdOnPlatform.TW, autoRefreshAd);	
			adView.setAdListener(this);	
			adViewLayout.AddSubView(adView);
			
		} catch (IllegalArgumentException e) {
			adViewLayout.rollover();
			return;
		}
	}

	@Override
	public void onFailedToRecevieAd(AdView arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Vpon fail");
		arg0.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		  //adViewLayout.rotateThreadedPri(1);
		
	}

	@Override
	public void onRecevieAd(AdView arg0) {
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
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
		
	}



}
