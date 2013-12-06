package com.kyview.adapters;

import android.app.Activity;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.umengAd.android.AdView;
import com.umengAd.controller.UmengAdListener;


public class UmengAdapter extends AdViewAdapter implements UmengAdListener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_UMENG;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.umengAd.android.AdView") != null) {
				registry.registerClass(networkType(), UmengAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public UmengAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		AdViewUtil.logInfo("Into Umeng");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	//Extra extra = adViewLayout.extra;
		//int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		//int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}  	     
		// Instantiate an ad view and add it to the view

		if(adViewLayout.umengView==null)
		{
			AdView adView = new AdView(activity);
			//adView.adInit("10013", "10050");
			//adView.adInit("fab1c58b2e9d5a7f", "c0bece60ff61ed90");
			adView.adInit(ration.key, ration.key2);
			//adView.setTextColor(fgColor);
			//adView.setBannerColor(bgColor);
			
			adViewLayout.umengView = adView;
		}
		
		//adView.setUmengAdListener(this);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adViewLayout.umengView));
		adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void onRequestFail(AdView paramAdView) {
		AdViewUtil.logInfo("Umeng failure");
		paramAdView.setUmengAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onRequestSuccess(AdView paramAdView) {
		AdViewUtil.logInfo("Umeng success");
		paramAdView.setUmengAdListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, paramAdView));
		adViewLayout.rotateThreadedDelayed();
	}


}
