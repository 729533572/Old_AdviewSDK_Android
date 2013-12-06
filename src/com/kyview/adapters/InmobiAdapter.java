package com.kyview.adapters;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class InmobiAdapter extends AdViewAdapter  implements IMBannerListener{
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_INMOBI;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.inmobi.monetization.IMBannerListener") != null) {
			
				registry.registerClass(networkType(), InmobiAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public InmobiAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into Inmobi");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		// set the test mode to true (Make sure you set the test mode to false
		// when distributing to the users)
		
		InMobi.initialize(activity, ration.key);
		
		IMBanner bannerAdView = new IMBanner(activity,ration.key,IMBanner.INMOBI_AD_UNIT_320X50);
	
		Map<String,String> reqParams = new HashMap<String,String>();
		reqParams.put("tp","c_adview");

		bannerAdView.setRequestParams(reqParams);
		bannerAdView.setIMBannerListener(this);
		bannerAdView.loadBanner();


	}


	@Override
	public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("InMobi onBannerInteraction");
	}

	@Override
	public void onBannerRequestFailed(IMBanner arg0, IMErrorCode arg1) {
		AdViewUtil.logInfo("ImMobi failure "+arg1.toString());
		arg0.setIMBannerListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) 
			return; 
		super.onFailed(adViewLayout, ration);
	}

	@Override
	public void onBannerRequestSucceeded(IMBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("InMobi success");

		arg0.setIMBannerListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) 
			  return;
		  super.onSuccessed(adViewLayout, ration);
		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		  adViewLayout.rotateThreadedDelayed(); 
	}

	@Override
	public void onDismissBannerScreen(IMBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("InMobi onDismissBannerScreen");
	}

	@Override
	public void onLeaveApplication(IMBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("InMobi onLeaveApplication");
	}

	@Override
	public void onShowBannerScreen(IMBanner arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("InMobi onShowBannerScreen");
	}



}
