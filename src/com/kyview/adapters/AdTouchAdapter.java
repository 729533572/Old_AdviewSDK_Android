package com.kyview.adapters;

import android.app.Activity;

import com.energysource.szj.embeded.AdListener;
import com.energysource.szj.embeded.AdManager;
import com.energysource.szj.embeded.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.AdArea;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;



public class AdTouchAdapter extends AdViewAdapter implements AdListener{
	
	private int area=AdManager.Bottom_Banner;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADTOUCH;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.energysource.szj.embeded.AdView") != null) {
				registry.registerClass(networkType(), AdTouchAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public AdTouchAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		AdViewUtil.logInfo("Into AdTouch");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			AdManager.openDebug();
		AdManager.openPermissionJudge();
		
		
		AdManager.initAd(activity, ration.key);
		
		if(AdViewTargeting.getAdArea()==AdArea.BOTTOM)
			area=AdManager.Bottom_Banner;
		else
			area=AdManager.Top_Banner;
		AdManager.addAd(101, AdManager.AD_FILL_PARENT, area, 0, 0);
		AdManager.setAdListener(this);
		AdManager.openAllAdView();
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
		
		
	}

	@Override
	public void failedReceiveAd(AdView arg0) {
		AdViewUtil.logInfo("AdTouch fail");
		AdManager.setAdListener(null);
		AdViewLayout adViewLayout=adViewLayoutReference.get();
		if(adViewLayout==null)
			return;
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void receiveAd(AdView arg0) {
		AdViewUtil.logInfo("AdTouch success");
		AdManager.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		super.onSuccessed(adViewLayout, ration);
		adViewLayout.reportImpression(); 
	}



	

}
