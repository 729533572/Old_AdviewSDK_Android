package com.kyview.adapters;

import android.app.Activity;
import android.graphics.Color;

import com.fractalist.android.ads.ADView;
import com.fractalist.android.ads.AdStatusListener;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class FractalAdapter extends AdViewAdapter implements AdStatusListener{

	ADView view;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_FRACTAL;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.fractalist.android.ads.ADView") != null) {
				registry.registerClass(networkType(), FractalAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public FractalAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		AdViewUtil.logInfo("Into Fractal");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	Extra extra = adViewLayout.extra;
		int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		//int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		
		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}  	     
		// Instantiate an ad view and add it to the view

		ADView.setPublishId(ration.key);
		view=new ADView(activity);
		//view.setStateHander(hand);
		view.setFreshInterval(30);
		//view.setShowCloseButton(true);        
		view.setFadeImage(false);
		view.setBackgroundColor(bgColor);
		//view.setAdStatusListener(this);

		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, view));
		adViewLayout.rotateThreadedDelayed();
		
	}

	@Override
	public void onClick() {
		
	}

	@Override
	public void onFail() {
		AdViewUtil.logInfo("Fractal failure");
		view.setAdStatusListener(null);
		view = null;

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onFailed(adViewLayout, ration);
		//adViewLayout.rotateThreadedPri(1);
	}

	@Override
	public void onReceiveAd() {
		AdViewUtil.logInfo("Fractal success");
		view.setAdStatusListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, view));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onRefreshAd() {
		AdViewUtil.logInfo("Fractal, onRefreshAd");
	}

	//@Override
	public void fullScreenAdClose(boolean arg0) {
		
	}


}
