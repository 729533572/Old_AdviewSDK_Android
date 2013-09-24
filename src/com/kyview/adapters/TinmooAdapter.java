package com.kyview.adapters;

import android.app.Activity;

import com.ignitevision.android.ads.AdManager;
import com.ignitevision.android.ads.AdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class TinmooAdapter extends AdViewAdapter{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_TINMOO;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.ignitevision.android.ads.AdView") != null) {
				registry.registerClass(networkType(), TinmooAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public TinmooAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into Tinmoo");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
 		if(adViewLayout == null) {
 			return;
 		}
 		
 		Activity activity = adViewLayout.activityReference.get();
 		if(activity == null) {
		  return;
 		}
 		if(AdViewTargeting.getRunMode()==RunMode.TEST)
 			AdManager.setTest(true);
 		else
 			AdManager.setTest(false);
		AdManager.setPublisherKey(ration.key);
 		AdView adView = new AdView(activity);
     
        adViewLayout.adViewManager.resetRollover();
	    adViewLayout.handler.post(new AdViewLayout.ViewAdRunnable(adViewLayout, adView));
	    adViewLayout.rotateThreadedDelayed();
		super.onSuccessed(adViewLayout, ration);
	}

	
	
}
